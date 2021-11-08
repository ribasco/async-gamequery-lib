/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core.transport;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.ChannelType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.*;

@SuppressWarnings({"unchecked", "SameParameterValue"})
abstract public class NettyTransport<M extends AbstractRequest> implements Transport<M> {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

    private static final Logger log = LoggerFactory.getLogger(NettyTransport.class);

    private NettyChannelInitializer channelInitializer;

    private final ExecutorService executorService;

    public NettyTransport(ChannelType channelType) {
        this(channelType, Executors.newFixedThreadPool(8, new ThreadFactoryBuilder().setNameFormat("transport-el-%d").setDaemon(true).build()));
    }

    public NettyTransport(ChannelType channelType, ExecutorService executor) {
        //Make sure we have a type set
        if (channelType == null)
            throw new IllegalStateException("No channel type has been specified");

        //Set resource leak detection if debugging is enabled
        if (log.isDebugEnabled())
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

        //Initialize bootstrap
        Bootstrap bootstrap = new Bootstrap();

        //Create event loop group
        EventLoopGroup eventLoopGroup = createEventLoopGroup(executor, channelType);

        //Default channel options
        bootstrap.option(ChannelOption.ALLOCATOR, allocator);
        bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS));
                ch.pipeline().addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
            }
        });
        bootstrap.group(eventLoopGroup).channel(channelType.getChannelClass());

        this.eventLoopGroup = eventLoopGroup;
        this.bootstrap = bootstrap;
        this.executorService = executor;
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
    @SuppressWarnings("unchecked")
    public <V> CompletableFuture<V> send(M message) {
        message.setSender((InetSocketAddress) bootstrap.config().localAddress());
        return (CompletableFuture<V>) send(message, true);
    }

    public final CompletableFuture<Void> send(M message, boolean flushImmediately) {
        if (message == null)
            throw new IllegalStateException("Message cannot be null");
        //Obtain a channel then write to it once acquired
        return getChannel(message)
                //once a channel has been acquired, apply default operations
                .thenApply(channel -> applyProperties(channel, message))
                .thenCompose(channel -> writeToChannel(channel, message, flushImmediately));
    }

    protected Channel applyProperties(Channel channel, M message) {
        //update message transaction id
        message.setTransactionId(channel.id().asShortText());
        //associate the message/request to the channel
        channel.attr(ChannelAttributes.REQUEST).set(message);
        return channel;
    }

    /**
     * <p>A method to send data over the transport. Since the current netty version does not yet support {@link
     * CompletableFuture}, we need to convert the returned {@link ChannelFuture} to it's {@link CompletableFuture}
     * version.</p>
     *
     * @param channel
     *         The underlying {@link Channel} to be used for data transport.
     * @param request
     *         An instance of {@link AbstractRequest} that will be sent through the transport
     * @param flushImmediately
     *         True if transport should immediately flush the message after send.
     *
     * @return A {@link CompletableFuture} with return type of {@link Channel} (The channel used for the transport)
     */
    private CompletableFuture<Void> writeToChannel(Channel channel, M request, boolean flushImmediately) {
        CompletableFuture<Void> writeResultFuture = new CompletableFuture<>();
        log.debug("writeToChannel() : Writing data '{}' to channel : {} (id: {})", request, channel, channel.id());
        ChannelFuture writeFuture = (flushImmediately) ? channel.writeAndFlush(request) : channel.write(request);
        writeFuture.addListener((ChannelFuture future) -> {
            try {
                if (future.isSuccess())
                    writeResultFuture.complete(null);
                else
                    writeResultFuture.completeExceptionally(future.cause());
            } finally {
                cleanupChannel(future.channel());
            }
        });
        return writeResultFuture;
    }

    /**
     * <p>Perform cleanupChannel operations on a channel after calling {@link #send(AbstractRequest, boolean)}</p>
     *
     * @param c
     *         The {@link Channel} to perform clean-up operations on
     */
    public void cleanupChannel(Channel c) {
        //this method is meant to be overriden to perform cleanup operations (optional only)
    }

    /**
     * <p>A factory method that manufactures {@link EventLoopGroup} based on {@link ChannelType}. If the platform
     * supports
     * Epoll and the channel type is NIO, it will return {@link EpollEventLoopGroup} instead.</p>
     *
     * @param type
     *         The {@link ChannelType} that will determine which {@link EventLoopGroup} will be returned.
     *
     * @return The concrete {@link EventLoopGroup} instance that will be used by the transport.
     */
    private EventLoopGroup createEventLoopGroup(Executor executor, ChannelType type) {
        switch (type) {
            case NIO_TCP:
            case NIO_UDP:
                if (Epoll.isAvailable()) {
                    log.debug("Using EpollEventLoopGroup");
                    return new EpollEventLoopGroup(0, executor, DefaultSelectStrategyFactory.INSTANCE);
                }
                return new NioEventLoopGroup(0, executor, SelectorProvider.provider(), DefaultSelectStrategyFactory.INSTANCE);
        }
        return null;
    }

    /**
     * @return The {@link ByteBufAllocator} used by this transport.
     */
    public ByteBufAllocator getAllocator() {
        return allocator;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

    protected NettyChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    @Override
    public void close() throws IOException {
        try {
            log.debug("Attempting to shutdown '{}' gracefully", this.getClass().getSimpleName());
            eventLoopGroup.shutdownGracefully().awaitUninterruptibly(5000);
            if (executorService != null) {
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    abstract public CompletableFuture<Channel> getChannel(M message);
}
