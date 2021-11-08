/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

package com.ibasco.agql.core.transport.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.pool.*;

import java.net.SocketAddress;

/**
 * The default {@link ChannelPoolMap} implementation using a {@link FixedChannelPool}. A new channel is acquired/created for each remote address.
 *
 * @author Rafael Luis Ibasco
 */
public class DefaultChannelPoolMap extends AbstractChannelPoolMap<SocketAddress, ChannelPool> {

    private final Bootstrap bootstrap;

    private final ChannelPoolHandler channelPoolHandler;

    public DefaultChannelPoolMap(Bootstrap bootstrap, ChannelPoolHandler channelPoolHandler) {
        this.bootstrap = bootstrap;
        this.channelPoolHandler = channelPoolHandler;
    }

    @Override
    protected ChannelPool newPool(SocketAddress key) {
        return new FixedChannelPool(
                bootstrap.remoteAddress(key),
                channelPoolHandler,
                ChannelHealthChecker.ACTIVE,
                FixedChannelPool.AcquireTimeoutAction.NEW,
                1000,
                100, //per my test, source capped the max number of queries to 100
                Integer.MAX_VALUE,
                true);
    }
}
