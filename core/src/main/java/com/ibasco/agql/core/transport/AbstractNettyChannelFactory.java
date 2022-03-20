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
 * Base class for all {@link Channel} factories utilitizing netty's {@link Bootstrap}
 *
 * @author Rafael Luis Ibasco
 */
abstract public class AbstractNettyChannelFactory implements NettyChannelFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractNettyChannelFactory.class);

    //<editor-fold desc="Private Members">
    private final Bootstrap bootstrap = new Bootstrap();

    private final Options options;

    private final TransportType transportType;

    private final Class<? extends Channel> channelClass;

    private final NettyPropertyResolver resolver;

    private EventLoopGroup eventLoopGroup;

    private NettyChannelInitializer channelInitializer;

    private final ChannelFactory<Channel> DEFAULT_CHANNEL_FACTORY = new ChannelFactory<Channel>() {

        private ReflectiveChannelFactory<? extends Channel> factory;

        @Override
        public Channel newChannel() {
            if (factory == null)
                factory = new ReflectiveChannelFactory<>(channelClass);
            return factory.newChannel();
        }
    };
    //</editor-fold>

    //<editor-fold desc="Constructor">
    protected AbstractNettyChannelFactory(final TransportType type, final Options options, final NettyPropertyResolver resolver) {
        this(type, options, resolver, null);
    }

    protected AbstractNettyChannelFactory(final TransportType type, final Options options, final NettyPropertyResolver resolver, final NettyChannelInitializer initializer) {
        this.transportType = type;
        this.options = options;
        this.resolver = resolver == null ? DefaultPropertyResolver.INSTANCE : resolver;
        this.channelClass = Platform.getChannelClass(type, options.getOrDefault(TransportOptions.USE_NATIVE_TRANSPORT));
        this.channelInitializer = initializer == null ? new NettyChannelInitializer() : initializer;
        initialize();
    }
    //</editor-fold>

    abstract protected CompletableFuture<Channel> newChannel(final Object data);

    protected void configureBootstrap(Bootstrap bootstrap) {}

    protected void initialize() {
        //Initialize event loop group
        this.eventLoopGroup = newEventLoopGroup();
        //Initialize bootstrap
        initializeBootstrap();
    }

    @Override
    public final CompletableFuture<Channel> create(final Object data) {
        return newChannel(data);
    }

    @Override
    public final CompletableFuture<Channel> create(final Object data, EventLoop eventLoop) {
        if (eventLoop == null)
            return create(data);
        return NettyUtil.useEventLoop(newChannel(data), eventLoop);
    }

    //<editor-fold desc="Public Methods">
    @Override
    public TransportType getTransportType() {
        return transportType;
    }

    @Override
    public NettyChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    @Override
    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public NettyPropertyResolver getResolver() {
        return resolver;
    }

    @Override
    public EventLoopGroup getExecutor() {
        return eventLoopGroup;
    }

    @Override
    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public Options getOptions() {
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
    //</editor-fold>

    //<editor-fold desc="Private and Protected Methods">
    private void initializeBootstrap() {
        assert eventLoopGroup != null;

        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Initializing Bootstrap");
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Channel Class '{}'", channelClass.getSimpleName());
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Channel Factory: '{}'", DEFAULT_CHANNEL_FACTORY);
        this.bootstrap.channelFactory(DEFAULT_CHANNEL_FACTORY);
        this.bootstrap.group(eventLoopGroup);
        this.bootstrap.handler(channelInitializer);

        configureDefaultOptions();
        configureDefaultAttributes();
        configureBootstrap(this.bootstrap);
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Successfully Initialized Bootstrap (Event Loop Group: '{}', Channel Class: '{}', Default Channel Handler: '{}')", eventLoopGroup.getClass().getSimpleName(), channelClass.getSimpleName(), bootstrap.config().handler());
    }

    protected EventLoopGroup newEventLoopGroup() {
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

    private void configureDefaultOptions() {
        //Default channel options
        bootstrap.option(ChannelOption.SO_SNDBUF, getOptions().getOrDefault(TransportOptions.SOCKET_SNDBUF))
                 .option(ChannelOption.SO_RCVBUF, getOptions().getOrDefault(TransportOptions.SOCKET_RECVBUF))
                 .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator()) //new FixedRecvByteBufAllocator(9216)
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

    private void configureDefaultAttributes() {
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
    //</editor-fold>
}
