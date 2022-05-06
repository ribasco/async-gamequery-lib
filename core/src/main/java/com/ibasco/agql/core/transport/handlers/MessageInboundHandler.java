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
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * <p>Abstract MessageInboundHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unused")
abstract public class MessageInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MessageInboundHandler.class);

    private final BiFunction<Channel, String, String> logtemplate;

    private Channel channel;

    /**
     * <p>Constructor for MessageInboundHandler.</p>
     */
    protected MessageInboundHandler() {
        this.logtemplate = (ch, msg) -> Netty.id(ch) + " (" + getClass().getSimpleName() + ") INB => " + msg;
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

    /** {@inheritDoc} */
    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.channel == null || this.channel != ctx.channel())
            this.channel = ctx.channel();
        checkEnvelope(ctx);
        readMessage(ctx, msg);
    }

    private void checkEnvelope(ChannelHandlerContext ctx) {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        Envelope<AbstractRequest> requestEnvelope = context.properties().envelope();
        if (requestEnvelope == null)
            throw new IllegalStateException("No request envelope is attached to this channel");
        if (requestEnvelope.content() == null)
            throw new IllegalStateException("Request envelope's content is null");
    }

    /**
     * <p>readMessage.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg
     *         a {@link java.lang.Object} object
     *
     * @throws java.lang.Exception
     *         if any.
     */
    protected void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    /**
     * <p>isDebugEnabled.</p>
     *
     * @return a boolean
     */
    protected boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * <p>trace.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void trace(String msg, Object... args) {
        log(msg, log::trace, args);
    }

    /**
     * <p>log.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param level
     *         a {@link java.util.function.BiConsumer} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected final void log(String msg, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(logtemplate.apply(channel, msg), args);
    }

    /**
     * <p>error.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void error(String msg, Object... args) {
        log(msg, log::error, args);
    }

    /**
     * <p>error.</p>
     *
     * @param logger
     *         a {@link org.slf4j.Logger} object
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void error(Logger logger, ChannelHandlerContext ctx, String msg, Object... args) {
        log(msg, ctx, logger::error, args);
    }

    /**
     * <p>log.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param level
     *         a {@link java.util.function.BiConsumer} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected final void log(String msg, ChannelHandlerContext ctx, BiConsumer<String, Object[]> level, Object... args) {
        level.accept(logtemplate.apply(ctx.channel(), msg), args);
    }

    /**
     * <p>info.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void info(String msg, Object... args) {
        log(msg, log::info, args);
    }

    /**
     * <p>debug.</p>
     *
     * @param logger
     *         a {@link org.slf4j.Logger} object
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void debug(Logger logger, String msg, Object... args) {
        log(msg, logger::debug, args);
    }

    /**
     * <p>debug.</p>
     *
     * @param logger
     *         a {@link org.slf4j.Logger} object
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void debug(Logger logger, ChannelHandlerContext ctx, String msg, Object... args) {
        log(msg, ctx, logger::debug, args);
    }

    /**
     * <p>debug.</p>
     *
     * @param logger
     *         a {@link org.slf4j.Logger} object
     * @param marker
     *         a {@link org.slf4j.Marker} object
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void debug(Logger logger, Marker marker, String msg, Object... args) {
        logger.debug(marker, logtemplate.apply(channel, msg), args);
    }

    /**
     * <p>warn.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected final void warn(String msg, Object... args) {
        log(msg, log::warn, args);
    }

    /**
     * <p>printField.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     * @param value
     *         a {@link java.lang.Object} object
     */
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

    /**
     * <p>debug.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected void debug(String msg, Object... args) {
        log(msg, log::debug, args);
    }
}
