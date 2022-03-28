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

import java.net.InetSocketAddress;
import java.util.Vector;

public class MasterServerPartialResponse extends MasterServerResponse {

    private final boolean endOfResponse;

    private final InetSocketAddress lastSeedAddress;

    public MasterServerPartialResponse(Vector<InetSocketAddress> serverList, boolean endOfResponse, InetSocketAddress lastSeedAddress) {
        super(serverList);
        this.endOfResponse = endOfResponse;
        this.lastSeedAddress = lastSeedAddress;
    }

    public final InetSocketAddress getLastSeedAddress() {
        return lastSeedAddress;
    }

    public final boolean isEndOfResponse() {
        return endOfResponse;
    }
}
