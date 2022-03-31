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
import com.ibasco.agql.core.exceptions.NoMessageHandlerException;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    //TODO: Move to a separate re-usable class
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
                context.receiveResponse((AbstractResponse) response);
            } else {
                //report unwanted instances of objects not being released properly
                if (response instanceof ReferenceCounted && ReferenceCountUtil.refCnt(response) == 0) {
                    throw new IllegalStateException(String.format("Memory was not properly deallocated for object '%s' (Reference count: %d)", response.getClass().getSimpleName(), ReferenceCountUtil.refCnt(response)));
                } else {
                    Exception error = new NoMessageHandlerException("No handlers found for message: " + response.getClass().getSimpleName());
                    log.debug("{} ROUTER (INBOUND) => Fail! Expected a decoded response of type 'AbstractResponse' but got '{} ({})' instead (Details: {})", context.id(), response.getClass().getSimpleName(), response.hashCode(), response, error);
                    context.receiveResponse(error);
                }
            }
        } finally {
            log.debug("{} ROUTER (INBOUND) => Releasing message '{}'", context.id(), response.getClass().getSimpleName());
            if (ReferenceCountUtil.release(response))
                log.debug("{} ROUTER (INBOUND) => Released reference counted message", context.id());
        }
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
        context.receiveResponse(error);
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
