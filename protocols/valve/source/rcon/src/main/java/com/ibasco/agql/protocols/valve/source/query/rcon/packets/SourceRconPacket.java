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

package com.ibasco.agql.protocols.valve.source.query.rcon.packets;

import com.ibasco.agql.core.AbstractPacket;
import com.ibasco.agql.core.util.Bytes;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;

/**
 * <p>SourceRconPacket class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconPacket extends AbstractPacket implements Comparable<SourceRconPacket> {

    private Integer size;

    private int id;

    private int type;

    private int terminator;

    /**
     * <p>Constructor for SourceRconPacket.</p>
     *
     * @param type
     *         a int
     * @param payload
     *         a {@link io.netty.buffer.ByteBuf} object
     */
    public SourceRconPacket(int type, ByteBuf payload) {
        super(payload);
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>size</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getSize() {
        return size;
    }

    /**
     * <p>Setter for the field <code>size</code>.</p>
     *
     * @param size
     *         a {@link java.lang.Integer} object
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * <p>Getter for the field <code>terminator</code>.</p>
     *
     * @return The terminating byte (can be either 0x00 or 0x01)
     */
    public int getTerminator() {
        return terminator;
    }

    /**
     * <p>Setter for the field <code>terminator</code>.</p>
     *
     * @param terminator
     *         a int
     */
    public void setTerminator(int terminator) {
        this.terminator = terminator;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        buildToString(builder);
        return builder.toString();
    }

    /**
     * <p>buildToString.</p>
     *
     * @param builder
     *         a {@link org.apache.commons.lang3.builder.ToStringBuilder} object
     */
    protected void buildToString(ToStringBuilder builder) {
        builder
                .append("packet size", size)
                .append("id", id)
                .append("type", type)
                .append("terminator", Bytes.toHexString(terminator));
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(@NotNull SourceRconPacket o) {
        return Comparator.comparingInt(SourceRconPacket::getId)
                         .thenComparing(SourceRconPacket::getType)
                         .compare(this, o);
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
     * @param id
     *         a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return The packet type
     */
    public int getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type
     *         a int
     */
    protected void setType(int type) {
        this.type = type;
    }
}
