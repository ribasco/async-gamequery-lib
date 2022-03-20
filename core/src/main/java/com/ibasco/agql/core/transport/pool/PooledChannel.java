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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

abstract public class PooledChannel implements Channel {

    private static final Logger log = LoggerFactory.getLogger(PooledChannel.class);

    private final Set<BiConsumer<PooledChannel, Throwable>> listeners = new HashSet<>();

    /**
     * Add release listener
     *
     * @param listener
     *         The callback to notify when this channel has been released
     */
    public void addListener(BiConsumer<PooledChannel, Throwable> listener) {
        listeners.add(listener);
    }

    /**
     * Remove release listener
     *
     * @param listener
     *         The callback to remove
     */
    public void removeListener(BiConsumer<PooledChannel, Throwable> listener) {
        listeners.remove(listener);
    }

    void notifyRelease() {
        notifyRelease(null);
    }

    void notifyRelease(Throwable error) {
        listeners.forEach(c -> {
            try {
                c.accept(this, error);
            } catch (Exception e) {
                log.warn("Error occured while notifying listeners on channel release: {}", this);
            }
        });
    }

    abstract public NettyChannelPool getChannelPool();

    abstract public CompletableFuture<Void> release();
}
