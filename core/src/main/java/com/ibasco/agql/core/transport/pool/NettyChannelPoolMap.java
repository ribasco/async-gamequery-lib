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

import java.io.Closeable;

/**
 * <p>NettyChannelPoolMap interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyChannelPoolMap<K, P extends NettyChannelPool> extends Closeable {

    /**
     * Return the {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} for the {@code code}. This will never return {@code null},
     * but create a new {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} if non exists for they requested {@code key}.
     * <p>
     * Please note that {@code null} keys are not allowed.
     *
     * @param key
     *         a K object
     *
     * @return a P object
     */
    P get(K key);

    /**
     * Returns {@code true} if a {@link com.ibasco.agql.core.transport.pool.NettyChannelPool} exists for the given {@code key}.
     * <p>
     * Please note that {@code null} keys are not allowed.
     *
     * @param key
     *         a K object
     *
     * @return a boolean
     */
    boolean contains(K key);
}
