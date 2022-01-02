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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;

abstract public class MessageInboundDecoder extends MessageInboundHandler {

    private boolean suppressLog;

    abstract protected boolean acceptMessage(AbstractRequest request, final Object msg);

    abstract protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, final Object msg) throws Exception;

    protected void beforeDecode(ChannelHandlerContext ctx) {}

    protected void afterDecode(ChannelHandlerContext ctx) {}

    @Override
    public final void readMessage(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final AbstractRequest request = getRequest().content();

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

    protected void setSuppressLog(boolean suppressLog) {
        this.suppressLog = suppressLog;
    }

    protected boolean isSuppressLog() {
        return suppressLog;
    }

    private void baseDebug(String msg, Object... args) {
        if (!isSuppressLog())
            super.debug(msg, args);
    }
}