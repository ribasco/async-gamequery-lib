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
import dev.failsafe.event.ExecutionCompletedEvent;
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

    private FailsafeExecutor<SourceQueryResponse<?>> executor;

    private RateLimiter<SourceQueryResponse<?>> rateLimiter;

    private RetryPolicy<SourceQueryResponse<?>> retryPolicy;

    private final boolean failsafeEnabled;

    private final EventListener<ExecutionCompletedEvent<SourceQueryResponse<?>>> retryExceededListener = new EventListener<ExecutionCompletedEvent<SourceQueryResponse<?>>>() {
        @Override
        public void accept(ExecutionCompletedEvent<SourceQueryResponse<?>> event) throws Throwable {
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

        final List<Policy<SourceQueryResponse<?>>> policies = new ArrayList<>();

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

    private RetryPolicy<SourceQueryResponse<?>> buildRetryPolicy(final Options options) {
        RetryPolicyBuilder<SourceQueryResponse<?>> builder = FailsafeBuilder.buildRetryPolicy(options);
        builder.abortOn(RejectedExecutionException.class);
        builder.onRetriesExceeded(retryExceededListener);
        return builder.build();
    }

    private RateLimiter<SourceQueryResponse<?>> buildRateLimiterPolicy(final Options options) {
        RateLimiterBuilder<SourceQueryResponse<?>> builder = FailsafeBuilder.buildRateLimiter(options);
        return builder.build();
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<SourceQueryResponse<?>> send(InetSocketAddress address, SourceQueryRequest request) {
        if (executor != null && failsafeEnabled) {
            return executor.getStageAsync(new QueryContextSupplier(address, request));
        } else {
            return sendQuery(address, request);
        }
    }

    private CompletableFuture<SourceQueryResponse<?>> sendQuery(InetSocketAddress address, SourceQueryRequest request) {
        if (getExecutor().isShutdown() || getExecutor().isShuttingDown() || getExecutor().isTerminated())
            return Concurrency.failedFuture(new RejectedExecutionException());
        CompletableFuture<NettyChannelContext> contextFuture = acquireContext(new ImmutablePair<>(address, request));
        return contextFuture
                .thenApply(NettyChannelContext::disableAutoRelease)
                .thenCombine(CompletableFuture.completedFuture(request), SourceQueryMessenger::attach)
                .thenApply(this::acquireSendPermit)
                .thenCompose(super::send)
                .handle(this::response);
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

    /**
     * <p>Acquire a send permit. If no permits available, this method will block until there is one</p>
     *
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext acquireSendPermit(NettyChannelContext context) {
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

    private static NettyChannelContext attach(NettyChannelContext context, SourceQueryRequest request) {
        context.attach(request);
        return context;
    }

    private SourceQueryResponse<?> response(NettyChannelContext context, Throwable error) {
        try {
            if (error != null) {
                if (error instanceof MessengerException) {
                    MessengerException ex = (MessengerException) error;
                    context = ex.getContext();
                    log.debug("{} MESSENGER (SourceQueryMessenger) => Releasing context '{}' in error", context.id(), context, error);
                    throw ex;
                } else {
                    throw new CompletionException(Errors.unwrap(error));
                }
            } else {
                assert context != null;
                SourceQueryResponse<?> response = context.properties().response();
                if (response == null)
                    throw new IllegalStateException("Missing response: " + context);
                return response;
            }
        } finally {
            if (context != null)
                context.close();
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
            if (data instanceof ImmutablePair<?, ?>) {
                //noinspection unchecked
                ImmutablePair<InetSocketAddress, SourceQueryRequest> pair = (ImmutablePair<InetSocketAddress, SourceQueryRequest>) data;
                return pair.getFirst();
            } else {
                return defaultResolver.resolveRemoteAddress(data);
            }
        }
    }

    private class QueryContextSupplier implements ContextualSupplier<SourceQueryResponse<?>, CompletableFuture<SourceQueryResponse<?>>> {

        private final InetSocketAddress address;

        private final SourceQueryRequest request;

        private final int maxAttemptCount;

        private QueryContextSupplier(InetSocketAddress address, SourceQueryRequest request) {
            this.address = address;
            this.request = request;
            this.maxAttemptCount = retryPolicy.getConfig().getMaxAttempts();
        }

        @Override
        public CompletableFuture<SourceQueryResponse<?>> get(ExecutionContext<SourceQueryResponse<?>> executionContext) throws Throwable {
            log.debug("MESSENGER (SourceQueryMessenger) => Executing request for address '{}' with request '{}' (Attempts: {}, Last Error: {}, Is Retry: {})", address, request, executionContext.getAttemptCount(), executionContext.getLastException() != null ? executionContext.getLastException().getClass().getSimpleName() : "N/A", executionContext.isRetry());
            return sendQuery(address, request).handle((response, error) -> handleTimeouts(response, error, executionContext));
        }

        public SourceQueryResponse<?> handleTimeouts(SourceQueryResponse<?> response, Throwable error, ExecutionContext<SourceQueryResponse<?>> executionContext) {
            if (error != null) {
                if (error instanceof MessengerException) {
                    Throwable cause = Errors.unwrap(error);
                    NettyChannelContext ctx = ((MessengerException) error).getContext();
                    int attemptCount = executionContext.getAttemptCount() + 1;
                    if (attemptCount >= maxAttemptCount)
                        throw new MaxAttemptsReachedException(cause, ctx.properties().envelope().recipient(), ctx.properties().request(), attemptCount, maxAttemptCount);
                    throw (MessengerException) error;
                } else {
                    throw new CompletionException(Errors.unwrap(error));
                }
            }
            return response;
        }
    }
}
