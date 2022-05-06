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

import com.ibasco.agql.core.util.Concurrency;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import static com.ibasco.agql.core.transport.pool.NettyChannelPool.isPooled;

/**
 * <p>
 * A class that wraps an existing netty based {@link io.netty.channel.Channel} representing a channel that has been acquired from a {@link com.ibasco.agql.core.transport.pool.NettyChannelPool}.
 * </p>
 * <p>
 * Note that using {@link io.netty.channel.ChannelFuture#channel()} does not return the actual {@link com.ibasco.agql.core.transport.pool.PooledChannel} type but the underlying {@link io.netty.channel.Channel} instance.
 * This can be a problem when accessing a {@link com.ibasco.agql.core.transport.pool.PooledChannel} instance from a {@link java.util.Collection}
 * </p>
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultPooledChannel extends PooledChannel {

    private final Channel channel;

    DefaultPooledChannel(Channel channel) {
        this.channel = channel;
    }

    /** {@inheritDoc} */
    @Override
    public ChannelId id() {
        return channel.id();
    }

    /** {@inheritDoc} */
    @Override
    public EventLoop eventLoop() {
        return channel.eventLoop();
    }

    /** {@inheritDoc} */
    @Override
    public Channel parent() {
        return channel.parent();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelConfig config() {
        return channel.config();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRegistered() {
        return channel.isRegistered();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelMetadata metadata() {
        return channel.metadata();
    }

    /** {@inheritDoc} */
    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    /** {@inheritDoc} */
    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture closeFuture() {
        return channel.closeFuture();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWritable() {
        return channel.isWritable();
    }

    /** {@inheritDoc} */
    @Override
    public long bytesBeforeUnwritable() {
        return channel.bytesBeforeUnwritable();
    }

    /** {@inheritDoc} */
    @Override
    public long bytesBeforeWritable() {
        return channel.bytesBeforeWritable();
    }

    /** {@inheritDoc} */
    @Override
    public Unsafe unsafe() {
        return channel.unsafe();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelPipeline pipeline() {
        return channel.pipeline();
    }

    /** {@inheritDoc} */
    @Override
    public ByteBufAllocator alloc() {
        return channel.alloc();
    }

    /** {@inheritDoc} */
    @Override
    public Channel read() {
        return channel.read();
    }

    /** {@inheritDoc} */
    @Override
    public Channel flush() {
        return channel.flush();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        return channel.bind(localAddress);
    }

    /** {@inheritDoc} */
    @Override
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        return channel.attr(key);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return channel.connect(remoteAddress);
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean hasAttr(AttributeKey<T> key) {
        return channel.hasAttr(key);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return channel.connect(remoteAddress, localAddress);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture disconnect() {
        return channel.disconnect();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture close() {
        return channel.close();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture deregister() {
        return channel.deregister();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return channel.bind(localAddress, promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return channel.connect(remoteAddress, promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return channel.connect(remoteAddress, localAddress, promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture disconnect(ChannelPromise promise) {
        return channel.disconnect(promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture close(ChannelPromise promise) {
        return channel.close(promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture deregister(ChannelPromise promise) {
        return channel.deregister(promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture write(Object msg) {
        return channel.write(msg);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return channel.write(msg, promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return channel.writeAndFlush(msg, promise);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return channel.writeAndFlush(msg);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelPromise newPromise() {
        return channel.newPromise();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return channel.newProgressivePromise();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture newSucceededFuture() {
        return channel.newSucceededFuture();
    }

    /** {@inheritDoc} */
    @Override
    public ChannelFuture newFailedFuture(Throwable cause) {
        return channel.newFailedFuture(cause);
    }

    /** {@inheritDoc} */
    @Override
    public ChannelPromise voidPromise() {
        return channel.voidPromise();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(@NotNull Channel o) {
        if (this == o) {
            return 0;
        }
        return channel.id().compareTo(o.id());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Channel))
            return false;
        return channel.id().equals(((Channel) obj).id());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(POOLED) " + channel.toString();
    }

    //NOTE: Not symmetric (A Channel != DefaultPooledChannel)

    /** {@inheritDoc} */
    @Override
    public NettyChannelPool getChannelPool() {
        return attr(NettyChannelPool.CHANNEL_POOL).get();
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> release() {
        if (eventLoop().isShutdown())
            return Concurrency.failedFuture(new RejectedExecutionException("Executor has shutdown"));
        if (!isPooled(this))
            return CompletableFuture.completedFuture(null);
        final NettyChannelPool pool = getChannelPool();//NettyUtil.getChannelPool(this);
        assert pool != null;
        return pool.release(this);
    }

}
