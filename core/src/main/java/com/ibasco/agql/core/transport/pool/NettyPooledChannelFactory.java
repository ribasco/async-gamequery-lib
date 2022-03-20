
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
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * A decorator for {@link NettyChannelFactory} which adds support for {@link Channel} pooling.
 *
 * @author Rafael Luis Ibasco
 */
public class NettyPooledChannelFactory extends NettyChannelFactoryDecorator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final NettyChannelPoolMap<Object, NettyChannelPool> channelPoolMap;

    private final NettyChannelPoolFactory channelPoolFactory;

    private final NettyPoolPropertyResolver resolver;

    /**
     * Creates a new instance using the provided {@link NettyChannelFactory}. A default {@link NettyChannelPoolFactoryProvider} will be used to obtain a {@link NettyChannelPoolFactory}
     *
     * @param channelFactory
     *         The {@link NettyChannelFactory} that will be used to create {@link Channel} instances
     */
    public NettyPooledChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        final ChannelPoolType poolType = getOptions().getOrDefault(TransportOptions.POOL_TYPE);
        this.resolver = new DefaultPoolPropertyResolver();
        this.channelPoolFactory = NettyChannelPoolFactoryProvider.DEFAULT.getFactory(poolType, channelFactory);
        log.debug("[INIT] POOL => Using channel pool factory '{}'", channelPoolFactory);
        this.channelPoolMap = new MessageChannelPoolMap(channelPoolFactory, resolver);
        log.debug("[INIT] POOL => Using channel pool map '{}'", this.channelPoolMap);
    }

    @Override
    public CompletableFuture<Channel> create(Object data) {
        final InetSocketAddress remoteAddress = this.getResolver().resolveRemoteAddress(data);
        final NettyChannelPool pool = channelPoolMap.get(data);
        assert pool != null;
        log.debug("[POOL] Acquiring channel for address '{}' (Channel Pool: {})", remoteAddress, pool);
        return pool.acquire(remoteAddress);
    }

    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return NettyUtil.useEventLoop(create(data), eventLoop);
    }

    public NettyChannelFactory getChannelFactory() {
        return this.channelPoolFactory.getChannelFactory();
    }

    @Override
    public NettyPoolPropertyResolver getResolver() {
        return this.resolver;
    }

    @Override
    public EventLoopGroup getExecutor() {
        return channelPoolFactory.getChannelFactory().getExecutor();
    }

    @Override
    public void close() throws IOException {
        this.channelPoolMap.close();
    }
}
