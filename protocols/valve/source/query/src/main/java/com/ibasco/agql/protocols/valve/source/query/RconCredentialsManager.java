/*
 * Copyright (c) 2021-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic In-Memory {@link CredentialsManager}
 *
 * @author Rafael Luis Ibasco
 */
public class RconCredentialsManager implements CredentialsManager {

    private final ConcurrentHashMap<InetSocketAddress, Credentials> credentials = new ConcurrentHashMap<>();

    @Override
    public Credentials get(InetSocketAddress address) {
        return credentials.get(address);
    }

    @Override
    public void add(InetSocketAddress address, byte[] passphrase) {
        final Credentials oldCredentials = credentials.put(address, new RconCredentials(passphrase));
        if (oldCredentials != null)
            oldCredentials.invalidate();
    }

    @Override
    public void remove(InetSocketAddress address) {
        credentials.remove(address);
    }

    @Override
    public void clear() {
        invalidate();
        credentials.clear();
    }

    @Override
    public boolean exists(InetSocketAddress address) {
        return credentials.containsKey(address);
    }

    @Override
    public boolean isValid(InetSocketAddress address) {
        if (!exists(address))
            return false;
        return credentials.get(address).isValid();
    }

    @Override
    public void invalidate(InetSocketAddress address) {
        if (!exists(address))
            return;
        get(address).invalidate();
    }

    @Override
    public void invalidate() {
        credentials.forEach((address, credentials) -> credentials.invalidate());
    }
}
