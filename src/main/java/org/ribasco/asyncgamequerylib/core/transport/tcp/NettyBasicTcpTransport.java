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

package org.ribasco.asyncgamequerylib.core.transport.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.ribasco.asyncgamequerylib.core.AbstractRequest;
import org.ribasco.asyncgamequerylib.core.enums.ChannelType;
import org.ribasco.asyncgamequerylib.core.transport.NettyTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * <p>A basic TCP Transport Implementation which creates a new TCP Channel per connection</p>
 *
 * @param <M> A request extending {@link AbstractRequest}
 */
public class NettyBasicTcpTransport<M extends AbstractRequest> extends NettyTransport<M> {

    private static final Logger log = LoggerFactory.getLogger(NettyBasicTcpTransport.class);

    public NettyBasicTcpTransport(ChannelType channelType) {
        super(channelType);
        NettyTransport transport = this;
        getBootstrap().handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                getChannelInitializer().initializeChannel(ch, transport);
            }
        });
    }

    @Override
    public CompletableFuture<Channel> getChannel(M address) {
        final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
        ChannelFuture f = getBootstrap().connect(address.recipient());
        //Acquire from pool and listen for completion
        f.addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                channelFuture.complete(future.channel());
            } else {
                channelFuture.completeExceptionally(future.cause());
            }
        });
        return channelFuture;
    }
}
