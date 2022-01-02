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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.NettyPoolingStrategy;
import com.ibasco.agql.core.transport.udp.UdpTransportFactory;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.Platform;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQueryPacketDecoder;
import com.ibasco.agql.protocols.valve.source.query.handlers.SourceQuerySplitPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.protocols.challenge.SourceQueryChallengeDecoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.challenge.SourceQueryChallengeEncoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoDecoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoEncoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.players.SourceQueryPlayersDecoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.players.SourceQueryPlayersEncoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesDecoder;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesEncoder;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
 * Messenger implementation for the Source Query Protocol
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceQueryMessenger extends NettyMessenger<InetSocketAddress, SourceQueryRequest, SourceQueryResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    public SourceQueryMessenger(OptionMap options) {
        super(options, new UdpTransportFactory(true));
    }

    @Override
    protected void configure(OptionMap options) {
        //enable pooling by default
        defaultOption(options, TransportOptions.CONNECTION_POOLING, true);
        defaultOption(options, TransportOptions.POOL_STRATEGY, NettyPoolingStrategy.MESSAGE_TYPE);
        defaultOption(options, TransportOptions.POOL_TYPE, ChannelPoolType.FIXED);
        defaultOption(options, TransportOptions.POOL_MAX_CONNECTIONS, Platform.getDefaultPoolSize());
        defaultOption(options, TransportOptions.READ_TIMEOUT, 3000);
    }

    @Override
    public void registerInboundHandlers(final LinkedList<ChannelInboundHandler> handlers) {
        log.debug("INIT => Registering Source Query INBOUND Handlers");
        handlers.addLast(new SourceQueryPacketDecoder());
        handlers.addLast(new SourceQuerySplitPacketAssembler());
        handlers.addLast(new SourceQueryChallengeDecoder());
        handlers.addLast(new SourceQueryInfoDecoder());
        handlers.addLast(new SourceQueryPlayersDecoder());
        handlers.addLast(new SourceQueryRulesDecoder());
    }

    @Override
    public void registerOutboundHandlers(final LinkedList<ChannelOutboundHandler> handlers) {
        log.debug("INIT => Registering Source Query OUTBOUND Handlers");
        handlers.addLast(new SourceQueryChallengeEncoder());
        handlers.addLast(new SourceQueryInfoEncoder());
        handlers.addLast(new SourceQueryPlayersEncoder());
        handlers.addLast(new SourceQueryRulesEncoder());
    }
}
