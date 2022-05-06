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

import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.protocols.valve.steam.master.MasterServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerChannelContext;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerAddressPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;
import java.net.InetSocketAddress;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>MasterServerPacketDecoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerPacketDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(MasterServerPacketDecoder.class);

    private static final int PACKET_SIZE = 6;

    private InetSocketAddress lastAddress;

    /** {@inheritDoc} */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try {
            if (lastAddress == null)
                return;
            MasterServerChannelContext context = MasterServerChannelContext.getContext(ctx.channel());
            log.debug("{} MASTER => Decoder complete (Last IP Packet: {})", Netty.id(ctx.channel()), lastAddress);
            //update seed address in context
            context.properties().lastSeedAddress(lastAddress);
        } finally {
            ctx.fireChannelReadComplete();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Do we have enough bytes to read an address packet?
        if (in.readableBytes() < PACKET_SIZE)
            return;

        MasterServerAddressPacket packet = new MasterServerAddressPacket(in.readRetainedSlice(PACKET_SIZE));
        lastAddress = packet.getAddress();

        //ignore header
        if (MasterServer.isHeaderPacket(packet)) {
            log.debug("{} MASTER => Ignoring HEADER packet: {}", Netty.id(ctx.channel()), packet.getAddressString(true));
            packet.release();
        } else {
            if (MasterServer.isTerminatingPacket(packet)) {
                log.debug("{} MASTER => Received terminator packet: {}", Netty.id(ctx.channel()), lastAddress);
            }
            out.add(packet);
        }
    }

    private MasterServerRequest getRequest(ChannelHandlerContext ctx) {
        return NettyChannelContext.getContext(ctx.channel()).properties().request();
    }

    /** {@inheritDoc} */
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        lastAddress = null;
        super.channelActive(ctx);
    }
}
