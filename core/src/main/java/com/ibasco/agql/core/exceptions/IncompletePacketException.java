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

package com.ibasco.agql.core.exceptions;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Thrown when we did not receive the expected number of split-packets from the sever
 *
 * @author Rafael Luis Ibasco
 */
public class IncompletePacketException extends RuntimeException {

    private final int received;

    private final int count;

    private final List<ByteBuf> packets;

    /**
     * <p>Constructor for IncompletePacketException.</p>
     *
     * @param received a int
     * @param count a int
     * @param packets a {@link java.util.List} object
     */
    public IncompletePacketException(int received, int count, List<ByteBuf> packets) {
        this(null, received, count, packets);
    }

    /**
     * <p>Constructor for IncompletePacketException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param received a int
     * @param count a int
     * @param packets a {@link java.util.List} object
     */
    public IncompletePacketException(String message, int received, int count, List<ByteBuf> packets) {
        this(message, null, received, count, packets);
    }

    /**
     * <p>Constructor for IncompletePacketException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param received a int
     * @param count a int
     * @param packets a {@link java.util.List} object
     */
    public IncompletePacketException(String message, Throwable cause, int received, int count, List<ByteBuf> packets) {
        super(message, cause);
        this.received = received;
        this.count = count;
        this.packets = packets;
    }

    /**
     * <p>Getter for the field <code>received</code>.</p>
     *
     * @return The number of packets received from the server
     */
    public int getReceived() {
        return received;
    }

    /**
     * <p>Getter for the field <code>count</code>.</p>
     *
     * @return The number of packets we expect to receive from the server
     */
    public int getCount() {
        return count;
    }

    /**
     * <p>Getter for the field <code>packets</code>.</p>
     *
     * @return The split-packets received from the server. This is ordered by packet
     * number. So if packet 2 is missing, getting the index 2 should return {@code null}.
     */
    public List<ByteBuf> getPackets() {
        return packets;
    }
}
