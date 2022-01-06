
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

package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A special decorator for {@link NettyChannelFactory} which cache's {@link Channel} instances in a {@link NettyChannelPool}
 *
 * @author Rafael Luis Ibasco
 */
public class PooledNettyChannelFactory extends NettyChannelFactory {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MessageChannelPoolMap channelPoolMap;

    /**
     * Creates a new instance using the provided {@link Bootstrap} for creating new channels/connections
     *
     * @param poolFactory
     *         A {@link NettyChannelPoolFactory} that is responsible for manufacturing {@link ChannelPool} instances
     */
    public PooledNettyChannelFactory(final NettyChannelPoolFactory poolFactory) {
        super(poolFactory.getChannelFactory().getBootstrap(), poolFactory.getChannelFactory().getOptions());
        final NettyPoolingStrategy poolStrategy = poolFactory.getChannelFactory().getOptions().getOrDefault(TransportOptions.POOL_STRATEGY);
        log.debug("[INIT] POOL => Using pool strategy '{}'", poolStrategy.getName());
        log.debug("[INIT] POOL => Using channel pool factory '{}'", poolFactory);
        this.channelPoolMap = new MessageChannelPoolMap(poolFactory, poolStrategy);
        log.debug("[INIT] POOL => Using channel pool map '{}'", this.channelPoolMap);
    }

    @Override
    public synchronized CompletableFuture<Channel> create(final Envelope<? extends AbstractRequest> envelope) {
        Objects.requireNonNull(envelope, "Envelope cannot be null");
        final NettyChannelPool pool = channelPoolMap.get(envelope);
        assert pool != null;
        log.debug("[POOL] Acquiring channel for envelope '{}' (Channel Pool: {})", envelope, pool);
        return pool.acquire(envelope);
    }
}
