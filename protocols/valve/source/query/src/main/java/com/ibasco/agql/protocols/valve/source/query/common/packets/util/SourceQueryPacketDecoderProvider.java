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
import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import com.ibasco.agql.protocols.valve.source.query.common.exceptions.InvalidPacketTypeException;
import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQueryPacket;

/**
 * A provider for {@link PacketDecoder}
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceQueryPacketDecoderProvider {

    /**
     * Get a singleton instance of a {@link PacketDecoder} based on the type provided.
     *
     * @param type
     *         The lookup header type
     * @param <T>
     *         The concrete capture type of {@link PacketDecoder}
     *
     * @return A singleton instance of {@link PacketDecoder}
     *
     * @throws InvalidPacketTypeException
     *         When the provided type is not recoognized/supported.
     * @see SourceQuery#SOURCE_PACKET_TYPE_SINGLE
     * @see SourceQuery#SOURCE_PACKET_TYPE_SPLIT
     */
    @SuppressWarnings("unchecked")
    public static <T extends PacketDecoder<P>, P extends SourceQueryPacket> T getDecoder(int type) {
        switch (type) {
            case SourceQuery.SOURCE_PACKET_TYPE_SINGLE:
                return (T) SourceQuerySinglePacketDecoder.INSTANCE;
            case SourceQuery.SOURCE_PACKET_TYPE_SPLIT:
                return (T) SourceQuerySplitPacketDecoder.INSTANCE;
            default:
                throw new InvalidPacketTypeException(type, "Invalid packet type");
        }
    }
}
