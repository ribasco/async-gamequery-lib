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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>MasterServerChannelContext class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerChannelContext extends NettyChannelContext {

    /**
     * <p>Constructor for MasterServerChannelContext.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     * @param messenger
     *         a {@link com.ibasco.agql.core.NettyMessenger} object
     */
    public MasterServerChannelContext(Channel channel, NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse, ? extends Options> messenger) {
        super(channel, messenger);
    }

    /**
     * <p>Constructor for MasterServerChannelContext.</p>
     *
     * @param context a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    protected MasterServerChannelContext(NettyChannelContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public MasterServerMessenger messenger() {
        return (MasterServerMessenger) super.messenger();
    }

    /** {@inheritDoc} */
    @Override
    public MasterServerProperties properties() {
        return (MasterServerProperties) super.properties();
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<MasterServerChannelContext> send() {
        //noinspection unchecked
        return (CompletableFuture<MasterServerChannelContext>) super.send();
    }

    /** {@inheritDoc} */
    @Override
    public MasterServerChannelContext save() {
        return (MasterServerChannelContext) super.save();
    }

    /** {@inheritDoc} */
    @Override
    public MasterServerChannelContext restore() {
        return (MasterServerChannelContext) super.restore();
    }

    /** {@inheritDoc} */
    @Override
    protected Properties newProperties(Properties copy) {
        if (copy instanceof MasterServerProperties)
            return new MasterServerProperties(copy);
        return new MasterServerProperties();
    }

    /** {@inheritDoc} */
    public static MasterServerChannelContext getContext(Channel channel) {
        return (MasterServerChannelContext) NettyChannelContext.getContext(channel);
    }

    public class MasterServerProperties extends Properties {

        private AtomicReference<InetSocketAddress> lastSeedAddress = new AtomicReference<>();

        private final Set<InetSocketAddress> addressSet;

        public MasterServerProperties() {
            addressSet = new HashSet<>();
        }

        public MasterServerProperties(Properties properties) {
            super(properties);
            this.lastSeedAddress = ((MasterServerProperties) properties).lastSeedAddress;
            this.addressSet = new HashSet<>(((MasterServerProperties) properties).addressSet);
        }

        public Set<InetSocketAddress> addressSet() {
            return addressSet;
        }

        public InetSocketAddress lastSeedAddress() {
            return lastSeedAddress.get();
        }

        public void lastSeedAddress(InetSocketAddress lastSeedAddress) {
            assert inEventLoop();
            this.lastSeedAddress.getAndSet(lastSeedAddress);
        }

    }
}
