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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.ChannelRegistrationException;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

/**
 * A registry/store for all managed netty based {@link Channel}. Implementation of this class must be thread-safe
 *
 * @author Rafael Luis Ibasco
 */
public interface ChannelRegistry {

    /**
     * Register a newly acquired {@link Channel}. This method is thread-safe.
     *
     * @param channel
     *         The {@link Channel} to be registered
     *
     * @throws ChannelRegistrationException
     *         If the registration fails
     */
    void register(Channel channel) throws ChannelRegistrationException;

    /**
     * Unregister a {@link Channel}. This method is thread-safe.
     *
     * @param channel
     *         The {@link Channel} to be unregistered
     *
     * @return {@code true} if the {@link Channel} was successfuly unregistred.
     */
    boolean unregister(Channel channel);

    /**
     * Check if {@link Channel} is registered by this instance.
     *
     * @param channel
     *         The {@link Channel} to check.
     *
     * @return {@code true} if the {@link Channel} is registered by this instance.
     */
    boolean isRegistered(Channel channel);

    Set<Map.Entry<InetSocketAddress, Channel>> getEntries();

    Set<InetSocketAddress> getAddresses();

    Set<Channel> getChannels(InetSocketAddress address);

    /**
     * The number of {@link Channel} registered for the specified {@link InetSocketAddress}
     *
     * @param address
     *         The {@link InetSocketAddress} to lookup
     *
     * @return The total number of registered {@link Channel} for the specified address
     */
    int getCount(InetSocketAddress address);
}