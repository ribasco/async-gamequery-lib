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
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import java.net.InetSocketAddress;

/**
 * <p>RconAuthException class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class RconAuthException extends RconException {

    private final SourceRconAuthReason reason;

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param reason
     *         a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public RconAuthException(AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason) {
        super(request, address);
        this.reason = reason;
    }

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param reason
     *         a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public RconAuthException(String message, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason) {
        super(message, request, address);
        this.reason = reason;
    }

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param reason
     *         a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public RconAuthException(String message, Throwable cause, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason) {
        super(message, cause, request, address);
        this.reason = reason;
    }

    /**
     * <p>Constructor for RconAuthException.</p>
     *
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param reason
     *         a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public RconAuthException(Throwable cause, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason) {
        super(cause, request, address);
        this.reason = reason;
    }

    /**
     * <p>Getter for the field <code>reason</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason} object
     */
    public SourceRconAuthReason getReason() {
        return reason;
    }
}
