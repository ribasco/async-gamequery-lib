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

package com.ibasco.agql.protocols.valve.source.query.common.packets;

import com.ibasco.agql.core.AbstractPacket;
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import io.netty.buffer.ByteBuf;

/**
 * The base class for all Source Query type packets
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceQueryPacket extends AbstractPacket {

    private final int type;

    /**
     * Creates a new source query packet with the type and underlying payload
     *
     * @param type
     *         The type of this packet. Single (0xFFFFFFFF or -1) or Split-packet (0xFFFFFFFE or -2).
     * @param data
     *         The underlying payload of this packet wrapped in {@link ByteBuf}
     *
     * @see SourceQuery#SOURCE_PACKET_TYPE_SINGLE
     * @see SourceQuery#SOURCE_PACKET_TYPE_SPLIT
     */
    public SourceQueryPacket(int type, ByteBuf data) {
        super(data);
        this.type = type;
    }

    /**
     * The type of this {@link SourceQueryPacket}
     *
     * @return The type of this packet. Single (0xFFFFFFFF or -1) or Split-packet (0xFFFFFFFE or -2).
     *
     * @see SourceQuery#SOURCE_PACKET_TYPE_SINGLE
     * @see SourceQuery#SOURCE_PACKET_TYPE_SPLIT
     */
    public final int getType() {
        return type;
    }
}
