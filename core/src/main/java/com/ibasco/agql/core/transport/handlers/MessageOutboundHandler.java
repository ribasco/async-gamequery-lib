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
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

/**
 * <p>Abstract MessageOutboundHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class MessageOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final Logger log;

    private Channel channel;

    private final Class<? extends AbstractRequest> filterRequestClass;

    private final Class<?> filterMessageType;

    /**
     * <p>Constructor for MessageOutboundHandler.</p>
     */
    protected MessageOutboundHandler() {
        this(null, null);
    }

    /**
     * <p>Constructor for MessageOutboundHandler.</p>
     *
     * @param filterRequestClass a {@link java.lang.Class} object
     * @param filterMessageType a {@link java.lang.Class} object
     */
    protected MessageOutboundHandler(Class<? extends AbstractRequest> filterRequestClass, Class<?> filterMessageType) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.filterRequestClass = filterRequestClass;
        this.filterMessageType = filterMessageType;
    }

    /**
     * <p>writeMessage.</p>
     *
     * @param ctx a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg a {@link java.lang.Object} object
     * @param promise a {@link io.netty.channel.ChannelPromise} object
     * @throws java.lang.Exception if any.
     */
    protected void writeMessage(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    /** {@inheritDoc} */
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

    private void ensureValidState() {
        Envelope<AbstractRequest> requestEnvelope = getRequest();
        if (requestEnvelope == null)
            throw new IllegalStateException("No request envelope is attached to this channel");
        if (requestEnvelope.content() == null)
            throw new IllegalStateException("Request envelope's content is null");
    }

    /**
     * <p>getContext.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    protected final NettyChannelContext getContext() {
        assert channel != null;
        return NettyChannelContext.getContext(channel);
    }

    /**
     * <p>getRequest.</p>
     *
     * @return a {@link com.ibasco.agql.core.Envelope} object
     */
    protected final Envelope<AbstractRequest> getRequest() {
        return getContext().properties().envelope();
    }

    /**
     * <p>trace.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void trace(String msg, Object... args) {
        log(msg, log::trace, args);
    }

    /**
     * <p>error.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void error(String msg, Object... args) {
        log(msg, log::error, args);
    }

    /**
     * <p>info.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void info(String msg, Object... args) {
        log(msg, log::info, args);
    }

    /**
     * <p>debug.</p>
     *
     * @param msg a {@link java.lang.String} object
     * @param args a {@link java.lang.Object} object
     */
    protected final void debug(String msg, Object... args) {
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
        level.accept(String.format("%s INB => %s", Netty.id(channel), msg), args);
    }

}
