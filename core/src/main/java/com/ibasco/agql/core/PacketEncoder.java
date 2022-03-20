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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.PacketEncodeException;
import io.netty.buffer.ByteBuf;

/**
 * Encodes {@link Packet} instances it's {@link ByteBuf} representation.
 *
 * @param <P>
 *         The type of {@link Packet}
 *
 * @author Rafael Luis Ibasco
 */
@FunctionalInterface
public interface PacketEncoder<P extends Packet> {

    /**
     * Encodes a {@link Packet} into a {@link ByteBuf}
     *
     * @param packet
     *         The {@link Packet} to be encoded
     *
     * @return The encoded {@link ByteBuf}
     *
     * @throws PacketEncodeException
     *         When an error occurs during the encoding process
     */
    ByteBuf encode(P packet) throws PacketEncodeException;
}
