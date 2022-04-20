/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.exceptions.ReadTimeoutException;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.steam.master.exception.MasterServerTimeoutException;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerPartialResponse;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerResponse;
import dev.failsafe.*;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.function.CheckedFunction;
import dev.failsafe.function.CheckedPredicate;
import dev.failsafe.function.ContextualSupplier;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles the internal processing of the master server request/response.
 *
 * @author Rafael Luis Ibasco
 */
@MessengerProperties(optionClass = MasterServerOptions.class)
public final class MasterServerMessenger extends NettyMessenger<MasterServerRequest, MasterServerResponse> {

    private static final Logger log = LoggerFactory.getLogger(MasterServerMessenger.class);

    private static final CheckedPredicate<Throwable> TIMEOUT_ERROR = MasterServerMessenger::handleError;

    private FailsafeExecutor<MasterServerResponse> requestExecutor;

    private RetryPolicy<MasterServerResponse> retryPolicy;

    private RateLimiter<MasterServerChannelContext> rateLimiter;

    /**
     * <p>Constructor for MasterServerMessenger.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    public MasterServerMessenger(Options options) {
        super(options);
        initFailSafe(getOptions(), getExecutor());
    }

    //<editor-fold desc="Public Methods">

    //<editor-fold desc="Failsafe">
    private void initFailSafe(final Options options, final ScheduledExecutorService executor) {
        assert options != null;
        if (!options.getOrDefault(MasterServerOptions.FAILSAFE_ENABLED))
            return;

        //initialize failsafe policies
        Fallback<MasterServerResponse> fallbackPolicy = Fallback.builder((CheckedFunction<ExecutionAttemptedEvent<? extends MasterServerResponse>, MasterServerResponse>) event -> {
            if (event.getLastException() instanceof MasterServerTimeoutException) {
                MasterServerTimeoutException timeoutException = (MasterServerTimeoutException) event.getLastException();
                return new MasterServerResponse(new HashSet<>(timeoutException.getAddresses()));
            }
            return new MasterServerResponse(new HashSet<>());
        }).build();

        //retry policy
        if (options.getOrDefault(MasterServerOptions.FAILSAFE_RETRY_ENABLED)) {
            this.retryPolicy = buildRetryPolicy(options);
        }
        //rate limiter
        if (options.getOrDefault(MasterServerOptions.FAILSAFE_RATELIMIT_ENABLED)) {
            this.rateLimiter = buildRateLimiterPolicy(options);
        }
        //initialize executors
        this.requestExecutor = Failsafe.with(fallbackPolicy);
        //Add retry policy (optional)
        if (retryPolicy != null)
            this.requestExecutor = requestExecutor.compose(retryPolicy);
        if (executor != null)
            this.requestExecutor.with(executor);
    }
    //</editor-fold>

    //<editor-fold desc="Protected Methods">

    private RetryPolicy<MasterServerResponse> buildRetryPolicy(final Options options) {
        //Console.println("BUILDING RETRY");
        RetryPolicyBuilder<MasterServerResponse> builder = FailsafeBuilder.buildRetryPolicy(options);
        builder.onRetry(new EventListener<ExecutionAttemptedEvent<MasterServerResponse>>() {
            @Override
            public void accept(ExecutionAttemptedEvent<MasterServerResponse> event) throws Throwable {
                //Console.println("MASTER RETRY: %d", event.getAttemptCount());
            }
        });
        builder.onFailedAttempt(new EventListener<ExecutionAttemptedEvent<MasterServerResponse>>() {
            @Override
            public void accept(ExecutionAttemptedEvent<MasterServerResponse> event) throws Throwable {
                //Console.error("Failed attempt: %s (Count: %d)", event.getLastException(), event.getAttemptCount());
            }
        });
        return builder.build();
    }

    private RateLimiter<MasterServerChannelContext> buildRateLimiterPolicy(final Options options) {
        RateLimiterBuilder<MasterServerChannelContext> rateLimiterBuilder = FailsafeBuilder.buildRateLimiter(options);
        return rateLimiterBuilder.build();
    }

    private static boolean handleError(Throwable e) {
        return e instanceof TimeoutException || e instanceof SocketException;
    }

    /**
     * Sends a new request to the master server
     *
     * @param request
     *         The {@link com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest} containing the details of the request.
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which is notified once the request has been completed.
     */
    public CompletableFuture<MasterServerResponse> send(MasterServerRequest request) {
        if (this.requestExecutor != null) {
            Console.println("Sending master request: %s", request);
            MasterServerContextualSupplier supplier = new MasterServerContextualSupplier(request);
            return requestExecutor.getStageAsync(supplier).whenComplete(supplier::onCompletion);
        } else
            return super.send(request.getType().getMasterAddress(), request);
    }
    //</editor-fold>

