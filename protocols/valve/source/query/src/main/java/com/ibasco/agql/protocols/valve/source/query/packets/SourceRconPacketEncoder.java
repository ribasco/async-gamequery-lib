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

package com.ibasco.agql.protocols.valve.source.query.packets;

import com.ibasco.agql.core.PacketEncoder;
import com.ibasco.agql.core.exceptions.PacketEncodeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes a {@link SourceRconPacket} into a {@link ByteBuf} instance which can then be sent over the transport
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconPacketEncoder implements PacketEncoder<SourceRconPacket> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconPacketEncoder.class);

    private final ByteBufAllocator allocator;

    public SourceRconPacketEncoder() {
        this(null);
    }

    public SourceRconPacketEncoder(ByteBufAllocator allocator) {
        this.allocator = allocator == null ? PooledByteBufAllocator.DEFAULT : allocator;
    }

    @Override
    public ByteBuf encode(SourceRconPacket packet) throws PacketEncodeException {
        try {
            int packetSize = computeSize(packet);
            ByteBuf buffer = allocator.directBuffer(packetSize + 4);
            buffer.writeIntLE(packetSize); //not this field should not be included in the size computation
            buffer.writeIntLE(packet.getId());
            buffer.writeIntLE(packet.getType());
            ByteBuf payload = packet.content();
            //if payload is empty, write NULL byte as per specification
            if (payload.readableBytes() >= 1)
                buffer.writeBytes(packet.content()); //payload size + id (4) + type (4) + term (1) (this is assuming that the payload is null terminated)
            else
                buffer.writeByte(0);
            buffer.writeByte(0);
            assert buffer.readableBytes() >= 14;
            return buffer;
        } catch (Exception e) {
            throw new PacketEncodeException(e);
        }
    }

    private int computeSize(SourceRconPacket packet) {
        return (packet.content().readableBytes() == 0 ? packet.content().capacity() : packet.content().readableBytes()) + 9;
    }
}
