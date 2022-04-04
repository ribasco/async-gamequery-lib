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

import java.net.InetSocketAddress;

abstract public class AbstractResponse extends AbstractMessage {

    private InetSocketAddress address;

    private AbstractRequest request;

    /**
     * The sender address
     *
     * @return An {@link InetSocketAddress} representing the address and port of the sender
     */
    public final InetSocketAddress getAddress() {
        return address;
    }

    protected void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * The originating request
     *
     * @return The {@link AbstractRequest}
     */
    public final AbstractRequest getRequest() {
        return request;
    }

    protected void setRequest(AbstractRequest request) {
        this.request = request;
    }
}
