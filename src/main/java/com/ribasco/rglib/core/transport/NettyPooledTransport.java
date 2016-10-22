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
import com.ribasco.rglib.core.transport.pool.MessageChannelPoolMap;
import com.ribasco.rglib.protocols.valve.source.SourceChannelAttributes;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * A transport that use a pool implementation to create or re-use a {@link Channel}
 */
abstract class NettyPooledTransport<M extends AbstractRequest, K> extends NettyTransport<M> {
    private static final Logger log = LoggerFactory.getLogger(NettyPooledTransport.class);

    private MessageChannelPoolMap<M, K> poolMap;
    final ChannelPoolHandler channelPoolHandler = new AbstractChannelPoolHandler() {
        @Override
        public void channelCreated(Channel ch) throws Exception {
            onChannelCreate(ch);
        }

        @Override
        public void channelAcquired(Channel ch) throws Exception {
            onChannelAcquire(ch);
        }

        @Override
        public void channelReleased(Channel ch) throws Exception {
            onChannelRelease(ch);
        }
    };

    @Override
    public void initialize() {
        super.initialize();
        //Initialize our pool map instance
        poolMap = new MessageChannelPoolMap<>(this::createKey, this::createChannelPool);
    }

    @Override
    public CompletableFuture<Channel> getChannel(M message) {
        final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();
        //Retrieve our channel pool based on the message
        final ChannelPool pool = poolMap.get(message);
        //Acquire a channel from the pool and listen for completion
        pool.acquire().addListener((Future<Channel> future) -> {
            if (future.isSuccess()) {
                Channel channel = future.getNow();
                channel.attr(SourceChannelAttributes.CHANNEL_POOL).set(pool);
                channelFuture.complete(channel);
            } else {
                channelFuture.completeExceptionally(future.cause());
            }
        });
        return channelFuture;
    }

    @Override
    public Void cleanupChannel(Channel c) {
        //Release channel from the pool
        if (c.hasAttr(SourceChannelAttributes.CHANNEL_POOL)) {
            c.attr(SourceChannelAttributes.CHANNEL_POOL).get().release(c);
        }
        return null;
    }

    private void onChannelAcquire(Channel ch) {
        //no implementation
    }

    private void onChannelRelease(Channel ch) {
        //no implementation
    }

    private void onChannelCreate(Channel ch) {
        getChannelInitializer().initializeChannel(ch, this);
    }

    public ChannelPoolHandler getChannelPoolHandler() {
        return channelPoolHandler;
    }

    /**
     * Creates a key from the {@link com.ribasco.rglib.core.AbstractMessage} provided which will be used in the pool map
     *
     * @param message
     *
     * @return The resolved key from the message
     */
    public abstract K createKey(M message);

    /**
     * @param key
     *
     * @return
     */
    public abstract ChannelPool createChannelPool(K key);
}
