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
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * <p>Abstract MessageOutboundEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class MessageOutboundEncoder<T extends AbstractRequest> extends MessageToMessageEncoder<Envelope<T>> {

    private final Logger log;

    private Channel channel;

    /**
     * <p>Constructor for MessageOutboundEncoder.</p>
     */
    protected MessageOutboundEncoder() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * <p>Constructor for MessageOutboundEncoder.</p>
     *
     * @param outboundMessageType a {@link java.lang.Class} object
     */
    protected MessageOutboundEncoder(Class<Envelope<T>> outboundMessageType) {
        super(outboundMessageType);
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * <p>acceptMessage.</p>
     *
     * @param requestClass a {@link java.lang.Class} object
     * @param envelope a {@link com.ibasco.agql.core.Envelope} object
     * @return a boolean
     * @throws java.lang.Exception if any.
     */
    protected boolean acceptMessage(Class<T> requestClass, Envelope<T> envelope) throws Exception {
        return super.acceptOutboundMessage(envelope);
    }

    /**
     * <p>encodeMessage.</p>
     *
     * @param ctx a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg a {@link com.ibasco.agql.core.Envelope} object
     * @param out a {@link java.util.List} object
     * @throws java.lang.Exception if any.
     */
    abstract protected void encodeMessage(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception;

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        super.write(ctx, msg, promise);
    }

    /** {@inheritDoc} */
    @Override
    protected final void encode(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        encodeMessage(ctx, msg, out);
    }

    /** {@inheritDoc} */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ensureNotSharable();
        this.channel = ctx.channel();
    }

    /** {@inheritDoc} */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.channel = null;
    }

    /**
     * <p>getRequest.</p>
     *
     * @return a {@link com.ibasco.agql.core.Envelope} object
     */
    protected final Envelope<AbstractRequest> getRequest() {
        assert channel != null;
        return getContext().properties().envelope();
    }

    /**
     * <p>getContext.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    protected final NettyChannelContext getContext() {
        return NettyChannelContext.getContext(channel);
    }

    /**
     * <p>isDebugEnabled.</p>
     *
     * @return a boolean
     */
    protected final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * <p>trace.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void trace(String msg, Object... args) {
        if (log.isTraceEnabled())
            log(msg, log::trace, args);
    }

    /**
     * <p>error.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void error(String msg, Object... args) {
        if (log.isErrorEnabled())
            log(msg, log::error, args);
    }

    /**
     * <p>info.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void info(String msg, Object... args) {
        if (log.isInfoEnabled())
            log(msg, log::info, args);
    }

    /**
     * <p>debug.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void debug(String msg, Object... args) {
        if (log.isDebugEnabled())
            log(msg, log::debug, args);
    }

    /**
     * <p>warn.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    /**
     * <p>log.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param level a {@link java.util.function.BiConsumer} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(String.format("%s OUT => %s", Netty.id(channel), msg), args);
    }
}
