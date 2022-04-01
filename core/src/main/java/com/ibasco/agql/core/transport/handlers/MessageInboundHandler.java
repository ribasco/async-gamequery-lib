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
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
abstract public class MessageInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MessageInboundHandler.class);

    private Channel channel;

    protected MessageInboundHandler() {

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
        checkEnvelope();
        readMessage(ctx, msg);
    }

    private void checkEnvelope() {
        Envelope<AbstractRequest> requestEnvelope = getRequest();
        if (requestEnvelope == null)
            throw new IllegalStateException("No request envelope is attached to this channel");
        if (requestEnvelope.content() == null)
            throw new IllegalStateException("Request envelope's content is null");
    }

    protected boolean isDebugEnabled() {
        return log.isDebugEnabled();
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

    protected void debug(Logger logger, String msg, Object... args) {
        log(msg, logger::debug, args);
    }

    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(String.format("%s (%-25s) INB => %s", NettyUtil.id(channel), getClass().getSimpleName(), msg), args);
    }

    protected final NettyChannelContext getContext() {
        return NettyChannelContext.getContext(channel);
    }

    protected final Envelope<AbstractRequest> getRequest() {
        assert channel != null;
        return getContext().properties().envelope();
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
