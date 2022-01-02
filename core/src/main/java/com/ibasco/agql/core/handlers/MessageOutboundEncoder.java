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

package com.ibasco.agql.core.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

abstract public class MessageOutboundEncoder<T extends AbstractRequest> extends MessageToMessageEncoder<Envelope<T>> {

    private final Logger log;

    private Channel channel;

    protected MessageOutboundEncoder() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    protected MessageOutboundEncoder(Class<Envelope<T>> outboundMessageType) {
        super(outboundMessageType);
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    protected boolean acceptMessage(Class<T> requestClass, Envelope<T> envelope) throws Exception {
        return super.acceptOutboundMessage(envelope);
    }

    abstract protected void encodeMessage(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception;

    @Override
    public final boolean acceptOutboundMessage(Object msg) throws Exception {
        //only accept envelope requests
        if (!(msg instanceof Envelope<?>)) {
            debug("REJECTED message '{}' (Reason: Not an envelope instance)", msg);
            return false;
        }

        Envelope<?> env = (Envelope<?>) msg;
        if (!(env.content() instanceof AbstractRequest)) {
            debug("REJECTED message '{}' (Reason: Content must be a request type)", env);
            return false;
        }
        //noinspection unchecked
        boolean accepted = acceptMessage((Class<T>) env.content().getClass(), (Envelope<T>) env);
        if (accepted) {
            debug("ACCEPTED message '{}'", msg.getClass().getSimpleName());
        } else {
            debug("REJECTED message '{}' (Reason: Rejected by concrete handler)", msg.getClass().getSimpleName());
        }
        return accepted;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        super.write(ctx, msg, promise);
    }

    @Override
    protected final void encode(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        encodeMessage(ctx, msg, out);
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

    protected final Envelope<AbstractRequest> getRequest() {
        assert channel != null;
        return channel.attr(ChannelAttributes.REQUEST).get();
    }

    protected final Envelope<AbstractResponse> getResponse() {
        assert channel != null;
        return channel.attr(ChannelAttributes.RESPONSE).get();
    }

    protected final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    protected final void trace(String msg, Object... args) {
        if (log.isTraceEnabled())
            log(msg, log::trace, args);
    }

    protected final void error(String msg, Object... args) {
        if (log.isErrorEnabled())
            log(msg, log::error, args);
    }

    protected final void info(String msg, Object... args) {
        if (log.isInfoEnabled())
            log(msg, log::info, args);
    }

    protected final void debug(String msg, Object... args) {
        if (log.isDebugEnabled())
            log(msg, log::debug, args);
    }

    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(String.format("%s OUT => %s", NettyUtil.id(channel), msg), args);
    }
}
