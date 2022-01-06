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

package com.ibasco.agql.core.transport.udp;

import com.ibasco.agql.core.transport.*;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactoryProvider;
import com.ibasco.agql.core.transport.pool.PooledNettyChannelFactory;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

public class UdpTransport extends NettyTransport {

    private final boolean connectionLess;

    public UdpTransport(NettyChannelHandlerInitializer channelHandlerInitializer, OptionMap options, boolean connectionLess) {
        super(getChannelClass(TransportType.UDP, options.getOrDefault(TransportOptions.USE_NATIVE_TRANSPORT)), channelHandlerInitializer, options);
        this.connectionLess = connectionLess;
    }

    @Override
    protected ChannelFactory<Channel> newChannelFactory(Bootstrap bootstrap, boolean pooled) {
        final NettyChannelFactory channelFactory = new UdpNettyChannelFactory(bootstrap.clone(), getOptions(), connectionLess);
        if (pooled) {
            final ChannelPoolType type = getOptions().getOrDefault(TransportOptions.POOL_TYPE);
            final NettyChannelPoolFactory channelPoolFactory = NettyChannelPoolFactoryProvider.DEFAULT.getFactory(type, channelFactory);
            return new PooledNettyChannelFactory(channelPoolFactory);
        }
        return channelFactory;
    }
}
