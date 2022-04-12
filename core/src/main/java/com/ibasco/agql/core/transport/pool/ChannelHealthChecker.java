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

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Called before a {@link io.netty.channel.Channel} will be returned via {@link com.ibasco.agql.core.transport.pool.NettyChannelPool#acquire(InetSocketAddress)} or
 * {@link com.ibasco.agql.core.transport.pool.NettyChannelPool#acquire(InetSocketAddress, CompletableFuture)}.
 *
 * @author Rafael Luis Ibasco
 */
public interface ChannelHealthChecker {

    /**
     * {@link ChannelHealthChecker} implementation that checks if {@link Channel#isActive()} returns {@code true}.
     */
    ChannelHealthChecker ACTIVE = channel -> channel.isActive() ? CompletableFuture.completedFuture(Boolean.TRUE) : CompletableFuture.completedFuture(Boolean.FALSE);

    /**
     * Check if the given channel is healthy which means it can be used. The returned {@link java.util.concurrent.CompletableFuture} is notified once
     * the check is complete. If notified with {@link java.lang.Boolean#TRUE} it can be used {@link java.lang.Boolean#FALSE} otherwise.
     * <p>
     * This method will be called by the {@link io.netty.channel.EventLoop} of the {@link io.netty.channel.Channel}.
     *
     * @param channel a {@link io.netty.channel.Channel} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Boolean> isHealthy(Channel channel);
}
