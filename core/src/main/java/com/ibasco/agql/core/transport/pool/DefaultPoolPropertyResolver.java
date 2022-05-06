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

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.NettyPropertyResolver;
import java.net.InetSocketAddress;

/**
 * <p>DefaultPoolPropertyResolver class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultPoolPropertyResolver implements NettyPoolPropertyResolver {

    private final NettyPropertyResolver propertyResolver;

    /**
     * <p>Constructor for DefaultPoolPropertyResolver.</p>
     *
     * @param propertyResolver
     *         a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     */
    public DefaultPoolPropertyResolver(NettyPropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    /** {@inheritDoc} */
    @Override
    public Object resolvePoolKey(Object data) {
        if (data == null)
            throw new IllegalStateException("Data key not provided");
        if (data instanceof InetSocketAddress)
            return data;
        else if (data instanceof Envelope<?>)
            return ((Envelope<?>) data).recipient();
        else
            throw new IllegalStateException("Unsupported key type: " + data.getClass());
    }

    /** {@inheritDoc} */
    @Override
    public InetSocketAddress resolveRemoteAddress(Object data) throws IllegalStateException {
        return propertyResolver.resolveRemoteAddress(data);
    }

    /** {@inheritDoc} */
    @Override
    public InetSocketAddress resolveLocalAddress(Object data) {
        return propertyResolver.resolveLocalAddress(data);
    }
}
