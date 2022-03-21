/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core;

import java.net.InetSocketAddress;

/**
 * <p>A storage for {@link Credentials}</p>
 *
 * <p>
 * Note: Implementation of this interface must be thread-safe.
 * </p>
 *
 * @author Rafael Luis Ibasco
 */
public interface CredentialsStore {

    /**
     * Gets a {@link Credentials} from the storage.
     *
     * @param address
     *         The {@link InetSocketAddress} to be used for lookup
     *
     * @return The {@link Credentials} associated with the {@link InetSocketAddress}. {@code null} if no {@link Credentials} is present.
     */
    Credentials get(InetSocketAddress address);

    /**
     * Add a valid {@link Credentials} to the registry
     *
     * @param address
     *         The {@link InetSocketAddress} to register
     * @param passphrase
     *         The byte array containing the passphrase to be used for authentication
     */
    void add(InetSocketAddress address, byte[] passphrase);

    /**
     * Clear {@link Credentials} for a specific address
     *
     * @param address
     *         The {@link InetSocketAddress} to clear
     */
    void remove(InetSocketAddress address);

    /**
     * Clear all registered {@link Credentials}
     */
    void clear();

    /**
     * Checks if a {@link Credentials} is registered for the specified address. Note: This only checks for the existence of a {@link Credentials} not the validity.
     *
     * @param address
     *         The {@link InetSocketAddress} to check
     *
     * @return {@code true} if a {@link Credentials} is registered for the specified address.
     */
    boolean exists(InetSocketAddress address);
}
