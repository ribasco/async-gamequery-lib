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

import static com.ibasco.agql.core.util.Functions.TRUE;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Enhanced version of netty's {@link io.netty.channel.pool.ChannelPool} interface with {@link CompletableFuture} support.
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyChannelPool extends Closeable {

    @FunctionalInterface
    interface ReleaseStrategy {

        void onRelease(Channel channel);
    }

    ReleaseStrategy NONE = channel -> {};

    ReleaseStrategy DISCONNECT_ON_RELEASE = ChannelOutboundInvoker::disconnect;

    /**
     * The default {@link Channel} attribute for {@link NettyChannelPool}
     */
    AttributeKey<NettyChannelPool> CHANNEL_POOL = AttributeKey.newInstance(NettyChannelPool.class.getName());

    /**
     * Acquire a {@link Channel} from this {@link NettyChannelPool}. The returned {@link CompletableFuture} is notified once
     * the acquire is successful and failed otherwise.
     *
     * <strong>Its important that an acquired is always released to the pool again, even if the {@link Channel}
     * is explicitly closed..</strong>
     *
     * @param remoteAddress
     *         The remote address to connect to
     */
    CompletableFuture<Channel> acquire(InetSocketAddress remoteAddress);

    /**
     * Acquire a {@link Channel} from this {@link NettyChannelPool}. The returned {@link CompletableFuture} is notified once
     * the acquire is successful and failed otherwise.
     *
     * <strong>Its important that an acquired is always released to the pool again, even if the {@link Channel}
     * is explicitly closed..</strong>
     *
     * @param remoteAddress
     *         The remote address to connect to
     * @param promise
     *         The {@link CompletableFuture} that is notified once the acquire operation is complete.
     */
    CompletableFuture<Channel> acquire(InetSocketAddress remoteAddress, final CompletableFuture<Channel> promise);

    /**
     * Release a {@link Channel} back to this {@link NettyChannelPool}. The returned {@link Future} is notified once
     * the release is successful and failed otherwise. When failed the {@link Channel} will automatically closed.
     */
    CompletableFuture<Void> release(Channel channel);

    /**
     * Release a {@link Channel} back to this {@link NettyChannelPool}. The given {@link Promise} is notified once
     * the release is successful and failed otherwise. When failed the {@link Channel} will automatically closed.
     */
    CompletableFuture<Void> release(Channel channel, CompletableFuture<Void> promise);

    /**
     * Attempts to release the {@link Channel} if it is pooleed.
     *
     * @param channel
     *         The {@link Channel} to release
     *
     * @return A {@link CompletableFuture} which is notified once the operation is marked as completed. A value of {@code true} will be returned if the {@link Channel} was released, otherwise {@code false} if the {@link Channel} is not pooled or if the release operation was unsuccessful. The returned future will never complete exceptionally.
     */
    static CompletableFuture<Boolean> tryRelease(Channel channel) {
        NettyChannelPool pool = getPool(channel);
        if (pool == null)
            return CompletableFuture.completedFuture(false);
        return pool.release(channel).thenApply(TRUE).exceptionally(e -> false);
    }

    /**
     * Retrieve the {@link NettyChannelPool} which was used to acquire the specified {@link Channel}
     *
     * @param channel
     *         The {@link Channel} to be used as lookup
     *
     * @return The {@link NettyChannelPool} or {@code null} if the {@link Channel} was not acquired from a {@link NettyChannelPool}
     */
    static NettyChannelPool getPool(Channel channel) {
        if (channel == null)
            throw new IllegalArgumentException("Channel must not be null");
        if (!channel.hasAttr(CHANNEL_POOL))
            return null;
        return channel.attr(CHANNEL_POOL).get();
    }

    /**
     * Checks if the {@link Channel} is currectly pooled
     *
     * @param channel
     *         The {@link Channel} to be used as lookup
     *
     * @return {@code true} if the {@link Channel} has been acquired from a {@link NettyChannelPool}
     */
    static boolean isPooled(Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        return getPool(channel) != null;
    }

    @Override
    void close();
}
