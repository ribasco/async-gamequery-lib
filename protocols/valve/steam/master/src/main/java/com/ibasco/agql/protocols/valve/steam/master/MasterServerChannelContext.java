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
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class MasterServerChannelContext extends NettyChannelContext {

    public MasterServerChannelContext(Channel channel, NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse> messenger) {
        super(channel, messenger);
    }

    protected MasterServerChannelContext(NettyChannelContext context) {
        super(context);
    }

    @Override
    public MasterServerMessenger messenger() {
        return (MasterServerMessenger) super.messenger();
    }

    @Override
    public MasterServerProperties properties() {
        return (MasterServerProperties) super.properties();
    }

    @Override
    public CompletableFuture<MasterServerChannelContext> send() {
        //noinspection unchecked
        return (CompletableFuture<MasterServerChannelContext>) super.send();
    }

    @Override
    public MasterServerChannelContext save() {
        return (MasterServerChannelContext) super.save();
    }

    @Override
    public MasterServerChannelContext restore() {
        return (MasterServerChannelContext) super.restore();
    }

    @Override
    protected Properties newProperties(Properties copy) {
        if (copy instanceof MasterServerProperties)
            return new MasterServerProperties(copy);
        return new MasterServerProperties();
    }

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
            this.lastSeedAddress.getAndSet(lastSeedAddress);
        }

    }
}
