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

import org.jetbrains.annotations.ApiStatus;
import java.net.InetSocketAddress;

/**
 * <p>Abstract AbstractResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractResponse<T> extends AbstractMessage {

    private final T result;

    private InetSocketAddress address;

    private AbstractRequest request;

    /**
     * <p>Constructor for AbstractResponse.</p>
     *
     * @param result
     *         a T object
     */
    protected AbstractResponse(T result) {
        this.result = result;
    }

    /**
     * <p>The decoded result object</p>
     *
     * @return a T object
     */
    protected T getResult() {
        return result;
    }

    /**
     * The sender address
     *
     * @return An {@link java.net.InetSocketAddress} representing the address and port of the sender
     */
    public final InetSocketAddress getAddress() {
        return address;
    }

    /**
     * <p>Setter for the field <code>address</code>.</p>
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     */
    @ApiStatus.Internal
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * The originating request
     *
     * @return The {@link com.ibasco.agql.core.AbstractRequest}
     */
    public final AbstractRequest getRequest() {
        return request;
    }

    /**
     * <p>Setter for the field <code>request</code>.</p>
     *
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     */
    @ApiStatus.Internal
    public void setRequest(AbstractRequest request) {
        this.request = request;
    }
}
