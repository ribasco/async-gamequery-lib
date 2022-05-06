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

import com.ibasco.agql.core.AbstractPacketEncoder;
import com.ibasco.agql.core.exceptions.PacketEncodeException;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySinglePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * <p>SourceQuerySinglePacketEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQuerySinglePacketEncoder extends AbstractPacketEncoder<SourceQuerySinglePacket> {

    /**
     * <p>Constructor for SourceQuerySinglePacketEncoder.</p>
     *
     * @param allocator
     *         a {@link io.netty.buffer.ByteBufAllocator} object
     */
    public SourceQuerySinglePacketEncoder(ByteBufAllocator allocator) {
        super(allocator);
    }

    /** {@inheritDoc} */
    @Override
    public ByteBuf encode(SourceQuerySinglePacket packet) throws PacketEncodeException {
        ByteBuf buffer = getAllocator().directBuffer();
        buffer.writeIntLE(packet.getType());
        buffer.writeByte(packet.getHeader());
        return buffer;
    }
}
