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

package org.ribasco.asyncgamequerylib.protocols.valve.source;

import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import org.ribasco.asyncgamequerylib.core.enums.ChannelType;
import org.ribasco.asyncgamequerylib.core.enums.ProcessingMode;
import org.ribasco.asyncgamequerylib.core.messenger.GameServerMessenger;
import org.ribasco.asyncgamequerylib.core.transport.NettyPooledUdpTransport;
import org.ribasco.asyncgamequerylib.protocols.valve.source.request.*;
import org.ribasco.asyncgamequerylib.protocols.valve.source.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceQueryMessenger extends GameServerMessenger<SourceServerRequest, SourceServerResponse, NettyPooledUdpTransport<SourceServerRequest>> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryMessenger.class);

    public SourceQueryMessenger() {
        //Use the default session manager
        super(new NettyPooledUdpTransport<>(), ProcessingMode.ASYNCHRONOUS);
    }

    @Override
    public void configureTransport(NettyPooledUdpTransport<SourceServerRequest> transport) {
        //Set to NIO UDP Type
        transport.setChannelType(ChannelType.NIO_UDP);
        //Set our channel initializer
        transport.setChannelInitializer(new SourceQueryChannelInitializer(this));
        //Channel Options
        transport.addChannelOption(ChannelOption.SO_SNDBUF, 1048576);
        transport.addChannelOption(ChannelOption.SO_RCVBUF, 1048576 * 8);
        transport.addChannelOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(8192));
    }

    @Override
    public void configureMappings(Map<Class<? extends SourceServerRequest>, Class<? extends SourceServerResponse>> map) {
        map.put(SourceInfoRequest.class, SourceInfoResponse.class);
        map.put(SourceChallengeRequest.class, SourceChallengeResponse.class);
        map.put(SourcePlayerRequest.class, SourcePlayerResponse.class);
        map.put(SourceRulesRequest.class, SourceRulesResponse.class);
        map.put(SourceMasterServerRequest.class, SourceMasterServerResponse.class);
    }
}
