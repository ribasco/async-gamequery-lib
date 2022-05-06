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

import io.netty.buffer.ByteBufAllocator;

/**
 * <p>Abstract AbstractPacketEncoder class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractPacketEncoder<P extends Packet> implements PacketEncoder<P> {

    private final ByteBufAllocator allocator;

    /**
     * <p>Constructor for AbstractPacketEncoder.</p>
     *
     * @param allocator
     *         a {@link io.netty.buffer.ByteBufAllocator} object
     */
    public AbstractPacketEncoder(ByteBufAllocator allocator) {
        this.allocator = allocator;
    }

    /**
     * <p>Getter for the field <code>allocator</code>.</p>
     *
     * @return a {@link io.netty.buffer.ByteBufAllocator} object
     */
    protected final ByteBufAllocator getAllocator() {
        return allocator;
    }
}
