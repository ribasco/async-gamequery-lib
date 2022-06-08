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

/**
 * <p>MasterServerAddressDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerAddressDecoder extends MessageInboundDecoder {

    private Set<InetSocketAddress> fullSet;

    private Set<InetSocketAddress> partialSet;

    private boolean terminatorReceived;

    /** {@inheritDoc} */
    @Override
    protected boolean acceptMessage(AbstractRequest request, Object msg) {
        if (!(request instanceof MasterServerRequest)) {
            return false;
        }
        return msg instanceof MasterServerAddressPacket;
    }

    /** {@inheritDoc} */
    @Override
    protected Object decodeMessage(ChannelHandlerContext ctx, AbstractRequest request, Object msg) {
        NettyChannelContext context = NettyChannelContext.getContext(ctx.channel());
        Envelope<AbstractRequest> envelope = context.properties().envelope();
        MasterServerRequest masterRequest = (MasterServerRequest) request;
        MasterServerAddressPacket addressPacket = (MasterServerAddressPacket) msg;

        if (MasterServer.isTerminatingPacket(addressPacket)) {
            debug("Terminating packet found. Returning response");
            terminatorReceived = true;
            return null;
        }

        InetSocketAddress address = addressPacket.getAddress();
        if (masterRequest.getCallback() != null && fullSet.add(address)) {
            partialSet.add(address);
            try {
                if (context.isValid())
                    masterRequest.getCallback().accept(address, envelope.recipient(), null);
            } catch (Exception e) {
                error("Error thrown by the callback", e);
                throw e;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        debug("MASTER => Initializing Address Set");
        partialSet = new HashSet<>();
        fullSet = new HashSet<>();
        terminatorReceived = false;
    }

    /** {@inheritDoc} */
    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        partialSet = null;
    }

    /** {@inheritDoc} */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        debug("MASTER => CHANNEL READ COMPLETE (Terminator received: {}, Last Seed Address: {})", terminatorReceived);
        if (!partialSet.isEmpty()) {
            MasterServerChannelContext context = MasterServerChannelContext.getContext(ctx.channel());
            context.properties().addressSet().addAll(partialSet);
            ctx.fireChannelRead(new MasterServerPartialResponse(new HashSet<>(partialSet), terminatorReceived, context.properties().lastSeedAddress()));
            partialSet.clear();
        }
        super.channelReadComplete(ctx);
    }
}
