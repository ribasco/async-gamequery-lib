/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ibasco.agql.protocols.mojang.minecraft.query;

import com.ibasco.agql.core.AbstractPacketBuilder;
import com.ibasco.agql.protocols.mojang.minecraft.query.enums.McRconResponseType;
import com.ibasco.agql.protocols.mojang.minecraft.query.packets.response.McRconAuthResponsePacket;
import com.ibasco.agql.protocols.mojang.minecraft.query.packets.response.McRconCmdResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * <p>An Packet builder for the Minecraft RCON Protocol</p>
 */
public class McRconPacketBuilder extends AbstractPacketBuilder<McRconPacket> {

    private static final Logger log = LoggerFactory.getLogger(McRconPacketBuilder.class);

    public McRconPacketBuilder(ByteBufAllocator allocator) {
        super(allocator);
    }

    public static McRconResponsePacket getResponsePacket(int type) {
        McRconResponseType responseType = McRconResponseType.get(type);
        if (responseType != null) {
            switch (responseType) {
                case AUTH:
                    return new McRconAuthResponsePacket();
                case COMMAND:
                    return new McRconCmdResponsePacket();
                default:
                    break;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends McRconPacket> T construct(ByteBuf data) {
        try {
            if (data.readableBytes() < 14) {
                log.warn("Packet is less than 10 bytes. Unsupported packet.");
                if (log.isDebugEnabled())
                    log.debug("Unrecognized Packet: \n{}", ByteBufUtil.prettyHexDump(data));
                return null;
            }

            //Remember the reader index
            data.markReaderIndex();

            //Read from the listen
            data.readerIndex(0);
            int size = data.readIntLE();
            int id = data.readIntLE();
            int type = data.readIntLE();

            String body = data.readCharSequence(data.readableBytes() - 2, StandardCharsets.UTF_8).toString();

            McRconResponsePacket packet = getResponsePacket(type);

            if (packet != null) {
                //Ok, we have a valid response packet. Lets keep reading.
                packet.setId(id);
                packet.setSize(size);
                packet.setType(type);
                packet.setBody(body);
                return (T) packet;
            }
        } finally {
            //Reset the index
            data.resetReaderIndex();
        }
        return null;
    }

    @Override
    public byte[] deconstruct(McRconPacket packet) {
        //1) size = int (4 bytes)
        //2) id = int (4 bytes)
        //3) type = int (4 bytes)
        //4) body = string (length + 1 null byte)
        //5) trailer = null byte

        int id = packet.getId();
        int type = packet.getType();
        final String body = StringUtils.defaultString(packet.getBody());
        int packetSize = 10 + body.length();
        final ByteBuf buf = createBuffer(packetSize);
        byte[] data;
        try {
            buf.writeIntLE(packetSize);
            buf.writeIntLE(id);
            buf.writeIntLE(type);
            buf.writeBytes(body.getBytes());
            buf.writeByte(0);
            buf.writeByte(0);
            data = new byte[buf.readableBytes()];
            buf.readBytes(data);
        } finally {
            buf.release();
        }
        return data;
    }

}
