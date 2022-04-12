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

/**
 * <p>NettyChannelFactory interface.</p>
 *
 * @author Rafael Luis Ibasco
 */
public interface NettyChannelFactory extends Closeable {

    /**
     * <p>create.</p>
     *
     * @param data a {@link java.lang.Object} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Channel> create(final Object data);

    /**
     * <p>create.</p>
     *
     * @param data a {@link java.lang.Object} object
     * @param eventLoop a {@link io.netty.channel.EventLoop} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<Channel> create(final Object data, EventLoop eventLoop);

    /**
     * <p>getTransportType.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.enums.TransportType} object
     */
    TransportType getTransportType();

    /**
     * <p>getChannelInitializer.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelInitializer} object
     */
    NettyChannelInitializer getChannelInitializer();

    /**
     * <p>setChannelInitializer.</p>
     *
     * @param channelInitializer a {@link com.ibasco.agql.core.transport.NettyChannelInitializer} object
     */
    void setChannelInitializer(NettyChannelInitializer channelInitializer);

    /**
     * <p>getResolver.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     */
    NettyPropertyResolver getResolver();

    /**
     * <p>setResolver.</p>
     *
     * @param resolver a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     */
    void setResolver(NettyPropertyResolver resolver);

    /**
     * <p>getExecutor.</p>
     *
     * @return a {@link io.netty.channel.EventLoopGroup} object
     */
    EventLoopGroup getExecutor();

    /**
     * <p>getBootstrap.</p>
     *
     * @return a {@link io.netty.bootstrap.Bootstrap} object
     */
    Bootstrap getBootstrap();

    /**
     * <p>getOptions.</p>
     *
     * @return a {@link com.ibasco.agql.core.util.Options} object
     */
    Options getOptions();
}
