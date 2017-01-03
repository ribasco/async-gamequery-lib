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

package com.ibasco.agql.core.transport.tcp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.enums.ChannelType;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.transport.NettyPooledTransport;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * <p>A Pooled TCP Transport implementation which creates and reuse channels stored within a pool map.
 * {@link InetSocketAddress} is used as the key for the {@link io.netty.channel.pool.ChannelPoolMap} implementation</p>
 *
 * @param <M> A type of {@link AbstractRequest} that will be used as a lookup reference for our key
 */
public class NettyPooledTcpTransport<M extends AbstractRequest> extends NettyPooledTransport<M, InetSocketAddress> {

    private static final Logger log = LoggerFactory.getLogger(NettyPooledTcpTransport.class);

    public NettyPooledTcpTransport(ChannelType channelType) {
        super(channelType);
    }

    @Override
    public InetSocketAddress createKey(M message) {
        return message.recipient();
    }

    @Override
    public ChannelPool createChannelPool(InetSocketAddress key) {
        return new SimpleChannelPool(getBootstrap().remoteAddress(key), channelPoolHandler);
    }

    @Override
    public CompletableFuture<Channel> getChannel(M message) {
        return super.getChannel(message).thenApply(c -> {
            c.attr(ChannelAttributes.LAST_REQUEST_SENT).set(message);
            return c;
        });
    }
}
