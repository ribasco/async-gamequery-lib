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

package com.ibasco.agql.protocols.valve.steam.master.packets;

import com.ibasco.agql.core.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * <p>MasterServerQueryPacket class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerQueryPacket extends AbstractPacket {

    private int type;

    private int region;

    private String address;

    private String filter;

    /**
     * <p>Constructor for MasterServerQueryPacket.</p>
     */
    public MasterServerQueryPacket() {
        super(Unpooled.EMPTY_BUFFER);
    }

    /**
     * <p>Constructor for MasterServerQueryPacket.</p>
     *
     * @param payload
     *         a {@link io.netty.buffer.ByteBuf} object
     */
    public MasterServerQueryPacket(ByteBuf payload) {
        super(payload);
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a int
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
    public void setType(int type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>region</code>.</p>
     *
     * @return a int
     */
    public int getRegion() {
        return region;
    }

    /**
     * <p>Setter for the field <code>region</code>.</p>
     *
     * @param region
     *         a int
     */
    public void setRegion(int region) {
        this.region = region;
    }

    /**
     * <p>Getter for the field <code>address</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAddress() {
        return address;
    }

    /**
     * <p>Setter for the field <code>address</code>.</p>
     *
     * @param address
     *         a {@link java.lang.String} object
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * <p>Getter for the field <code>filter</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFilter() {
        return filter;
    }

    /**
     * <p>Setter for the field <code>filter</code>.</p>
     *
     * @param filter
     *         a {@link java.lang.String} object
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
