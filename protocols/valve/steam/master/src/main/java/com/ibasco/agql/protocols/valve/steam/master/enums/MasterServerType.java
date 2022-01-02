/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.master.enums;

import java.net.InetSocketAddress;

public enum MasterServerType {
    SOURCE(new InetSocketAddress("hl2master.steampowered.com", 27011)),
    GOLDSRC(new InetSocketAddress("hl1master.steampowered.com", 27010));

    private final InetSocketAddress masterAddress;

    MasterServerType(InetSocketAddress address) {
        this.masterAddress = address;
    }

    public InetSocketAddress getMasterAddress() {
        return masterAddress;
    }
}
