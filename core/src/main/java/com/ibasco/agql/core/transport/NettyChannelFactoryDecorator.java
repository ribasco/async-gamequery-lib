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
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base decorator class meant to be overriden by concrete decorator classes.
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyChannelFactoryDecorator implements NettyChannelFactory {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelFactoryDecorator.class);

    private final NettyChannelFactory channelFactory;

    /**
     * <p>Constructor for NettyChannelFactoryDecorator.</p>
     *
     * @param channelFactory
     *         a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    protected NettyChannelFactoryDecorator(final NettyChannelFactory channelFactory) {
        this.channelFactory = Objects.requireNonNull(channelFactory, "Channel factory must not be null");
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data) {
        return this.channelFactory.create(data);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Channel> create(Object data, EventLoop eventLoop) {
        return this.channelFactory.create(data, eventLoop);
    }

    /** {@inheritDoc} */
    @Override
    public TransportType getTransportType() {
        return this.channelFactory.getTransportType();
    }

    /** {@inheritDoc} */
    @Override
    public NettyChannelInitializer getChannelInitializer() {
        return this.channelFactory.getChannelInitializer();
    }

    /** {@inheritDoc} */
    @Override
    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelFactory.setChannelInitializer(channelInitializer);
    }

    /** {@inheritDoc} */
    @Override
    public NettyPropertyResolver getResolver() {
        return this.channelFactory.getResolver();
    }

    /** {@inheritDoc} */
    @Override
    public void setResolver(NettyPropertyResolver resolver) {
        this.channelFactory.setResolver(resolver);
    }

    /** {@inheritDoc} */
    @Override
    public EventLoopGroup getExecutor() {
        return this.channelFactory.getExecutor();
    }

    /** {@inheritDoc} */
    @Override
    public Bootstrap getBootstrap() {
        return this.channelFactory.getBootstrap();
    }

    /** {@inheritDoc} */
    @Override
    public Options getOptions() {
        return this.channelFactory.getOptions();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        this.channelFactory.close();
    }
}
