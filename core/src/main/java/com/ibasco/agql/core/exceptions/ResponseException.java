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

    public ResponseException(AbstractRequest request, InetSocketAddress remoteAddress) {
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    public ResponseException(String message, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    public ResponseException(String message, Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    public ResponseException(Throwable cause, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(cause);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    public ResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractRequest request, InetSocketAddress remoteAddress) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.request = request;
        this.remoteAddress = remoteAddress;
    }

    public AbstractRequest getRequest() {
        return request;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
