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

import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.util.AttributeKey;
import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import static com.ibasco.agql.core.util.Functions.TRUE;

/**
 * Enhanced version of netty's {@link io.netty.channel.pool.ChannelPool} interface with {@link java.util.concurrent.CompletableFuture} support.
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyChannelPool extends Closeable {

    /** Constant <code>NONE</code> */
    ReleaseStrategy NONE = channel -> {};

    /** Constant <code>DISCONNECT_ON_RELEASE</code> */
    ReleaseStrategy DISCONNECT_ON_RELEASE = ChannelOutboundInvoker::disconnect;

    /**
     * The default {@link Channel} attribute for {@link NettyChannelPool}
     */
    AttributeKey<NettyChannelPool> CHANNEL_POOL = AttributeKey.newInstance(NettyChannelPool.class.getName());

    /**
     * Attempts to release the {@link io.netty.channel.Channel} if it is pooleed.
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to release
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which is notified once the operation is marked as completed. A value of {@code true} will be returned if the {@link io.netty.channel.Channel} was released, otherwise {@code false} if the {@link io.netty.channel.Channel} is not pooled or if the release operation was unsuccessful. The returned future will never complete exceptionally.
     */
    static CompletableFuture<Boolean> tryRelease(Channel channel) {
        NettyChannelPool pool = getPool(channel);
        if (pool == null)
            return CompletableFuture.completedFuture(false);
        return pool.release(channel).thenApply(TRUE).exceptionally(e -> false);
    }

    /**
     * Retrieve the {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} which was used to acquire the specified {@link io.netty.channel.Channel}
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to be used as lookup
     *
     * @return The {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} or {@code null} if the {@link io.netty.channel.Channel} was not acquired from a {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}
     */
    static NettyChannelPool getPool(Channel channel) {
        if (channel == null)
            throw new IllegalArgumentException("Channel must not be null");
        if (!channel.hasAttr(CHANNEL_POOL))
            return null;
        return channel.attr(CHANNEL_POOL).get();
    }

    /**
     * Release a {@link io.netty.channel.Channel} back to this {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}. The returned {@link io.netty.util.concurrent.Future} is notified once
     * the release is successful and failed otherwise. When failed the {@link io.netty.channel.Channel} will automatically closed.
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Void> release(Channel channel);

    /**
     * Checks if the {@link io.netty.channel.Channel} is currectly pooled
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to be used as lookup
     *
     * @return {@code true} if the {@link io.netty.channel.Channel} has been acquired from a {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}
     */
    static boolean isPooled(Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        return getPool(channel) != null;
    }

    /**
     * Acquire a {@link io.netty.channel.Channel} from this {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}. The returned {@link java.util.concurrent.CompletableFuture} is notified once
     * the acquire is successful and failed otherwise.
     *
     * <strong>Its important that an acquired is always released to the pool again, even if the {@link io.netty.channel.Channel}
     * is explicitly closed..</strong>
     *
     * @param remoteAddress
     *         The remote address to connect to
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Channel> acquire(InetSocketAddress remoteAddress);

    /**
     * Acquire a {@link io.netty.channel.Channel} from this {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}. The returned {@link java.util.concurrent.CompletableFuture} is notified once
     * the acquire is successful and failed otherwise.
     *
     * <strong>Its important that an acquired is always released to the pool again, even if the {@link io.netty.channel.Channel}
     * is explicitly closed..</strong>
     *
     * @param remoteAddress
     *         The remote address to connect to
     * @param promise
     *         The {@link java.util.concurrent.CompletableFuture} that is notified once the acquire operation is complete.
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Channel> acquire(InetSocketAddress remoteAddress, final CompletableFuture<Channel> promise);

    /**
     * Release a {@link io.netty.channel.Channel} back to this {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}. The given {@link io.netty.util.concurrent.Promise} is notified once
     * the release is successful and failed otherwise. When failed the {@link io.netty.channel.Channel} will automatically closed.
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     * @param promise
     *         a {@link java.util.concurrent.CompletableFuture} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Void> release(Channel channel, CompletableFuture<Void> promise);

    /**
     * <p>getSize.</p>
     *
     * @return a int
     */
    int getSize();

    /** {@inheritDoc} */
    @Override
    void close();

    @FunctionalInterface
    interface ReleaseStrategy {

        void onRelease(Channel channel);
    }
}
