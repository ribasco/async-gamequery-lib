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
import static com.ibasco.agql.core.transport.NettyChannelAttributes.CHANNEL_CONTEXT;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Pair;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Attaches a {@link NettyChannelContext} for each acquired/created {@link Channel}. Note that there is only one context assigned for each {@link Channel}.
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unused")
public class NettyContextChannelFactory extends NettyChannelFactoryDecorator {

    private static final Logger log = LoggerFactory.getLogger(NettyContextChannelFactory.class);

    private NettyChannelContextFactory contextFactory;

    public NettyContextChannelFactory(final NettyChannelFactory channelFactory) {
        this(channelFactory, null);
    }

    public NettyContextChannelFactory(final NettyChannelFactory channelFactory, final NettyChannelContextFactory contextFactory) {
        super(channelFactory);
        this.contextFactory = contextFactory;
    }

    @Override
    public CompletableFuture<Channel> create(final Object data) {
        checkContextFactory();
        final InetSocketAddress address = getResolver().resolveRemoteAddress(data);
        return super.create(data).thenCombine(CompletableFuture.completedFuture(address), Pair::new).thenCompose(this::initializeEL);
    }

    @Override
    public CompletableFuture<Channel> create(final Object data, final EventLoop eventLoop) {
        checkContextFactory();
        final InetSocketAddress address = getResolver().resolveRemoteAddress(data);
        if (eventLoop == null)
            return create(data);
        return super.create(data, eventLoop).thenCombineAsync(CompletableFuture.completedFuture(address), this::initializeContext, eventLoop);
    }

    private CompletableFuture<Channel> initializeEL(Pair<Channel, InetSocketAddress> pair) {
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
            log.debug("CHANNEL_FACTORY ({}) => Initializing NEW context for channel '{}' with envelope '{}' (Event Loop: {})", getClass().getSimpleName(), channel, address, NettyUtil.getThreadName(channel));
            context = contextFactory.create(channel);
            attr.set(context);
        } else {
            //If the references are not the same but points to the same underlying channel, then we need to recreate the context
            if (channel != context.channel() && channel.id().equals(context.channel().id())) {
                log.debug("CHANNEL_FACTORY ({}) => Reference mismatch. Replacing channel context from '{}' to '{}'", getClass().getSimpleName(), context.channel(), channel);
                context = contextFactory.create(channel);
                attr.set(context);
            } else {
                log.debug("CHANNEL_FACTORY ({}) => Initializing EXISTING context for channel '{}' to '{}' (Response Promise: {})", getClass().getSimpleName(), context.id(), address, context.properties().responsePromise());
                if (context.properties().writeInProgress() && context.properties().responsePromise() != null && !context.properties().responsePromise().isDone())
                    throw new IllegalStateException(String.format("Previous tranaction has not yet been marked as completed (Response promise: %s)", context.properties().responsePromise()));
            }
        }
        assert channel == context.channel();

        //update envelope address if necessary
        if (context.properties().envelope().recipient() == null || !context.properties().envelope().recipient().equals(address)) {
            log.debug("CHANNEL_FACTORY => Updating context address for channel '{}' -> {}", channel, address);
            context.properties().envelope().recipient(address);
        }

        return channel;
    }

    public final NettyChannelContextFactory getContextFactory() {
        return contextFactory;
    }

    public final void setContextFactory(NettyChannelContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    private void checkContextFactory() {
        if (contextFactory == null)
            throw new IllegalStateException("Missing context factory");
    }
}
