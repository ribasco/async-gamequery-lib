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

    /**
     * <p>Constructor for SourceQuerySplitPacket.</p>
     *
     * @param payload a {@link io.netty.buffer.ByteBuf} object
     */
    public SourceQuerySplitPacket(ByteBuf payload) {
        super(SourceQuery.SOURCE_PACKET_TYPE_SPLIT, payload);
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a int
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>packetCount</code>.</p>
     *
     * @return a int
     */
    public int getPacketCount() {
        return packetCount;
    }

    /**
     * <p>Setter for the field <code>packetCount</code>.</p>
     *
     * @param packetCount a int
     */
    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    /**
     * <p>Getter for the field <code>packetNumber</code>.</p>
     *
     * @return a int
     */
    public int getPacketNumber() {
        return packetNumber;
    }

    /**
     * <p>Setter for the field <code>packetNumber</code>.</p>
     *
     * @param packetNumber a int
     */
    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    /**
     * <p>Getter for the field <code>packetSize</code>.</p>
     *
     * @return a int
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * <p>Setter for the field <code>packetSize</code>.</p>
     *
     * @param packetSize a int
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * <p>Getter for the field <code>packetMaxSize</code>.</p>
     *
     * @return a int
     */
    public int getPacketMaxSize() {
        return packetMaxSize;
    }

    /**
     * <p>Setter for the field <code>packetMaxSize</code>.</p>
     *
     * @param packetMaxSize a int
     */
    public void setPacketMaxSize(int packetMaxSize) {
        this.packetMaxSize = packetMaxSize;
    }

    /**
     * <p>Getter for the field <code>decompressedSize</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getDecompressedSize() {
        return decompressedSize;
    }

    /**
     * <p>Setter for the field <code>decompressedSize</code>.</p>
     *
     * @param decompressedSize a {@link java.lang.Integer} object
     */
    public void setDecompressedSize(Integer decompressedSize) {
        this.decompressedSize = decompressedSize;
    }

    /**
     * <p>Getter for the field <code>crcChecksum</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getCrcChecksum() {
        return crcChecksum;
    }

    /**
     * <p>Setter for the field <code>crcChecksum</code>.</p>
     *
     * @param crcChecksum a {@link java.lang.Integer} object
     */
    public void setCrcChecksum(Integer crcChecksum) {
        this.crcChecksum = crcChecksum;
    }

    /**
     * <p>isCompressed.</p>
     *
     * @return a boolean
     */
    public boolean isCompressed() {
        return compressed;
    }

    /**
     * <p>Setter for the field <code>compressed</code>.</p>
     *
     * @param compressed a boolean
     */
    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SourceQuerySplitPacket that = (SourceQuerySplitPacket) o;
        return getId() == that.getId() && getPacketNumber() == that.getPacketNumber();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getPacketNumber());
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(SourceQuerySplitPacket o) {
        return Comparator.comparingInt(SourceQuerySplitPacket::getId)
                         .thenComparing(SourceQuerySplitPacket::getPacketNumber)
                         .compare(this, o);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[SPLIT-PACKET] ID: %d, NO: %d, COUNT: %d, SIZE: %d, MAX SIZE: %d, COMPRESSED: %s", getId(), getPacketNumber(), getPacketCount(), getPacketSize(), getPacketMaxSize(), isCompressed());
    }
}
