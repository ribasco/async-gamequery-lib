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

import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.transport.handlers.MessageDecoder;
import com.ibasco.agql.core.transport.handlers.MessageEncoder;
import com.ibasco.agql.core.transport.handlers.MessageRouter;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>NettyChannelInitializer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class NettyChannelInitializer extends ChannelInitializer<Channel> {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelInitializer.class);

    private static final ChannelFutureListener CLOSE_LISTENER = future -> {
        Channel channel = future.channel();
        channelClosed(channel, future.cause());
    };

    private NettyChannelHandlerInitializer handlerInitializer;

    /**
     * <p>channelClosed.</p>
     *
     * @param ch
     *         a {@link io.netty.channel.Channel} object
     * @param error
     *         a {@link java.lang.Throwable} object
     */
    public static void channelClosed(Channel ch, Throwable error) {
        log.debug("{} HANDLER => Channel closed (Error: {})", Netty.id(ch), error == null ? "None" : error.getLocalizedMessage());
        ch.pipeline().fireUserEventTriggered(ChannelEvent.CLOSED);
    }

    /** {@inheritDoc} */
    @Override
    protected void initChannel(@NotNull Channel ch) {
        try {
            //register handlers
            initializeChannelHandlers(ch);
        } finally {
            //fire event
            ch.pipeline().fireUserEventTriggered(ChannelEvent.CREATED);
            //register close listener
            ch.closeFuture().addListener(CLOSE_LISTENER);
        }
    }

    private void initializeChannelHandlers(final Channel ch) {
        final ChannelPipeline pipe = ch.pipeline();
        pipe.addLast(MessageDecoder.NAME, new MessageDecoder());
        if (handlerInitializer != null) {
            //synchronized (this) {
            //register messenger specific inbound handlers
            Netty.registerHandlers(pipe, handlerInitializer::registerInboundHandlers, Netty.INBOUND);

            //terminating handler
            pipe.addLast(MessageRouter.NAME, new MessageRouter());

            //register messenger specific outbound handlers
            Netty.registerHandlers(pipe, handlerInitializer::registerOutboundHandlers, Netty.OUTBOUND);
            //}
        }
        pipe.addLast(MessageEncoder.NAME, new MessageEncoder());

        if (!NettyChannelPool.isPooled(ch)) {
            log.debug("{} HANDLER => Channel is not pooled. Registering timeout handlers", Netty.id(ch));
            Netty.registerTimeoutHandlers(ch);
        }

        Netty.printChannelPipeline(log, ch);
    }

    /**
     * <p>Getter for the field <code>handlerInitializer</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelHandlerInitializer} object
     */
    public NettyChannelHandlerInitializer getHandlerInitializer() {
        return handlerInitializer;
    }

    /**
     * <p>Setter for the field <code>handlerInitializer</code>.</p>
     *
     * @param handlerInitializer
     *         a {@link com.ibasco.agql.core.transport.NettyChannelHandlerInitializer} object
     */
    public void setHandlerInitializer(NettyChannelHandlerInitializer handlerInitializer) {
        this.handlerInitializer = handlerInitializer;
    }
}
