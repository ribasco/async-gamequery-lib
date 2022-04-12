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

package com.ibasco.agql.protocols.valve.source.query.rcon.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.handlers.MessageInboundDecoder;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconChannelContext;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * <p>SourceRconAuthDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconAuthDecoder extends MessageInboundDecoder {

    /** {@inheritDoc} */
    @Override
    protected boolean acceptMessage(AbstractRequest request, Object msg) {
        if (SourceRconCmdRequest.class.equals(request.getClass()) && (msg instanceof SourceRconPacket) && SourceRcon.isAuthResponsePacket((SourceRconPacket) msg))
            return true;
        if (!SourceRconAuthRequest.class.equals(request.getClass()))
            return false;
        if (!(msg instanceof SourceRconPacket))
            return false;
        SourceRconPacket packet = (SourceRconPacket) msg;
        return SourceRcon.isResponseValuePacket(packet) || SourceRcon.isAuthResponsePacket(packet);
    }

    /** {@inheritDoc} */
    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        final SourceRconChannelContext context = SourceRconChannelContext.getContext(ctx.channel());
        final Envelope<AbstractRequest> envelope = context.properties().envelope();
        final SourceRconRequest rconRequest = (SourceRconRequest) request;
        final SourceRconPacket packet = (SourceRconPacket) msg;
        assert envelope != null;

        if (!(request instanceof SourceRconAuthRequest)) {
            warn("Expected 'SourceRconAuthRequest' but got '{}'. Skipping decode.", request.getClass());
            return null;
        }

        //Do nothing
        if (SourceRcon.isResponseValuePacket(packet)) {
            ByteBuf content = packet.content();
            String body = Netty.readString(content);
            debug("Ignoring auth response packet (Packet Id: {}, Body: {})", packet.getId(), body);
            return null;
        }

        //Make sure the packet is an auth response packet
        if (!SourceRcon.isAuthResponsePacket(packet))
            throw new IllegalStateException("Expected packet to be an auth response");

        final int requestId = rconRequest.getRequestId();
        final boolean authenticated = packet.getId() != -1 && packet.getId() == requestId;

        debug("Received AUTH response packet: {} (Content: {})", packet, Netty.readString(packet.content()));
        debug("Updated context authentication flag to '{}' (Packet Id [{}] == Request Id [{}])", authenticated, packet.getId(), rconRequest.getRequestId());

        //Ensure we are responding to the right request
        if (authenticated) {
            return new SourceRconAuthResponse(true);
        } else {
            return new SourceRconAuthResponse(false, "Bad Password", SourceRconAuthReason.BAD_PASSWORD);
        }
    }
}
