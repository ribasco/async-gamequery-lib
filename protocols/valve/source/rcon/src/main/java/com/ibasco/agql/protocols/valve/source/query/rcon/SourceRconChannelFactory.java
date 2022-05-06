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

import com.ibasco.agql.core.transport.DefaultPropertyResolver;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryInitializer;
import com.ibasco.agql.core.transport.pool.DefaultPoolPropertyResolver;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconAuthDecoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconAuthEncoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconCmdDecoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconCmdEncoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconPacketAssembler;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconPacketDecoder;
import com.ibasco.agql.protocols.valve.source.query.rcon.handlers.SourceRconPacketEncoder;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A special channel factory which decorates an exiting {@link com.ibasco.agql.core.transport.NettyChannelFactory} and ensures that the acquired/created {@link io.netty.channel.Channel} is fully authenticated by the remote server
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceRconChannelFactory extends NettyChannelFactoryInitializer {

    private static final Logger log = LoggerFactory.getLogger(SourceRconChannelFactory.class);

    /**
     * <p>Constructor for SourceRconChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public SourceRconChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        setResolver(new DefaultPoolPropertyResolver(new DefaultPropertyResolver()));
    }

    /** {@inheritDoc} */
    @Override
    public void registerInboundHandlers(final LinkedList<ChannelInboundHandler> handlers) {
        handlers.addLast(new SourceRconPacketDecoder());
        handlers.addLast(new SourceRconPacketAssembler());
        handlers.addLast(new SourceRconAuthDecoder());
        handlers.addLast(new SourceRconCmdDecoder());
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutboundHandlers(final LinkedList<ChannelOutboundHandler> handlers) {
        handlers.addLast(new SourceRconAuthEncoder());
        handlers.addLast(new SourceRconCmdEncoder());
        handlers.addLast(new SourceRconPacketEncoder());
    }
}
