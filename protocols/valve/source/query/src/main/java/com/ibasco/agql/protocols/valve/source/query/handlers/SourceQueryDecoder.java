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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.transport.handlers.MessageInboundHandler;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;

import java.util.Objects;

/**
 * A special decoder used for messages based on the Source Query Protocol. This handler Accepts either an instance of {@link SourceQueryPacket} or a {@link SourceQueryResponse}
 *
 * @param <T>
 *         The type of {@link SourceQueryRequest} this decoder will handle/process
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryDecoder<T extends SourceQueryRequest> extends MessageInboundHandler {

    abstract protected boolean acceptPacket(final SourceQueryMessage msg);

    abstract protected Object decodePacket(ChannelHandlerContext ctx, T request, final SourceQuerySinglePacket msg);

    @Override
    public final void readMessage(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final SourceQueryRequest request = (SourceQueryRequest) getRequest().content();

        Object out = msg;
        try {
            //only accept SourceQuerySinglePacket instances
            if (!(msg instanceof SourceQuerySinglePacket)) {
                debug("SKIPPING message of type '{}'", msg.getClass().getSimpleName());
                return;
            }

            final SourceQuerySinglePacket packet = (SourceQuerySinglePacket) msg;

            if (acceptPacket(new SourceQueryMessage(request, packet))) {
                debug("ACCEPTED message of type '{}'", msg.getClass().getSimpleName());
                try {
                    @SuppressWarnings("unchecked")
                    T cast = (T) request;
                    out = decodePacket(ctx, cast, packet);
                } finally {
                    //release assuming we have decoded and consumed the message
                    debug("Releasing reference counted message '{}' (Decoded message: {})", msg.getClass().getSimpleName(), out);
                    ReferenceCountUtil.release(msg);
                }
            } else {
                debug("REJECTED message of type '{}' (Reason: Rejected by the concrete handler)", msg.getClass().getSimpleName());
            }
        } catch (DecoderException e) {
            throw e;
        } catch (Exception e) {
            throw new DecoderException(e);
        } finally {
            if (out != null) {
                if (out != msg)
                    debug("DECODED messsage to '{}'. Passing to next handler", out.getClass().getSimpleName());
                ctx.fireChannelRead(out);
            } else {
                debug("No decoded message received. Do not propagate.");
            }
        }
    }

    protected static final class SourceQueryMessage {

        private final SourceQueryRequest request;

        private final SourceQuerySinglePacket msg;

        private SourceQueryMessage(SourceQueryRequest request, SourceQuerySinglePacket msg) {
            this.request = Objects.requireNonNull(request, "Request cannot be null");
            this.msg = Objects.requireNonNull(msg, "Message cannot be null");
        }

        public boolean hasRequest(Class<? extends SourceQueryRequest> cls) {
            return Objects.requireNonNull(cls, "Missing request class").equals(request.getClass());
        }

        public boolean hasHeader(int header) {
            return msg.getHeader() == header;
        }

        public SourceQueryRequest getRequest() {
            return request;
        }

        public SourceQuerySinglePacket getPacket() {
            return this.msg;
        }
    }
}