    /** {@inheritDoc} */
    @Override
    protected void configure(Options options) {
        //we disable using native transports (e.g. epoll) by default. Per my tests, it seems NIO seems to be more reliable.
        //This can still be overriden by the developer
        //defaultOption(options, GlobalOptions.USE_NATIVE_TRANSPORT, false);
        defaultOption(options, GlobalOptions.CONNECTION_POOLING, false);
        defaultOption(options, GlobalOptions.READ_TIMEOUT, 8000);
    }

    /** {@inheritDoc} */
    @Override
    protected NettyChannelFactory createChannelFactory() {
        NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.UDP, getOptions(), new MasterServerChannelContextFactory(this));
        return new MasterServerChannelFactory(channelFactory);
    }
    //</editor-fold>

    /** {@inheritDoc} */
    @Override
    protected void receive(@NotNull NettyChannelContext context, AbstractResponse response, Throwable error) {
        final MasterServerChannelContext masterContext = (MasterServerChannelContext) context;

        //Did we receive an error?
        if (error != null) {
            Throwable cause = Errors.unwrap(error);
            log.debug("{} MASTER (RECEIVE) => Encountered an error during send", context.id(), cause);
            super.receive(context, response, cause);
            return;
        }

        if (response == null)
            throw new IllegalStateException("Response cannot be null");

        //handle partial response
        if (response instanceof MasterServerPartialResponse) {
            MasterServerPartialResponse partialResponse = (MasterServerPartialResponse) response;
            InetSocketAddress lastSeedAddress = masterContext.properties().lastSeedAddress();
            assert lastSeedAddress == partialResponse.getLastSeedAddress();
            Set<InetSocketAddress> addressSet = masterContext.properties().addressSet();

            log.debug("{} MASTER (RECEIVE) => Received Partial Response: {} (Received: {}, Total Collected: {}, Last Address Received: {})", masterContext.id(), response, partialResponse.getServerList().size(), addressSet.size(), lastSeedAddress);

            //have we reached the end of the response?
            if (partialResponse.isEndOfResponse()) {
                //create a new response and notify the promise
                masterContext.markSuccess(new MasterServerResponse(new HashSet<>(addressSet)));
                //Clear the address set
                addressSet.clear();
            } else {
                //request for more address
                if (!MasterServer.isTerminatingAddress(lastSeedAddress)) {
                    requestNewBatch(masterContext, lastSeedAddress);
                } else {
                    super.receive(context, response, null);
                }
            }
        } else {
            throw new IllegalStateException("Unsupported response type:" + response);
        }
    }

    /**
     * Sends a request for a new batch of addresses to be collected. Requests are rate-limited by default unless deactivated by configuration.
     *
     * @param context
     *         The {@link MasterServerChannelContext} containing all the necessary properties for the request
     * @param address
     *         The {@link InetSocketAddress} to be used as a seed address.
     */
    private void requestNewBatch(final MasterServerChannelContext context, final InetSocketAddress address) {
        if (context == null)
            throw new IllegalStateException("Context must not be null");
        if (address == null)
            throw new IllegalStateException("Address must not be null");

        assert context.inEventLoop();

        //Update request last seed address
        final MasterServerRequest request = context.properties().request();
        request.setAddress(address.getAddress().getHostAddress() + ":" + address.getPort());

        if (rateLimiter != null) {
            try {
                log.debug("{} MASTER => Acquiring permit", context.id());
                Console.println("%s (RATE LIMITER) Acquiring permit (Max rate: %s sec(s))", context.id(), DurationFormatUtils.formatDuration(rateLimiter.getConfig().getMaxRate().toMillis(), "HH:mm:ss"));
                rateLimiter.acquirePermit();
                context.send();
            } catch (InterruptedException e) {
                context.markInError(e);
            }
        } else {
            context.send();
        }
        log.debug("{} MASTER (REQUEST) => Sent next batch request with seed address '{}' to master server '{}' (Rate Limited: {})", context.id(), request.getAddress(), context.remoteAddress(), rateLimiter != null ? "Yes" : "No");
    }

    private class MasterServerContextualSupplier implements ContextualSupplier<MasterServerResponse, CompletableFuture<MasterServerResponse>> {

        private final MasterServerRequest request;

        private final AtomicReference<MasterServerChannelContext> currentContext = new AtomicReference<>();

        private InetSocketAddress masterAddress;

        private int index;

        private MasterServerContextualSupplier(MasterServerRequest request) {
            this.request = request;
            this.index = 0;
        }

        @Override
        public CompletableFuture<MasterServerResponse> get(ExecutionContext<MasterServerResponse> executionContext) throws Throwable {
            String contextId = (getContext() != null) ? getContext().id() : "[N/A]";
            int maxAttempts = retryPolicy.getConfig().getMaxAttempts();

            if (!executionContext.isFirstAttempt() && executionContext.isRetry() && executionContext.getLastException() instanceof ReadTimeoutException) {
                log.debug("{} MASTER => Encountered a READ TIMEOUT in the last request for address '{}'. Selecting an alternative address", contextId, masterAddress);
                masterAddress = null;
            }

            if (masterAddress == null) {
                masterAddress = nextMasterAddress();
                log.debug("{} MASTER => Selected new master server address '{}'", contextId, masterAddress);
            }

            //Acquire a permit if rate limiter is enabled
            if (rateLimiter != null)
                rateLimiter.acquirePermit();

            log.debug("{} MASTER => Querying master server address '{}' (Attempts {} of {}, Address Index: {}, Seed Address: {}, Type: {}, Seed: {}, Delay: {})", contextId, masterAddress, executionContext.getAttemptCount() + 1, maxAttempts, index, request.getAddress(), request.getType(), request.getAddress(), request.getRequestDelay());
            return acquire(executionContext)
                    .thenApply(this::updateContext)
                    .thenApply(NettyChannelContext::disableAutoRelease)
                    .thenApply(ctx -> ctx.attach(request))
                    .thenCompose(NettyChannelContext::send)
                    .handle(this::response);
        }

        private MasterServerChannelContext getContext() {
            return currentContext.get();
        }

        private InetSocketAddress nextMasterAddress() {
            InetSocketAddress[] addresses = MasterServer.getCachedMasterAddress(request.getType(), false);
            if (index > (addresses.length - 1))
                this.index = 0;
            return addresses[index++];
        }

        private CompletableFuture<NettyChannelContext> acquire(ExecutionContext<MasterServerResponse> executionContext) {
            final MasterServerChannelContext currentContext = getContext();

            boolean isValid = currentContext != null && currentContext.isValid();

            boolean acquireNew = executionContext.getLastException() != null && handleError(executionContext.getLastException());

            if (isValid && !acquireNew) {
                log.debug("{} MASTER => Reusing previous context: {} for address '{}' (Last error: {})", currentContext.id(), currentContext, masterAddress, executionContext.getLastException());
                return CompletableFuture.completedFuture(currentContext);
            } else {
                log.debug("{} MASTER => Acquiring new context for address '{}' (Previous: {}, Previous Valid: {})", currentContext != null ? currentContext.id() : "[N/A]", masterAddress, currentContext, isValid);
                CompletableFuture<NettyChannelContext> newContextFuture;
                if (currentContext != null) {
                    //if there was a previous context, we need to transfer the properties over to the new one
                    newContextFuture = MasterServerMessenger.super.acquireContext(masterAddress).thenCompose(this::copyProperties);
                    newContextFuture.thenRun(currentContext::close);
                } else {
                    newContextFuture = MasterServerMessenger.super.acquireContext(masterAddress);
                }
                return newContextFuture;
            }
        }

        private NettyChannelContext updateContext(NettyChannelContext context) {
            final MasterServerChannelContext currentContext = getContext();
            if ((context == currentContext) && (currentContext != null && currentContext.isValid()))
                return context;
            this.currentContext.set((MasterServerChannelContext) context);
            return context;
        }

        private MasterServerResponse response(NettyChannelContext context, Throwable error) {
            if (error != null) {
                Throwable cause = Errors.unwrap(error);
                if (cause instanceof ReadTimeoutException) {
                    MasterServerChannelContext ctx = getContext();
                    if (ctx != null) {
                        cause = new MasterServerTimeoutException(new HashSet<>(ctx.properties().addressSet()));
                    } else {
                        cause = new MasterServerTimeoutException(new HashSet<>());
                    }
                }
                throw new CompletionException(cause);
            }
            MasterServerResponse response = context.properties().response();
            if (log.isDebugEnabled()) {
                log.debug("Got Master Server List (Total: {})", response.getServerList().size());
                Set<InetSocketAddress> serverList = response.getServerList();
                int ctr = 1;
                for (InetSocketAddress addr : serverList) {
                    log.debug("{}) {}", ctr++, addr);
                }
            }
            return response;
        }

        public CompletableFuture<NettyChannelContext> copyProperties(final NettyChannelContext context) {
            return CompletableFuture.supplyAsync(() -> {
                MasterServerChannelContext oldContext = getContext();
                MasterServerChannelContext newContext = (MasterServerChannelContext) context;
                log.debug("{} MASTER => Copying previously collected addresses from context '{}' to '{}' (Total addresses to copy: {})", context.id(), oldContext.id(), context.id(), oldContext.properties().addressSet().size());
                newContext.properties().addressSet().addAll(new HashSet<>(oldContext.properties().addressSet()));
                newContext.properties().lastSeedAddress(oldContext.properties().lastSeedAddress());
                return newContext;
            }, context.eventLoop());
        }

        private void onCompletion(MasterServerResponse response, Throwable throwable) {
            MasterServerChannelContext context = getContext();
            if (context != null && context.isValid()) {
                context.close();
            }
        }
    }
}
