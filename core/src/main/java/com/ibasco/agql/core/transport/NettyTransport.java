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
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory;
import com.ibasco.agql.core.util.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Base class for all classee who would like to utilize the netty framework
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyTransport implements Transport<Channel, Envelope<AbstractRequest>> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    //<editor-fold desc="Private Members">
    private final ChannelHandler defaultChannelHandler;

    private final OptionMap options;

    private final Class<? extends Channel> channelClass;

    private final EventLoopGroup eventLoopGroup;

    private final Bootstrap bootstrap = new Bootstrap();

    private ChannelFactory<Channel> channelFactory;

    private Executor executor;
    //</editor-fold>

    //TODO: Move all bootstrap code to NettyChannelFactory
    //<editor-fold desc="Default Constructor">
    protected NettyTransport(final Class<? extends Channel> channelClass, final NettyChannelHandlerInitializer channelHandlerInitializer, final OptionMap options) {
        Objects.requireNonNull(channelHandlerInitializer, "Default channel handler cannot be null");

        this.options = Objects.requireNonNull(options, "[INIT] TRANSPORT => Missing options");
        this.channelClass = Objects.requireNonNull(channelClass, "[INIT] TRANSPORT => Missing channel class");
        this.defaultChannelHandler = new DefaultNettyChannelInitializer<>(channelHandlerInitializer);
        //Set resource leak detection if debugging is enabled
        if (log.isDebugEnabled())
            ResourceLeakDetector.setLevel(getOrDefault(TransportOptions.RESOURCE_LEAK_DETECTOR_LEVEL));

        this.eventLoopGroup = initializeEventLoopGroup();
        initializeBootstrap();
    }
    //</editor-fold>

    @Override
    public final void initialize() {
        log.debug("[INIT] TRANSPORT => Initialize");
        this.channelFactory = initializeChannelFactory();
    }

    private ChannelFactory<Channel> initializeChannelFactory() {
        boolean pooled = getOrDefault(TransportOptions.CONNECTION_POOLING);
        log.debug("[INIT] TRANSPORT => Initializing Channel Factory (Pooling enabled: {})", pooled);
        ChannelFactory<Channel> factory = newChannelFactory(this.bootstrap, pooled);
        log.debug("[INIT] TRANSPORT => Using channel factory '{}' (Pooling enabled: {})", factory.getClass().getSimpleName(), pooled);
        return Objects.requireNonNull(factory, "Channel factory is null");
    }

    private EventLoopGroup initializeEventLoopGroup() {
        //final int elThreadSize = getOrDefault(TransportOptions.THREAD_EL_SIZE);
        EventLoopGroup eventLoopGroup = getOrDefault(TransportOptions.THREAD_EL_GROUP);
        Integer elThreadSize = null;
        //use a shared instance of event loop group by default
        if (eventLoopGroup == null) {
            eventLoopGroup = Platform.getDefaultEventLoopGroup();
            elThreadSize = Platform.getDefaultPoolSize();
            log.debug("[INIT] TRANSPORT => Using default global event loop group with a a limit of '{}' thread(s) (Instance: '{}')", elThreadSize, eventLoopGroup);
        }
        log.debug("[INIT] TRANSPORT => Event Loop Group '{}' (Max Threads: {}, Instance: {})", eventLoopGroup, elThreadSize == null ? "N/A" : elThreadSize, eventLoopGroup.hashCode());
        return eventLoopGroup;
    }

    private void initializeBootstrap() {
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Initializing Bootstrap");
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Channel Class '{}'", channelClass.getSimpleName());
        bootstrap.channel(this.channelClass);
        bootstrap.group(this.eventLoopGroup);
        bootstrap.handler(this.defaultChannelHandler);

        configureDefaultOptions(bootstrap);
        configureDefaultAttributes(bootstrap);
        configureBootstrap(bootstrap);
        log.debug("[INIT] TRANSPORT (BOOTSTRAP) => Successfully Initialized Bootstrap (Event Loop Group: '{}', Channel Class: '{}')", eventLoopGroup.getClass().getSimpleName(), channelClass.getSimpleName());
    }

    private void configureDefaultOptions(Bootstrap bootstrap) {
        //Default channel options
        bootstrap.option(ChannelOption.SO_SNDBUF, getOrDefault(TransportOptions.SOCKET_SNDBUF))
                 .option(ChannelOption.SO_RCVBUF, getOrDefault(TransportOptions.SOCKET_RECVBUF))
                 .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(9216))
                 .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                 .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                 .option(ChannelOption.AUTO_READ, true)
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getOrDefault(TransportOptions.SOCKET_CONNECT_TIMEOUT));
        //.option(ChannelOption.SO_REUSEADDR, true);

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
        bootstrap.attr(ChannelAttributes.REPORT_INCOMPLETE_PACKET, getOrDefault(TransportOptions.REPORT_INCOMPLETE_PACKET));
        bootstrap.attr(ChannelAttributes.READ_TIMEOUT, getOrDefault(TransportOptions.READ_TIMEOUT));
        bootstrap.attr(ChannelAttributes.WRITE_TIMEOUT, getOrDefault(TransportOptions.WRITE_TIMEOUT));
        bootstrap.attr(ChannelAttributes.AUTO_RELEASE, true);

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

    protected void configureBootstrap(final Bootstrap bootstrap) {
        //no-operation
    }

    @Override
    public final CompletableFuture<Channel> send(final Envelope<AbstractRequest> envelope) {
        return send(envelope, null);
    }

    public final CompletableFuture<Channel> send(final Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> channelFuture) {
        //fail-fast
        if (envelope == null)
            throw new IllegalStateException("Envelope is null");
        if (envelope.content() == null)
            throw new IllegalStateException("Request is null");
        channelFuture = channelFuture == null ? newChannel(envelope) : channelFuture;
        return prepare(envelope, channelFuture).thenCompose(this::write).handle(this::finalize);
    }

    /**
     * Wraps the request to a {@link Parcel} instance and attaches a newly acquired/created {@link Channel} to it
     *
     * @param envelope
     *         The {@link Envelope} containing the message to send to the transport's pipeline
     * @param channelFuture
     *         The {@link Channel} to use for delivering the message. {@code null} to acquire a new/exisitng channeel from the {@link NettyChannelPoolFactory}
     *
     * @return A {@link CompletableFuture} that will be notified once the initialization is complete. Note: This will never complete exceptionally. Errors are caught and stored inside the parcel. Use {@link Parcel#hasError()} to check for errors.
     */
    private CompletableFuture<Parcel> prepare(final Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> channelFuture) {
        if (channelFuture == null)
            throw new IllegalStateException("A channel future was not provided");
        final Parcel parcel = new Parcel(envelope);
        log.debug("{} TRANSPORT => Preparing transport parcel for envelope: '{}' (Channel: {})", NettyUtil.id(envelope), envelope, channelFuture);
        try {
            return channelFuture.thenCombine(CompletableFuture.completedFuture(parcel), parcel::attach).exceptionally(parcel::error);
        } catch (Exception e) {
            //we return a completed future to ensure that the last handler in the chain still receives a parcel instance
            return CompletableFuture.completedFuture(parcel.error(e));
        }
    }

    /**
     * Send the parcel's message to the pipeline. The returned {@link CompletableFuture} will never complete exceptionally.
     * Use {@link Parcel#hasError()} to check if the write operation failed.
     *
     * @param parcel
     *         The {@link Parcel} to send
     *
     * @return A {@link CompletableFuture} which will be notified once the parcel's message has been successfully delivered through its channel's pipeline
     */
    private CompletableFuture<Parcel> write(final Parcel parcel) {
        log.debug("{} TRANSPORT => Writing parcel to channel", NettyUtil.id(parcel.channel));

        Objects.requireNonNull(parcel, "Parcel is null");
        //Fail fast
        if (!parcel.hasChannel())
            throw new IllegalStateException("No channel is attached to the parcel");

        //if parcel is in error, return immediately
        if (parcel.hasError()) {
            log.debug("{} Parcel is currently in-error. Returning completed future (Error: {}).", NettyUtil.id(parcel.channel), parcel.error.getMessage());
            return CompletableFuture.completedFuture(parcel);
        }

        CompletableFuture<Parcel> promise = new CompletableFuture<>();
        if (parcel.channel().eventLoop().inEventLoop()) {
            write0(parcel, promise);
        } else {
            parcel.channel().eventLoop().execute(() -> write0(parcel, promise));
        }
        return promise.exceptionally(parcel::error);
    }

    private void write0(final Parcel parcel, CompletableFuture<Parcel> promise) {
        checkParcel(parcel);
        assert parcel.channel().eventLoop().inEventLoop();

        try {
            Channel channel = parcel.channel();
            ChannelFuture writeFuture = channel.writeAndFlush(parcel.envelope());
            if (writeFuture.isDone()) {
                if (!writeFuture.isSuccess()) {
                    parcel.error(writeFuture.cause());
                    log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", NettyUtil.id(channel), parcel.error());
                } else
                    log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline", NettyUtil.id(channel));
                if (!promise.complete(parcel))
                    log.warn("{} TRANSPORT => Failed to mark write promise as completed (Reason: Already completed, Promise: {})", NettyUtil.id(channel), promise);
            } else {
                writeFuture.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess())
                        log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline ({})", NettyUtil.id(channel), channel.pipeline().names().size());
                    else {
                        //catch any error(s) that occured during processing of an outbound request from the channel's pipeline
                        parcel.error(future.cause());
                        log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", NettyUtil.id(channel), future.cause());
                    }
                    promise.complete(parcel); //don't complete exceptionally
                });
            }
        } catch (Throwable e) {
            log.debug("{} TRANSPORT => Error occured during write operation", NettyUtil.id(parcel.channel()), e);
            if (!promise.isDone()) {
                promise.complete(parcel.error(e));
            }
        }
    }

    /**
     * Perform clean-up operations after write. If a parcel was in-error, the exception will be propagated down the chain.
     *
     * @param parcel
     *         The {@link Parcel} containing transport data
     * @param error
     *         Error that occured during send. {@code null} if no error occured.
     *
     * @return Thee {@link Channel} that was used for the write operation
     */
    private Channel finalize(Parcel parcel, Throwable error) {
        try {
            //check error
            Throwable cError = (parcel != null && parcel.hasError()) ? parcel.error() : error;
            log.debug("TRANSPORT (FINALIZE) => Parcel (Has error: {})", cError != null);
            if (cError != null)
                throw new TransportWriteException(String.format("An error occured while trying to send parcel over transport (Parcel: %s)", parcel), cError);
            assert parcel != null;
            assert parcel.hasChannel();
            //If no error, return the parcel's channel
            return parcel.channel();
        } finally {
            cleanup(parcel);
        }
    }

    private void cleanup(Parcel parcel) {
        if (parcel == null)
            return;
        try {
            //if an error occured before it reached the pipeline and the channel is pooled,
            // make sure we release it back to the pool
            if (parcel.hasError()) {
                //If we have acquired a channel, make sure we release it
                if (parcel.hasChannel()) {
                    final Channel channel = parcel.channel();
                    if (NettyUtil.isPooled(channel)) {
                        log.debug("{} TRANSPORT (CLEANUP) => Found error ({}) during parcel send. Requesting to release channel from pool", NettyUtil.id(channel), parcel.error().getClass().getSimpleName());
                        NettyUtil.release(channel);
                    } else {
                        log.debug("{} TRANSPORT (CLEANUP) => Channel is not pooled. Closing connection.", NettyUtil.id(channel));
                        channel.close();
                    }
                } else {
                    //if no channel was acquired, notify the client regarding the failure
                    assert parcel.envelope() != null;
                    if (!parcel.envelope().isCompleted()) {
                        if (parcel.envelope().promise().completeExceptionally(parcel.error())) {
                            log.debug("[N/A] TRANSPORT (CLEANUP) => Notified '{}' regarding exception", parcel.envelope());
                        } else {
                            log.debug("[N/A] TRANSPORT (CLEANUP) => Failed to notify '{}' ({})", parcel.envelope(), parcel.envelope().promise());
                        }
                    } else {
                        log.debug("[N/A] TRANSPORT (CLEANUP) => Did not complete. Promise has already been notifieed '{}'", parcel.envelope().promise());
                    }
                }
            } else {
                log.debug("{} TRANSPORT (CLEANUP) => Parcel successfully sent via transport", NettyUtil.id(parcel.channel()));
            }
        } finally {
            //release parcel
            log.debug("{} TRANSPORT (CLEANUP) => Released parcel: {}", NettyUtil.id(parcel.channel()), parcel);
            parcel.release();
        }
    }

    @Override
    public void close() {
        if (ConcurrentUtil.shutdown(eventLoopGroup, getOrDefault(TransportOptions.CLOSE_TIMEOUT), TimeUnit.MILLISECONDS)) {
            log.debug("TRANSPORT (CLOSE) => Transport closed gracefully");
        } else {
            log.debug("TRANSPORT (CLOSE) => Shutdown interrupted");
        }
    }

    public final ChannelHandler getDefaultChannelHandler() {
        return defaultChannelHandler;
    }

    @Override
    public final OptionMap getOptions() {
        return options;
    }

    /**
     * @return The underlying {@link ChannelFactory} for this transport.
     */
    public ChannelFactory<Channel> getChannelFactory() {
        return this.channelFactory;
    }

    abstract protected ChannelFactory<Channel> newChannelFactory(Bootstrap bootstrap, boolean pooled);

    //<editor-fold desc="Threading">
    @Override
    public EventLoopGroup getExecutor() {
        return this.eventLoopGroup;
    }

    public final Bootstrap getBootstrap() {
        return bootstrap;
    }
    //</editor-fold>

    public Class<? extends Channel> getChannelClass() {
        return this.channelClass;
    }

    protected static Class<? extends Channel> getChannelClass(TransportType transportType, boolean useNative) {
        return Platform.getChannelClass(transportType, useNative);
    }

    protected CompletableFuture<Channel> newChannel(final Envelope<? extends AbstractRequest> envelope) {
        return channelFactory.create(envelope);
    }

    private static void checkParcel(Parcel parcel) {
        //fail fast
        if (parcel == null)
            throw new IllegalStateException("Parcel is null");
        if (!parcel.hasChannel())
            throw new IllegalStateException("No channel is attached to this parcel");
    }

    class Parcel {

        //<editor-fold desc="Members">
        private Envelope<? extends AbstractRequest> envelope;

        private Channel channel;

        private Throwable error;
        //</editor-fold>

        Parcel(Envelope<? extends AbstractRequest> envelope) {
            this.envelope = envelope;
        }

        Parcel(Channel channel, Envelope<AbstractRequest> envelope) {
            this.envelope = envelope;
            attach(channel);
        }

        //<editor-fold desc="Convenience methods">
        boolean hasChannel() {
            return channel != null;
        }

        Channel channel() {
            return this.channel;
        }

        Envelope<? extends AbstractRequest> envelope() {
            return envelope;
        }

        Throwable error() {
            return error;
        }

        Parcel error(Throwable error) {
            log.debug(String.format("%s Capturing error into parcel (%s)", NettyUtil.id(channel), error == null ? "N/A" : error.getClass().getSimpleName()), error);
            if (error != null && log.isDebugEnabled())
                error.printStackTrace();
            this.error = ConcurrentUtil.unwrap(error);
            return this;
        }

        boolean hasError() {
            return error != null;
        }

        //attach the request envelope to the channel
        //update the sender field of the envelope
        Parcel attach(Channel channel) {
            if (this.channel != null)
                throw new IllegalStateException("A channel is already attached to this parcel");

            this.channel = Objects.requireNonNull(channel, "Channel must not be null");

            //update request channel attribute
            //noinspection unchecked
            channel.attr(ChannelAttributes.REQUEST).set((Envelope<AbstractRequest>) envelope);

            //update sender address
            this.envelope.sender(channel.localAddress());

            log.debug("{} TRANSPORT => Successfully attached channel '{}' to parcel '{}'", NettyUtil.id(channel()), this, channel());
            return this;
        }

        void release() {
            this.channel = null;
            this.error = null;
            this.envelope = null;
        }
        //</editor-fold>

        @Override
        public String toString() {
            return String.format("[PARCEL] %s", envelope);
        }

        private Parcel attach(Channel channel, Parcel parcel) {
            assert parcel == this;
            return parcel.attach(channel);
        }
    }
}
