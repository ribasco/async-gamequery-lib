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

import com.ibasco.agql.core.AbstractResponse;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Vector;

public class MasterServerResponse extends AbstractResponse {

    private Vector<InetSocketAddress> serverList;

    public MasterServerResponse(Vector<InetSocketAddress> serverList) {
        this.serverList = serverList;
    }

    public Vector<InetSocketAddress> getServerList() {
        if (this.serverList == null) {
            this.serverList = new Vector<>();
        }
        return serverList;
    }

    public void setServerList(Collection<InetSocketAddress> addressList) {
        getServerList().clear();
        getServerList().addAll(addressList);
    }
}
