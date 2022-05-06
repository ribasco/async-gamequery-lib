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
import com.ibasco.agql.core.NettyChannelContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;

/**
 * <p>Abstract MessageInboundDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class MessageInboundDecoder extends MessageInboundHandler {

    private boolean suppressLog;

    /** {@inheritDoc} */
    @Override
    public final void readMessage(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        final AbstractRequest request = context.properties().request();

        Object out = msg;
        try {
            beforeDecode(ctx);
            if (acceptMessage(request, msg)) {
                baseDebug("ACCEPTED message of type '{}'", msg.getClass().getSimpleName());
                try {
                    out = decodeMessage(ctx, request, msg);
                } finally {
                    //release assuming we have decoded and consumed the message
                    if (ReferenceCountUtil.release(msg)) {
                        baseDebug("Released reference counted message '{}'", msg.getClass().getSimpleName());
                    } else {
                        baseDebug("Retained reference counted message '{}'", msg.getClass().getSimpleName());
                    }
                }
            } else {
                baseDebug("REJECTED message of type '{}' (Reason: Rejected by the concrete handler)", msg.getClass().getSimpleName());
            }
        } catch (DecoderException e) {
            throw e;
        } catch (Exception e) {
            throw new DecoderException(e);
        } finally {
            if (out != null) {
                if (out != msg)
                    baseDebug("DECODED messsage to '{}'. Passing to next handler", out.getClass().getSimpleName());
                ctx.fireChannelRead(out);
            } else {
                baseDebug("No decoded message received. Do not propagate.");
            }
            afterDecode(ctx);
        }
    }

    /**
     * <p>beforeDecode.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     */
    protected void beforeDecode(ChannelHandlerContext ctx) {}

    /**
     * <p>acceptMessage.</p>
     *
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param msg
     *         a {@link java.lang.Object} object
     *
     * @return a boolean
     */
    abstract protected boolean acceptMessage(AbstractRequest request, final Object msg);

    private void baseDebug(String msg, Object... args) {
        if (!isSuppressLog())
            super.debug(msg, args);
    }

    /**
     * <p>decodeMessage.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param msg
     *         a {@link java.lang.Object} object
     *
     * @return a {@link java.lang.Object} object
     *
     * @throws java.lang.Exception
     *         if any.
     */
    abstract protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, final Object msg) throws Exception;

    /**
     * <p>afterDecode.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     */
    protected void afterDecode(ChannelHandlerContext ctx) {}

    /**
     * <p>isSuppressLog.</p>
     *
     * @return a boolean
     */
    protected boolean isSuppressLog() {
        return suppressLog;
    }

    /**
     * <p>Setter for the field <code>suppressLog</code>.</p>
     *
     * @param suppressLog
     *         a boolean
     */
    protected void setSuppressLog(boolean suppressLog) {
        this.suppressLog = suppressLog;
    }
}
