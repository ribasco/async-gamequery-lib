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

package com.ibasco.agql.core.exceptions;

import com.ibasco.agql.core.AbstractRequest;

import java.net.InetSocketAddress;

/**
 * Exception thrown when there is an error that occured in the transaction.
 *
 * @author Rafael Luis Ibasco
 */
public class ResponseException extends AgqlRuntimeException {

    private final AbstractRequest request;

    private final InetSocketAddress remoteAddress;

    /**
     * <p>Constructor for ResponseException.</p>
     *
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public ResponseException(AbstractRequest request, InetSocketAddress remoteAddress) {
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    /**
     * <p>Constructor for ResponseException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public ResponseException(String message, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    /**
     * <p>Constructor for ResponseException.</p>
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
    public ResponseException(String message, Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    /**
     * <p>Constructor for ResponseException.</p>
     *
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     * @param remoteAddress
     *         a {@link java.net.InetSocketAddress} object
     */
    public ResponseException(Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(cause);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    /**
     * <p>Constructor for ResponseException.</p>
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
    public ResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    /**
     * <p>Getter for the field <code>request</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.AbstractRequest} object
     */
    public AbstractRequest getRequest() {
        return request;
    }

    /**
     * <p>Getter for the field <code>remoteAddress</code>.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
