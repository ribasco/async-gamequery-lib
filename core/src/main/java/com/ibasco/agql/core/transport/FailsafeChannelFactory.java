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

import com.ibasco.agql.core.exceptions.RejectedRequestException;
import com.ibasco.agql.core.util.*;
import dev.failsafe.*;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.event.ExecutionCompletedEvent;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * Adds {@link dev.failsafe.Failsafe} support for the underlying {@link com.ibasco.agql.core.transport.NettyChannelFactory}.
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class FailsafeChannelFactory extends NettyChannelFactoryDecorator {

    private static final Logger log = LoggerFactory.getLogger(FailsafeChannelFactory.class);

    /**
     * A map for contextual suppliers. For every address, there is only one supplier
     */
    private static final ConcurrentMap<InetSocketAddress, ChannelSupplier> supplierMap = new ConcurrentHashMap<>();

    private final FailsafeExecutor<Channel> acquireExecutor;

    private final RetryPolicy<Channel> retryPolicy;

    private final CircuitBreaker<Channel> circuitBreaker;

    private final Fallback<Channel> fallbackPolicy;

    /**
     * <p>Constructor for FailsafeChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    protected FailsafeChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        Options globalOptions = GlobalOptions.getContainer();
        this.fallbackPolicy = buildFallbackPolicy(globalOptions);
        this.retryPolicy = buildRetryPolicy(globalOptions);
        this.circuitBreaker = buildCircuitBreakerPolicy(globalOptions);
        this.acquireExecutor = Failsafe.with(fallbackPolicy, retryPolicy, circuitBreaker).with(getExecutor());
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data) {
        return acquireExecutor.getStageAsync(getContextualSupplier(data));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return Netty.useEventLoop(create(data), eventLoop);
    }

    private Fallback<Channel> buildFallbackPolicy(final Options options) {
        FallbackBuilder<Channel> builder = Fallback.builderOfException(event -> {
            if (event.getLastException() instanceof CircuitBreakerOpenException) {
                CircuitBreakerOpenException openException = (CircuitBreakerOpenException) event.getLastException();
                return new RejectedRequestException("The internal circuit-breaker has been OPENED. Temporarily not accepting any more requests", openException.getCause());
            }
            return new CompletionException(Errors.unwrap(event.getLastException()));
        });
        return builder.build();
    }

    private CircuitBreaker<Channel> buildCircuitBreakerPolicy(final Options options) {
        CircuitBreakerBuilder<Channel> builder = FailsafeBuilder.buildCircuitBreaker(options);
        builder.handleIf(e -> Errors.unwrap(e) instanceof ConnectException);
        return builder.build();
    }

    private RetryPolicy<Channel> buildRetryPolicy(final Options options) {
        RetryPolicyBuilder<Channel> builder = FailsafeBuilder.buildRetryPolicy(options);
        builder.handleIf(e -> Errors.unwrap(e) instanceof SocketException); //handle all instances of socket related exceptions
        builder.abortIf(channel -> {
            EventLoopGroup group = channel.eventLoop().parent();
            return group.isShutdown() || group.isShuttingDown() || group.isTerminated();
        });
        builder.abortOn(RejectedExecutionException.class);
        if (Properties.isVerbose()) {
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
                    Console.error("[CONNECT] Retries Exceeded: %d (Error: %s)", event.getAttemptCount(), event.getException());
                }
            });
            builder.onFailure(new EventListener<ExecutionCompletedEvent<Channel>>() {
                @Override
                public void accept(ExecutionCompletedEvent<Channel> event) throws Throwable {
                    Console.error("[CONNECT] Unable to connect to server. All attempts have failed. (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount());
                }
            });
            builder.onAbort(new EventListener<ExecutionCompletedEvent<Channel>>() {
                @Override
                public void accept(ExecutionCompletedEvent<Channel> event) throws Throwable {
                    Console.colorize().red().text("[CONNECT]").white().textln("Retry Aborted (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount()).print();
                    //Console.error("[CONNECT] Retry Aborted (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount());
                }
            });
            builder.onFailedAttempt(new EventListener<ExecutionAttemptedEvent<Channel>>() {
                @Override
                public void accept(ExecutionAttemptedEvent<Channel> event) throws Throwable {
                    Console.error("[CONNECT] Failed Attempt (Error: %s, Attempts: %d)", event.getLastException(), event.getAttemptCount());
                }
            });
        }
        //builder.withMaxAttempts(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_MAX_CONNECT));
        //builder.withBackoff(Duration.ofSeconds(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_BACKOFF_MIN)), Duration.ofSeconds(getOptions().getOrDefault(GlobalOptions.FAILSAFE_ACQUIRE_BACKOFF_MAX)));
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
            CompletableFuture<Channel> channelFuture = FailsafeChannelFactory.super.create(address);
            channelFuture.thenAccept(this::removeOnClose);
            return channelFuture;
        }

        private void removeOnClose(Channel channel) {
            if (channel.closeFuture().isDone()) {
                if (supplierMap.remove(address) != null) {
                    log.debug("CHANNEL_FACTORY ({}) => Removed channel supplier entry from cache for address '{}'", Netty.id(channel), address);
                }
            } else {
                channel.closeFuture().addListener((ChannelFutureListener) future -> {
                    if (supplierMap.remove(address) != null) {
                        log.debug("CHANNEL_FACTORY ({}) => Removed channel supplier entry from cache for address '{}'", Netty.id(future.channel()), address);
                    }
                });
            }
        }
    }
}
