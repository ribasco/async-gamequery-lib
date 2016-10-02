/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.core.transport;

import com.ribasco.rglib.core.Message;
import com.ribasco.rglib.core.Transport;
import com.ribasco.rglib.core.enums.ChannelType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Promise;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 9/15/2016.
 */
public abstract class NettyTransport<T extends Channel> implements Transport<Message> {
    private Bootstrap bootstrap;
    private static EventLoopGroup eventLoopGroup;
    private Map<AttributeKey, Object> channelAttributes;
    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private static final Logger log = LoggerFactory.getLogger(NettyTransport.class);
    private ChannelInitializerCallback channelInitializer;
    private ChannelType channelType;

    public interface ChannelInitializerCallback {
        void initializeChannel(Channel channel);
    }

    public NettyTransport() {
        bootstrap = new Bootstrap();
        channelAttributes = new HashMap<>();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) bootstrap.config().localAddress();
    }

    protected ChannelFuture bind() {
        return bind(0);
    }

    protected ChannelFuture bind(int inetPort) {
        return bind(new InetSocketAddress(inetPort));
    }

    protected ChannelFuture bind(InetSocketAddress address) {
        return this.bootstrap.bind(address);
    }

    public <A> void addChannelOption(ChannelOption<A> channelOption, A value) {
        bootstrap.option(channelOption, value);
    }

    @Override
    public void initialize() {
        //Make sure we have a type set
        if (channelType == null)
            throw new IllegalStateException("No channel type has been specified");

        //Pick the proper event loop group
        if (eventLoopGroup == null) {
            eventLoopGroup = createEventLoopGroup(channelType);
        }

        //Default Channel Options
        addChannelOption(ChannelOption.ALLOCATOR, allocator);
        addChannelOption(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT);
        addChannelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        //Set resource leak detection if debugging is enabled
        if (log.isDebugEnabled())
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

        if (channelType == null)
            throw new IllegalStateException("No channel type has been specified");

        //Initialize bootstrap
        bootstrap.group(eventLoopGroup).channel(channelType.getChannelClass());
    }

    @Override
    public <V> Promise<V> send(Message data) {
        return send(data, false);
    }

    @Override
    public <V> Promise<V> send(Message data, boolean flushImmediately) {
        synchronized (this) {
            T c = null;
            ChannelPromise writePromise = null;
            try {
                c = getChannel(data.recipient());
                writePromise = c.newPromise();
                if (flushImmediately) {
                    c.writeAndFlush(data, writePromise);
                } else
                    c.write(data, writePromise);
            } finally {
                if (c != null)
                    cleanupChannel(c);
            }
            return (Promise<V>) writePromise;
        }
    }

    @Override
    public void send(Message data, boolean flushImmediately, ChannelPromise writePromise) {
        synchronized (this) {
            T c = null;
            try {
                c = getChannel(data.recipient());
                if (flushImmediately) {
                    c.writeAndFlush(data, writePromise);
                } else
                    c.write(data, writePromise);
            } finally {
                if (c != null)
                    cleanupChannel(c);
            }
        }
    }

    /**
     * Perform cleanupChannel operations on a channel after calling {@link #send(Message, boolean)}
     *
     * @param c Channel
     */
    public void cleanupChannel(Channel c) {
        //this method is meant to be overriden to perform cleanup operations (optional only)
    }

    @Override
    public Channel flush() {
        throw new NotImplementedException("No concrete class has implemented this method");
    }

    public ChannelPromise newChannelPromise(InetSocketAddress address) {
        return getChannel(address).newPromise();
    }

    public ChannelPromise newVoidPromise(InetSocketAddress address) {
        return getChannel(address).voidPromise();
    }

    public <V> Promise<V> newPromise() {
        return eventLoopGroup.next().newPromise();
    }

    public EventLoopGroup createEventLoopGroup(ChannelType type) {
        switch (channelType) {
            case OIO_TCP:
            case OIO_UDP:
                return new OioEventLoopGroup();
            case NIO_TCP:
            case NIO_UDP:
                return new NioEventLoopGroup();
        }
        return null;
    }

    public ByteBufAllocator getAllocator() {
        return allocator;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public T getChannel() {
        return getChannel(null);
    }

    public ChannelInitializerCallback getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializerCallback channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        NettyTransport.eventLoopGroup = eventLoopGroup;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    @Override
    public void close() throws IOException {
        eventLoopGroup.shutdownGracefully();
    }

    public abstract T getChannel(InetSocketAddress address);
}
