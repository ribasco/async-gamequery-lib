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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ribasco.rglib.core.AbstractMessage;
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
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 9/15/2016.
 */
public abstract class NettyTransport<Msg extends AbstractMessage> implements Transport<Msg> {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> send(Msg data) {
        return send(data, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> send(Msg data, boolean flushImmediately) {
        //Obtain a channel then write to it once acquired
        return getChannel(data.recipient()).thenCompose(channel -> writeToChannel(channel, data, flushImmediately));
    }

    /**
     * A method to send data over the transport. Since the current netty version does not yet support {@link CompletableFuture},
     * we need to convert the returned {@link ChannelFuture} to it's {@link CompletableFuture} version.
     *
     * @param channel          The underlying {@link Channel} to be used for data transport.
     * @param data             An instance of {@link AbstractMessage} that will be sent through the transport
     * @param flushImmediately True if transport should immediately flush the message after send.
     *
     * @return A {@link CompletableFuture} with return type of {@link Channel} (The channel used for the transport)
     */
    private CompletableFuture<Void> writeToChannel(Channel channel, Msg data, boolean flushImmediately) {
        final CompletableFuture<Void> writeResultFuture = new CompletableFuture<>();
        final ChannelFuture writeFuture = (flushImmediately) ? channel.writeAndFlush(data) : channel.write(data);
        writeFuture.addListener((ChannelFuture future) -> {
            try {
                if (future.isSuccess())
                    writeResultFuture.complete(null);
                else
                    writeResultFuture.completeExceptionally(future.cause());
            } finally {
                //Clean-up channel after write
                cleanupChannel(channel);
            }
        });
        return writeResultFuture;
    }

    /**
     * Perform cleanupChannel operations on a channel after calling {@link #send(AbstractMessage, boolean)}
     *
     * @param c Channel
     */
    public Void cleanupChannel(Channel c) {
        //this method is meant to be overriden to perform cleanup operations (optional only)
        return null;
    }

    @Override
    public Channel flush() {
        throw new NotImplementedException("No concrete class has implemented this method");
    }

    public EventLoopGroup createEventLoopGroup(ChannelType type) {
        switch (channelType) {
            case OIO_TCP:
            case OIO_UDP:
                return new OioEventLoopGroup();
            case NIO_TCP:
            case NIO_UDP:
                return new NioEventLoopGroup(12, new ThreadFactoryBuilder().setNameFormat("event-loop-%d").setDaemon(true).build());
        }
        return null;
    }

    public ByteBufAllocator getAllocator() {
        return allocator;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
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
        log.debug("Shutting down gracefully");
        eventLoopGroup.shutdownGracefully();
    }

    public abstract CompletableFuture<Channel> getChannel(InetSocketAddress address);
}
