/*
 * Copyright 2022-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport;

import com.ibasco.agql.core.handlers.MessageDecoder;
import com.ibasco.agql.core.handlers.MessageEncoder;
import com.ibasco.agql.core.handlers.MessageRouter;
import com.ibasco.agql.core.transport.enums.ChannelEvent;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNettyChannelInitializer<C extends Channel> extends ChannelInitializer<C> {

    private static final Logger log = LoggerFactory.getLogger(DefaultNettyChannelInitializer.class);

    private final NettyChannelHandlerInitializer channelHandlerInitializer;

    public DefaultNettyChannelInitializer(final NettyChannelHandlerInitializer channelHandlerInitializer) {
        this.channelHandlerInitializer = channelHandlerInitializer;
    }

    private static final ChannelFutureListener CLOSE_LISTENER = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) {
            Channel channel = future.channel();
            channelClosed(channel, future.cause());
            channel.closeFuture().removeListener(this);
        }
    };

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

    public static void channelClosed(Channel ch, Throwable error) {
        log.debug("{} HANDLER => Channel closed (Error: {})", NettyUtil.id(ch), error == null ? "None" : error.getLocalizedMessage());
        try {
            ch.pipeline().fireUserEventTriggered(ChannelEvent.CLOSED);
        } finally {
            NettyUtil.clearAttribute(ch, ChannelAttributes.REQUEST);
            NettyUtil.clearAttribute(ch, ChannelAttributes.RESPONSE);
        }
    }

    private void initializeChannelHandlers(final Channel ch) {
        final ChannelPipeline pipe = ch.pipeline();

        pipe.addLast(MessageDecoder.NAME, new MessageDecoder());
        synchronized (channelHandlerInitializer) {
            //register messenger specific inbound handlers
            NettyUtil.registerHandlers(pipe, channelHandlerInitializer::registerInboundHandlers, NettyUtil.INBOUND);

            //terminating handler
            pipe.addLast(MessageRouter.NAME, new MessageRouter());

            //register messenger specific outbound handlers
            NettyUtil.registerHandlers(pipe, channelHandlerInitializer::registerOutboundHandlers, NettyUtil.OUTBOUND);
        }
        pipe.addLast(MessageEncoder.NAME, new MessageEncoder());

        if (!NettyUtil.isPooled(ch)) {
            log.debug("{} HANDLER => Channel is not pooled. Registering timeout handlers", NettyUtil.id(ch));
            NettyUtil.registerTimeoutHandlers(ch);
        }

        NettyUtil.printChannelPipeline(log, ch);
    }
}
