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

public class MasterServerRequest extends AbstractRequest {

    private TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback;

    private MasterServerType type;

    private MasterServerRegion region;

    private MasterServerFilter filter;

    private String address = "0.0.0.0:0";

    private Integer requestDelay = null;

    public MasterServerRequest() {
        this(null);
    }

    public MasterServerRequest(TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        this.callback = callback;
    }

    public MasterServerType getType() {
        return type;
    }

    public void setType(MasterServerType type) {
        this.type = type;
    }

    public TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> getCallback() {
        return callback;
    }

    public void setCallback(TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        this.callback = callback;
    }

    public MasterServerRegion getRegion() {
        return region;
    }

    public void setRegion(MasterServerRegion region) {
        this.region = region;
    }

    public MasterServerFilter getFilter() {
        return filter;
    }

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

    public Integer getRequestDelay() {
        return requestDelay;
    }

    public void setRequestDelay(Integer requestDelay) {
        this.requestDelay = requestDelay;
    }
}
