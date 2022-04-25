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
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * <p>FixedNettyChannelPoolFactory class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class FixedNettyChannelPoolFactory extends NettyChannelPoolFactory {

    private static final Logger log = LoggerFactory.getLogger(FixedNettyChannelPoolFactory.class);

    private final FixedNettyChannelPool.AcquireTimeoutAction action;

    private final long acquireTimeoutMillis;

    private final int maxConnections;

    private final int maxPendingAcquires;

    /**
     * <p>Constructor for FixedNettyChannelPoolFactory.</p>
     *
     * @param channelFactory a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public FixedNettyChannelPoolFactory(NettyChannelFactory channelFactory) {
        super(channelFactory);
        final Options options = channelFactory.getOptions();
        this.action = options.getOrDefault(GeneralOptions.POOL_ACQUIRE_TIMEOUT_ACTION);
        this.acquireTimeoutMillis = options.getOrDefault(GeneralOptions.POOL_ACQUIRE_TIMEOUT);
        this.maxConnections = options.getOrDefault(GeneralOptions.POOL_MAX_CONNECTIONS);
        this.maxPendingAcquires = options.getOrDefault(GeneralOptions.POOL_ACQUIRE_MAX);
    }

    /** {@inheritDoc} */
    @Override
    public NettyChannelPool create(InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        NettyChannelPool pool = new FixedNettyChannelPool(getChannelFactory(), getChannelPoolHandler(), getChannelHealthChecker(), action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, false, NettyChannelPool.NONE);
        log.debug("[INIT] POOL => Initialized FixedNettyChannelPool (Address: {}, Acquire Timeout: {},  Max Connections: {}, Max Pending Acquires: {}, Instance: {}#{})", remoteAddress, acquireTimeoutMillis, maxConnections, maxPendingAcquires, pool.getClass().getSimpleName(), pool.hashCode());
        return pool;
    }
}
