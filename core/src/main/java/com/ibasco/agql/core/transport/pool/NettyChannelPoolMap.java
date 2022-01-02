/*
 * Copyright (c) 2021-2022 Asynchronous Game Query Library
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

public interface NettyChannelPoolMap<K, P extends NettyChannelPool> {
    /**
     * Return the {@link NettyChannelPool} for the {@code code}. This will never return {@code null},
     * but create a new {@link NettyChannelPool} if non exists for they requested {@code key}.
     *
     * Please note that {@code null} keys are not allowed.
     */
    P get(K key);

    /**
     * Returns {@code true} if a {@link NettyChannelPool} exists for the given {@code key}.
     *
     * Please note that {@code null} keys are not allowed.
     */
    boolean contains(K key);
}