
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
import com.ibasco.agql.core.transport.NettyChannelFactoryDecorator;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * A decorator for {@link com.ibasco.agql.core.transport.NettyChannelFactory} which adds support for {@link io.netty.channel.Channel} pooling.
 *
 * @author Rafael Luis Ibasco
 */
public class NettyPooledChannelFactory extends NettyChannelFactoryDecorator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final NettyChannelPoolMap<Object, NettyChannelPool> channelPoolMap;

    private final NettyChannelPoolFactory channelPoolFactory;

    /**
     * Creates a new instance using the provided {@link com.ibasco.agql.core.transport.NettyChannelFactory}. A default {@link com.ibasco.agql.core.transport.pool.NettyChannelPoolFactoryProvider} will be used to obtain a {@link com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory}
     *
     * @param channelFactory
     *         The {@link com.ibasco.agql.core.transport.NettyChannelFactory} that will be used to create {@link io.netty.channel.Channel} instances
     */
    public NettyPooledChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        final ChannelPoolType poolType = getOptions().getOrDefault(GeneralOptions.POOL_TYPE);
        this.channelPoolFactory = NettyChannelPoolFactoryProvider.DEFAULT.getFactory(poolType, channelFactory);
        log.debug("[INIT] POOL => Using channel pool factory '{}'", channelPoolFactory);
        this.channelPoolMap = new MessageChannelPoolMap(this);
        log.debug("[INIT] POOL => Using channel pool map '{}'", this.channelPoolMap);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data) {
        final InetSocketAddress remoteAddress = getResolver().resolveRemoteAddress(data);
        final NettyChannelPool pool = channelPoolMap.get(data);
        assert pool != null;
        log.debug("[POOL] Acquiring channel for address '{}' (Channel Pool: {}, Pool Size: {})", remoteAddress, pool, pool.getSize());
        return pool.acquire(remoteAddress);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return Netty.useEventLoop(create(data), eventLoop);
    }

    /**
     * <p>Getter for the field <code>channelPoolFactory</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory} object
     */
    public NettyChannelPoolFactory getChannelPoolFactory() {
        return this.channelPoolFactory;
    }

    /**
     * <p>getChannelFactory.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public NettyChannelFactory getChannelFactory() {
        return this.channelPoolFactory.getChannelFactory();
    }

    /** {@inheritDoc} */
    @Override
    public EventLoopGroup getExecutor() {
        return channelPoolFactory.getChannelFactory().getExecutor();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        this.channelPoolMap.close();
    }
}
