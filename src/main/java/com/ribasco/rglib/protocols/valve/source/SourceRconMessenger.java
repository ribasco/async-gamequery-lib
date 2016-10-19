/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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

package com.ribasco.rglib.protocols.valve.source;

import com.ribasco.rglib.core.enums.ChannelType;
import com.ribasco.rglib.core.enums.ProcessingMode;
import com.ribasco.rglib.core.handlers.ErrorHandler;
import com.ribasco.rglib.core.messenger.GameServerMessenger;
import com.ribasco.rglib.core.transport.NettyTcpTransport;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconPacketAssembler;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconPacketDecoder;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconRequestEncoder;
import com.ribasco.rglib.protocols.valve.source.request.SourceRconAuthRequest;
import com.ribasco.rglib.protocols.valve.source.request.SourceRconCmdRequest;
import com.ribasco.rglib.protocols.valve.source.request.SourceRconTerminator;
import com.ribasco.rglib.protocols.valve.source.response.SourceRconAuthResponse;
import com.ribasco.rglib.protocols.valve.source.response.SourceRconCmdResponse;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by raffy on 9/24/2016.
 */
public class SourceRconMessenger extends GameServerMessenger<SourceRconRequest, SourceRconResponse, NettyTcpTransport<SourceRconRequest>> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    public SourceRconMessenger() {
        super(new NettyTcpTransport(), new SourceRconSessionIdFactory(), ProcessingMode.SYNCHRONOUS);
    }

    @Override
    public void configureTransport(NettyTcpTransport transport) {
        SourceRconPacketBuilder builder = new SourceRconPacketBuilder(transport.getAllocator());
        final SourceRconMessenger m = this;
        transport.setChannelType(ChannelType.NIO_TCP);
        //Set our channel initializer
        transport.setChannelInitializer(channel -> {
            channel.pipeline().addLast(new ErrorHandler());
            channel.pipeline().addLast(new SourceRconRequestEncoder(builder));
            channel.pipeline().addLast(new SourceRconPacketAssembler());
            channel.pipeline().addLast(new SourceRconPacketDecoder(builder, m));
        });

        //Channel Options
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 4);
    }

    @Override
    public void configureMappings(Map<Class<? extends SourceRconRequest>, Class<? extends SourceRconResponse>> map) {
        map.put(SourceRconAuthRequest.class, SourceRconAuthResponse.class);
        map.put(SourceRconCmdRequest.class, SourceRconCmdResponse.class);
        map.put(SourceRconTerminator.class, SourceRconCmdResponse.class);
    }
}
