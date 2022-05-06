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
import com.ibasco.agql.core.transport.handlers.MessageOutboundEncoder;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/**
 * A base class for encoding {@link com.ibasco.agql.core.Envelope} wrapped messages
 *
 * @param <T>
 *         The underlying type of the {@link com.ibasco.agql.core.Envelope}'s content
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryEncoder<T extends SourceQueryRequest> extends MessageOutboundEncoder<T> {

    /** {@inheritDoc} */
    @Override
    protected final void encodeMessage(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception {
        encodeQueryRequest(ctx, msg, out);
    }

    /**
     * <p>encodeQueryRequest.</p>
     *
     * @param ctx
     *         a {@link io.netty.channel.ChannelHandlerContext} object
     * @param msg
     *         a {@link com.ibasco.agql.core.Envelope} object
     * @param out
     *         a {@link java.util.List} object
     *
     * @throws java.lang.Exception
     *         if any.
     */
    abstract protected void encodeQueryRequest(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception;

    /** {@inheritDoc} */
    @Override
    protected final boolean acceptMessage(Class<T> requestClass, Envelope<T> envelope) throws Exception {
        //noinspection unchecked
        return acceptQueryRequest(requestClass, (Envelope<SourceQueryRequest>) envelope);
    }

    /**
     * <p>acceptQueryRequest.</p>
     *
     * @param cls
     *         a {@link java.lang.Class} object
     * @param envelope
     *         a {@link com.ibasco.agql.core.Envelope} object
     *
     * @return a boolean
     */
    abstract protected boolean acceptQueryRequest(Class<? extends SourceQueryRequest> cls, Envelope<SourceQueryRequest> envelope);
}
