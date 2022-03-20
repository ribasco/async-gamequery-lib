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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Base decorator class meant to be overriden by concrete decorator classes.
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyChannelFactoryDecorator implements NettyChannelFactory {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelFactoryDecorator.class);

    private final NettyChannelFactory channelFactory;

    protected NettyChannelFactoryDecorator(final NettyChannelFactory channelFactory) {
        this.channelFactory = Objects.requireNonNull(channelFactory, "Channel factory must not be null");
    }

    @Override
    public CompletableFuture<Channel> create(Object data) {
        return this.channelFactory.create(data);
    }

    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return this.channelFactory.create(data, eventLoop);
    }

    @Override
    public EventLoopGroup getExecutor() {
        return this.channelFactory.getExecutor();
    }

    @Override
    public TransportType getTransportType() {
        return this.channelFactory.getTransportType();
    }

    @Override
    public NettyChannelInitializer getChannelInitializer() {
        return this.channelFactory.getChannelInitializer();
    }

    @Override
    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelFactory.setChannelInitializer(channelInitializer);
    }

    @Override
    public NettyPropertyResolver getResolver() {
        return this.channelFactory.getResolver();
    }

    @Override
    public Bootstrap getBootstrap() {
        return this.channelFactory.getBootstrap();
    }

    @Override
    public Options getOptions() {
        return this.channelFactory.getOptions();
    }

    @Override
    public void close() throws IOException {
        this.channelFactory.close();
    }
}
