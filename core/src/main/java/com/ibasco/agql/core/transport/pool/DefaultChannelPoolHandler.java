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

import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.transport.handlers.ReadTimeoutHandler;
import com.ibasco.agql.core.transport.handlers.WriteTimeoutHandler;
import com.ibasco.agql.core.util.Net;
import com.ibasco.agql.core.util.Netty;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>DefaultChannelPoolHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultChannelPoolHandler extends AbstractChannelPoolHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultChannelPoolHandler.class);

    private static final ChannelFutureListener RELEASE_ON_CLOSE = future -> {
        Channel channel = future.channel();
        //ensure that the channel is released when closed pre-maturely
        if (NettyChannelPool.isPooled(channel)) {
            log.debug("{} HANDLER => Channel closed. Releasing from the pool", Netty.id(channel));
            NettyChannelPool.tryRelease(channel);
        }
    };

    private final ChannelHandler defaultChannelHandler;

    /**
     * <p>Constructor for DefaultChannelPoolHandler.</p>
     *
     * @param bootstrap
     *         a {@link io.netty.bootstrap.Bootstrap} object
     */
    public DefaultChannelPoolHandler(final Bootstrap bootstrap) {
        Objects.requireNonNull(bootstrap, "Bootstrap cannot be null");
        assert bootstrap.config().handler() != null;
        this.defaultChannelHandler = bootstrap.config().handler();
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                assert ch.eventLoop().inEventLoop();
                channelCreated(ch);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void channelCreated(Channel ch) {
        log.debug("{} HANDLER => Channel Created", Netty.id(ch));
        ch.closeFuture().addListener(RELEASE_ON_CLOSE);
        ch.pipeline().addFirst("initializer", defaultChannelHandler);
    }

    /** {@inheritDoc} */
    @Override
    public void channelAcquired(Channel ch) {
        log.debug("{} HANDLER => Channel Acquired. (Local Address: '{}', Remote Address: '{}') ({})", Netty.id(ch), Net.hostString(ch.localAddress()), Net.hostString(ch.remoteAddress()), NettyChannelPool.isPooled(ch) ? "POOLED" : "NOT POOLED");
        ch.pipeline().fireUserEventTriggered(ChannelEvent.ACQUIRED);
    }

    /** {@inheritDoc} */
    @Override
    public void channelReleased(Channel ch) {
        log.debug("{} HANDLER => Channel Released (Active: {}, Open: {}, Registered: {})", Netty.id(ch), ch.isActive(), ch.isOpen(), ch.isRegistered());
        try {
            if (ch.pipeline().get(ReadTimeoutHandler.class) != null)
                ch.pipeline().remove(ReadTimeoutHandler.class);
            if (ch.pipeline().get(WriteTimeoutHandler.class) != null)
                ch.pipeline().remove(WriteTimeoutHandler.class);
            log.debug("{} HANDLER => Removed READ/WRITE Timeout Handlers", Netty.id(ch));
        } catch (NoSuchElementException e) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s HANDLER => Failed to remove timeout handler(s)", Netty.id(ch)), e);
            } else
                log.warn(String.format("%s HANDLER => Failed to remove timeout handler(s)", Netty.id(ch)));
        } finally {
            ch.pipeline().fireUserEventTriggered(ChannelEvent.RELEASED);
            //NettyUtil.clearAttribute(ch, NettyChannelAttributes.REQUEST);
        }
    }

}
