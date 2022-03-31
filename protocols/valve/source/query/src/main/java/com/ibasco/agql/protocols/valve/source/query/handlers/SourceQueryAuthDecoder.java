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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.util.ByteUtil;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceChallengeException;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceQuerySinglePacket;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
        //some servers throw an empty info response for any type of requests (info, players and rules) so as long as we get a matching request AND the header and packet is empty, then we accept it
        boolean emptyInfoResponse = msg.hasHeader(SourceQuery.SOURCE_QUERY_INFO_RES) && msg.getPacket().content().readableBytes() == 0;
        return msg.hasRequest(requestClass) && (emptyInfoResponse || msg.hasHeader(responseHeader) || msg.hasHeader(SourceQuery.SOURCE_QUERY_CHALLENGE_RES));
    }

    @Override
    protected final Object decodePacket(ChannelHandlerContext ctx, T request, SourceQuerySinglePacket packet) {
        //did we receive a challenge response from the server?
        if (packet.getHeader() == SourceQuery.SOURCE_QUERY_CHALLENGE_RES) {
            Envelope<AbstractRequest> envelope = getRequest();
            int challenge = packet.content().readIntLE();
            //if auto update is not set, throw an exception instead
            if (!request.isAutoUpdate()) {
                debug("Auto-Update challenge is disabled. Exception will be thrown");
                ctx.fireExceptionCaught(new SourceChallengeException(String.format("Server '%s' responded with a challenge number: '%d' (%s). Please re-send the request using the received challenge number.", envelope.recipient(), challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN)), challenge));
                return null;
            }
            debug("Got challenge response: {} ({})", challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN));
            debug("Resending '{}' request with challenge (Challenge: {} ({}), Destination: {})", request.getClass().getSimpleName(), challenge, ByteUtil.toHexString(challenge, ByteOrder.LITTLE_ENDIAN), getRequest().recipient());
            request.setChallenge(challenge);
            //resend auth request
            Envelope<AbstractRequest> reauthRequest = MessageEnvelopeBuilder.createFrom(getRequest(), request).build();
            ChannelFuture writeFuture = ctx.channel().writeAndFlush(reauthRequest);
            if (writeFuture.isDone()) {
                if (writeFuture.isSuccess()) {
                    debug("Successfully sent re-auth request to the pipline: {}", reauthRequest);
                } else {
                    ctx.channel().pipeline().fireExceptionCaught(writeFuture.cause());
                }
            } else {
                writeFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
            return null;
        }
        return decodeQueryPacket(ctx, request, packet);
    }
}
