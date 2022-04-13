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

import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;

import java.net.InetSocketAddress;

/**
 * <p>RconAuthException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class RconAuthException extends AsyncGameLibUncheckedException {

    private final InetSocketAddress address;

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param address a {@link java.net.InetSocketAddress} object
     */
    public RconAuthException(InetSocketAddress address) {
        this(null, address);
    }

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param address a {@link java.net.InetSocketAddress} object
     */
    public RconAuthException(String message, InetSocketAddress address) {
        this(message, null, address);
    }

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     * @param address a {@link java.net.InetSocketAddress} object
     */
    public RconAuthException(String message, Throwable cause, InetSocketAddress address) {
        super(message, cause);
        this.address = address;
    }

    /**
     * <p>Getter for the field <code>address</code>.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public final InetSocketAddress getAddress() {
        return address;
    }
}