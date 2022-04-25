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
 * A registry/store for all managed netty based {@link io.netty.channel.Channel}. Implementation of this class must be thread-safe
 *
 * @author Rafael Luis Ibasco
 */
public interface ChannelRegistry {

    /**
     * Register a newly acquired {@link io.netty.channel.Channel}. This method is thread-safe.
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to be registered
     * @throws com.ibasco.agql.core.exceptions.ChannelRegistrationException
     *         If the registration fails
     */
    void register(Channel channel) throws ChannelRegistrationException;

    /**
     * Unregister a {@link io.netty.channel.Channel}. This method is thread-safe.
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to be unregistered
     * @return {@code true} if the {@link io.netty.channel.Channel} was successfuly unregistred.
     */
    boolean unregister(Channel channel);

    /**
     * Check if {@link io.netty.channel.Channel} is registered by this instance.
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to check.
     * @return {@code true} if the {@link io.netty.channel.Channel} is registered by this instance.
     */
    boolean isRegistered(Channel channel);

    /**
     * <p>getEntries.</p>
     *
     * @return a {@link java.util.Set} object
     */
    Set<Map.Entry<InetSocketAddress, Channel>> getEntries();

    /**
     * <p>A unique set of registered {@link java.net.InetSocketAddress}</p>
     *
     * @return a {@link java.util.Set} object
     */
    Set<InetSocketAddress> getAddresses();

    /**
     * <p>The {@link io.netty.channel.Channel}'s acquired for the specified {@link java.net.InetSocketAddress}</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} to be used for the lookup
     *
     * @return a {@link java.util.Set} of {@link io.netty.channel.Channel} instances acquired for the address
     */
    Set<Channel> getChannels(InetSocketAddress address);

    /**
     * The number of {@link io.netty.channel.Channel} registered for the specified {@link java.net.InetSocketAddress}
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} to lookup
     * @return The total number of registered {@link io.netty.channel.Channel} for the specified address
     */
    int getCount(InetSocketAddress address);
}
