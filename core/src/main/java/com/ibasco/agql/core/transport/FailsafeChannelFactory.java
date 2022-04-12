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

import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.TransportOptions;
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

    protected FailsafeChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        this.acquireExecutor = Failsafe.with(newRetryPolicy()).with(channelFactory.getExecutor());
    }

    protected void configureRetryPolicy(RetryPolicyBuilder<Channel> builder) {}

    @Override
    public CompletableFuture<Channel> create(Object data) {
        return acquireExecutor.getStageAsync(getContextualSupplier(data));
    }

    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return Netty.useEventLoop(create(data), eventLoop);
    }

    private RetryPolicy<Channel> newRetryPolicy() {
        RetryPolicyBuilder<Channel> retryPolicyBuilder = RetryPolicy.builder();
        retryPolicyBuilder.handleIf(e -> Errors.unwrap(e) instanceof ConnectException)
                          .abortIf(channel -> channel.eventLoop().isShutdown() || channel.eventLoop().isShuttingDown())
                          .abortOn(RejectedExecutionException.class)
                          .onRetry(new EventListener<ExecutionAttemptedEvent<Channel>>() {
                              @Override
                              public void accept(ExecutionAttemptedEvent<Channel> event) throws Throwable {
                                  System.err.printf("[CONNECT] Retrying connect (Reason: %s, Attempts: %d)\n", event.getLastException(), event.getAttemptCount());
                                  log.error("CHANNEL_FACTORY ({}) => Failed to acquire channel. Retrying (Attempts: {}, Last Failure: {})", getClass().getSimpleName(), event.getAttemptCount(), event.getLastException() != null ? event.getLastException().getClass().getSimpleName() : "N/A");
                              }
                          })
                          .onFailure(new EventListener<ExecutionCompletedEvent<Channel>>() {
                              @Override
                              public void accept(ExecutionCompletedEvent<Channel> event) throws Throwable {
                                  System.err.printf("[CONNECT] Unable to connect to server (Error: %s, Attempts: %d)\n", event.getException(), event.getAttemptCount());
                              }
                          })
                          //.onRetry(event -> log.error("CHANNEL_FACTORY ({}) => Failed to acquire channel. Retrying (Attempts: {}, Last Failure: {})", getClass().getSimpleName(), event.getAttemptCount(), event.getLastException() != null ? event.getLastException().getClass().getSimpleName() : "N/A"))
                          .withMaxAttempts(getOptions().getOrDefault(TransportOptions.FAILSAFE_ACQUIRE_MAX_CONNECT))
                          .withBackoff(
                                  Duration.ofSeconds(getOptions().getOrDefault(TransportOptions.FAILSAFE_ACQUIRE_BACKOFF_MIN))
                                  , Duration.ofSeconds(getOptions().getOrDefault(TransportOptions.FAILSAFE_ACQUIRE_BACKOFF_MAX))
                          );
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
            log.debug("CHANNEL_FACTORY ({}) => Acquiring channel for address '{}' (Supplier: {}, Attempt: {}, Executions: {}, Last Result: {}, Last Failure: {})", FailsafeChannelFactory.class.getSimpleName(), address, this, context.getAttemptCount(), context.getExecutionCount(), context.getLastResult(), context.getLastException());
            return FailsafeChannelFactory.super.create(address);
        }
    }
}