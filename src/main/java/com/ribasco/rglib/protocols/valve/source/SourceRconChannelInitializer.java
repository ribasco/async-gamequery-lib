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

import com.ribasco.rglib.core.transport.ChannelInitializer;
import com.ribasco.rglib.core.transport.NettyTransport;
import com.ribasco.rglib.core.transport.handlers.ErrorHandler;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconPacketAssembler;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconPacketDecoder;
import com.ribasco.rglib.protocols.valve.source.handlers.SourceRconRequestEncoder;
import io.netty.channel.Channel;

/**
 * Created by raffy on 10/22/2016.
 */
public class SourceRconChannelInitializer implements ChannelInitializer {
    private SourceRconMessenger messenger;

    public SourceRconChannelInitializer(SourceRconMessenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        SourceRconPacketBuilder rconBuilder = new SourceRconPacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new ErrorHandler());
        channel.pipeline().addLast(new SourceRconRequestEncoder(rconBuilder));
        channel.pipeline().addLast(new SourceRconPacketAssembler());
        channel.pipeline().addLast(new SourceRconPacketDecoder(rconBuilder, messenger));
    }
}
