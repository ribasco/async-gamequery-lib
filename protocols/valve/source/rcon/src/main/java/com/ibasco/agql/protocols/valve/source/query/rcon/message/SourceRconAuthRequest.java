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

package com.ibasco.agql.protocols.valve.source.query.rcon.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>SourceRconAuthRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconAuthRequest extends SourceRconRequest {

    private byte[] password;

    /**
     * <p>Constructor for SourceRconAuthRequest.</p>
     */
    public SourceRconAuthRequest() {
        this(null);
    }

    /**
     * <p>Constructor for SourceRconAuthRequest.</p>
     *
     * @param password an array of {@link byte} objects
     */
    public SourceRconAuthRequest(byte[] password) {
        this.password = password;
    }

    /**
     * <p>Getter for the field <code>password</code>.</p>
     *
     * @return an array of {@link byte} objects
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password an array of {@link byte} objects
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }

    /** {@inheritDoc} */
    @Override
    protected void buildToString(ToStringBuilder builder) {
        super.buildToString(builder);
        builder.append("password", String.format("%d byte(s)", getPassword() != null ? getPassword().length : 0));
    }
}