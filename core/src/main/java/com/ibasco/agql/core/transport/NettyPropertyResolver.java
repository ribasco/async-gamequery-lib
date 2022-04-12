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

package com.ibasco.agql.core.transport;

import java.net.InetSocketAddress;

/**
 * Accepts an {@link java.lang.Object} instance and attempts to resolve the required address propreties.
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyPropertyResolver {

    /**
     * Resolves the remote {@link java.net.InetSocketAddress} of the recipient
     *
     * @param data
     *         The {@link java.lang.Object} containing the remote {@link java.net.InetSocketAddress} of the recipient
     * @return The remote {@link java.net.InetSocketAddress} of the recipient
     * @throws java.lang.IllegalStateException
     *         If the address could not be resolved
     */
    InetSocketAddress resolveRemoteAddress(Object data) throws IllegalStateException;

    /**
     * <p>resolveLocalAddress.</p>
     *
     * @param data a {@link java.lang.Object} object
     * @return a {@link java.net.InetSocketAddress} object
     */
    default InetSocketAddress resolveLocalAddress(Object data) {
        //use system assigned local address, so return null
        return null;
    }
}
