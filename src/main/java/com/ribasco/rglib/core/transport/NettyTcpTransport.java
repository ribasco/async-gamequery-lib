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
import com.ribasco.rglib.protocols.valve.source.SourceChannelAttributes;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 9/13/2016.
 */
public class NettyTcpTransport<M extends AbstractMessage> extends NettyTransport<M> {

    private static final Logger log = LoggerFactory.getLogger(NettyTcpTransport.class);

    //Provide Channel Pooling mechanism here
    private ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    @Override
    public void initialize() {
        super.initialize();
        //Initialize our pool map instance
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new SimpleChannelPool(getBootstrap().remoteAddress(key), new AbstractChannelPoolHandler() {
                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        getChannelInitializer().initializeChannel(ch);
                        log.debug("Channel Created : {} for {}", ch, ch.remoteAddress());
                    }

                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                        super.channelReleased(ch);
                        log.debug("Channel Released : {} for {}", ch, ch.remoteAddress());
                    }

                    @Override
                    public void channelAcquired(Channel ch) throws Exception {
                        super.channelAcquired(ch);
                        log.debug("Channel Acquired: {} for {}", ch, ch.remoteAddress());
                    }
                });
            }
        };
    }


    @Override
    public Void cleanupChannel(Channel c) {
        //Release channel from the pool
        if (c.hasAttr(SourceChannelAttributes.CHANNEL_POOL)) {
            c.attr(SourceChannelAttributes.CHANNEL_POOL).get().release(c);
        }
        return null;
    }

    @Override
    public CompletableFuture<Channel> getChannel(InetSocketAddress address) {
        final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
        final SimpleChannelPool pool = poolMap.get(address);
        //Acquire from pool and listen for completion
        pool.acquire().addListener((Future<Channel> future) -> {
            if (future.isSuccess()) {
                SocketChannel channel = (SocketChannel) future.get();
                channel.attr(SourceChannelAttributes.CHANNEL_POOL).set(pool);
                channelFuture.complete(channel);
            } else {
                channelFuture.completeExceptionally(future.cause());
            }
        });
        return channelFuture;
    }
}
