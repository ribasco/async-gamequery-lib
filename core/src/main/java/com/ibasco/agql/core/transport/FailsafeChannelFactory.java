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

import com.ibasco.agql.core.util.Console;
import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.GlobalOptions;
import com.ibasco.agql.core.util.Netty;
import dev.failsafe.*;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.event.ExecutionCompletedEvent;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;

/**
 * Adds {@link dev.failsafe.Failsafe} support for the underlying {@link com.ibasco.agql.core.transport.NettyChannelFactory}.
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

    private final RetryPolicy<Channel> retryPolicy;

    private final CircuitBreaker<Channel> circuitBreakerPolicy;

    /**
     * <p>Constructor for FailsafeChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    protected FailsafeChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        this.retryPolicy = buildRetryPolicy();
        this.circuitBreakerPolicy = buildCircuitBreakerPolicy();
        this.acquireExecutor = Failsafe.with(retryPolicy, circuitBreakerPolicy).with(channelFactory.getExecutor());
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data) {
        return acquireExecutor.getStageAsync(new ChannelSupplier(getResolver().resolveRemoteAddress(data)));//getContextualSupplier(data);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return Netty.useEventLoop(create(data), eventLoop);
    }

    private CircuitBreaker<Channel> buildCircuitBreakerPolicy() {
        CircuitBreakerBuilder<Channel> builder = CircuitBreaker.builder();
        builder.handleIf(e -> Errors.unwrap(e) instanceof ConnectException);
        builder.withFailureThreshold(3, 10);
        builder.withSuccessThreshold(1);
        builder.withDelay(Duration.ofSeconds(5));
        return builder.build();
    }

    private RetryPolicy<Channel> buildRetryPolicy() {
        RetryPolicyBuilder<Channel> builder = RetryPolicy.builder();
        builder.handleIf(e -> Errors.unwrap(e) instanceof ConnectException);
        builder.abortIf(channel -> channel.eventLoop().isShutdown() || channel.eventLoop().isShuttingDown());
        builder.abortOn(RejectedExecutionException.class);
        builder.onRetry(new EventListener<ExecutionAttemptedEvent<Channel>>() {
            @Override
            public void accept(ExecutionAttemptedEvent<Channel> event) throws Throwable {
                Console.error("[CONNECT] Retrying connect (Reason: %s, Attempts: %d)", event.getLastException(), event.getAttemptCount());
                log.error("CHANNEL_FACTORY ({}) => Failed to acquire channel. Retrying (Attempts: {}, Last Failure: {})", getClass().getSimpleName(), event.getAttemptCount(), event.getLastException() != null ? event.getLastException().getClass().getSimpleName() : "N/A");
            }
        });
        builder.onRetriesExceeded(new EventListener<ExecutionCompletedEvent<Channel>>() {
            @Override
            public void accept(ExecutionCompletedEvent<Channel> event) throws Throwable {
                Console.error("[CONNECT] Retriex Exceeded: %d (Error: %s)", event.getAttemptCount(), event.getException());
            }
        });
        builder.onFailure(new EventListener<ExecutionCompletedEvent<Channel>>() {
            @Override
            public void accept(ExecutionCompletedEvent<Channel> event) throws Throwable {
                Console.error("[CONNECT] Unable to connect to server (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount());
            }
        });
        builder.withMaxAttempts(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_MAX_CONNECT));
        builder.withBackoff(Duration.ofSeconds(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_BACKOFF_MIN)), Duration.ofSeconds(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_BACKOFF_MAX)));
        return builder.build();
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
            log.debug("CHANNEL_FACTORY ({}) => Acquiring channel for address '{}' (Supplier: {}, Attempt: {}, Executions: {}, Last Result: {}, Last Failure: {})", FailsafeChannelFactory.class.getSimpleName(), address, this, context.getAttemptCount(), context.getExecutionCount(), context.getLastResult(), context.getLastException());
            return FailsafeChannelFactory.super.create(address);
        }
    }
}
