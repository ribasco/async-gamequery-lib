/*
 * Copyright 2022-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport.udp;

import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory;
import com.ibasco.agql.core.transport.pool.PooledNettyChannelFactory;
import com.ibasco.agql.core.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UDP Channel factory with connection pooling support
 *
 * @author Rafael Luis Ibasco
 */
public class UdpPooledNettyChannelFactory extends PooledNettyChannelFactory {

    private static final Logger log = LoggerFactory.getLogger(UdpPooledNettyChannelFactory.class);

    private final boolean connectionless;

    //TODO: Remove connectionless parameter
    public UdpPooledNettyChannelFactory(NettyChannelPoolFactory channelPoolFactory, boolean connectionless) {
        super(channelPoolFactory);
        this.connectionless = connectionless;
    }

    protected void releaseChannel(Channel channel, Promise<Void> promise) {
        //if channel is connectionless, then no need to disconnect on release
        if (connectionless)
            return;
        log.debug("{} POOL => Requesting to release channel", NettyUtil.id(channel));
        promise.addListener((FutureListener<Void>) future -> {
            log.debug("{} POOL => Channel released. Requesting to disconnect.", NettyUtil.id(channel));
            channel.disconnect().addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess())
                    log.debug("{} POOL => Successfully disconnected channel", NettyUtil.id(channel));
                else
                    log.debug("{} POOL => Failed to disconnect channel", NettyUtil.id(channel));
            });
        });
    }
}
