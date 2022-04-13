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

import com.ibasco.agql.core.enums.BufferAllocatorType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Base class for all {@link io.netty.channel.Channel} factories utilitizing netty's {@link io.netty.bootstrap.Bootstrap}
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

    private NettyPropertyResolver resolver;

    private final EventLoopGroup eventLoopGroup;

    private ExecutorService executorService;

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

    /**
     * <p>Constructor for AbstractNettyChannelFactory.</p>
     *
     * @param type
     *         a {@link com.ibasco.agql.core.transport.enums.TransportType} object
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param resolver
     *         a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     */
    protected AbstractNettyChannelFactory(final TransportType type, final Options options, final NettyPropertyResolver resolver) {
        this(type, options, resolver, null);
    }

    /**
     * <p>Constructor for AbstractNettyChannelFactory.</p>
     *
     * @param type
     *         a {@link com.ibasco.agql.core.transport.enums.TransportType} object
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param resolver
     *         a {@link com.ibasco.agql.core.transport.NettyPropertyResolver} object
     * @param initializer
     *         a {@link com.ibasco.agql.core.transport.NettyChannelInitializer} object
     */
    protected AbstractNettyChannelFactory(final TransportType type, final Options options, final NettyPropertyResolver resolver, final NettyChannelInitializer initializer) {
        this.transportType = type;
        this.options = options;
        this.resolver = resolver == null ? DefaultPropertyResolver.INSTANCE : resolver;
        this.channelClass = Platform.getChannelClass(type);

        //initialize event loop group
        this.executorService = options.get(TransportOptions.THREAD_EXECUTOR_SERVICE);
        if (executorService == null)
            executorService = Platform.getDefaultExecutor();
        this.eventLoopGroup = initializeEventLoopGroup(channelClass, executorService);
        this.channelInitializer = initializer == null ? new NettyChannelInitializer() : initializer;

        initializeBootstrap();
    }
    //</editor-fold>

    /**
     * <p>Initialize {@link io.netty.channel.EventLoopGroup}.</p>
     *
     * @param channelClass
     *         a {@link java.lang.Class} object
     * @param executorService
     *         a {@link java.util.concurrent.ExecutorService} object
     *
     * @return a {@link io.netty.channel.EventLoopGroup} object
     */
    protected EventLoopGroup initializeEventLoopGroup(@NotNull Class<? extends Channel> channelClass, @NotNull ExecutorService executorService) {
        Integer nThreads = getOptions().get(TransportOptions.THREAD_CORE_SIZE);
        EventLoopGroup group;
        //1. if the executor service is the default global executor, then we simply return the default global EventLoopGroup
        //2. if the provided executor service is user-defined, then we create a new EventLoopGroup instance
        if (Platform.isDefaultExecutor(executorService)) {
            group = Platform.getDefaultEventLoopGroup();
        } else {
            //since we are dealing with a user provided executor service the option 'TransportOptions.THREAD_CORE_SIZE' is required
            // unless we are able to automatically determine it's core pool size
            //Attempt to determine the number of threads supported by the executor service
            if (nThreads == null) {
                if (executorService instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
                    nThreads = tpe.getCorePoolSize();
                } else if (executorService instanceof AgqlManagedExecutorService) {
                    ThreadPoolExecutor tpe = ((AgqlManagedExecutorService) executorService).getResource();
                    nThreads = tpe.getCorePoolSize();
                } else {
                    throw new IllegalStateException("Please specify the core pool size for the  (See TransportOptions.THREAD_CORE_SIZE)");
                }
            }
            group = Platform.createEventLoopGroup(channelClass, executorService, nThreads);
        }

        log.debug("CHANNEL_FACTORY (INIT) => Channel Class '{}'", channelClass);
        log.debug("CHANNEL_FACTORY (INIT) => Executor Service: '{}'", executorService);
        log.debug("CHANNEL_FACTORY (INIT) => Event Loop Group: '{}' (Event Loop Threads: {})", group, nThreads);
        return group;
    }

    /**
     * <p>newChannel.</p>
     *
     * @param data
     *         a {@link java.lang.Object} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    abstract protected CompletableFuture<Channel> newChannel(final Object data);

    /**
     * <p>configureBootstrap.</p>
     *
     * @param bootstrap
     *         a {@link io.netty.bootstrap.Bootstrap} object
     */
    protected void configureBootstrap(Bootstrap bootstrap) {}

    /** {@inheritDoc} */
    @Override
    public final CompletableFuture<Channel> create(final Object data) {
        return newChannel(data);
    }

    /** {@inheritDoc} */
    @Override
    public final CompletableFuture<Channel> create(final Object data, EventLoop eventLoop) {
        if (eventLoop == null)
            return create(data);
        return Netty.useEventLoop(newChannel(data), eventLoop);
    }

    //<editor-fold desc="Public Methods">

    /** {@inheritDoc} */
    @Override
    public TransportType getTransportType() {
        return transportType;
    }

    /** {@inheritDoc} */
    @Override
    public NettyChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    /** {@inheritDoc} */
    @Override
    public void setChannelInitializer(NettyChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    /** {@inheritDoc} */
    @Override
    public NettyPropertyResolver getResolver() {
        return resolver;
    }

    /** {@inheritDoc} */
    @Override
    public void setResolver(NettyPropertyResolver resolver) {
        this.resolver = resolver;
    }

    /** {@inheritDoc} */
    @Override
    public EventLoopGroup getExecutor() {
        return eventLoopGroup;
    }

    /** {@inheritDoc} */
    @Override
    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    /** {@inheritDoc} */
    @Override
    public Options getOptions() {
        return options;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        //if the executor service is a managed resource, attempt to release it
        ManagedResource.release(executorService);

        if (Concurrency.shutdown(eventLoopGroup, options.getOrDefault(TransportOptions.CLOSE_TIMEOUT), TimeUnit.MILLISECONDS)) {
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

    private void configureDefaultOptions() {
        //Default channel options
        bootstrap.option(ChannelOption.SO_SNDBUF, getOptions().getOrDefault(TransportOptions.SOCKET_SNDBUF))
                 .option(ChannelOption.SO_RCVBUF, getOptions().getOrDefault(TransportOptions.SOCKET_RECVBUF))
                 .option(ChannelOption.RCVBUF_ALLOCATOR, createRecvByteBufAllocator())
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

    /**
     * <p>createRecvByteBufAllocator.</p>
     *
     * @return a {@link io.netty.channel.RecvByteBufAllocator} object
     */
    protected RecvByteBufAllocator createRecvByteBufAllocator() {
        BufferAllocatorType allocatorType = getOptions().getOrDefault(TransportOptions.SOCKET_RECVBUF_ALLOC_TYPE);
        log.debug("[INIT] Using a receive buffer allocator type of '{}'", allocatorType);
        switch (allocatorType) {
            case ADAPTIVE: {
                int initSize = getOptions().getOrDefault(TransportOptions.SOCKET_ALLOC_ADAPTIVE_INIT_SIZE);
                int minSize = getOptions().getOrDefault(TransportOptions.SOCKET_ALLOC_ADAPTIVE_MIN_SIZE);
                int maxSize = getOptions().getOrDefault(TransportOptions.SOCKET_ALLOC_ADAPTIVE_MAX_SIZE);
                log.debug("[INIT] Adaptive Allocator Parameters (Init Size: {}, Min Size: {}, Max Size: {})", initSize, minSize, maxSize);
                return new AdaptiveRecvByteBufAllocator(minSize, initSize, maxSize);
            }
            case FIXED: {
                int fixedSize = getOptions().getOrDefault(TransportOptions.SOCKET_ALLOC_FIXED_SIZE);
                log.debug("[INIT] Fixed Allocator Parameters (Size: {})", fixedSize);
                return new FixedRecvByteBufAllocator(fixedSize);
            }
            default: {
                throw new IllegalStateException("Invalid allocator type");
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
