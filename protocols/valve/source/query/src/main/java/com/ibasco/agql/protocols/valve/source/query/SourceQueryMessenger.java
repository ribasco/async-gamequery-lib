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
import com.ibasco.agql.core.exceptions.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;

/**
 * Messenger implementation for the Source Query Protocol
 *
 * @author Rafael Luis Ibasco
 */
@MessengerProperties(optionClass = SourceQueryOptions.class)
public final class SourceQueryMessenger extends NettyMessenger<SourceQueryRequest, SourceQueryResponse<?>> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    private FailsafeExecutor<NettyChannelContext> executor;

    private RateLimiter<NettyChannelContext> rateLimiter;

    private RetryPolicy<NettyChannelContext> retryPolicy;

    private final boolean failsafeEnabled;

    private final EventListener<ExecutionCompletedEvent<NettyChannelContext>> retryExceededListener = new EventListener<ExecutionCompletedEvent<NettyChannelContext>>() {
        @Override
        public void accept(ExecutionCompletedEvent<NettyChannelContext> event) throws Throwable {
            if (event.getException() instanceof MaxAttemptsReachedException) {
                MaxAttemptsReachedException mException = (MaxAttemptsReachedException) event.getException();
                log.error("Maximum number of attempts reached on address '{}' for request '{}' (Attempts: {}, Max Attempts: {}, Elapsed: {}, Cause: {})", mException.getRemoteAddress(), mException.getRequest(), event.getAttemptCount(), mException.getMaxAttemptCount(), Time.getTimeDesc(event.getElapsedTime()), simplify(mException.getCause()));
            } else {
                log.error("Maximum number of attempts reached for request (Attempts: {}, Max Attempts: {}, Error: {})", event.getAttemptCount(), retryPolicy.getConfig().getMaxAttempts(), simplify(event.getException()), event.getException());
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
        this.failsafeEnabled = getOptions().getOrDefault(SourceQueryOptions.FAILSAFE_ENABLED);
        initFailSafe(getOptions());
    }

    /** {@inheritDoc} */
    @Override
    protected void configure(Options options) {
        //enable pooling by default
        defaultOption(options, GlobalOptions.CONNECTION_POOLING, false);
        defaultOption(options, GlobalOptions.POOL_TYPE, ChannelPoolType.ADAPTIVE);
        defaultOption(options, GlobalOptions.POOL_MAX_CONNECTIONS, Properties.getDefaultPoolSize());
        defaultOption(options, GlobalOptions.READ_TIMEOUT, 10000);
        //default rate limiting options
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED, false);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_PERIOD, 5000L);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 650L);
    }

    private void initFailSafe(final Options options) {
        if (!failsafeEnabled)
            return;

        final List<Policy<NettyChannelContext>> policies = new ArrayList<>();

        //fallback policy
        Fallback<NettyChannelContext> fallbackPolicy = buildFallbackPolicy(options);
        policies.add(fallbackPolicy);

        //retry policy
        if (options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_ENABLED)) {
            this.retryPolicy = buildRetryPolicy(options);
            policies.add(retryPolicy);
        }
        //rate limiter (standalone)
        if (options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED)) {
            this.rateLimiter = buildRateLimiterPolicy(options);
        }

        //Initialize executor
        this.executor = Failsafe.with(policies).with(getExecutor());
    }

    private Fallback<NettyChannelContext> buildFallbackPolicy(final Options options) {
        return Fallback.builderOfException((CheckedFunction<ExecutionAttemptedEvent<? extends NettyChannelContext>, Exception>) event -> {
            int maxAttempts = retryPolicy.getConfig().getMaxAttempts();
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
        RetryPolicyBuilder<NettyChannelContext> builder = FailsafeBuilder.buildRetryPolicy(options);
        builder.abortOn(RejectedExecutionException.class);
        builder.onRetriesExceeded(retryExceededListener);
        return builder.build();
    }

    private RateLimiter<NettyChannelContext> buildRateLimiterPolicy(final Options options) {
        RateLimiterBuilder<NettyChannelContext> builder = FailsafeBuilder.buildRateLimiter(options);
        return builder.build();
    }

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

    //NOTE: We override this to ensure that we only acquire channels from a single pool instance (if pooling is enabled).
    // By overriding this, we then need to make sure to register a custom property resolver.

    /** {@inheritDoc} */
    @Override
    protected Object transformProperties(InetSocketAddress address, SourceQueryRequest request) {
        return new ImmutablePair<>(address, request);
    }

    /** {@inheritDoc} */
    @Override
    protected NettyChannelFactory createChannelFactory() {
        NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.UDP_CONNLESS, getOptions(), new DefaultChannlContextFactory<>(this));
        channelFactory.setResolver(new PropertyResolver(channelFactory.getResolver()));
        return new SourceQueryChannelFactory(channelFactory);
    }

    private class RequestContext {

        private final InetSocketAddress address;

        private final SourceQueryRequest request;

        private NettyChannelContext context;

        private RequestContext(InetSocketAddress address, SourceQueryRequest request) {
            this.address = address;
            this.request = request;
        }

        private void initialize(NettyChannelContext context) {
            setContext(context);
            context.disableAutoRelease();
            context.attach(request);
        }

        public CompletableFuture<NettyChannelContext> execute() {
            if (getExecutor().isShutdown() || getExecutor().isShuttingDown() || getExecutor().isTerminated())
                return Concurrency.failedFuture(new RejectedExecutionException());
            CompletableFuture<NettyChannelContext> contextFuture = acquireContext(this);
            contextFuture.thenAccept(this::initialize);
            return contextFuture
                    .thenApply(this::acquirePermit)
                    .thenCompose(SourceQueryMessenger.super::send);
        }

        public CompletableFuture<NettyChannelContext> execute(ExecutionContext<NettyChannelContext> context) {
            return execute();
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
                    assert this.context == context;
                    this.context.close();
                }
            }
        }
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
}
