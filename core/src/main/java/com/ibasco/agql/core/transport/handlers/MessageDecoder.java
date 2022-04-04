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

package com.ibasco.agql.core.transport.handlers;

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The initial global intercept for all raw data recived from a remote address. This handler unwraps the message's content then passes it
 * to the next handlers in the pipeline for further processing.
 *
 * @author Rafael Luis Ibasco
 */
public class MessageDecoder extends ChannelInboundHandlerAdapter {

    public static final String NAME = "responseDecoder";

    private static final Logger log = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NotNull Object msg) {
        final NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        //Make sure we have a request associated, otherwise do not propagate
        if (hasInvalidRequest(context.channel())) {
            log.debug("{} INB => Received incoming data but No VALID request found. It has either been cleared or has been marked as completed. Not propagating (Msg: {}, Request: {})", context.id(), msg, context.properties().envelope());
            if (msg instanceof ByteBuf && log.isDebugEnabled()) {
                ByteBuf b = (ByteBuf) msg;
                NettyUtil.dumpBuffer(log::debug, String.format("%s INB => Discarded packet (Size: %d)", context.id(), b.readableBytes()), b, 32);
            }
            ReferenceCountUtil.release(msg);
            return;
        }

        try {
            log.debug("{} INB => Received incoming data from server of type: {} (Length: {} bytes)", context.id(), String.format("%s (%d)", msg.getClass().getSimpleName(), msg.hashCode()), getResponseLength(msg));
            Object decoded;
            //TODO: Should we process addressedenvelope instances so we can retrieve sender address information?
            if (msg instanceof ByteBufHolder) {
                decoded = ((ByteBufHolder) msg).content();
                log.debug("{} INB => Passing decoded message ({}) to the next handler(s)", context.id(), decoded.getClass().getSimpleName());
            } else {
                decoded = msg;
                log.debug("{} INB => Passing message ({}) to the next handler(s)", context.id(), decoded.getClass().getSimpleName());
            }
            ctx.fireChannelRead(decoded);
        } catch (Throwable ex) {
            log.error(String.format("%s INB => Error occured during initialization", context.id()), ex);
            ctx.fireExceptionCaught(ex);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        final Channel channel = ctx.channel();
        final NettyChannelContext context = NettyChannelContext.getContext(channel);
        final String id = NettyUtil.id(channel);
        //Make sure we have a request associated, otherwise do not propagate
        if (hasInvalidRequest(channel)) {
            log.debug("{} INB => No VALID request found (Error: {}, Request: {})", id, cause.getClass().getSimpleName(), context.properties().envelope(), cause);
            return;
        }
        ctx.fireExceptionCaught(cause);
    }

    private boolean hasInvalidRequest(Channel channel) {
        NettyChannelContext context = NettyChannelContext.getContext(channel);
        return context.properties().envelope() == null || context.properties().responsePromise().isDone();
    }

    private static int getResponseLength(Object msg) {
        if (msg instanceof DatagramPacket) {
            return ((DatagramPacket) msg).content().readableBytes();
        } else if (msg instanceof ByteBuf) {
            return ((ByteBuf) msg).readableBytes();
        } else {
            return -1;
        }
    }
}
