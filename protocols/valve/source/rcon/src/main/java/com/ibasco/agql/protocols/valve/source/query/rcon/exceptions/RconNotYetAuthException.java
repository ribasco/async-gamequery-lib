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

package com.ibasco.agql.protocols.valve.source.query.rcon.exceptions;

import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;

import java.net.InetSocketAddress;

/**
 * Thrown when the address has not yet been authenticated by the remote server
 *
 * @author Rafael Luis Ibasco
 */
public class RconNotYetAuthException extends RconAuthException {

    private final SourceRconAuthReason reason;

    /**
     * <p>Constructor for RconNotYetAuthException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param reason a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     * @param address a {@link java.net.InetSocketAddress} object
     */
    public RconNotYetAuthException(String message, SourceRconAuthReason reason, InetSocketAddress address) {
        super(message, address);
        this.reason = reason;
    }

    /**
     * <p>Getter for the field <code>reason</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public final SourceRconAuthReason getReason() {
        return reason;
    }
}