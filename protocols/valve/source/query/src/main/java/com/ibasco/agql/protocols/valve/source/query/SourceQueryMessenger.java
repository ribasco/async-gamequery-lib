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
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.transport.DefaultChannlContextFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.NettyPropertyResolver;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.transport.pool.NettyPoolPropertyResolver;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import dev.failsafe.*;
import dev.failsafe.function.ContextualSupplier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Messenger implementation for the Source Query Protocol
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceQueryMessenger extends NettyMessenger<SourceQueryRequest, SourceQueryResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    private FailsafeExecutor<SourceQueryResponse> executor;

    private RateLimiter<SourceQueryResponse> rateLimiter;

    private RetryPolicy<SourceQueryResponse> retryPolicy;

    public SourceQueryMessenger(Options options) {
        super(options);
        initFailSafe(options);
    }

    private void initFailSafe(final Options options) {
        if (!getOrDefault(SourceQueryOptions.FAILSAFE_ENABLED))
            return;

        final List<Policy<SourceQueryResponse>> policies = new ArrayList<>();

        //retry policy
        if (options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_ENABLED)) {
            this.retryPolicy = buildRetryPolicy(options);
            policies.add(retryPolicy);
        }
        //rate limiter
        if (options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED)) {
            this.rateLimiter = buildRateLimiterPolicy(options);
            policies.add(rateLimiter);
        }
        //Initialize executor
        this.executor = Failsafe.with(policies).with(getExecutor());
    }

    private RetryPolicy<SourceQueryResponse> buildRetryPolicy(final Options options) {
        RetryPolicyBuilder<SourceQueryResponse> builder = RetryPolicy.<SourceQueryResponse>builder().handleIf(e -> e instanceof TimeoutException);
        Long retryDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_DELAY);
        Integer maxAttempts = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_MAX_ATTEMPTS);
        Boolean backOffEnabled = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_ENABLED);
        if (retryDelay != null)
            builder.withDelay(Duration.ofMillis(retryDelay));
        if (maxAttempts != null)
            builder.withMaxAttempts(maxAttempts);
        if (backOffEnabled != null && backOffEnabled) {
            Long backoffDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY);
            Long backoffMaxDelay = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY);
            Double backoffDelayFactor = options.getOrDefault(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR);
            builder.withBackoff(Duration.ofMillis(backoffDelay), Duration.ofMillis(backoffMaxDelay), backoffDelayFactor);
        }
        return builder.build();
    }

    private RateLimiter<SourceQueryResponse> buildRateLimiterPolicy(final Options options) {
        Long maxExecutions = options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_MAX_EXEC);
        Long period = options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_PERIOD);
        Long maxWaitTime = options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_MAX_WAIT_TIME);
        RateLimitType rateLimitType = options.getOrDefault(SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE);
        //noinspection unchecked
        RateLimiterBuilder<SourceQueryResponse> builder = (RateLimiterBuilder<SourceQueryResponse>) rateLimitType.getBuilder().apply(maxExecutions, Duration.ofMillis(period));
        if (maxWaitTime != null)
            builder.withMaxWaitTime(Duration.ofMillis(maxWaitTime));
        return builder.build();
    }

    @Override
    public CompletableFuture<SourceQueryResponse> send(InetSocketAddress address, SourceQueryRequest request) {
        if (executor != null && getOrDefault(SourceQueryOptions.FAILSAFE_ENABLED)) {
            return executor.getStageAsync(new QueryContextSupplier(address, request));
        } else {
            return super.send(address, request);
        }
    }

    @Override
    protected void configure(Options options) {
        //enable pooling by default
        defaultOption(options, TransportOptions.CONNECTION_POOLING, true);
        defaultOption(options, TransportOptions.POOL_TYPE, ChannelPoolType.ADAPTIVE);
        defaultOption(options, TransportOptions.POOL_MAX_CONNECTIONS, Platform.getDefaultPoolSize());
        defaultOption(options, TransportOptions.READ_TIMEOUT, 1500);
        //default rate limiting options
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED, false);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_PERIOD, 5000L);
        defaultOption(options, SourceQueryOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 650L);
    }

    //NOTE: We override this to ensure that we only acquire channels from a single pool instance (if pooling is enabled).
    // By overriding this, we then need to make sure to register a custom property resolver.
    @Override
    protected Object transformProperties(InetSocketAddress address, SourceQueryRequest request) {
        return new ImmutablePair<>(address, request);
    }

    @Override
    protected NettyChannelFactory createChannelFactory() {
        NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.UDP_CONNLESS, getOptions(), new DefaultChannlContextFactory<>(this));
        final NettyPropertyResolver defaultResolver = channelFactory.getResolver();
        channelFactory.setResolver(new NettyPoolPropertyResolver() {
            @Override
            public Object resolvePoolKey(Object data) {
                //use a single key for every channel acquisition to ensure we get the same pool instance for each request
                //log.info("DATA: {}", data);
                /*if (data instanceof Pair<?, ?>) {
                    Pair<?, ?> pair = (Pair<?, ?>) data;
                    //log.info("DATA: {}, {}", pair.getFirst(), pair.getSecond());
                    return pair.getSecond().getClass();
                }*/
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
        });
        return new SourceQueryChannelFactory(channelFactory);
    }

    private static class RequestEntry extends ImmutablePair<InetSocketAddress, SourceQueryRequest> {

        private RequestEntry(InetSocketAddress address, SourceQueryRequest request) {
            super(address, request);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof RequestEntry)) return false;

            RequestEntry that = (RequestEntry) o;

            return new EqualsBuilder().append(getFirst(), that.getFirst()).append(getSecond(), that.getSecond()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(getFirst()).append(getSecond()).toHashCode();
        }
    }

    private class QueryContextSupplier implements ContextualSupplier<SourceQueryResponse, CompletableFuture<SourceQueryResponse>> {

        private final InetSocketAddress address;

        private final SourceQueryRequest request;

        private QueryContextSupplier(InetSocketAddress address, SourceQueryRequest request) {
            this.address = address;
            this.request = request;
        }

        @Override
        public CompletableFuture<SourceQueryResponse> get(ExecutionContext<SourceQueryResponse> context) throws Throwable {
            log.debug("MESSENGER (SourceQueryMessenger) => Executing request for address '{}' with request '{}' (Attempts: {})", address, request, context.getAttemptCount());
            CompletableFuture<NettyChannelContext> contextFuture = acquireContext(new ImmutablePair<>(address, request));
            return contextFuture
                    .thenApply(NettyChannelContext::disableAutoRelease)
                    .thenApply(this::attach)
                    .thenCompose(SourceQueryMessenger.super::send)
                    .thenCompose(NettyChannelContext::composedFuture)
                    .handle(this::response);
        }

        private NettyChannelContext attach(NettyChannelContext context) {
            context.attach(request);
            return context;
        }

        private SourceQueryResponse response(NettyChannelContext context, Throwable error) {
            if (error != null) {
                if (error instanceof ResponseException) {
                    ResponseException responseEx = (ResponseException) error;
                    context = responseEx.getContext();
                    log.debug("{} MESSENGER (SourceQueryMessenger) => Releasing context '{}' in error", context.id(), context, error);
                    context.close();
                    throw responseEx;
                }
                throw new CompletionException(ConcurrentUtil.unwrap(error));
            }
            SourceQueryResponse response = context.properties().response();
            if (response == null)
                throw new IllegalStateException("Missing response: " + context);
            context.close();
            return response;
        }
    }
}
