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
import com.ibasco.agql.core.util.Bytes;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQueryPacket;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * Decodes a raw {@link ByteBuf} instance into a {@link SourceQuerySinglePacket} instance
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySinglePacketDecoder implements PacketDecoder<SourceQueryPacket> {

    static final SourceQuerySinglePacketDecoder INSTANCE = new SourceQuerySinglePacketDecoder();

    @Override
    public SourceQueryPacket decode(ByteBuf data) throws PacketDecodeException {
        Objects.requireNonNull(data, "Payload is null");
        //at this point, we assume that the next available bytes is the response header
        if (data.readableBytes() < 1)
            throw new PacketDecodeException(String.format("Not enough bytes to process packet. Length must be greater than or equal to 2 (Size: %d)", data.readableBytes()));
        int header = data.readUnsignedByte();
        if (SourceQuery.isInvalidHeader(header))
            throw new PacketDecodeException(String.format("Invalid source query request/response header: %d (%s)", header, Bytes.toHexString(header)));
        SourceQuerySinglePacket packet = new SourceQuerySinglePacket(data.slice());
        packet.setHeader(header);
        return packet;
    }
}
