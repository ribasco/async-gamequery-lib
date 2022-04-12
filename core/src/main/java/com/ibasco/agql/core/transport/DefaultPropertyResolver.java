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

import com.ibasco.agql.core.Envelope;

import java.net.InetSocketAddress;

/**
 * <p>DefaultPropertyResolver class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultPropertyResolver implements NettyPropertyResolver {

    /** Constant <code>INSTANCE</code> */
    public static final DefaultPropertyResolver INSTANCE = new DefaultPropertyResolver();

    /** {@inheritDoc} */
    @Override
    public InetSocketAddress resolveRemoteAddress(Object data) {
        if (data == null)
            throw new IllegalStateException("Argument not provided");
        if (data instanceof Envelope<?>) {
            return ((Envelope<?>) data).recipient();
        } else if (data instanceof InetSocketAddress) {
            return (InetSocketAddress) data;
        }
        throw new IllegalStateException(String.format("Failed to extract remote address from argument. Define a custom property resolver. (Reason: Unsupported type '%s')", data.getClass().getSimpleName()));
    }
}
