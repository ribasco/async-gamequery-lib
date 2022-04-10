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

package com.ibasco.agql.protocols.valve.source.query.rcon;

import com.ibasco.agql.core.Credentials;
import com.ibasco.agql.core.CredentialsStore;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default In-Memory {@link CredentialsStore}
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public class InMemoryCredentialsStore implements CredentialsStore {

    private static final Logger log = LoggerFactory.getLogger(InMemoryCredentialsStore.class);

    private final Map<InetSocketAddress, Credentials> credentials = new ConcurrentHashMap<>();

    @Override
    public Credentials get(InetSocketAddress address) {
        return credentials.get(address);
    }

    @Override
    public void add(InetSocketAddress address, byte[] passphrase) {
        if (passphrase == null || passphrase.length == 0)
            throw new IllegalArgumentException("Passphrase cannot be null or empty");
        final Credentials oldCredentials = credentials.put(address, new SourceRconCredentials(passphrase));
        log.debug("CREDENTIALS_STORE => Registered new address '{}' with passphrase ({} bytes_", address, passphrase.length);
        if (oldCredentials != null) {
            oldCredentials.invalidate();
            log.debug("CREDENTIALS_STORE => Invalidated previous credentials for address '{}'", address);
        }
    }

    @Override
    public void remove(InetSocketAddress address) {
        if (credentials.remove(address) != null)
            log.debug("CREDENTIALS_STORE => Unregistered credentials for address '{}'", address);
    }

    @Override
    public void clear() {
        int size = credentials.size();
        credentials.clear();
        log.debug("CREDENTIALS_STORE => Cleared a total of '{}' credentials", size);
    }

    @Override
    public boolean exists(InetSocketAddress address) {
        return credentials.containsKey(address);
    }
}
