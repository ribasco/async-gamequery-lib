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

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.handlers.MessageOutboundEncoder;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * A base class for encoding {@link Envelope} wrapped messages
 *
 * @param <T>
 *         The underlying type of the {@link Envelope}'s content
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryEncoder<T extends SourceQueryRequest> extends MessageOutboundEncoder<T> {

    abstract protected boolean acceptQueryRequest(Class<? extends SourceQueryRequest> cls, Envelope<SourceQueryRequest> envelope);

    abstract protected void encodeQueryRequest(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception;

    @Override
    protected final boolean acceptMessage(Class<T> requestClass, Envelope<T> envelope) throws Exception {
        //noinspection unchecked
        return acceptQueryRequest(requestClass, (Envelope<SourceQueryRequest>) envelope);
    }

    @Override
    protected final void encodeMessage(ChannelHandlerContext ctx, Envelope<T> msg, List<Object> out) throws Exception {
        encodeQueryRequest(ctx, msg, out);
    }
}
