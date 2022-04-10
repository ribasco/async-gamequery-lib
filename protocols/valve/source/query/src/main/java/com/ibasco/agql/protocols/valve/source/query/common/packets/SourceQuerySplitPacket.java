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

import com.ibasco.agql.protocols.valve.source.query.SourceQuery;
import io.netty.buffer.ByteBuf;

import java.util.Comparator;
import java.util.Objects;

/**
 * A special source query packet representing a split-packet type.
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.valvesoftware.com/wiki/Server_queries#Multi-packet_Response_Format">Source multi-packet response format</a>
 */
public class SourceQuerySplitPacket extends SourceQueryPacket implements Comparable<SourceQuerySplitPacket> {

    private int id;

    private int packetCount;

    private int packetNumber;

    private int packetSize;

    private int packetMaxSize;

    private Integer decompressedSize;

    private Integer crcChecksum;

    private boolean compressed;

    public SourceQuerySplitPacket(ByteBuf payload) {
        super(SourceQuery.SOURCE_PACKET_TYPE_SPLIT, payload);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public int getPacketMaxSize() {
        return packetMaxSize;
    }

    public void setPacketMaxSize(int packetMaxSize) {
        this.packetMaxSize = packetMaxSize;
    }

    public Integer getDecompressedSize() {
        return decompressedSize;
    }

    public void setDecompressedSize(Integer decompressedSize) {
        this.decompressedSize = decompressedSize;
    }

    public Integer getCrcChecksum() {
        return crcChecksum;
    }

    public void setCrcChecksum(Integer crcChecksum) {
        this.crcChecksum = crcChecksum;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SourceQuerySplitPacket that = (SourceQuerySplitPacket) o;
        return getId() == that.getId() && getPacketNumber() == that.getPacketNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPacketNumber());
    }

    @Override
    public int compareTo(SourceQuerySplitPacket o) {
        return Comparator.comparingInt(SourceQuerySplitPacket::getId)
                         .thenComparing(SourceQuerySplitPacket::getPacketNumber)
                         .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("[SPLIT-PACKET] ID: %d, NO: %d, COUNT: %d, SIZE: %d, MAX SIZE: %d, COMPRESSED: %s", getId(), getPacketNumber(), getPacketCount(), getPacketSize(), getPacketMaxSize(), isCompressed());
    }
}
