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

package com.ibasco.agql.protocols.valve.steam.master.handlers;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.transport.handlers.MessageInboundDecoder;
import com.ibasco.agql.protocols.valve.steam.master.MasterServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerChannelContext;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerPartialResponse;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerAddressPacket;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MasterServerAddressDecoder extends MessageInboundDecoder {

    private Set<InetSocketAddress> fullSet;

    private Set<InetSocketAddress> partialSet;

    private boolean terminatorReceived;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*if (cause instanceof ReadTimeoutException) {
            if (!addressSet.isEmpty()) {
                debug("Timeout occured. Returning list of addresses (Total: {})", addressSet.size());
                ctx.fireChannelRead(new MasterServerResponse(new Vector<>(addressSet)));
                return;
            }
        }*/
        ctx.fireExceptionCaught(cause);
    }

    @Override
    protected boolean acceptMessage(AbstractRequest request, Object msg) {
        if (!(request instanceof MasterServerRequest)) {
            return false;
        }
        return msg instanceof MasterServerAddressPacket;
    }

    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        Envelope<AbstractRequest> envelope = context.properties().envelope();
        MasterServerRequest masterRequest = (MasterServerRequest) request;
        MasterServerAddressPacket addressPacket = (MasterServerAddressPacket) msg;

        if (MasterServer.isTerminatingPacket(addressPacket)) {
            debug("Terminating packet found. Returning response");
            terminatorReceived = true;
        }

        InetSocketAddress address = addressPacket.getAddress();
        if (masterRequest.getCallback() != null && fullSet.add(address)) {
            partialSet.add(address);
            try {
                masterRequest.getCallback().accept(address, envelope.recipient(), null);
            } catch (Exception e) {
                debug("Error thrown by the callback", e);
            }
        }
        return null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        debug("MASTER => CHANNEL READ COMPLETE (Terminator received: {}, Last Seed Address: {})", terminatorReceived);
        if (!partialSet.isEmpty()) {
            MasterServerChannelContext context = MasterServerChannelContext.getContext(ctx.channel());
            context.properties().addressSet().addAll(partialSet);
            ctx.fireChannelRead(new MasterServerPartialResponse(new Vector<>(partialSet), terminatorReceived, context.properties().lastSeedAddress()));
            partialSet.clear();
        }
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        debug("MASTER => Initializing Address Set");
        partialSet = new HashSet<>();
        fullSet = new HashSet<>();
        terminatorReceived = false;
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        partialSet = null;
    }
}