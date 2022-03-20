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

package com.ibasco.agql.core.transport;

import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.TransportOptions;
import dev.failsafe.*;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.Channel;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * Adds {@link Failsafe} support for the underlying {@link NettyChannelFactory}.
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unused")
public class FailsafeChannelFactory extends NettyChannelFactoryDecorator {

    private static final Logger log = LoggerFactory.getLogger(FailsafeChannelFactory.class);

    /**
     * A map for contextual suppliers. For every address, there is only one supplier
     */
    private static final ConcurrentMap<InetSocketAddress, ChannelSupplier> supplierMap = new ConcurrentHashMap<>();

    private final FailsafeExecutor<Channel> acquireExecutor;

    private final ScheduledExecutorService acquireScheduler;

    protected FailsafeChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        this.acquireScheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("agql-acquire"));
        this.acquireExecutor = Failsafe.with(newRetryPolicy()).with(acquireScheduler);
    }

    protected void configureRetryPolicy(RetryPolicyBuilder<Channel> builder) {}

    @Override
    public CompletableFuture<Channel> create(Object data) {
        return acquireExecutor.getStageAsync(getContextualSupplier(data));
    }

    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return NettyUtil.useEventLoop(create(data), eventLoop);
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            log.debug("CHANNEL_FACTORY ({}) => Shutting down acquire scheduler", getClass().getSimpleName());
            ConcurrentUtil.shutdown(acquireScheduler);
        }
    }

    private RetryPolicy<Channel> newRetryPolicy() {
        RetryPolicyBuilder<Channel> retryPolicyBuilder = RetryPolicy.builder();
        retryPolicyBuilder.handle(ConnectTimeoutException.class, ConnectException.class)
                          .abortIf(channel -> channel.eventLoop().isShutdown() || channel.eventLoop().isShuttingDown())
                          .abortOn(RejectedExecutionException.class)
                          .onRetry(event -> log.error("CHANNEL_FACTORY ({}) => Failed to acquire channel. Retrying (Attempts: {}, Last Failure: {})", getClass().getSimpleName(), event.getAttemptCount(), event.getLastFailure() != null ? event.getLastFailure().getClass().getSimpleName() : "N/A"))
                          .withMaxAttempts(getOptions().getOrDefault(TransportOptions.FAILSAFE_ACQUIRE_MAX_CONNECT))
                          .withBackoff(Duration.ofSeconds(1), Duration.ofSeconds(5));
        configureRetryPolicy(retryPolicyBuilder);
        return retryPolicyBuilder.build();
    }

    private ChannelSupplier getContextualSupplier(final Object data) {
        return supplierMap.computeIfAbsent(getResolver().resolveRemoteAddress(data), ChannelSupplier::new);
    }

    private class ChannelSupplier implements ContextualSupplier<Channel, CompletableFuture<Channel>> {

        private final InetSocketAddress address;

        private ChannelSupplier(InetSocketAddress address) {
            this.address = address;
        }

        @Override
        public CompletableFuture<Channel> get(ExecutionContext<Channel> context) throws Throwable {
            log.debug("CHANNEL_FACTORY ({}) => Acquiring channel for address '{}' (Supplier: {}, Attempt: {}, Executions: {}, Last Result: {}, Last Failure: {})", getClass().getSimpleName(), address, this, context.getAttemptCount(), context.getExecutionCount(), context.getLastResult(), context.getLastFailure());
            return FailsafeChannelFactory.super.create(address);
        }
    }
}