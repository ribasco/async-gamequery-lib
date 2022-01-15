/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.transport.tcp;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.BootstrapNettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelHandlerInitializer;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Options;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A netty based TCP {@link Channel} factory
 *
 * @author Rafael Luis Ibasco
 */
public class TcpNettyChannelFactory extends BootstrapNettyChannelFactory {

    public TcpNettyChannelFactory(NettyChannelHandlerInitializer initializer, Options options) {
        super(TransportType.TCP, initializer, options);
    }

    @Override
    protected void configure(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public CompletableFuture<Channel> create(Envelope<? extends AbstractRequest> envelope) {
        Objects.requireNonNull(envelope, "Envelope is null");
        return NettyUtil.makeCompletable(getBootstrap().clone().connect(envelope.recipient(), envelope.sender()));
    }
}
