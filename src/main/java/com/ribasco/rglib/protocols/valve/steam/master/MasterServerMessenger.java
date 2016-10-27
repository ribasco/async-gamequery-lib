/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam.master;

import com.ribasco.rglib.core.enums.ChannelType;
import com.ribasco.rglib.core.enums.ProcessingMode;
import com.ribasco.rglib.core.messenger.GameServerMessenger;
import com.ribasco.rglib.core.transport.NettyPooledUdpTransport;
import io.netty.channel.ChannelOption;

import java.util.Map;

/**
 * Created by raffy on 10/22/2016.
 */
public class MasterServerMessenger extends GameServerMessenger<MasterServerRequest, MasterServerResponse, NettyPooledUdpTransport<MasterServerRequest>> {

    public MasterServerMessenger() {
        super(new NettyPooledUdpTransport<>(), ProcessingMode.ASYNCHRONOUS);
    }

    @Override
    public void configureTransport(NettyPooledUdpTransport<MasterServerRequest> transport) {
        //Set to NIO UDP Type
        transport.setChannelType(ChannelType.NIO_UDP);

        //Instantiate our packet builder
        MasterServerPacketBuilder builder = new MasterServerPacketBuilder(transport.getAllocator());

        //Set our channel initializer
        transport.setChannelInitializer(new MasterServerChannelInitializer(this));

        //Channel Options
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 8);
    }

    @Override
    public void configureMappings(Map<Class<? extends MasterServerRequest>, Class<? extends MasterServerResponse>> map) {
        map.put(MasterServerRequest.class, MasterServerResponse.class);
    }
}
