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

package com.ibasco.agql.core.transport.udp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.OptionMap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

/**
 * A netty based UDP {@link Channel} factory
 *
 * @author Rafael Luis Ibasco
 */
public class UdpNettyChannelFactory extends NettyChannelFactory {

    private final boolean connectionless;

    public UdpNettyChannelFactory(Bootstrap bootstrap, OptionMap options, boolean connectionless) {
        super(bootstrap, options);
        this.connectionless = connectionless;
    }

    @Override
    public CompletableFuture<Channel> create(Envelope<? extends AbstractRequest> envelope) {
        Bootstrap bootstrap = getBootstrap().clone().localAddress(envelope.sender()).remoteAddress(envelope.recipient());
        return NettyUtil.makeCompletable((connectionless) ? bootstrap.bind() : bootstrap.connect());
    }
}
