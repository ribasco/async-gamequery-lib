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

package com.ibasco.agql.protocols.valve.steam.master.handlers;

import com.ibasco.agql.protocols.valve.steam.master.packets.MasterServerQueryPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MasterServerPacketEncoder extends MessageToByteEncoder<MasterServerQueryPacket> {

    private static final Logger log = LoggerFactory.getLogger(MasterServerPacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MasterServerQueryPacket packet, ByteBuf out) throws Exception {
        out.writeByte(packet.getType());
        out.writeByte(packet.getRegion());
        out.writeCharSequence(packet.getAddress(), StandardCharsets.US_ASCII);
        out.writeByte(0);
        out.writeCharSequence(packet.getFilter(), StandardCharsets.US_ASCII);
        out.writeByte(0);
        log.debug("Sent packet: {}", packet);
        log.debug(ByteBufUtil.prettyHexDump(out));
    }
}
