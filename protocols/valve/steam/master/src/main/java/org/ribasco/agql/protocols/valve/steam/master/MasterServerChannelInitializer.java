/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.valve.steam.master;

import io.netty.channel.Channel;
import org.ribasco.agql.core.transport.NettyChannelInitializer;
import org.ribasco.agql.core.transport.NettyTransport;
import org.ribasco.agql.core.transport.handlers.ErrorHandler;
import org.ribasco.agql.protocols.valve.steam.master.handlers.MasterServerPacketDecoder;
import org.ribasco.agql.protocols.valve.steam.master.handlers.MasterServerRequestEncoder;

import java.util.function.BiConsumer;

public class MasterServerChannelInitializer implements NettyChannelInitializer {

    private BiConsumer<MasterServerResponse, Throwable> responseHandler;

    public MasterServerChannelInitializer(BiConsumer<MasterServerResponse, Throwable> responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void initializeChannel(Channel channel, NettyTransport transport) {
        MasterServerPacketBuilder builder = new MasterServerPacketBuilder(transport.getAllocator());
        channel.pipeline().addLast(new ErrorHandler());
        channel.pipeline().addLast(new MasterServerRequestEncoder(builder));
        channel.pipeline().addLast(new MasterServerPacketDecoder(responseHandler, builder));
    }
}
