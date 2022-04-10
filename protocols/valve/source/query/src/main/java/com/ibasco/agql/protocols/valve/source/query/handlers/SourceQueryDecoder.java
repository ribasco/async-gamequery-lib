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

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.exceptions.DecodeException;
import com.ibasco.agql.core.transport.handlers.MessageInboundHandler;
import static com.ibasco.agql.core.util.Bits.isSet;
import com.ibasco.agql.core.util.Functions;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    abstract protected Object decodePacket(ChannelHandlerContext ctx, T request, final SourceQuerySinglePacket msg) throws Exception;

    @Override
    public final void readMessage(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        final SourceQueryRequest request = context.properties().request();

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

    //<editor-fold desc="Utility Functions for Sub-classes">
    protected <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Supplier<A> reader, Consumer<B> writer) throws DecodeException {
        decodeFlag(name, buf, flags, flag, reader, writer, null);
    }

    protected <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Supplier<A> reader, Consumer<B> writer, Function<A, B> transformer) throws DecodeException {
        decodeFlag(name, buf, flags, flag, buf1 -> reader.get(), writer, transformer);
    }

    protected <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Function<ByteBuf, A> reader, Consumer<B> writer) throws DecodeException {
        decodeFlag(name, buf, flags, flag, reader, writer, null);
    }

    protected <A, B> void decodeFlag(String name, ByteBuf buf, int flags, int flag, Function<ByteBuf, A> reader, Consumer<B> writer, Function<A, B> transformer) throws DecodeException {
        if (!isSet(flags, flag)) {
            debug("[O2] Flag '{}' not set. Skipping (Readable bytes: {})", name, buf.readableBytes());
            return;
        }
        if (!buf.isReadable()) {
            error("[O2] Skipped decoding flag '{}'. Not enough bytes, packet is incomplete. (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        int bytesBefore = -1;
        try {
            bytesBefore = buf.readableBytes();
            A fromValue = reader.apply(buf);
            if (writer != null) {
                if (transformer == null)
                    transformer = Functions::cast;
                B toValue = transformer.apply(fromValue);
                writer.accept(toValue);
                debug("[O2] Decoded flag '{}' at index position '{}' = '{}'", name, startPosition, toValue);
            }
        } catch (Throwable e) {
            error("[O2] Failed to decode flag '{}' at start position '{}' (remaining bytes: {})\n{}", name, startPosition, bytesBefore, Netty.prettyHexDump(buf), e);
        }
    }

    protected <A, B> void decodeField(String name, ByteBuf buf, Supplier<A> reader, Consumer<B> writer) throws DecodeException {
        decodeField(name, buf, reader, writer, null);
    }

    protected <A, B> void decodeField(String name, ByteBuf buf, Supplier<A> reader, Consumer<B> writer, Function<A, B> transformer) throws DecodeException {
        decodeField(name, buf, b -> reader.get(), writer, transformer);
    }

    protected <A, B> void decodeField(String name, ByteBuf buf, Function<ByteBuf, A> reader, Consumer<B> writer) throws DecodeException {
        decodeField(name, buf, reader, writer, null);
    }

    protected <A, B> void decodeField(String name, ByteBuf buf, Function<ByteBuf, A> reader, Consumer<B> writer, Function<A, B> transformer) throws DecodeException {
        if (!buf.isReadable()) {
            error("[O1] Skipped decoding field '{}'. Not enough bytes, packet is incomplete. (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return;
        }
        int startPosition = buf.readerIndex();
        int bytesBefore = -1;
        try {
            bytesBefore = buf.readableBytes();
            A fromValue = reader.apply(buf);
            if (writer != null) {
                if (transformer == null)
                    transformer = Functions::cast;
                B toValue = transformer.apply(fromValue);
                writer.accept(toValue);
                debug("[O1] Decoded field '{}' at index position '{}' = '{}'", name, startPosition, toValue);
            }
        } catch (Throwable e) {
            error("[O1] Failed to decode field '{}' at start position '{}' (remaining bytes: {})\n{}", name, startPosition, bytesBefore, Netty.prettyHexDump(buf), e);
        }
    }

    protected <V> V decodeField(String name, V defaultValue, ByteBuf buf, Function<ByteBuf, V> decoder) throws DecodeException {
        if (!buf.isReadable()) {
            error("[O2] Skipped decoding field '{}'. Not enough bytes, packet is incomplete. (Reader Index: {}, Readable Bytes: {})", name, buf.readerIndex(), buf.readableBytes());
            return defaultValue;
        }
        int startPosition = buf.readerIndex();
        int bytesBefore = -1;
        V returnValue;
        try {
            bytesBefore = buf.readableBytes();
            returnValue = decoder.apply(buf);
            debug("[O2] Decoded field '{}' at index position '{}' = '{}'", name, startPosition, returnValue);
        } catch (Throwable e) {
            error("[O2] Failed to decode field '{}' at start position '{}' (remaining bytes: {})\n{}", name, startPosition, bytesBefore, Netty.prettyHexDump(buf), e);
            returnValue = null;
        }
        returnValue = returnValue == null ? defaultValue : returnValue;
        return returnValue;
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
    //</editor-fold>
}
