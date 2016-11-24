/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.PacketSizeLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * A container class for handling split-packets
 */
public class SplitPacketContainer implements Iterable<Map.Entry<Integer, byte[]>> {
    private static final Logger log = LoggerFactory.getLogger(SplitPacketContainer.class);
    private TreeMap<Integer, byte[]> container = new TreeMap<>();
    private int maxSplitPackets;

    /**
     * Primary Constructor
     *
     * @param maxSplitPackets The number of split packets we expect to receive
     */
    public SplitPacketContainer(int maxSplitPackets) {
        this.maxSplitPackets = maxSplitPackets;
    }

    /**
     * <p>Adds a split packet to the container</p>
     *
     * @param packetNumber The packet number
     * @param packet       A byte array representing a single split-packet unit
     *
     * @throws PacketSizeLimitException thrown if the max number of allowed packets has been reached. This is usually specified by the protocol.
     */
    public void addPacket(int packetNumber, byte[] packet) {
        if (this.container.size() >= maxSplitPackets)
            throw new PacketSizeLimitException(String.format("You can no longer add any more packets. (Limit: of %d packets has been reached))", this.maxSplitPackets));
        this.container.put(packetNumber, packet);
    }

    /**
     * @param packetNumber The packet number to be removed
     */
    public void removePacket(int packetNumber) {
        this.container.remove(packetNumber);
    }

    /**
     * <p>Iterate each packet entry</p>
     *
     * @param action A {@link Consumer} callback
     */
    public void forEachEntry(Consumer<? super Map.Entry<Integer, byte[]>> action) {
        container.entrySet().stream().forEachOrdered(action);
    }

    /**
     * Verify that we received all packets and no packets are missing
     *
     * @return Returns true if we have complete packets
     */
    public boolean isComplete() {
        return maxSplitPackets == container.size();
    }

    /**
     * <p>Returns the sum of each packets within this container</p>
     *
     * @return The total number of packets within this container
     */
    public int getPacketSize() {
        return container.entrySet()
                .stream()
                .mapToInt(packetEntry -> packetEntry.getValue().length)
                .sum();
    }

    @Override
    public Iterator<Map.Entry<Integer, byte[]>> iterator() {
        return container.entrySet().iterator();
    }
}
