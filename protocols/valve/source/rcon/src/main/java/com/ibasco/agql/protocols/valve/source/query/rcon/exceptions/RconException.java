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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;

import java.net.InetSocketAddress;

/**
 * A exception wrapping all RCON related errors
 *
 * @author Rafael Luis Ibasco
 */
public class RconException extends ResponseException {

    /**
     * <p>Constructor for RconException.</p>
     *
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public RconException(AbstractRequest request, InetSocketAddress remoteAddress) {
        super(request, remoteAddress);
    }

    /**
     * <p>Constructor for RconException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public RconException(String message, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, request, remoteAddress);
    }

    /**
     * <p>Constructor for RconException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public RconException(String message, Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause, request, remoteAddress);
    }

    /**
     * <p>Constructor for RconException.</p>
     *
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public RconException(Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(cause, request, remoteAddress);
    }

    /**
     * <p>Constructor for RconException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param enableSuppression
     *         a boolean
     * @param writableStackTrace
     *         a boolean
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public RconException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause, enableSuppression, writableStackTrace, request, remoteAddress);
    }

    /** {@inheritDoc} */
    @Override
    public SourceRconRequest getRequest() {
        return (SourceRconRequest) super.getRequest();
    }
}
