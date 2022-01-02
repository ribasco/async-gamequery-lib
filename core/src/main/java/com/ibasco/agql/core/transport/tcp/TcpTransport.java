/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

import com.ibasco.agql.core.transport.*;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactoryProvider;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TcpTransport extends NettyTransport {

    private static final Logger log = LoggerFactory.getLogger(TcpTransport.class);

    private final boolean keepAlive;

    public TcpTransport(NettyChannelHandlerInitializer channelHandlerInitializer, OptionMap options) {
        super(getChannelClass(TransportType.TCP, options.getOrDefault(TransportOptions.USE_NATIVE_TRANSPORT)), channelHandlerInitializer, options);
        this.keepAlive = getOrDefault(TransportOptions.SOCKET_KEEP_ALIVE);
    }

    @Override
    protected void configureBootstrap(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    protected ChannelFactory<Channel> newChannelFactory(Bootstrap bootstrap, boolean pooled) {
        if (pooled) {
            bootstrap = bootstrap.clone();
            final NettyChannelFactory channelFactory = new TcpNettyChannelFactory(bootstrap, getOptions());
            final ChannelPoolType type = getOptions().getOrDefault(TransportOptions.POOL_TYPE);
            final NettyChannelPoolFactory channelPoolFactory = NettyChannelPoolFactoryProvider.DEFAULT.getFactory(type, channelFactory);
            return new TcpPooledNettyChannelFactory(channelPoolFactory);
        }
        return new TcpNettyChannelFactory(bootstrap, getOptions());
    }
}
