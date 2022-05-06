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

import com.ibasco.agql.core.transport.DefaultPropertyResolver;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryInitializer;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerAddressDecoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketDecoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketEncoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerRequestEncoder;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import java.util.LinkedList;

/**
 * <p>MasterServerChannelFactory class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MasterServerChannelFactory extends NettyChannelFactoryInitializer {

    /**
     * <p>Constructor for MasterServerChannelFactory.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public MasterServerChannelFactory(final NettyChannelFactory channelFactory) {
        super(channelFactory);
        setResolver(new DefaultPropertyResolver());
    }

    /** {@inheritDoc} */
    @Override
    public void registerInboundHandlers(LinkedList<ChannelInboundHandler> handlers) {
        handlers.addLast(new MasterServerPacketDecoder());
        handlers.addLast(new MasterServerAddressDecoder());
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutboundHandlers(LinkedList<ChannelOutboundHandler> handlers) {
        handlers.addLast(new MasterServerRequestEncoder());
        handlers.addLast(new MasterServerPacketEncoder());
    }
}
