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

import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.GlobalOptions;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.pool.ChannelPoolHandler;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * <p>Abstract NettyChannelPoolFactory class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyChannelPoolFactory {

    private final ChannelPoolHandler channelPoolHandler;

    private final ChannelHealthChecker channelHealthChecker;

    private final NettyChannelFactory channelFactory;

    /**
     * <p>Constructor for NettyChannelPoolFactory.</p>
     *
     * @param channelFactory a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    protected NettyChannelPoolFactory(NettyChannelFactory channelFactory) {
        final Options options = Objects.requireNonNull(channelFactory, "Channel factory must not be null").getOptions();
        this.channelFactory = channelFactory;
        this.channelPoolHandler = new DefaultChannelPoolHandler(channelFactory.getBootstrap());
        this.channelHealthChecker = options.getOrDefault(GlobalOptions.POOL_CHANNEL_HEALTH_CHECKER);
    }

    /**
     * <p>create.</p>
     *
     * @param localAddress a {@link java.net.InetSocketAddress} object
     * @param remoteAddress a {@link java.net.InetSocketAddress} object
     * @return a {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} object
     */
    abstract public NettyChannelPool create(InetSocketAddress localAddress, InetSocketAddress remoteAddress);

    /**
     * <p>Getter for the field <code>channelFactory</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public final NettyChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * <p>Getter for the field <code>channelHealthChecker</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.pool.ChannelHealthChecker} object
     */
    public final ChannelHealthChecker getChannelHealthChecker() {
        return channelHealthChecker;
    }

    /**
     * <p>Getter for the field <code>channelPoolHandler</code>.</p>
     *
     * @return a {@link io.netty.channel.pool.ChannelPoolHandler} object
     */
    public final ChannelPoolHandler getChannelPoolHandler() {
        return channelPoolHandler;
    }
}
