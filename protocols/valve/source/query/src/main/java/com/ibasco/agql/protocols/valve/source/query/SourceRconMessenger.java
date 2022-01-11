/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import com.ibasco.agql.core.transport.pool.NettyPoolingStrategy;
import com.ibasco.agql.core.transport.tcp.TcpNettyChannelFactoryProvider;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.handlers.*;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconResponse;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public final class SourceRconMessenger extends NettyMessenger<InetSocketAddress, SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    private final SourceRconAuthProxy proxy;

    public SourceRconMessenger(Options options) {
        super(options, new TcpNettyChannelFactoryProvider());
        this.proxy = new SourceRconAuthProxy(this, options.get(SourceRconOptions.CREDENTIALS_MANAGER, new RconCredentialsManager()));
    }

    @Override
    protected void configure(final Options options) {
        //connection pooling
        lockedOption(options, TransportOptions.POOL_STRATEGY, NettyPoolingStrategy.ADDRESS); //do not allow to be modified by the client
        defaultOption(options, TransportOptions.POOL_TYPE, ChannelPoolType.FIXED);
        defaultOption(options, SourceRconOptions.USE_TERMINATOR_PACKET, true);
        defaultOption(options, SourceRconOptions.STRICT_MODE, false);
        defaultOption(options, TransportOptions.POOL_ACQUIRE_TIMEOUT_ACTION, FixedNettyChannelPool.AcquireTimeoutAction.FAIL);
    }

    @Override
    public CompletableFuture<SourceRconResponse> send(InetSocketAddress address, SourceRconRequest request) {
        return proxy.send(address, request);
    }

    @Override
    public void registerInboundHandlers(LinkedList<ChannelInboundHandler> handlers) {
        handlers.addLast(new SourceRconPacketDecoder());
        handlers.addLast(new SourceRconPacketAssembler());
        handlers.addLast(new SourceRconAuthDecoder());
        handlers.addLast(new SourceRconCmdDecoder());
    }

    @Override
    public void registerOutboundHandlers(LinkedList<ChannelOutboundHandler> handlers) {
        handlers.addLast(new SourceRconAuthEncoder());
        handlers.addLast(new SourceRconCmdEncoder());
        handlers.addLast(new SourceRconPacketEncoder());
    }

    public SourceRconAuthProxy getAuthenticationProxy() {
        return proxy;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            proxy.close();
        }
    }
}