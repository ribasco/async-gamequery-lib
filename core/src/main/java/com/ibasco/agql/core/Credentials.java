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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.InvalidCredentialsException;

/**
 * An immutable class for storing credentials
 *
 * @author Rafael Luis Ibasco
 */
public interface Credentials {

    /**
     * Invalidates this instance
     */
    void invalidate();

    /**
     * Checks if the credentails is currently in a valid state
     *
     * @return {@code true} if the credentials are valid
     */
    boolean isValid();

    /**
     * <p>isEmpty.</p>
     *
     * @return {@code true} if the passphrase is empty or {@code null}
     */
    default boolean isEmpty() {
        return getPassphrase() == null || getPassphrase().length == 0;
    }

    /**
     * The passphrase. If the credentials has been marked as invalidated, then an exception will be thrown.
     *
     * @return The passphrase in byte array format
     *
     * @throws com.ibasco.agql.core.exceptions.InvalidCredentialsException
     *         when the credentials have been invalidated by the remote server.
     */
    byte[] getPassphrase() throws InvalidCredentialsException;
}
