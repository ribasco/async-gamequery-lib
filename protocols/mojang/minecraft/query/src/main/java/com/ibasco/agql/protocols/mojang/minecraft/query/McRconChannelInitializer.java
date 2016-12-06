/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ibasco.agql.protocols.mojang.minecraft.query;

import com.ibasco.agql.core.transport.NettyChannelInitializer;
import com.ibasco.agql.core.transport.NettyTransport;
import com.ibasco.agql.core.transport.handlers.ErrorHandler;
import com.ibasco.agql.protocols.mojang.minecraft.query.handlers.McRconPacketAssembler;
import com.ibasco.agql.protocols.mojang.minecraft.query.handlers.McRconPacketDecoder;
import com.ibasco.agql.protocols.mojang.minecraft.query.handlers.McRconRequestEncoder;
import com.ibasco.agql.protocols.mojang.minecraft.query.handlers.McRconResponseRouter;
import io.netty.channel.Channel;

class McRconChannelInitializer implements NettyChannelInitializer {

    private McRconMessenger rconMessenger;

    McRconChannelInitializer(McRconMessenger responseHandler) {
        this.rconMessenger = responseHandler;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        McRconPacketBuilder rconBuilder = new McRconPacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new McRconRequestEncoder(rconBuilder));
        channel.pipeline().addLast(new McRconPacketDecoder());
        channel.pipeline().addLast(new McRconPacketAssembler(rconMessenger.getRequestTypeMap()));
        channel.pipeline().addLast(new McRconResponseRouter(rconMessenger));
        channel.pipeline().addLast(new ErrorHandler());
    }
}
