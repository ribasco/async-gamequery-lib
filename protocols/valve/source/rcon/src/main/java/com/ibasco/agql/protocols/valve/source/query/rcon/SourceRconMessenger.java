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

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public final class SourceRconMessenger extends NettyMessenger<SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    private final SourceRconAuthManager authManager;

    public SourceRconMessenger(Options options) {
        super(options);
        this.authManager = new SourceRconAuthManager(this, options.get(SourceRconOptions.CREDENTIALS_STORE, new InMemoryCredentialsStore()));
    }

    public SourceRconAuthManager getAuthManager() {
        return authManager;
    }

    @Override
    protected void configure(final Options options) {
        defaultOption(options, TransportOptions.POOL_TYPE, ChannelPoolType.FIXED);
        defaultOption(options, SourceRconOptions.USE_TERMINATOR_PACKET, true);
        defaultOption(options, SourceRconOptions.STRICT_MODE, false);
        defaultOption(options, TransportOptions.POOL_ACQUIRE_TIMEOUT_ACTION, FixedNettyChannelPool.AcquireTimeoutAction.FAIL);
    }

    @Override
    protected NettyChannelFactory createChannelFactory() {
        final NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.TCP, getOptions(), new SourceRconChannelContextFactory(this));
        return new SourceRconChannelFactory(channelFactory);
    }

    @Override
    public CompletableFuture<SourceRconResponse> send(InetSocketAddress address, SourceRconRequest request) {
        return authManager.send(address, request);
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            authManager.close();
        }
    }
}