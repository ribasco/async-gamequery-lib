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
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyChannelInitializer extends ChannelInitializer<Channel> {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelInitializer.class);

    private NettyChannelHandlerInitializer handlerInitializer;

    private static final ChannelFutureListener CLOSE_LISTENER = future -> {
        Channel channel = future.channel();
        channelClosed(channel, future.cause());
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
        ch.pipeline().fireUserEventTriggered(ChannelEvent.CLOSED);
    }

    private void initializeChannelHandlers(final Channel ch) {
        final ChannelPipeline pipe = ch.pipeline();
        pipe.addLast(MessageDecoder.NAME, new MessageDecoder());
        if (handlerInitializer != null) {
            synchronized (this) {
                //register messenger specific inbound handlers
                NettyUtil.registerHandlers(pipe, handlerInitializer::registerInboundHandlers, NettyUtil.INBOUND);

                //terminating handler
                pipe.addLast(MessageRouter.NAME, new MessageRouter());

                //register messenger specific outbound handlers
                NettyUtil.registerHandlers(pipe, handlerInitializer::registerOutboundHandlers, NettyUtil.OUTBOUND);
            }
        }
        pipe.addLast(MessageEncoder.NAME, new MessageEncoder());

        if (!NettyChannelPool.isPooled(ch)) {
            log.debug("{} HANDLER => Channel is not pooled. Registering timeout handlers", NettyUtil.id(ch));
            NettyUtil.registerTimeoutHandlers(ch);
        }

        NettyUtil.printChannelPipeline(log, ch);
    }

    public NettyChannelHandlerInitializer getHandlerInitializer() {
        return handlerInitializer;
    }

    public void setHandlerInitializer(NettyChannelHandlerInitializer handlerInitializer) {
        this.handlerInitializer = handlerInitializer;
    }
}
