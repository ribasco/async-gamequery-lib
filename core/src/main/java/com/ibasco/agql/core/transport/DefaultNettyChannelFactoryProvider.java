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

import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.transport.pool.NettyPooledChannelFactory;
import com.ibasco.agql.core.util.ConnectOptions;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Options;

/**
 * <p>DefaultNettyChannelFactoryProvider class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultNettyChannelFactoryProvider implements NettyChannelFactoryProvider {

    /** {@inheritDoc} */
    @Override
    public NettyChannelFactory getFactory(final TransportType type, final Options options) {
        if (options == null)
            throw new IllegalStateException("Missing options");
        NettyChannelFactory factory;
        switch (type) {
            case TCP:
                factory = new TcpNettyChannelFactory(options);
                break;
            case UDP:
                factory = new UdpNettyChannelFactory(options, false);
                break;
            case UDP_CONNLESS:
                factory = new UdpNettyChannelFactory(options, true);
                break;
            default:
                throw new IllegalStateException("Unsupported transport type: " + type);
        }
        return factory;
    }

    /** {@inheritDoc} */
    @Override
    public NettyContextChannelFactory getContextualFactory(final TransportType type, final Options options) {
        return getContextualFactory(type, options, null);
    }

    /** {@inheritDoc} */
    @Override
    public NettyContextChannelFactory getContextualFactory(final TransportType type, final Options options, NettyChannelContextFactory contextFactory) {
        if (options == null)
            throw new IllegalStateException("Missing options");
        NettyChannelFactory factory = getFactory(type, options);
        //failsafe integration enabled?
        if (options.getOrDefault(ConnectOptions.FAILSAFE_ENABLED))
            factory = new FailsafeChannelFactory(factory);
        //connection pooling enabled?
        if (options.getOrDefault(GeneralOptions.CONNECTION_POOLING))
            factory = new NettyPooledChannelFactory(factory);
        if (contextFactory == null)
            return new NettyContextChannelFactory(factory);
        return new NettyContextChannelFactory(factory, contextFactory);
    }
}
