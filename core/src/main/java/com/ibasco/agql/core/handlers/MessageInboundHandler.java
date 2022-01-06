/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
abstract public class MessageInboundHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(MessageInboundHandler.class);

    private Channel channel;

    protected MessageInboundHandler() {
        //this.log = LoggerFactory.getLogger(this.getClass());
    }

    protected void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ensureNotSharable();
        this.channel = ctx.channel();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.channel = null;
    }

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        ensureValidState();
        readMessage(ctx, msg);
    }

    private void ensureValidState() {
        Envelope<AbstractRequest> requestEnvelope = getRequest();
        Envelope<AbstractResponse> responseEnvelope = getResponse();
        if (requestEnvelope == null)
            throw new IllegalStateException("No request envelope is attached to this channel");
        if (responseEnvelope == null)
            throw new IllegalStateException("No response envelope is attached to this channel");
        if (requestEnvelope.content() == null)
            throw new IllegalStateException("Request envelope's content is null");
    }

    protected void trace(String msg, Object... args) {
        log(msg, log::trace, args);
    }

    protected void error(String msg, Object... args) {
        log(msg, log::error, args);
    }

    protected void info(String msg, Object... args) {
        log(msg, log::info, args);
    }

    protected void debug(String msg, Object... args) {
        log(msg, log::debug, args);
    }

    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(String.format("%s INB => %s", NettyUtil.id(channel), msg), args);
    }

    protected final Envelope<AbstractRequest> getRequest() {
        assert channel != null;
        return channel.attr(NettyChannelAttributes.REQUEST).get();
    }

    protected final Envelope<AbstractResponse> getResponse() {
        assert channel != null;
        return channel.attr(NettyChannelAttributes.RESPONSE).get();
    }

    protected void printField(String name, Object value) {
        debug(String.format("    %-15s: ", name));
        if (value instanceof Number) {
            //noinspection MalformedFormatString
            debug(String.format("%-10d", value));
        } else if (value instanceof String || value instanceof Character) {
            debug(String.format("%-10s", value));
        }
        debug("");
    }
}
