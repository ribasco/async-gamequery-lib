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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A {@link NettyChannelFactory} which makes use of netty's {@link Bootstrap} class to create {@link Channel} instances
 *
 * @author Rafael Luis Ibasco
 */
abstract public class BootstrapNettyChannelFactory implements NettyChannelFactory {

    private static final Logger log = LoggerFactory.getLogger(BootstrapNettyChannelFactory.class);

    private final Bootstrap bootstrap = new Bootstrap();

    private final ChannelHandler channelInitializer;

    private final EventLoopGroup eventLoopGroup;

    private final Options options;

    private final TransportType transportType;

    private final NettyChannelHandlerInitializer channelHandlerInitializer;

    protected BootstrapNettyChannelFactory(final TransportType type, final NettyChannelHandlerInitializer handlerInitializer, final Options options) {
        final Class<? extends Channel> channelClass = Platform.getChannelClass(type, options.getOrDefault(TransportOptions.USE_NATIVE_TRANSPORT));
        this.options = options;
        this.transportType = type;
        this.channelHandlerInitializer = handlerInitializer;
        this.channelInitializer = new DefaultNettyChannelInitializer<>(handlerInitializer);
        this.eventLoopGroup = initializeEventLoopGroup();
        initializeBootstrap(channelClass, this.eventLoopGroup, this.channelInitializer);
    }

    private void initializeBootstrap(Class<? extends Channel> channelClass, EventLoopGroup eventLoopGroup, ChannelHandler defaultChannelHandler) {
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Initializing Bootstrap");
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Channel Class '{}'", channelClass.getSimpleName());
        this.bootstrap.channel(channelClass);
        this.bootstrap.group(eventLoopGroup);
        this.bootstrap.handler(defaultChannelHandler);

        configureDefaultOptions(this.bootstrap);
        configureDefaultAttributes(this.bootstrap);
        configure(this.bootstrap);
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Successfully Initialized Bootstrap (Event Loop Group: '{}', Channel Class: '{}', Default Channel Handler: '{}')", eventLoopGroup.getClass().getSimpleName(), channelClass.getSimpleName(), defaultChannelHandler);
    }

    private EventLoopGroup initializeEventLoopGroup() {
        EventLoopGroup eventLoopGroup = getOptions().getOrDefault(TransportOptions.THREAD_EL_GROUP);
        Integer elThreadSize = null;
        //use a shared instance of event loop group by default
        if (eventLoopGroup == null) {
            eventLoopGroup = Platform.getDefaultEventLoopGroup();
            elThreadSize = Platform.getDefaultPoolSize();
            log.debug("[INIT] CHANNEL_FACTORY => Using default global event loop group with a a limit of '{}' thread(s) (Instance: '{}')", elThreadSize, eventLoopGroup);
        }
        log.debug("[INIT] CHANNEL_FACTORY => Event Loop Group '{}' (Max Threads: {}, Instance: {})", eventLoopGroup, elThreadSize == null ? "N/A" : elThreadSize, eventLoopGroup.hashCode());
        return eventLoopGroup;
    }

    private void configureDefaultOptions(Bootstrap bootstrap) {
        //Default channel options
        bootstrap.option(ChannelOption.SO_SNDBUF, getOptions().getOrDefault(TransportOptions.SOCKET_SNDBUF))
                 .option(ChannelOption.SO_RCVBUF, getOptions().getOrDefault(TransportOptions.SOCKET_RECVBUF))
                 .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(9216))
                 .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                 .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                 .option(ChannelOption.AUTO_READ, true)
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getOptions().getOrDefault(TransportOptions.SOCKET_CONNECT_TIMEOUT));

        if (log.isDebugEnabled()) {
            int ctr = 0;
            log.debug("===================================================================================================================");
            log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Default Channel Options");
            log.debug("===================================================================================================================");
            for (Map.Entry<ChannelOption<?>, ?> option : bootstrap.config().options().entrySet()) {
                log.debug("[INIT] TRANSPORT (BOOTSTRAP) => ({}) Channel Option: '{}' (Value: {})", ++ctr, option.getKey().name(), option.getValue());
            }
        }
    }

    private void configureDefaultAttributes(Bootstrap bootstrap) {
        //Global default attributes
        bootstrap.attr(NettyChannelAttributes.AUTO_RELEASE, true);

        int ctr = 0;
        log.debug("===================================================================================================================");
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Auto initializing channel attributes whose autoCreate flag is set");
        log.debug("===================================================================================================================");
        if (Option.getOptions().size() > 0) {
            for (Option<?> option : Option.getOptions()) {
                if (option.isChannelAttribute() && option.isAutoCreate()) {
                    log.debug("[INIT] TRANSPORT (BOOTSTRAP) => ({}) Attribute: '{}' (Default Value: {})", ++ctr, option.getKey(), option.getDefaultValue());
                    //noinspection unchecked
                    bootstrap.attr((AttributeKey<Object>) option.toAttributeKey(), option.getDefaultValue());
                }
            }
        } else {
            log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Automatic channel attributes available");
        }

        ctr = 0;
        log.debug("===================================================================================================================");
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Populating default channel attributes (explicitly set by client/messenger)");
        log.debug("===================================================================================================================");
        if (getOptions().size() > 0) {
            for (Map.Entry<Option<?>, Object> entry : getOptions()) {
                Option<?> option = entry.getKey();
                Object value = entry.getValue();
                if (option.isChannelAttribute()) {
                    log.debug("[INIT] TRANSPORT (BOOTSTRAP) => ({}) Attribute: '{}' = '{}' (Default: {})", ++ctr, option.getKey(), value, option.getDefaultValue());
                    //noinspection unchecked
                    bootstrap.attr((AttributeKey<Object>) option.toAttributeKey(), value);
                }
            }
        } else {
            log.debug("[INIT] TRANSPORT (BOOTSTRAP) => No default channel attributes available");
        }
        log.debug("===================================================================================================================");
    }

    protected void configure(final Bootstrap bootstrap) {
        //no-operation
    }

    @Override
    abstract public CompletableFuture<Channel> create(Envelope<? extends AbstractRequest> envelope);

    @Override
    public EventLoopGroup getExecutor() {
        return eventLoopGroup;
    }

    public final Bootstrap getBootstrap() {
        return bootstrap;
    }

    public final Options getOptions() {
        return options;
    }

    @Override
    public void close() throws IOException {
        if (ConcurrentUtil.shutdown(eventLoopGroup, options.getOrDefault(TransportOptions.CLOSE_TIMEOUT), TimeUnit.MILLISECONDS)) {
            log.debug("TRANSPORT (CLOSE) => Transport closed gracefully");
        } else {
            log.debug("TRANSPORT (CLOSE) => Shutdown interrupted");
        }
    }
}
