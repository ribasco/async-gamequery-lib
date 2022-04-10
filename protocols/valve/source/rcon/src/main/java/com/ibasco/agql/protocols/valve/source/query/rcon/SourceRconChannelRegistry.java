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

package com.ibasco.agql.protocols.valve.source.query.rcon;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.ibasco.agql.core.ChannelRegistry;
import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.exceptions.ChannelRegistrationException;
import com.ibasco.agql.core.util.Netty;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.*;

/**
 * Default implementation for {@link ChannelRegistry}. Uses a {@link SetMultimap} to store managed netty based {@link Channel} instances.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconChannelRegistry implements ChannelRegistry {

    private static final Logger log = LoggerFactory.getLogger(SourceRconChannelRegistry.class);

    private final SetMultimap<InetSocketAddress, Channel> channels = Multimaps.synchronizedSetMultimap(MultimapBuilder.hashKeys().hashSetValues().build());

    private final ChannelFutureListener UNREGISTER_ON_CLOSE = future -> {
        final Channel channel = future.channel();
        if (unregister(channel)) {
            log.debug("{} REGISTRY => Successfully unregistered channel: {}", Netty.id(channel), channel);
        } else {
            log.debug("{} REGISTRY => Failed to unregister channel: {}", Netty.id(channel), future.channel(), future.cause());
        }
    };

    @Override
    public void register(Channel channel) throws ChannelRegistrationException {
        assert channel.eventLoop().inEventLoop();
        Objects.requireNonNull(channel, "Channel must not be null");
        if (isRegistered(channel))
            return;
        if (!channel.isActive())
            throw new ChannelRegistrationException(new ChannelClosedException("Can't register a channel that is inactive", channel));
        if (!(channel.remoteAddress() instanceof InetSocketAddress))
            throw new ChannelRegistrationException(new UnsupportedAddressTypeException());
        synchronized (channels) {
            log.debug("{} REGISTRY => Registering channel '{}'", Netty.id(channel), channel);
            final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            if (!channels.put(address, channel))
                throw new ChannelRegistrationException("Failed to register channel: " + channel);
            unregisterOnClose(channel);
        }
    }

    @Override
    public boolean unregister(Channel channel) {
        if (channel == null)
            throw new IllegalArgumentException("Channel must not be null");
        if (!isRegistered(channel))
            return true;
        synchronized (channels) {
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            if (address == null)
                throw new IllegalStateException("Missing remote address for channel: " + channel);
            Iterator<Map.Entry<InetSocketAddress, Channel>> it = channels.entries().iterator();
            while (it.hasNext()) {
                Map.Entry<InetSocketAddress, Channel> entry = it.next();
                if (channel.id().equals(entry.getValue().id())) {
                    it.remove();
                    log.debug("{} REGISTRY => Unregistered channel: {}", Netty.id(entry.getValue()), entry.getValue());
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isRegistered(Channel channel) {
        if (channel == null)
            throw new IllegalArgumentException("Channel must not be null");
        synchronized (channels) {
            for (Channel ch : channels.values()) {
                if (ch.id().equals(channel.id()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Set<Map.Entry<InetSocketAddress, Channel>> getEntries() {
        synchronized (channels) {
            return new HashSet<>(channels.entries());
        }
    }

    @Override
    public Set<InetSocketAddress> getAddresses() {
        synchronized (channels) {
            return new HashSet<>(channels.keySet());
        }
    }

    @Override
    public Set<Channel> getChannels(InetSocketAddress address) {
        synchronized (channels) {
            return new HashSet<>(channels.get(address));
        }
    }

    @Override
    public int getCount(InetSocketAddress address) {
        synchronized (channels) {
            return channels.get(address).size();
        }
    }

    private void unregisterOnClose(Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        if (channel.closeFuture().isDone()) {
            try {
                UNREGISTER_ON_CLOSE.operationComplete(channel.closeFuture());
            } catch (Exception e) {
                log.error("An error occured while trying to unregister channel '{}'", channel, e);
            }
        } else {
            channel.closeFuture().addListener(UNREGISTER_ON_CLOSE);
        }
    }
}