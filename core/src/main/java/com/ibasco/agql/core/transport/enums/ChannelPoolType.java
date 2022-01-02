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

package com.ibasco.agql.core.transport.enums;

/**
 * The type of {@link io.netty.channel.pool.ChannelPool} implementation to use.
 *
 * @author Rafael Luis Ibasco
 */
public enum ChannelPoolType {
    /**
     * An unbounded channel pool implementation. Connections will be made on-demand without limitation.
     *
     * @see io.netty.channel.pool.SimpleChannelPool
     * @see com.ibasco.agql.core.transport.pool.NettyChannelPool
     */
    ADAPTIVE,

    /**
     * A bounded channel pool implementation. A fixed number of connections will be used. If the requests surpass
     * the maximum number of connections allowed, then the requests will be queued until a connection is made available.
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see com.ibasco.agql.core.transport.pool.NettyChannelPool
     */
    FIXED
}
