/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.steam.master;

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.transport.pool.NettyPoolingStrategy;
import com.ibasco.agql.core.transport.udp.UdpTransportFactory;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerAddressDecoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketDecoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketEncoder;
import com.ibasco.agql.protocols.valve.steam.master.handlers.MasterServerRequestEncoder;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerResponse;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;

import java.net.InetSocketAddress;
import java.util.LinkedList;

public class MasterServerMessenger extends NettyMessenger<InetSocketAddress, MasterServerRequest, MasterServerResponse> {

    public MasterServerMessenger(OptionMap options) {
        super(options, new UdpTransportFactory(false));
    }

    @Override
    protected void configure(OptionMap options) {
        options.add(TransportOptions.CONNECTION_POOLING, false, true);
        options.add(TransportOptions.POOL_STRATEGY, NettyPoolingStrategy.ADDRESS);
        options.add(TransportOptions.READ_TIMEOUT, 8000, true);
    }

    @Override
    public void registerInboundHandlers(LinkedList<ChannelInboundHandler> handlers) {
        handlers.addLast(new MasterServerPacketDecoder());
        handlers.addLast(new MasterServerAddressDecoder());
    }

    @Override
    public void registerOutboundHandlers(LinkedList<ChannelOutboundHandler> handlers) {
        handlers.addLast(new MasterServerRequestEncoder());
        handlers.addLast(new MasterServerPacketEncoder());
    }
}
