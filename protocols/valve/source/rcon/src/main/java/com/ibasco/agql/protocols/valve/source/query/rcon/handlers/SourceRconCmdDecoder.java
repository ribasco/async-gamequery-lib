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
import com.ibasco.agql.core.transport.handlers.MessageInboundDecoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconChannelContext;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.packets.SourceRconPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SourceRconCmdDecoder extends MessageInboundDecoder {

    @Override
    protected boolean acceptMessage(AbstractRequest request, Object msg) {
        if (!SourceRconCmdRequest.class.equals(request.getClass()))
            return false;
        if (!(msg instanceof SourceRconPacket))
            return false;
        return SourceRcon.isResponseValuePacket((SourceRconPacket) msg);
    }

    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        final SourceRconCmdRequest cmd = (SourceRconCmdRequest) request;
        final SourceRconPacket packet = (SourceRconPacket) msg;
        final ByteBuf payload = packet.content();
        SourceRconCmdResponse response;

        //payload is null-terminated, make sure not to include it in the response
        final String result = (String) payload.readCharSequence(payload.capacity() - 1, StandardCharsets.UTF_8);
        SourceRconChannelContext context = SourceRconChannelContext.getContext(ctx.channel());

        //Make sure we are still authenticated.
        if (!context.properties().authenticated()) {
            debug("NOT_AUTH: Authentication flag is not set (Address: {})", ctx.channel().remoteAddress());
            ctx.fireExceptionCaught(new RconNotYetAuthException(String.format("Not yet authenticated (Reason: %s)", "Re-authentication required"), SourceRconAuthReason.INVALIDATED, (InetSocketAddress) ctx.channel().remoteAddress()));
            return null;
        } else if (result != null && result.contains("Bad Password")) {
            debug("NOT_AUTH: Found empty or not authenticated response");
            ctx.fireExceptionCaught(new RconNotYetAuthException(String.format("Not yet authenticated (Reason: %s)", result), SourceRconAuthReason.BAD_PASSWORD, (InetSocketAddress) ctx.channel().remoteAddress()));
            return null;
        } else {
            response = new SourceRconCmdResponse(result);
        }
        return response;
    }
}
