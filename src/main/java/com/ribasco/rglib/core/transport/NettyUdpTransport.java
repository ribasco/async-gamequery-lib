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

package com.ribasco.rglib.core.transport;

import com.ribasco.rglib.core.AbstractMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/13/2016.
 */
public class NettyUdpTransport<M extends AbstractMessage> extends NettyTransport<NioDatagramChannel, M> {

    private Logger log = LoggerFactory.getLogger(NettyUdpTransport.class);
    private NioDatagramChannel channel; //maintain only one channel

    @Override
    public void initialize() {
        super.initialize();
        getBootstrap().handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) throws Exception {
                getChannelInitializer().initializeChannel(ch);
            }
        });
    }

    @Override
    public void cleanupChannel(Channel c) {
        //do nothing
    }

    @Override
    public NioDatagramChannel getChannel(InetSocketAddress address) {
        //disregard address
        //lazy initialization
        if (channel == null || !channel.isOpen()) {
            try {
                //TODO: Change this to asynchronous, do not sync, return a ChannelFuture instead
                log.debug("Re-binding channel");
                channel = (NioDatagramChannel) bind(0).sync().channel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return channel;
    }
}
