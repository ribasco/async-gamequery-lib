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
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for {@link com.ibasco.agql.core.Credentials}
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconCredentials implements Credentials {

    private static final Logger log = LoggerFactory.getLogger(SourceRconCredentials.class);

    private final byte[] passphrase;

    private boolean valid;

    /**
     * <p>Constructor for SourceRconCredentials.</p>
     *
     * @param passphrase
     *         an array of {@code byte} objects
     */
    public SourceRconCredentials(byte[] passphrase) {
        if (passphrase == null || passphrase.length == 0)
            throw new IllegalStateException("Passphrase cannot be null or empty");
        this.passphrase = passphrase;
        this.valid = true;
    }

    /** {@inheritDoc} */
    @Override
    public void invalidate() {
        if (this.valid) {
            this.valid = false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid() {
        return valid;
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getPassphrase() {
        if (!this.valid)
            throw new IllegalStateException("The passphrase is invalidated");
        return this.passphrase;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Arrays.hashCode(getPassphrase());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceRconCredentials)) return false;
        SourceRconCredentials that = (SourceRconCredentials) o;
        return Arrays.equals(getPassphrase(), that.getPassphrase());
    }
}
