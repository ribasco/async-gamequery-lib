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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.handlers.MessageInboundDecoder;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.protocols.valve.source.query.SourceRcon;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.packets.SourceRconPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class SourceRconAuthDecoder extends MessageInboundDecoder {

    private static final ConcurrentHashMap<InetSocketAddress, SourceRconRequest> lastAuthRequest = new ConcurrentHashMap<>();

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

    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        final Envelope<AbstractRequest> envelope = ctx.channel().attr(ChannelAttributes.REQUEST).get();
        final SourceRconRequest rconRequest = (SourceRconRequest) request;
        final SourceRconPacket packet = (SourceRconPacket) msg;
        assert envelope != null;

        //Do nothing
        if (SourceRcon.isResponseValuePacket(packet)) {
            ByteBuf content = packet.content();
            String body = NettyUtil.readString(content);
            debug("Ignoring auth response packet (Packet Id: {}, Body: {})", packet.getId(), body);
            return null;
        }

        //Make sure the packet is an auth response packet
        if (!SourceRcon.isAuthResponsePacket(packet))
            throw new IllegalStateException("Expected packet to be an auth response");

        final int requestId = rconRequest.getRequestId();
        final boolean authenticated = packet.getId() != -1 && packet.getId() == requestId;

        //Update channel attribute and mark this channel as authenticated
        updateAuthFlag(ctx.channel(), authenticated);

        debug("Updated authentication flag to '{}' (Packet Id [{}] == Request Id [{}])", authenticated, packet.getId(), rconRequest.getRequestId());

        if (authenticated)
            lastAuthRequest.put((InetSocketAddress) ctx.channel().remoteAddress(), rconRequest);

        //Ensure we are responding to the right request
        if (request instanceof SourceRconAuthRequest) {
            SourceRconAuthResponse response = new SourceRconAuthResponse(requestId, authenticated, !authenticated ? "Bad Password" : null, true, null, !authenticated ? SourceRconAuthReason.BAD_PASSWORD : null);
            response.setAddress((InetSocketAddress) ctx.channel().remoteAddress());
            return response;
        } else {
            return null;
        }
    }

    private void updateAuthFlag(Channel channel, boolean value) {
        //Update channel attribute and mark this channel as authenticated
        channel.attr(SourceRcon.AUTHENTICATED).set(value);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //ctx.channel().attr(SourceRcon.AUTHENTICATED).set(false);
    }

    //TODO: Handle cases when rcon_password has been updated
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (ctx.channel().hasAttr(SourceRcon.AUTHENTICATED) && ctx.channel().attr(SourceRcon.AUTHENTICATED).compareAndSet(true, false)) {
            SourceRconRequest request = lastAuthRequest.get((InetSocketAddress) ctx.channel().remoteAddress());
            debug("Previously authenticated but channel is now inactive. Resetting authentication flag ({})", request);
        }
    }
}
