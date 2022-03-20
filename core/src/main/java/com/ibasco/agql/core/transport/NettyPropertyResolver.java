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
 * Accepts an {@link Object} instance and attempts to resolve the required address propreties.
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyPropertyResolver {

    /**
     * Resolves the remote {@link InetSocketAddress} of the recipient
     *
     * @param data
     *         The {@link Object} containing the remote {@link InetSocketAddress} of the recipient
     *
     * @return The remote {@link InetSocketAddress} of the recipient
     *
     * @throws IllegalStateException
     *         If the address could not be resolved
     */
    InetSocketAddress resolveRemoteAddress(Object data) throws IllegalStateException;

    default InetSocketAddress resolveLocalAddress(Object data) {
        //use system assigned local address, so return null
        return null;
    }
}