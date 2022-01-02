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

public final class RconCredentials implements Credentials {

    private final byte[] passphrase;

    private boolean valid;

    public RconCredentials(byte[] passphrase) {
        if (passphrase == null || passphrase.length == 0)
            throw new IllegalStateException("Passphrase cannot be null or empty");
        this.passphrase = passphrase;
        this.valid = true;
    }

    @Override
    public byte[] getPassphrase() {
        if (!this.valid)
            throw new IllegalStateException("The passphrase is invalidated");
        return this.passphrase;
    }

    @Override
    public boolean invalidate() {
        if (this.valid) {
            this.valid = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}
