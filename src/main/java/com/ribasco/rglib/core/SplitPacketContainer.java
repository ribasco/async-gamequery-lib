/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Created by raffy on 9/10/2016.
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
     * @param packetNumber
     * @param packet
     */
    public void addPacket(int packetNumber, byte[] packet) {
        if (this.container.size() >= maxSplitPackets)
            throw new IllegalStateException(String.format("You can no longer add any more packets. (Limit: of %d packets has been reached))", this.maxSplitPackets));
        this.container.put(packetNumber, packet);
    }

    /**
     * @param packetNumber
     */
    public void removePacket(int packetNumber) {
        this.container.remove(packetNumber);
    }

    /**
     * @param action
     */
    public void forEachEntry(Consumer<? super Map.Entry<Integer, byte[]>> action) {
        container.entrySet().stream().forEachOrdered(action);
    }

    /**
     * @param action
     */
    public void forEachPacket(Consumer<? super byte[]> action) {
        container.entrySet().stream().forEachOrdered(entry -> {
            action.accept(entry.getValue());
        });
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
     * Returns the sum of each packets within this container
     *
     * @return
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
