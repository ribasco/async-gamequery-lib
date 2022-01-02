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

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

import java.util.concurrent.CompletableFuture;

/**
 * Called before a {@link Channel} will be returned via {@link NettyChannelPool#acquire(com.ibasco.agql.core.Envelope)} or
 * {@link NettyChannelPool#acquire(com.ibasco.agql.core.Envelope, java.util.concurrent.CompletableFuture)}.
 */
public interface ChannelHealthChecker {

    /**
     * {@link ChannelHealthChecker} implementation that checks if {@link Channel#isActive()} returns {@code true}.
     */
    ChannelHealthChecker ACTIVE = channel -> channel.isActive() ? CompletableFuture.completedFuture(Boolean.TRUE) : CompletableFuture.completedFuture(Boolean.FALSE);

    /**
     * Check if the given channel is healthy which means it can be used. The returned {@link CompletableFuture} is notified once
     * the check is complete. If notified with {@link Boolean#TRUE} it can be used {@link Boolean#FALSE} otherwise.
     * <p>
     * This method will be called by the {@link EventLoop} of the {@link Channel}.
     */
    CompletableFuture<Boolean> isHealthy(Channel channel);
}