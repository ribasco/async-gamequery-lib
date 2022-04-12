/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport;

import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * A netty based TCP {@link io.netty.channel.Channel} factory
 *
 * @author Rafael Luis Ibasco
 */
public class TcpNettyChannelFactory extends AbstractNettyChannelFactory {

    /**
     * <p>Constructor for TcpNettyChannelFactory.</p>
     *
     * @param options a {@link com.ibasco.agql.core.util.Options} object
     */
    public TcpNettyChannelFactory(final Options options) {
        this(options, null);
    }

    /**
     * <p>Constructor for TcpNettyChannelFactory.</p>
     *
     * @param options a {@link com.ibasco.agql.core.util.Options} object
     * @param resolver a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     */
    public TcpNettyChannelFactory(final Options options, final NettyPropertyResolver resolver) {
        super(TransportType.TCP, options, resolver);
    }

    /** {@inheritDoc} */
    @Override
    protected void configureBootstrap(final Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, getOptions().getOrDefault(TransportOptions.SOCKET_KEEP_ALIVE));
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    /** {@inheritDoc} */
    @Override
    protected CompletableFuture<Channel> newChannel(final Object data) {
        final InetSocketAddress remoteAddress = getResolver().resolveRemoteAddress(data);
        final InetSocketAddress localAddress = getResolver().resolveLocalAddress(data);
        if (localAddress != null) {
            return Netty.toCompletable(getBootstrap().clone().connect(remoteAddress, localAddress));
        } else {
            return Netty.toCompletable(getBootstrap().clone().connect(remoteAddress));
        }
    }
}
