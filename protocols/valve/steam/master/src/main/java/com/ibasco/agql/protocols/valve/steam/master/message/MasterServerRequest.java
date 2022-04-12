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

package com.ibasco.agql.protocols.valve.steam.master.message;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.util.functions.TriConsumer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;

import java.net.InetSocketAddress;

/**
 * <p>MasterServerRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerRequest extends AbstractRequest {

    private TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback;

    private MasterServerType type;

    private MasterServerRegion region;

    private MasterServerFilter filter;

    private String address = "0.0.0.0:0";

    private Integer requestDelay = null;

    /**
     * <p>Constructor for MasterServerRequest.</p>
     */
    public MasterServerRequest() {
        this(null);
    }

    /**
     * <p>Constructor for MasterServerRequest.</p>
     *
     * @param callback a {@link com.ibasco.agql.core.util.functions.TriConsumer} object
     */
    public MasterServerRequest(TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        this.callback = callback;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType} object
     */
    public MasterServerType getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType} object
     */
    public void setType(MasterServerType type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>callback</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.functions.TriConsumer} object
     */
    public TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> getCallback() {
        return callback;
    }

    /**
     * <p>Setter for the field <code>callback</code>.</p>
     *
     * @param callback a {@link com.ibasco.agql.core.util.functions.TriConsumer} object
     */
    public void setCallback(TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        this.callback = callback;
    }

    /**
     * <p>Getter for the field <code>region</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion} object
     */
    public MasterServerRegion getRegion() {
        return region;
    }

    /**
     * <p>Setter for the field <code>region</code>.</p>
     *
     * @param region a {@link com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion} object
     */
    public void setRegion(MasterServerRegion region) {
        this.region = region;
    }

    /**
     * <p>Getter for the field <code>filter</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter} object
     */
    public MasterServerFilter getFilter() {
        return filter;
    }

    /**
     * <p>Setter for the field <code>filter</code>.</p>
     *
     * @param filter a {@link com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter} object
     */
    public void setFilter(MasterServerFilter filter) {
        this.filter = filter;
    }

    /**
     * The last seed address used
     *
     * @return A host string representing the last seed address used in the format of {@code &lt;ip:port&gt;}
     */
    public String getAddress() {
        return address;
    }

    /**
     * Update the seed address to be sent to the remote server.
     *
     * @param address A host string in the format of {@code &lt;ip:port&gt;}
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * <p>Getter for the field <code>requestDelay</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getRequestDelay() {
        return requestDelay;
    }

    /**
     * <p>Setter for the field <code>requestDelay</code>.</p>
     *
     * @param requestDelay a {@link java.lang.Integer} object
     */
    public void setRequestDelay(Integer requestDelay) {
        this.requestDelay = requestDelay;
    }
}
