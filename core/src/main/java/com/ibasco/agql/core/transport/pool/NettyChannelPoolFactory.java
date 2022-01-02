/*
 * Copyright 2021-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.pool.ChannelPoolHandler;

import java.net.InetSocketAddress;
import java.util.Objects;

abstract public class NettyChannelPoolFactory {

    private final ChannelPoolHandler channelPoolHandler;

    private final ChannelHealthChecker channelHealthChecker;

    private final NettyChannelFactory channelFactory;

    protected NettyChannelPoolFactory(NettyChannelFactory channelFactory) {
        final OptionMap options = Objects.requireNonNull(channelFactory, "Channel factory must not be null").getOptions();
        this.channelFactory = channelFactory;
        this.channelPoolHandler = new DefaultChannelPoolHandler(channelFactory.getBootstrap());
        this.channelHealthChecker = options.getOrDefault(TransportOptions.POOL_CHANNEL_HEALTH_CHECKER);
    }

    abstract public NettyChannelPool create(InetSocketAddress localAddress, InetSocketAddress remoteAddress);

    public final NettyChannelFactory getChannelFactory() {
        return channelFactory;
    }

    public final ChannelHealthChecker getChannelHealthChecker() {
        return channelHealthChecker;
    }

    public final ChannelPoolHandler getChannelPoolHandler() {
        return channelPoolHandler;
    }
}
