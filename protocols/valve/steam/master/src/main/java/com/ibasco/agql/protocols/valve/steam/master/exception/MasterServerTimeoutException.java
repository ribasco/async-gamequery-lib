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

package com.ibasco.agql.protocols.valve.steam.master.exception;

import com.ibasco.agql.core.exceptions.ReadTimeoutException;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MasterServerTimeoutException extends ReadTimeoutException {

    private final Set<InetSocketAddress> addresses;

    public MasterServerTimeoutException(Collection<InetSocketAddress> addresses) {
        this.addresses = new HashSet<>(addresses);
    }

    public MasterServerTimeoutException(String message, Collection<InetSocketAddress> addresses) {
        super(message);
        this.addresses = new HashSet<>(addresses);
    }

    public MasterServerTimeoutException(String message, Throwable cause, Collection<InetSocketAddress> addresses) {
        super(message, cause);
        this.addresses = new HashSet<>(addresses);
    }

    public final Set<InetSocketAddress> getAddresses() {
        return addresses;
    }
}
