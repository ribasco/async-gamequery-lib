/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport.tcp;

import com.ibasco.agql.core.transport.BootstrapNettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryProvider;
import com.ibasco.agql.core.transport.NettyChannelHandlerInitializer;
import com.ibasco.agql.core.transport.pool.PooledNettyChannelFactory;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.ChannelOption;

public class TcpNettyChannelFactoryProvider implements NettyChannelFactoryProvider {

    @Override
    public NettyChannelFactory getFactory(Options options, NettyChannelHandlerInitializer initializer) {
        final BootstrapNettyChannelFactory channelFactory = new TcpNettyChannelFactory(initializer, options);
        channelFactory.getBootstrap().option(ChannelOption.SO_KEEPALIVE, options.getOrDefault(TransportOptions.SOCKET_KEEP_ALIVE));
        channelFactory.getBootstrap().option(ChannelOption.TCP_NODELAY, true);
        return options.getOrDefault(TransportOptions.CONNECTION_POOLING) ? new PooledNettyChannelFactory(channelFactory) : channelFactory;
    }
}
