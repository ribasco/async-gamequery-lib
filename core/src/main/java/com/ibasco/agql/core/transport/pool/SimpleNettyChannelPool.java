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
package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.transport.AbstractNettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.Errors;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import static io.netty.util.internal.ObjectUtil.checkNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

/**
 * An enhanced version of netty's {@link io.netty.channel.pool.SimpleChannelPool} implementation
 *
 * @author Rafael Luis Ibasco
 */
public class SimpleNettyChannelPool implements NettyChannelPool {

    private static final Logger log = LoggerFactory.getLogger(SimpleNettyChannelPool.class);

    private final Deque<Channel> deque = new ConcurrentLinkedDeque<>();

    private final ChannelPoolHandler handler;

    private final ChannelHealthChecker healthCheck;

    private final boolean releaseHealthCheck;

    private final boolean lastRecentUsed;

    private final ReleaseStrategy releaseStrategy;

    private final NettyChannelFactory channelFactory;

    /**
     * Creates a new instance using the {@link ChannelHealthChecker#ACTIVE}.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     */
    @SuppressWarnings("unused")
    public SimpleNettyChannelPool(NettyChannelFactory channelFactory, final ChannelPoolHandler handler) {
        this(channelFactory, handler, ChannelHealthChecker.ACTIVE);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     */
    public SimpleNettyChannelPool(NettyChannelFactory channelFactory, final ChannelPoolHandler handler, ChannelHealthChecker healthCheck) {
        this(channelFactory, handler, healthCheck, true, null);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     * @param releaseHealthCheck
     *         will check channel health before offering back if this parameter set to {@code true};
     *         otherwise, channel health is only checked at acquisition time
     * @param releaseStrategy
     *         Default strategy to apply during release
     */
    public SimpleNettyChannelPool(NettyChannelFactory channelFactory, final ChannelPoolHandler handler, ChannelHealthChecker healthCheck, boolean releaseHealthCheck, ReleaseStrategy releaseStrategy) {
        this(channelFactory, handler, healthCheck, releaseHealthCheck, true, releaseStrategy);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     * @param releaseHealthCheck
     *         will check channel health before offering back if this parameter set to {@code true};
     *         otherwise, channel health is only checked at acquisition time
     * @param lastRecentUsed
     *         {@code true} {@link Channel} selection will be LIFO, if {@code false} FIFO.
     * @param releaseStrategy
     *         Default strategy to apply during release
     */
    public SimpleNettyChannelPool(NettyChannelFactory channelFactory, final ChannelPoolHandler handler, ChannelHealthChecker healthCheck, boolean releaseHealthCheck, boolean lastRecentUsed, ReleaseStrategy releaseStrategy) {
        this.handler = checkNotNull(handler, "handler");
        this.healthCheck = checkNotNull(healthCheck, "healthCheck");
        this.releaseHealthCheck = releaseHealthCheck;
        this.channelFactory = channelFactory;
        this.lastRecentUsed = lastRecentUsed;
        this.releaseStrategy = releaseStrategy == null ? NONE : releaseStrategy;
    }

    /**
     * The underlying {@link NettyChannelFactory} used to create new {@link Channel} instances.
     *
     * @return A type of {@link AbstractNettyChannelFactory}
     */
    public NettyChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * Returns the {@link ChannelPoolHandler} that will be notified for the different pool actions.
     *
     * @return the {@link ChannelPoolHandler} that will be notified for the different pool actions
     */
    @SuppressWarnings("unused")
    public ChannelPoolHandler getChannelPoolHandler() {
        return handler;
    }

    /**
     * Returns the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is healthy.
     *
     * @return the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is healthy
     */
    @SuppressWarnings("unused")
    protected ChannelHealthChecker getChannelHealthChecker() {
        return healthCheck;
    }

    /**
     * Indicates whether this pool will check the health of channels before offering them back into the pool.
     *
     * @return {@code true} if this pool will check the health of channels before offering them back into the pool, or
     * {@code false} if channel health is only checked at acquisition time
     */
    @SuppressWarnings("unused")
    protected boolean releaseHealthCheck() {
        return releaseHealthCheck;
    }

    @Override
    public final CompletableFuture<Channel> acquire(final InetSocketAddress remoteAddress) {
        return acquire(remoteAddress, new CompletableFuture<>());
    }

    @Override
    public CompletableFuture<Channel> acquire(final InetSocketAddress remoteAddress, CompletableFuture<Channel> promise) {
        return acquireHealthyFromPoolOrNew(remoteAddress, promise);
    }

    /**
     * Tries to retrieve healthy channel from the pool if any or creates a new channel otherwise.
     *
     * @return future for acquiring a channel.
     */
    private CompletableFuture<Channel> acquireHealthyFromPoolOrNew(final InetSocketAddress remoteAddress, CompletableFuture<Channel> promise) {
        try {
            final Channel ch = pollChannel();
            if (ch == null) {
                //create a new connected channel
                CompletableFuture<Channel> channelFuture = newChannel(remoteAddress);
                notifyOnComplete(promise, channelFuture);
            } else {
                //Channel is not null, ensure that health checker is run in the channel's event loop
                runInEventLoop(ch.eventLoop(), (v) -> doHealthCheck(ch, remoteAddress, promise));
            }
        } catch (Throwable cause) {
            promise.completeExceptionally(cause);
        }
        return promise;
    }

    protected CompletableFuture<Channel> newChannel(InetSocketAddress remoteAddress) {
        return channelFactory.create(remoteAddress).thenCompose(this::initializeChannel);
    }

    private CompletableFuture<Channel> initializeChannel(Channel channel) {
        return CompletableFuture.completedFuture(channel)
                                .thenApply(DefaultPooledChannel::new)
                                .thenApply(this::updateAttribute)
                                .whenCompleteAsync(this::notifyConnect, channel.eventLoop());
    }

    private Channel updateAttribute(Channel channel) {
        channel.attr(CHANNEL_POOL).set(this);
        return channel;
    }

    private void doHealthCheck(Channel ch, final InetSocketAddress remoteAddress, CompletableFuture<Channel> promise) {
        try {
            assert ch.eventLoop().inEventLoop();
            CompletableFuture<Boolean> healthFuture = healthCheck.isHealthy(ch);
            if (healthFuture.isDone()) {
                notifyHealthCheck(remoteAddress, healthFuture.get(), ch, promise);
            } else {
                healthFuture.whenComplete((healthy, error) -> {
                    if (error != null) {
                        closeAndFail(ch, error, promise);
                        return;
                    }
                    try {
                        notifyHealthCheck(remoteAddress, healthy, ch, promise);
                    } catch (Throwable e) {
                        closeAndFail(ch, e, promise);
                    }
                });
            }
        } catch (Throwable cause) {
            closeAndFail(ch, Errors.unwrap(cause), promise);
        }
    }

    private <V> void notifyOnComplete(CompletableFuture<V> promise, CompletableFuture<V> trackedFuture) {
        if (trackedFuture.isDone()) {
            try {
                V res = trackedFuture.get();
                promise.complete(res);
            } catch (Throwable e) {
                promise.completeExceptionally(Errors.unwrap(e));
            }
        } else {
            trackedFuture.whenComplete((res, error) -> {
                if (error != null)
                    promise.completeExceptionally(error);
                else
                    promise.complete(res);
            });
        }
    }

    private void notifyConnect(Channel channel, Throwable error) {
        assert channel.eventLoop().inEventLoop();

        if (error != null)
            throw new CompletionException(Errors.unwrap(error));
        try {
            handler.channelAcquired(channel);
        } catch (Throwable handlerError) {
            closeAndFail(channel, handlerError);
        }
    }

    private void runInEventLoop(EventLoop el, Consumer<Void> function) {
        if (function == null)
            return;
        if (el.inEventLoop()) {
            function.accept(null);
        } else {
            el.execute(() -> function.accept(null));
        }
    }

    private void notifyHealthCheck(final InetSocketAddress remoteAddress, boolean isHealthy, Channel channel, CompletableFuture<Channel> promise) throws Exception {
        assert channel.eventLoop().inEventLoop();
        //if the channel is healthy, mark the promise as completed
        if (isHealthy) {
            channel.attr(CHANNEL_POOL).set(this);
            handler.channelAcquired(channel);
            if (promise.complete(channel))
                log.debug("[{}] Acquired an existing healthy channel: {} for address '{}' (Local: {}, Active: {})", this, channel, remoteAddress, channel.localAddress(), channel.isActive());
        } else {
            //if channel is not healthy, close and acquire a new one
            closeChannel(channel);
            acquireHealthyFromPoolOrNew(remoteAddress, promise);
        }
    }

    @Override
    public final CompletableFuture<Void> release(Channel channel) {
        return release(channel, new CompletableFuture<>());
    }

    @Override
    public CompletableFuture<Void> release(final Channel channel, final CompletableFuture<Void> promise) {
        try {
            checkNotNull(channel, "channel");
            checkNotNull(promise, "promise");
            runInEventLoop(channel.eventLoop(), (v) -> doReleaseChannel(channel, promise));
        } catch (Throwable cause) {
            closeAndFail(channel, cause, promise);
        }
        return promise;
    }

    @Override
    public int getSize() {
        return deque.size();
    }

    private void doReleaseChannel(Channel channel, CompletableFuture<Void> promise) {
        try {
            assert channel.eventLoop().inEventLoop();
            // Remove the POOL_KEY attribute from the Channel and check if it was acquired from this pool, if not fail.
            if (channel.attr(CHANNEL_POOL).getAndSet(null) != this) {
                closeAndFail(channel, new IllegalArgumentException("Channel " + channel + " was not acquired from this ChannelPool"), promise);
            } else {
                if (releaseHealthCheck) {
                    doHealthCheckOnRelease(channel, promise);
                } else {
                    releaseAndOffer(channel, promise);
                }
            }
        } catch (Throwable cause) {
            closeAndFail(channel, cause, promise);
        }
    }

    private void doHealthCheckOnRelease(final Channel channel, final CompletableFuture<Void> promise) {
        final CompletableFuture<Boolean> f = healthCheck.isHealthy(channel);
        if (f.isDone()) {
            releaseAndOfferIfHealthy(channel, promise, f);
        } else {
            f.whenComplete((isHealthy, throwable) -> releaseAndOfferIfHealthy(channel, promise, CompletableFuture.completedFuture(isHealthy)));
        }
    }

    /**
     * Adds the channel back to the pool only if the channel is healthy.
     *
     * @param channel
     *         the channel to put back to the pool
     * @param promise
     *         offer operation promise.
     * @param future
     *         the future that contains information fif channel is healthy or not.
     */
    private void releaseAndOfferIfHealthy(Channel channel, CompletableFuture<Void> promise, CompletableFuture<Boolean> future) {
        try {
            if (future.getNow(false)) { //channel turns out to be healthy, offering and releasing it.
                releaseAndOffer(channel, promise);
            } else { //channel not healthy, just releasing it.
                handler.channelReleased(channel);
                promise.complete(null);
            }
        } catch (Throwable cause) {
            closeAndFail(channel, cause, promise);
        }
    }

    private void applyReleaseStrategy(Channel channel) {
        if (releaseStrategy == null)
            return;
        releaseStrategy.onRelease(channel);
    }

    private void releaseAndOffer(Channel channel, CompletableFuture<Void> promise) throws Exception {
        if (offerChannel(channel)) {
            if (channel instanceof PooledChannel)
                ((PooledChannel) channel).notifyRelease();
            handler.channelReleased(channel);
            applyReleaseStrategy(channel);
            promise.complete(null);
        } else {
            if (channel instanceof PooledChannel)
                ((PooledChannel) channel).notifyRelease(new ChannelPoolFullException());
            closeAndFail(channel, new ChannelPoolFullException(), promise);
        }
    }

    private void closeChannel(Channel channel) {
        if (channel == null)
            return;
        channel.attr(CHANNEL_POOL).getAndSet(null);
        channel.close();
    }

    private void closeAndFail(Channel channel, Throwable cause) {
        if (channel != null) {
            try {
                closeChannel(channel);
            } catch (Throwable e) {
                throw new CompletionException(Errors.unwrap(e));
            }
        }
        throw new CompletionException(Errors.unwrap(cause));
    }

    private void closeAndFail(Channel channel, Throwable cause, CompletableFuture<?> promise) {
        if (channel != null) {
            try {
                closeChannel(channel);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
            }
        }
        promise.completeExceptionally(cause);
    }

    /**
     * Poll a {@link Channel} out of the internal storage to reuse it. This will return {@code null} if no
     * {@link Channel} is ready to be reused.
     * <p>
     * Subclasses may override {@code pollChannel()} and {@code offerChannel()}. Be aware that
     * implementations of these methods needs to be thread-safe!
     */
    protected Channel pollChannel() {
        return lastRecentUsed ? deque.pollLast() : deque.pollFirst();
    }

    /**
     * Offer a {@link Channel} back to the internal storage. This will return {@code true} if the {@link Channel}
     * could be added, {@code false} otherwise.
     * <p>
     * Sub-classes may override {@code pollChannel()} and {@code offerChannel()}. Be aware that
     * implementations of these methods needs to be thread-safe!
     */
    protected boolean offerChannel(Channel channel) {
        return deque.offer(channel);
    }

    @Override
    public void close() {
        Channel channel;
        while ((channel = pollChannel()) != null) {
            // Just ignore any errors that are reported back from close().
            channel.close().awaitUninterruptibly();
        }
    }

    /**
     * Closes the pool in an async manner.
     *
     * @return Future which represents completion of the close task
     */
    public CompletableFuture<Void> closeAsync() {
        // Execute close asynchronously in case this is being invoked on an eventloop to avoid blocking
        return CompletableFuture.runAsync(this::close, GlobalEventExecutor.INSTANCE);
    }

    private static final class ChannelPoolFullException extends IllegalStateException {

        private ChannelPoolFullException() {
            super("ChannelPool full");
        }

        // Suppress a warning since the method doesn't need synchronization
        @Override
        public Throwable fillInStackTrace() {   // lgtm[java/non-sync-override]
            return this;
        }
    }
}
