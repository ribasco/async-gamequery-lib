/*
 * Copyright (c) 2021-2022 Asynchronous Game Query Library
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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import java.io.Closeable;
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

    AttributeKey<NettyChannelPool> CHANNEL_POOL = AttributeKey.newInstance(NettyChannelPool.class.getName());

    /**
     * Acquire a {@link Channel} from this {@link NettyChannelPool}. The returned {@link Future} is notified once
     * the acquire is successful and failed otherwise.
     *
     * <strong>Its important that an acquired is always released to the pool again, even if the {@link Channel}
     * is explicitly closed..</strong>
     */
    CompletableFuture<Channel> acquire(Envelope<? extends AbstractRequest> envelope);

    CompletableFuture<Channel> acquire(Envelope<? extends AbstractRequest> envelope, final CompletableFuture<Channel> promise);

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

    @Override
    void close();

    static NettyChannelPool getPool(Channel channel) {
        if (channel == null || !channel.hasAttr(CHANNEL_POOL))
            return null;
        return channel.attr(CHANNEL_POOL).get();
    }

    static boolean isPooled(Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        return getPool(channel) != null;
    }
}
