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

package com.ibasco.agql.protocols.valve.source.query.handlers;

import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceChallengeException;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;
import java.util.Objects;

/**
 * A special source query decoder that handles challenge-response based protocols
 *
 * @param <T>
 *         The underlying source query request type supporting challenge-response implementation
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryAuthDecoder<T extends SourceQueryAuthRequest> extends SourceQueryDecoder<T> {

    private final Class<T> requestClass;

    private final int responseHeader;

    protected SourceQueryAuthDecoder(Class<T> requestClass, int responseHeader) {
        this.requestClass = Objects.requireNonNull(requestClass, "Request class not provided");
        this.responseHeader = responseHeader;
    }

    abstract protected Object decodeQueryPacket(ChannelHandlerContext ctx, T request, SourceQuerySinglePacket msg);

    @Override
    protected final boolean acceptPacket(SourceQueryMessage msg) {
        return msg.hasRequest(requestClass) && (msg.hasHeader(responseHeader) || msg.hasHeader(SourceQuery.SOURCE_QUERY_CHALLENGE_RES));
    }

    @Override
    protected final Object decodePacket(ChannelHandlerContext ctx, T request, SourceQuerySinglePacket packet) {
        //did we receive a challenge response from the server?
        if (packet.getHeader() == SourceQuery.SOURCE_QUERY_CHALLENGE_RES) {
            int challenge = packet.content().readIntLE();
            //if auto update is not set, throw an exception instead
            if (!request.isAutoUpdate()) {
                debug("Auto-Update challenge is disabled. Exception will be thrown");
                ctx.fireExceptionCaught(new SourceChallengeException(String.format("Server '%s' responded with a challenge number: '%d' (%s). Please re-send the request using the received challenge number.", getResponse().sender(), challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN)), challenge));
                return null;
            }
            debug("Got challenge response: {} ({})", challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN));
            debug("Resending '{}' request with challenge (Challenge: {} ({}), Destination: {})", request.getClass().getSimpleName(), challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN), getRequest().recipient());
            request.setChallenge(challenge);
            ctx.channel().writeAndFlush(
                    MessageEnvelopeBuilder
                            .createNew()
                            .message(request)
                            .recipient(getRequest().recipient())
                            .build());
            return null;
        }
        return decodeQueryPacket(ctx, request, packet);
    }
}
