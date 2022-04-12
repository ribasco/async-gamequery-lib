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

package com.ibasco.agql.core.transport;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;

import java.util.LinkedList;

/**
 * A special {@link com.ibasco.agql.core.transport.NettyChannelFactoryDecorator} which allows concrete sub-classes to register inbound and outbound {@link io.netty.channel.ChannelHandler}s.
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyChannelFactoryInitializer extends NettyChannelFactoryDecorator implements NettyChannelHandlerInitializer {

    /**
     * <p>Constructor for NettyChannelFactoryInitializer.</p>
     *
     * @param channelFactory a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    protected NettyChannelFactoryInitializer(NettyChannelFactory channelFactory) {
        super(channelFactory);
        channelFactory.getChannelInitializer().setHandlerInitializer(this);
    }

    /** {@inheritDoc} */
    @Override
    abstract public void registerInboundHandlers(final LinkedList<ChannelInboundHandler> handlers);

    /** {@inheritDoc} */
    @Override
    abstract public void registerOutboundHandlers(final LinkedList<ChannelOutboundHandler> handlers);
}
