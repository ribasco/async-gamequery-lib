/*
 * Copyright 2022 Asynchronous Game Query Library
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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.handlers.MessageInboundDecoder;
import com.ibasco.agql.protocols.valve.source.query.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconCmdResponse;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;
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

        //Make sure we are still authenticated.
        if (!ctx.channel().hasAttr(SourceRcon.AUTHENTICATED) || !ctx.channel().attr(SourceRcon.AUTHENTICATED).get()) {
            debug("NOT_AUTH: Authentication flag is not set (Address: {})", ctx.channel().remoteAddress());
            Throwable error = new RconNotYetAuthException(String.format("Not yet authenticated (Reason: %s)", "Re-authentication required"), SourceRconAuthReason.REAUTHENTICATE, (InetSocketAddress) ctx.channel().remoteAddress());
            response = new SourceRconCmdResponse(cmd.getRequestId(), cmd.getCommand(), result, false, error);
        } else if (result != null && (result.contains("Bad Password"))) {
            debug("NOT_AUTH: Found empty or not authenticated response");
            Throwable error = new RconNotYetAuthException(String.format("Not yet authenticated (Reason: %s)", result), SourceRconAuthReason.BAD_PASSWORD, (InetSocketAddress) ctx.channel().remoteAddress());
            response = new SourceRconCmdResponse(cmd.getRequestId(), cmd.getCommand(), result, false, error);
        } else {
            response = new SourceRconCmdResponse(cmd.getRequestId(), cmd.getCommand(), result, true);
        }
        response.setAddress((InetSocketAddress) ctx.channel().remoteAddress());
        return response;
    }
}
