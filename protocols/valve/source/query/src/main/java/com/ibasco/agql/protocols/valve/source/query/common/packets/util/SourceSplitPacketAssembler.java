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

import com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket;
import io.netty.buffer.ByteBuf;
import java.util.List;

/**
 * A re-usable class for processing {@link com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket} instances.
 *
 * @author Rafael Luis Ibasco
 */
public interface SourceSplitPacketAssembler {

    /**
     * Adds a {@link com.ibasco.agql.protocols.valve.source.query.common.packets.SourceQuerySplitPacket} to the container. Once an assembler is marked in a completed state, this method throws an {@link java.lang.IllegalStateException} indicating that it is no longer accepting new packets. {@link #reset()} should be called
     *
     * @param packet
     *         The split-packet to be added. Note that packets can arrive at different order,
     *         as long as both the container and the split-packet are in a valid state, then calling this method should not fail.
     *
     * @return {@code true} if after calling this method caused the assembler in completed state (all split-packets have been received), {@code false} if not yet completed.
     *
     * @throws java.lang.IllegalStateException
     *         If the assembler has not been initialized or has already been marked as completed.
     * @throws java.lang.IndexOutOfBoundsException
     *         if the packet is not within bounds of the pre-allocated buffer
     */
    boolean add(SourceQuerySplitPacket packet);

    /**
     * <p>isProcessing.</p>
     *
     * @return {@code true} if the assembler is currently processing split-packets. This is similar to calling {@code size() > 0 && !isComplete()}
     *
     * @see #received()
     * @see #isComplete()
     */
    default boolean isProcessing() {
        return received() > 0 && !isComplete();
    }

    /**
     * <p>received.</p>
     *
     * @return The number of split-packets received by this container
     */
    int received();

    /**
     * <p>isComplete.</p>
     *
     * @return {@code true} if the container is now in completed state (all packets have been received). {@code false} if either the container has not
     * been initialized (not collecting) or if the container is not yet marked in a completed state
     *
     * @see #reset()
     * @see #getBuffer()
     */
    boolean isComplete();

    /**
     * <p>getBuffer.</p>
     *
     * @return The {@link io.netty.buffer.ByteBuf} containing the re-assembled packets.
     *
     * @throws java.lang.IllegalStateException
     *         is the assembler is not yet in a completed state
     */
    ByteBuf getBuffer();

    /**
     * <p>count.</p>
     *
     * @return The total count of expected split-packets
     */
    int count();

    /**
     * Resets the container back to it's initial state. This should be called only when the container has been marked in a completed state.
     *
     * @see #isComplete()
     */
    void reset();

    /**
     * Dump a snapshot of the current received packets. This operation creates a copy of the packets.
     *
     * @return A {@link java.util.Map} containing the currently received packets
     */
    List<ByteBuf> dump();
}
