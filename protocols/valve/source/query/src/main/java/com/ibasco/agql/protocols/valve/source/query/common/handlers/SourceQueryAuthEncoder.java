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

package com.ibasco.agql.protocols.valve.source.query.common.handlers;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import java.util.List;
import static com.ibasco.agql.core.util.Bytes.toHexString;

/**
 * <p>Abstract SourceQueryAuthEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryAuthEncoder<T extends SourceQueryAuthRequest> extends SourceQueryEncoder<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 9;

    private final Class<T> requestClass;

    private final int requestHeader;

    private final int initialCapacity;

    /**
     * <p>Constructor for SourceQueryAuthEncoder.</p>
     *
     * @param requestClass
     *         a {@link java.lang.Class} object
     * @param requestHeader
     *         a int
     */
    protected SourceQueryAuthEncoder(Class<T> requestClass, int requestHeader) {
        this(requestClass, requestHeader, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * <p>Constructor for SourceQueryAuthEncoder.</p>
     *
     * @param requestClass
     *         a {@link java.lang.Class} object
     * @param requestHeader
     *         a int
     * @param initialCapacity
     *         a int
     */
    protected SourceQueryAuthEncoder(Class<T> requestClass, int requestHeader, int initialCapacity) {
        this.requestClass = requestClass;
        this.requestHeader = requestHeader;
        this.initialCapacity = initialCapacity;
    }

    /** {@inheritDoc} */
    @Override
    protected void encodeQueryRequest(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception {
        ByteBuf payload = ctx.alloc().directBuffer(this.initialCapacity);
        payload.writeIntLE(SourceQuery.SOURCE_PACKET_TYPE_SINGLE);
        payload.writeByte(this.requestHeader);
        encodeChallenge(ctx, msg.content(), payload);

        if (isDebugEnabled()) {
            payload.markReaderIndex();
            byte[] content = new byte[payload.readableBytes()];
            payload.readBytes(content);
            debug("Encoding query request '{}' to DatagramPacket ({} bytes): {}", msg.content().getClass().getSimpleName(), content.length, toHexString(content));
            payload.resetReaderIndex();
        }
        out.add(new DatagramPacket(payload, msg.recipient()));
    }

    /**
     * <p>encodeChallenge.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param request
     *         a T object
     * @param payload
     *         a {@link io.netty.buffer.ByteBuf} object
     */
    protected void encodeChallenge(ChannelHandlerContext ctx, T request, ByteBuf payload) {
        payload.writeIntLE(request.getChallenge() == null ? -1 : request.getChallenge());
    }

    /** {@inheritDoc} */
    @Override
    protected final boolean acceptQueryRequest(Class<? extends SourceQueryRequest> cls, Envelope<SourceQueryRequest> envelope) {
        return this.requestClass.equals(cls) && SourceQueryAuthRequest.class.isAssignableFrom(cls);
    }
}
