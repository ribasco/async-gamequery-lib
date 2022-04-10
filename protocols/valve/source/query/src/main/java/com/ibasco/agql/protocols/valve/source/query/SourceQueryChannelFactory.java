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

import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryInitializer;
import com.ibasco.agql.protocols.valve.source.query.challenge.SourceQueryChallengeDecoder;
import com.ibasco.agql.protocols.valve.source.query.challenge.SourceQueryChallengeEncoder;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQueryPacketDecoder;
import com.ibasco.agql.protocols.valve.source.query.common.handlers.SourceQuerySplitPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoDecoder;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoEncoder;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayersDecoder;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayersEncoder;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesDecoder;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesEncoder;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class SourceQueryChannelFactory extends NettyChannelFactoryInitializer {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryChannelFactory.class);

    public SourceQueryChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
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
