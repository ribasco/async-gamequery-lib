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
import com.ibasco.agql.core.util.Options;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface NettyChannelFactory extends Closeable {

    CompletableFuture<Channel> create(final Object data);

    CompletableFuture<Channel> create(final Object data, EventLoop eventLoop);

    TransportType getTransportType();

    NettyChannelInitializer getChannelInitializer();

    void setChannelInitializer(NettyChannelInitializer channelInitializer);

    NettyPropertyResolver getResolver();

    EventLoopGroup getExecutor();

    Bootstrap getBootstrap();

    Options getOptions();
}
