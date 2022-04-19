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

package com.ibasco.agql.protocols.valve.source.query.common.packets.util;

import com.ibasco.agql.core.PacketDecoder;
import com.ibasco.agql.core.exceptions.PacketDecodeException;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket;
import io.netty.buffer.ByteBuf;

/**
 * A factory that decodes a payload identified as a split-type source query packet into {@link com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket}
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySplitPacketDecoder implements PacketDecoder<SourceQueryPacket> {

    static final SourceQuerySplitPacketDecoder INSTANCE = new SourceQuerySplitPacketDecoder();

    /** {@inheritDoc} */
    @Override
    public SourceQueryPacket decode(ByteBuf buffer) throws PacketDecodeException {
        try {
            int packetId = buffer.readIntLE();
            boolean compressed = ((packetId & 0x80000000) != 0);
            int packetCount = buffer.readUnsignedByte();
            int packetNumber = buffer.readUnsignedByte();
            int packetMaxSize = buffer.readShortLE();
            Integer decompressedSize = null;
            Integer crcChecksum = null;

            //as per docs, the following fields are only available if the
            // compression flag is set and only found within the first packet
            if (compressed && packetNumber == 0) {
                decompressedSize = buffer.readIntLE();
                crcChecksum = buffer.readIntLE();
            }

            //compute for the current packet size
            int payloadSize = packetMaxSize > 0 ? Math.min(buffer.readableBytes(), packetMaxSize) : buffer.readableBytes();

            ByteBuf payload = buffer.slice();
            assert payloadSize == payload.readableBytes();

            SourceQuerySplitPacket packet = new SourceQuerySplitPacket(payload);
            packet.setId(packetId);
            packet.setCompressed(compressed);
            packet.setPacketCount(packetCount);
            packet.setPacketNumber(packetNumber);
            packet.setPacketSize(payloadSize);
            packet.setPacketMaxSize(packetMaxSize);
            packet.setDecompressedSize(decompressedSize);
            packet.setCrcChecksum(crcChecksum);
            return packet;
        } catch (Exception e) {
            throw new PacketDecodeException("Failed to decode ByteBuf into SourceQuerySplitPacket", e);
        }
    }
}
