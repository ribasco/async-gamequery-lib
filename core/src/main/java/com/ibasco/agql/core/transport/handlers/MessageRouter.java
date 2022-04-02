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

import com.ibasco.agql.core.*;
import com.ibasco.agql.core.exceptions.InvalidPacketException;
import com.ibasco.agql.core.exceptions.NoMessageHandlerException;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * The last handler in the chain that is responsible for routing the response back to the {@link Messenger}
 *
 * @author Rafael Luis Ibasco
 */
public class MessageRouter extends ChannelDuplexHandler {

    public static final String NAME = "messageRouter";

    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);

    public static final Marker UNFREED_RESOURCE = MarkerFactory.getMarker("UNFREED_RESOURCE");

    private static final ChannelFutureListener REGISTER_READ_TIMEOUT = future -> {
        Channel ch = future.channel();
        assert ch.eventLoop().inEventLoop();
        if (future.isSuccess()) {
            registerReadTimeoutHandler(ch);
        } else {
            log.error("{} ROUTER => Error during read timout handler registration", NettyUtil.id(ch), future.cause());
            if (log.isDebugEnabled()) {
                future.cause().printStackTrace(System.err);
            }
        }
    };

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        final NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        final Envelope<AbstractRequest> request = context.properties().envelope();
        if (request == null)
            throw new IllegalStateException("Missing request envelope for channel: " + ctx.channel());
        if (request.content() != null) {
            log.debug("{} ROUTER (OUTBOUND) => Writing message '{}' to transport (Request Id: {}, Type: {})", context.id(), msg, request.content().id(), request.content().getClass().getSimpleName());
        } else {
            log.debug("{} ROUTER (OUTBOUND) => Writing message '{}' to transport (Request Id: N/A)", context.id(), msg);
        }
        registerTimeoutOnWrite(promise, ctx.channel());
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NotNull Object response) throws Exception {
        final NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        final Envelope<AbstractRequest> envelope = context.properties().envelope();
        if (context.messenger() == null)
            throw new IllegalStateException("Messenger not found for context: " + context);
        try {
            //send the response back to the messenger
            if (response instanceof AbstractResponse) {
                log.debug("{} ROUTER (INBOUND) => Response Received. Notifying messenger (Promise: {}, Request: {}, Response: {})", NettyUtil.id(context.channel()), context.properties().responsePromise(), envelope.content(), response);
                context.receive((AbstractResponse) response);
            } else {
                //throw an error if we did not receive an AbstractResponse type
                if (!isAccessible(response)) {
                    context.receive(new NoMessageHandlerException(String.format("Resource '%s' is no longer accessible as it has already been released. No handlers available for this message (Reference count: %d, Context Id: %s)", response.getClass().getSimpleName(), ReferenceCountUtil.refCnt(response), context.id())));
                } else {
                    Exception cause;
                    //If we get a raw ByteBuf instance, then we did not have any handlers available to process this packet. Possibly a malformed or unsupported packet response type.
                    if (response instanceof ByteBuf) {
                        byte[] data = NettyUtil.getBufferContents((ByteBuf) response);
                        cause = new InvalidPacketException("Received a RAW unsupported/malformed packet from the server and no handlers were available to process it", data);
                        log.error("{} ROUTER (ERROR) => Packet Dump of raw ByteBuf '{} of request '{}'\n{}", context.id(), response.getClass().getSimpleName(), context.properties().request(), ByteUtil.toHexString(data));
                    }
                    //If we get a raw Packet instance, this means we successfully decoded it, but no other handlers were available to process it. Why?
                    else if (response instanceof AbstractPacket) {
                        byte[] data = NettyUtil.getBufferContents(((AbstractPacket) response).content(), null);
                        cause = new InvalidPacketException("Received a PARTIAL decoded packet but no other handlers were available to process it to produce a desirable response", data);
                        log.error("{} ROUTER (ERROR) => Packet Dump of Packet type '{}' of request '{}'\n{}", context.id(), response.getClass().getSimpleName(), context.properties().request(), ByteUtil.toHexString(data));
                    } else {
                        cause = new IllegalStateException(String.format("Received unknown message type '%s' in response", response.getClass().getSimpleName()));
                    }
                    //report back error
                    Exception error = new NoMessageHandlerException(String.format("No handlers found for message type '%s' (Request: %s)", response.getClass().getSimpleName(), context.properties().request()), cause);
                    log.debug("{} ROUTER (INBOUND) => Fail! Expected a decoded response of type 'AbstractResponse' but got '{} ({})' instead (Details: {})", context.id(), response.getClass().getSimpleName(), response.hashCode(), response, error);
                    context.receive(error);
                }
            }
        } finally {
            log.debug("{} ROUTER (INBOUND) => Releasing message '{}'", context.id(), response.getClass().getSimpleName());
            try {
                if (ReferenceCountUtil.release(response))
                    log.debug("{} ROUTER (INBOUND) => Released reference counted message", context.id());
            } catch (IllegalReferenceCountException e) {
                int refCnt = ReferenceCountUtil.refCnt(response);
                log.warn("{} ROUTER (INBOUND) => Attempted to de-allocate resource '{}' but the resource has already been released. (Reference count: {})", context.id(), response, refCnt, e);
            }
        }
    }

    private boolean isAccessible(Object msg) {
        return !(msg instanceof ReferenceCounted) || ReferenceCountUtil.refCnt(msg) > 0;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable error) throws Exception {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        log.debug(String.format("%s ROUTER (ERROR) => Type: %s, Message: %s (Channel: %s, Pooled: %s)",
                                context.id(),
                                error.getClass().getSimpleName(),
                                StringUtils.defaultString(error.getMessage(), "N/A"),
                                context.channel(),
                                NettyChannelPool.isPooled(context.channel())), error);
        context.receive(error);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("{} ROUTER (INBOUND) => Channel Closed (Pooled: {})", NettyUtil.id(ctx.channel()), NettyChannelPool.isPooled(ctx.channel()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
        log.debug("{} ROUTER (INBOUND) => Read Complete", NettyUtil.id(ctx.channel()));
    }

    private static void registerReadTimeoutHandler(Channel channel) {
        try {
            //ensure they are not existing within the current pipeline
            channel.pipeline().remove(ReadTimeoutHandler.class);
        } catch (NoSuchElementException ignored) {
        }
        Integer readTimeout = TransportOptions.READ_TIMEOUT.attr(channel);
        assert readTimeout != null;
        channel.pipeline().addBefore(MessageDecoder.NAME, "readTimeout", new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS));
        log.debug("{} ROUTER (OUTBOUND) => Registered ReadTimeoutHandler (Read Timeout: {} ms)", NettyUtil.id(channel), readTimeout);
    }

    private static void registerTimeoutOnWrite(ChannelPromise promise, Channel channel) {
        NettyChannelContext context = NettyChannelContext.getContext(channel);
        Envelope<AbstractRequest> envelope = context.properties().envelope();
        if (envelope != null && context.properties().responsePromise().isDone()) {
            log.warn("Skipping timeout registration. Response already received (Promise: {})", context.properties().responsePromise());
            return;
        }
        if (promise.isDone()) {
            if (promise.isSuccess())
                registerReadTimeoutHandler(channel);
            else {
                log.error("Failed write operation for request '{}'", envelope, promise.cause());
            }
        } else {
            promise.addListener(REGISTER_READ_TIMEOUT);
        }
    }
}
