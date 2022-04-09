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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * A netty based UDP {@link Channel} factory
 *
 * @author Rafael Luis Ibasco
 */
public class UdpNettyChannelFactory extends AbstractNettyChannelFactory {

    private final boolean connectionless;

    public UdpNettyChannelFactory(final Options options, final boolean connectionless) {
        this(options, null, connectionless);
    }

    public UdpNettyChannelFactory(final Options options, final NettyPropertyResolver resolver, final boolean connectionless) {
        super(TransportType.UDP, options, resolver);
        this.connectionless = connectionless;
    }

    @Override
    protected CompletableFuture<Channel> newChannel(final Object data) {
        final InetSocketAddress remoteAddress = getResolver().resolveRemoteAddress(data);
        final InetSocketAddress localAddress = getResolver().resolveLocalAddress(data);
        final Bootstrap bootstrap = getBootstrap().clone();
        //optional
        if (localAddress != null)
            bootstrap.localAddress(localAddress);
        else
            bootstrap.localAddress(0);
        bootstrap.remoteAddress(remoteAddress);
        return Netty.toCompletable((connectionless) ? bootstrap.bind() : bootstrap.connect());
    }
}
