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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SimpleNettyChannelPoolFactory extends NettyChannelPoolFactory {

    private static final Logger log = LoggerFactory.getLogger(SimpleNettyChannelPoolFactory.class);

    public SimpleNettyChannelPoolFactory(NettyChannelFactory channelFactory) {
        super(channelFactory);
    }

    @Override
    public NettyChannelPool create(InetSocketAddress localAddress, InetSocketAddress remoteAddress) {
        NettyChannelPool pool = new SimpleNettyChannelPool(getChannelFactory(), getChannelPoolHandler(), getChannelHealthChecker(), false, NettyChannelPool.NONE);
        log.debug("[INIT] POOL => Initialized SimpleNettyChannelPool (Address: {}, Instance: {}#{})", remoteAddress, pool.getClass().getSimpleName(), pool.hashCode());
        return pool;
    }
}