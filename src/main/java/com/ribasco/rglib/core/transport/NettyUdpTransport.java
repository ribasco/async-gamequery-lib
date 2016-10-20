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

import com.ribasco.rglib.core.AbstractRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 9/13/2016.
 */
public class NettyUdpTransport<M extends AbstractRequest> extends NettyTransport<M> {

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
    public Void cleanupChannel(Channel c) {
        return null; //do nothing
    }

    @Override
    public CompletableFuture<Channel> getChannel(InetSocketAddress address) {
        final CompletableFuture<Channel> cf = new CompletableFuture<>();
        //lazy initialization
        if (channel == null || !channel.isOpen()) {
            bind(0).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    channel = (NioDatagramChannel) future.channel();
                    cf.complete(channel);
                } else {
                    channel = null;
                    cf.completeExceptionally(future.cause());
                }
            });
        } else {
            cf.complete(channel);
        }
        return cf;
    }
}
