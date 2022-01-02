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

package com.ibasco.agql.core.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

abstract public class MessageOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final Logger log;

    private Channel channel;

    private final Class<? extends AbstractRequest> filterRequestClass;

    private final Class<?> filterMessageType;

    protected MessageOutboundHandler() {
        this(null, null);
    }

    protected MessageOutboundHandler(Class<? extends AbstractRequest> filterRequestClass, Class<?> filterMessageType) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.filterRequestClass = filterRequestClass;
        this.filterMessageType = filterMessageType;
    }

    protected void writeMessage(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Override
    public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ensureValidState();
        if (filterRequestClass != null && getRequest().content().getClass().equals(filterRequestClass)) {
            ctx.write(msg, promise);
            return;
        }
        if (msg.getClass().equals(filterMessageType)) {
            ctx.write(msg, promise);
            return;
        }
        writeMessage(ctx, msg, promise);
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

    private void ensureValidState() {
        Envelope<AbstractRequest> requestEnvelope = getRequest();
        if (requestEnvelope == null)
            throw new IllegalStateException("No request envelope is attached to this channel");
        if (requestEnvelope.content() == null)
            throw new IllegalStateException("Request envelope's content is null");
    }

    protected final Envelope<AbstractRequest> getRequest() {
        assert channel != null;
        return channel.attr(ChannelAttributes.REQUEST).get();
    }

    protected final Envelope<AbstractResponse> getResponse() {
        assert channel != null;
        return channel.attr(ChannelAttributes.RESPONSE).get();
    }

    protected final void trace(String msg, Object... args) {
        log(msg, log::trace, args);
    }

    protected final void error(String msg, Object... args) {
        log(msg, log::error, args);
    }

    protected final void info(String msg, Object... args) {
        log(msg, log::info, args);
    }

    protected final void debug(String msg, Object... args) {
        log(msg, log::debug, args);
    }

    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(String.format("%s INB => %s", NettyUtil.id(channel), msg), args);
    }

}
