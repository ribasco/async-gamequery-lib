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

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.ImmutablePair;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ibasco.agql.core.transport.NettyChannelAttributes.CHANNEL_CONTEXT;

/**
 * Attaches a {@link com.ibasco.agql.core.NettyChannelContext} for each acquired/created {@link io.netty.channel.Channel}. Note that there is only one context assigned for each {@link io.netty.channel.Channel}.
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unused")
public class NettyContextChannelFactory extends NettyChannelFactoryDecorator {

    private static final Logger log = LoggerFactory.getLogger(NettyContextChannelFactory.class);

    private NettyChannelContextFactory contextFactory;

    /**
     * <p>Constructor for NettyContextChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public NettyContextChannelFactory(final NettyChannelFactory channelFactory) {
        this(channelFactory, null);
    }

    /**
     * <p>Constructor for NettyContextChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     * @param contextFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelContextFactory} object
     */
    public NettyContextChannelFactory(final NettyChannelFactory channelFactory, final NettyChannelContextFactory contextFactory) {
        super(channelFactory);
        this.contextFactory = contextFactory;
    }

    /**
     * <p>Getter for the field <code>contextFactory</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelContextFactory} object
     */
    public final NettyChannelContextFactory getContextFactory() {
        return contextFactory;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(final Object data) {
        checkContextFactory();
        final InetSocketAddress address = getResolver().resolveRemoteAddress(data);
        return super.create(data).thenCombine(CompletableFuture.completedFuture(address), ImmutablePair::new).thenCompose(this::initializeEL);
    }

    /**
     * <p>Setter for the field <code>contextFactory</code>.</p>
     *
     * @param contextFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelContextFactory} object
     */
    public final void setContextFactory(NettyChannelContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(final Object data, final EventLoop eventLoop) {
        checkContextFactory();
        final InetSocketAddress address = getResolver().resolveRemoteAddress(data);
        if (eventLoop == null)
            return create(data);
        return super.create(data, eventLoop).thenCombineAsync(CompletableFuture.completedFuture(address), this::initializeContext, eventLoop);
    }

    private CompletableFuture<Channel> initializeEL(ImmutablePair<Channel, InetSocketAddress> pair) {
        if (pair.getFirst().eventLoop().inEventLoop()) {
            return CompletableFuture.completedFuture(pair.getFirst()).thenCombine(CompletableFuture.completedFuture(pair.getSecond()), this::initializeContext);
        } else {
            return CompletableFuture.completedFuture(pair.getFirst()).thenCombineAsync(CompletableFuture.completedFuture(pair.getSecond()), this::initializeContext, pair.getFirst().eventLoop());
        }
    }

    private Channel initializeContext(Channel channel, InetSocketAddress address) {
        assert channel.eventLoop().inEventLoop();
        Attribute<NettyChannelContext> attr = channel.attr(CHANNEL_CONTEXT);
        NettyChannelContext context = attr.get();
        //no context available yet, create and initialize
        if (context == null) {
            log.debug("{} CHANNEL_FACTORY ({}) => Initializing NEW context for channel '{}' with envelope '{}' (Event Loop: {})", Netty.id(channel), getClass().getSimpleName(), channel, address, Netty.getThreadName(channel));
            context = contextFactory.create(channel);
            attr.set(context);
        } else {
            //If the references are not the same but points to the same underlying channel, then we need to recreate the context
            if (channel != context.channel() && channel.id().equals(context.channel().id())) {
                log.debug("{} CHANNEL_FACTORY ({}) => Reference mismatch. Replacing channel context from '{}' to '{}'", Netty.id(channel), getClass().getSimpleName(), context.channel(), channel);
                context = contextFactory.create(channel);
                attr.set(context);
            } else {
                log.debug("{} CHANNEL_FACTORY ({}) => Initializing EXISTING context for channel '{}' to '{}' (Response Promise: {})", context.id(), getClass().getSimpleName(), context.id(), address, context.properties().responsePromise());
                if (context.properties().writeInProgress() && context.properties().responsePromise() != null && !context.properties().responsePromise().isDone())
                    throw new IllegalStateException(String.format("Previous tranaction has not yet been marked as completed (Response promise: %s)", context.properties().responsePromise()));
            }
        }
        assert channel == context.channel();

        //update envelope address if necessary
        if (context.properties().envelope().recipient() == null || !context.properties().envelope().recipient().equals(address)) {
            log.debug("{} CHANNEL_FACTORY => Updating context address for channel '{}' -> {}", context.id(), channel, address);
            context.properties().envelope().recipient(address);
        }

        return channel;
    }

    private void checkContextFactory() {
        if (contextFactory == null)
            throw new IllegalStateException("Missing context factory");
    }
}
