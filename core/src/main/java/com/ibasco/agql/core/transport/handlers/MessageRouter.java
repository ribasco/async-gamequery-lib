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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.Messenger;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
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

    private static final ChannelFutureListener REGISTER_READ_TIMEOUT = future -> {
        Channel ch = future.channel();
        assert ch.eventLoop().inEventLoop();
        if (future.isSuccess()) {
            registerReadTimeoutHandler(ch);
        } else {
            log.error("{} ROUTER => Error during read timout handler registration", NettyUtil.id(ch), future.cause());
        }
    };

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Envelope<AbstractRequest> request = getRequest(ctx.channel());
        if (request != null && request.content() != null) {
            log.debug("{} ROUTER (OUTBOUND) => Writing message '{}' to transport (Request Id: {}, Type: {})", NettyUtil.id(ctx.channel()), msg, request.content().id(), request.content().getClass().getSimpleName());
        } else {
            log.debug("{} ROUTER (OUTBOUND) => Writing message '{}' to transport (Request Id: N/A)", NettyUtil.id(ctx.channel()), msg);
        }
        registerTimeoutOnWrite(promise, ctx.channel());
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NotNull Object response) throws Exception {
        final Channel channel = ctx.channel();
        checkResponse(channel);
        final Envelope<AbstractResponse> envelope = getResponse(channel);
        try {
            //send the response back to the client/messenger
            if (response instanceof AbstractResponse) {
                log.debug("{} ROUTER (INBOUND) => Success! Received a decoded response: {}", NettyUtil.id(channel), response);
                envelope.content((AbstractResponse) response);
                log.debug("{} ROUTER (INBOUND) => Notifying messenger (Promise: {}, Content: {})", NettyUtil.id(channel), envelope.promise(), envelope.content());
                envelope.messenger().receive(envelope, null);
            } else {
                if (response instanceof ReferenceCounted && ReferenceCountUtil.refCnt(response) == 0) {
                    log.error("{} ROUTER (INBOUND) => Fail! Expected a decoded response of type 'AbstractResponse' but got '{}' (Reference Count has reached 0)", NettyUtil.id(channel), response.getClass().getSimpleName());
                } else {
                    log.debug("{} ROUTER (INBOUND) => Fail! Expected a decoded response of type 'AbstractResponse' but got '{} ({})' instead (Details: {})", NettyUtil.id(channel), response.getClass().getSimpleName(), response.hashCode(), response);
                }
                envelope.messenger().receive(envelope, new IllegalStateException("No handlers found for message: " + response.getClass().getSimpleName()));
            }
        } finally {
            log.debug("{} ROUTER (INBOUND) => Releasing message '{}'", NettyUtil.id(channel), response.getClass().getSimpleName());
            if (ReferenceCountUtil.release(response)) {
                log.debug("{} ROUTER (INBOUND) => Released reference counted message", NettyUtil.id(channel));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable error) throws Exception {
        Channel channel = ctx.channel();
        assert channel != null;
        checkResponse(channel);
        Envelope<AbstractResponse> response = getResponse(channel);
        log.error(String.format("%s ROUTER (ERROR) => Type: %s, Message: %s (Channel: %s, Pooled: %s)",
                                NettyUtil.id(channel),
                                error.getClass().getSimpleName(),
                                StringUtils.defaultString(error.getMessage(), "N/A"),
                                channel,
                                NettyChannelPool.isPooled(channel)), error);
        response.messenger().receive(response, error);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("{} ROUTER (INBOUND) => Channel Closed (Pooled: {})", NettyUtil.id(ctx.channel()), NettyUtil.isPooled(ctx.channel()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
        Envelope<AbstractRequest> request = getRequest(ctx.channel());
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

    private void checkResponse(Channel channel) {
        Envelope<AbstractResponse> response = getResponse(channel);
        if (response == null)
            throw new IllegalStateException("Missing response envelope for channel '" + NettyUtil.id(channel) + "'");
        if (response.promise() == null)
            throw new IllegalStateException("Missing client promise for channel '" + NettyUtil.id(channel) + "'");
        if (response.messenger() == null)
            throw new IllegalStateException("Missing messenger instance for channel '" + NettyUtil.id(channel) + "'");
    }

    private static Envelope<AbstractRequest> getRequest(Channel channel) {
        return channel.attr(NettyChannelAttributes.REQUEST).get();
    }

    private static Envelope<AbstractResponse> getResponse(Channel channel) {
        return channel.attr(NettyChannelAttributes.RESPONSE).get();
    }

    private static void registerTimeoutOnWrite(ChannelPromise promise, Channel channel) {
        Envelope<AbstractRequest> requestEnvelope = getRequest(channel);
        if (requestEnvelope != null && requestEnvelope.promise().isDone()) {
            log.warn("Skipping timeout registration. Response already received (Promise: {})", requestEnvelope.promise());
            return;
        }
        if (promise.isDone()) {
            if (promise.isSuccess())
                registerReadTimeoutHandler(channel);
            else {
                log.error("Failed write operation for request '{}'", requestEnvelope, promise.cause());
            }
        } else {
            promise.addListener(REGISTER_READ_TIMEOUT);
        }
    }
}
