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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.exceptions.AgqlRuntimeException;
import com.ibasco.agql.core.exceptions.MaxAttemptsReachedException;
import com.ibasco.agql.core.exceptions.MessengerException;
import com.ibasco.agql.core.exceptions.RejectedRequestException;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.transport.DefaultChannlContextFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.NettyPropertyResolver;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.transport.pool.NettyPoolPropertyResolver;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;
import dev.failsafe.*;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.event.ExecutionCompletedEvent;
import dev.failsafe.function.CheckedFunction;
import dev.failsafe.function.ContextualSupplier;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Messenger implementation for the Source Query Protocol
 *
 * @author Rafael Luis Ibasco
 */
@MessengerProperties(optionClass = SourceQueryOptions.class)
public final class SourceQueryMessenger extends NettyMessenger<SourceQueryRequest, SourceQueryResponse<?>> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    private final boolean failsafeEnabled;

    /**
     * Executor that is used for acquiring permits (applicable only if rate limiting is enabled)
     */
    private final ExecutorService permitExecutor;

    private FailsafeExecutor<NettyChannelContext> executor;

    private RateLimiter<NettyChannelContext> rateLimiter;

    private RetryPolicy<NettyChannelContext> retryPolicy;

    private final EventListener<ExecutionCompletedEvent<NettyChannelContext>> retryExceededListener = new EventListener<ExecutionCompletedEvent<NettyChannelContext>>() {
        @Override
        public void accept(ExecutionCompletedEvent<NettyChannelContext> event) throws Throwable {
            if (event.getException() instanceof MaxAttemptsReachedException) {
                MaxAttemptsReachedException mException = (MaxAttemptsReachedException) event.getException();
                log.error("Maximum number of attempts reached on address '{}' for request '{}' (Attempts: {}, Max Attempts: {}, Elapsed: {}, Last Error: {})", mException.getRemoteAddress(), mException.getRequest(), event.getAttemptCount(), mException.getMaxAttemptCount(), Time.getTimeDesc(event.getElapsedTime()), simplify(mException.getCause()));
            } else {
                log.error("Maximum number of attempts reached for request (Attempts: {}, Max Attempts: {})", event.getAttemptCount(), retryPolicy.getConfig().getMaxAttempts(), event.getException());
            }
        }

        //if it's a timeout exception, just return the name. we do not need to
        // print the whole stacktrace for these types of exceptions
        public Object simplify(Throwable error) {
            if (error == null)
                return "N/A";
            if (error instanceof TimeoutException) {
                return error.getClass().getSimpleName();
            }
            return error;
        }
    };

    /**
     * <p>Constructor for SourceQueryMessenger.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    public SourceQueryMessenger(Options options) {
        super(options);
        //note: use getOptions() instead of options, to guarante that we do not receive a null value in case developer did not provide a user-defined options.
        this.failsafeEnabled = getOptions().getOrDefault(FailsafeOptions.FAILSAFE_ENABLED);
        this.permitExecutor = failsafeEnabled ? Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("rate-limiter")) : null;
        initFailSafe(getOptions());
    }

    private void initFailSafe(final Options options) {
        if (!failsafeEnabled)
            return;

        final List<Policy<NettyChannelContext>> policies = new ArrayList<>();

        //fallback policy
        Fallback<NettyChannelContext> fallbackPolicy = buildFallbackPolicy(options);
        policies.add(fallbackPolicy);

        //retry policy
        if (options.getOrDefault(FailsafeOptions.FAILSAFE_RETRY_ENABLED)) {
            this.retryPolicy = buildRetryPolicy(options);
            policies.add(retryPolicy);
        }
        //rate limiter (standalone)
        if (options.getOrDefault(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED)) {
            //note: we will not add the rate limiter to the list of policies for the executor. since we are using the standalone way of handling permits.
            this.rateLimiter = buildRateLimiterPolicy(options);
        }

        //Initialize executor
        this.executor = Failsafe.with(policies).with(getExecutor());
    }

    private Fallback<NettyChannelContext> buildFallbackPolicy(final Options options) {
        return Fallback.builderOfException((CheckedFunction<ExecutionAttemptedEvent<? extends NettyChannelContext>, Exception>) event -> {
            int maxAttempts = retryPolicy != null ? retryPolicy.getConfig().getMaxAttempts() : FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS.getDefaultValue();
            if (event.getLastException() instanceof MessengerException) {
                MessengerException mException = (MessengerException) event.getLastException();
                Throwable cause = Errors.unwrap(mException);
                if (cause instanceof TimeoutException && event.getAttemptCount() >= maxAttempts) {
                    //re-wrap the messenger exception and change the cause to MaxAttemptsReachedException
                    MaxAttemptsReachedException maxAttemptException = new MaxAttemptsReachedException(cause, mException.getRemoteAddress(), mException.getRequest(), event.getAttemptCount(), maxAttempts);
                    return new MessengerException(maxAttemptException, mException.getContext());
                }
                return mException;
            } else if (event.getLastException() instanceof CircuitBreakerOpenException) {
                CircuitBreakerOpenException openException = (CircuitBreakerOpenException) event.getLastException();
                return new RejectedRequestException("The internal circuit-breaker has been OPENED. Temporarily not accepting any more requests", openException.getCause());
            }
            return new CompletionException(Errors.unwrap(event.getLastException()));
        }).build();
    }

    private RetryPolicy<NettyChannelContext> buildRetryPolicy(final Options options) {
        RetryPolicyBuilder<NettyChannelContext> builder = FailsafeBuilder.buildRetryPolicy(FailsafeOptions.class, options);
        builder.abortOn(RejectedExecutionException.class, RateLimitExceededException.class);
        builder.onRetriesExceeded(retryExceededListener);
        /*if (Properties.isVerbose()) {
            builder.onRetry(event -> {
                Throwable error = event.getLastException();
                if (error instanceof MessengerException) {
                    MessengerException mEx = (MessengerException) error;
                    NettyChannelContext context = mEx.getContext();
                    Console.error("Last request failed. Retrying execution: (Request: %s, Attempts: %d, Error: %s)", context.properties().request(), event.getAttemptCount(), mEx.getCause());
                } else {
                    Console.error("Last request failed. Retrying execution (Attempts: %d, Error: %s)", event.getAttemptCount(), event.getLastException());
                }
            });
            builder.onSuccess(new EventListener<ExecutionCompletedEvent<NettyChannelContext>>() {
                @Override
                public void accept(ExecutionCompletedEvent<NettyChannelContext> event) throws Throwable {
                    if (event.getAttemptCount() >= 1) {
                        Console.println("[SUCCESSFUL RETRY] %s", event.getResult().properties().request());
                    }
                }
            });
        }*/
        return builder.build();
    }

    private RateLimiter<NettyChannelContext> buildRateLimiterPolicy(final Options options) {
        RateLimiterBuilder<NettyChannelContext> builder = FailsafeBuilder.buildRateLimiter(FailsafeOptions.class, options);
        return builder.build();
    }

    /** {@inheritDoc} */
    @Override
    protected void configure(Options options) {
        //default general config
        applyDefault(GeneralOptions.CONNECTION_POOLING, false);
        applyDefault(GeneralOptions.POOL_TYPE, ChannelPoolType.ADAPTIVE);
        applyDefault(GeneralOptions.POOL_MAX_CONNECTIONS, Properties.getDefaultPoolSize());
        applyDefault(GeneralOptions.READ_TIMEOUT, 5000);

        //connect options
        applyDefault(ConnectOptions.FAILSAFE_ENABLED, true);

        //connect - retry
        applyDefault(ConnectOptions.FAILSAFE_RETRY_ENABLED, true);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_DELAY, 1000L); //1000L
        applyDefault(ConnectOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 5);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 50L);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d);

        //connect - circuit breaker
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_ENABLED, true);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_DELAY, 1000);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD, Properties.getDefaultPoolSize());
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP, Properties.getDefaultPoolSize() * 2);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD, 1);

        //query - rate limiting
        applyDefault(FailsafeOptions.FAILSAFE_ENABLED, true);
        applyDefault(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED, false);
        applyDefault(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH);
        applyDefault(FailsafeOptions.FAILSAFE_RATELIMIT_PERIOD, 5000L);
        applyDefault(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 650L);
        applyDefault(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, 10000L);

        //query - retry
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_ENABLED, true);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_DELAY, 1000L); //1000L
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 5);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 50L);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d);
    }

    /** {@inheritDoc} */
    @Override
    protected NettyChannelFactory createChannelFactory() {
        NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.UDP_CONNLESS, getOptions(), new DefaultChannlContextFactory<>(this));
        channelFactory.setResolver(new PropertyResolver(channelFactory.getResolver()));
        return new SourceQueryChannelFactory(channelFactory);
    }

    //NOTE: We override this to ensure that we only acquire channels from a single pool instance (if pooling is enabled).
    // By overriding this, we then need to make sure to register a custom property resolver.

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<SourceQueryResponse<?>> send(InetSocketAddress address, SourceQueryRequest request) {
        CompletableFuture<NettyChannelContext> future;
        RequestContext query = new RequestContext(address, request);
        if (executor != null && failsafeEnabled)
            future = executor.getStageAsync((ContextualSupplier<NettyChannelContext, CompletableFuture<NettyChannelContext>>) query::execute);
        else
            future = query.execute();
        return future.handle(query::completion);
    }

    /** {@inheritDoc} */
    @Override
    protected Object transformProperties(InetSocketAddress address, SourceQueryRequest request) {
        return new ImmutablePair<>(address, request);
    }

    @Override
    public void close() throws IOException {
        super.close();
        Concurrency.shutdown(permitExecutor);
    }

    private static class PropertyResolver implements NettyPoolPropertyResolver {

        private final NettyPropertyResolver defaultResolver;

        private PropertyResolver(NettyPropertyResolver defaultResolver) {
            this.defaultResolver = defaultResolver;
        }

        @Override
        public Object resolvePoolKey(Object data) {
            //use a single key for every channel acquisition to ensure we get the same pool instance for each request
            return SourceQuery.class;
        }

        @Override
        public InetSocketAddress resolveRemoteAddress(Object data) throws IllegalStateException {
            if (data instanceof RequestContext) {
                return ((RequestContext) data).getAddress();
            } else {
                return defaultResolver.resolveRemoteAddress(data);
            }
        }
    }

    private class RequestContext {

        private final InetSocketAddress address;

        private final SourceQueryRequest request;

        private NettyChannelContext context;

        private RequestContext(InetSocketAddress address, SourceQueryRequest request) {
            this.address = address;
            this.request = request;
        }

        public CompletableFuture<NettyChannelContext> execute(ExecutionContext<NettyChannelContext> context) {
            return execute();
        }

        public CompletableFuture<NettyChannelContext> execute() {
            if (getExecutor().isShutdown() || getExecutor().isShuttingDown() || getExecutor().isTerminated())
                return Concurrency.failedFuture(new RejectedExecutionException());
            CompletableFuture<NettyChannelContext> contextFuture = acquireContext(this);
            contextFuture.thenAccept(this::initialize);
            if (failsafeEnabled) {
                //make sure we do not block the event loop, so we need to run this at another thread
                contextFuture = contextFuture.thenApplyAsync(this::acquirePermit, permitExecutor);
            }
            return contextFuture.thenCompose(SourceQueryMessenger.super::send);
        }

        private void initialize(NettyChannelContext context) {
            setContext(context);
            context.disableAutoRelease();
            context.attach(request);
        }

        /**
         * <p>Acquire a send permit. If no permits available, this method will block until there is one</p>
         *
         * @param context
         *         a {@link com.ibasco.agql.core.NettyChannelContext} object
         *
         * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
         */
        private NettyChannelContext acquirePermit(NettyChannelContext context) {
            if (!failsafeEnabled || rateLimiter == null)
                return context;
            try {
                log.debug("{} MESSENGER => (SourceQueryMessenger) Acquiring send permit from rate limiter: {} (Request: {})", context.id(), rateLimiter, context.properties().request());
                Console.println("Acquiring send permit for %s (Max Rate: %dms)", context.properties().request(), (rateLimiter.getConfig().getMaxRate() != null) ? rateLimiter.getConfig().getMaxRate().toMillis() : -1);
                rateLimiter.acquirePermit();
                log.debug("{} MESSENGER => (SourceQueryMessenger) Successfully acquired permit from rate limiter: {} (Request: {})", context.id(), rateLimiter, context.properties().request());
                return context;
            } catch (InterruptedException e) {
                log.debug("{} MESSENGER => (SourceQueryMessenger) Successfully acquired permit from rate limiter: {} (Request: {})", context.id(), rateLimiter, context.properties().request(), e);
                throw new AgqlRuntimeException(e);
            }
        }

        private void setContext(NettyChannelContext context) {
            this.context = context;
        }

        private InetSocketAddress getAddress() {
            return address;
        }

        private SourceQueryResponse<?> completion(NettyChannelContext context, Throwable error) {
            try {
                if (error != null) {
                    Exception cause = (Exception) Errors.unwrap(error);
                    if (error instanceof MessengerException) {
                        context = ((MessengerException) error).getContext();
                        log.debug("{} MESSENGER (SourceQueryMessenger) => Releasing context '{}' in error", context.id(), context, error);
                    }
                    throw new CompletionException(cause);
                } else {
                    assert context != null;
                    SourceQueryResponse<?> response = context.properties().response();
                    if (response == null)
                        throw new IllegalStateException("Missing response: " + context);
                    return response;
                }
            } finally {
                if (this.context != null) {
                    this.context.close();
                }
            }
        }
    }
}
