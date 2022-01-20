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

package com.ibasco.agql.core.transport;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.exceptions.TransportWriteException;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.transport.pool.NettyChannelPoolFactory;
import com.ibasco.agql.core.transport.pool.PooledChannel;
import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Base class for all classee who would like to utilize the netty framework
 *
 * @author Rafael Luis Ibasco
 */
public class NettyTransport implements Transport<Channel, Envelope<AbstractRequest>> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    //<editor-fold desc="Private Members">
    private final Options options;

    private final NettyChannelFactory channelFactory;
    //</editor-fold>

    //<editor-fold desc="Default Constructor">
    public NettyTransport(final NettyChannelFactory channelFactory, final Options options) {
        this.channelFactory = Objects.requireNonNull(channelFactory, "Channel factory must not be null");
        this.options = Objects.requireNonNull(options, "[INIT] TRANSPORT => Missing options");
        //Set resource leak detection if debugging is enabled
        if (log.isDebugEnabled())
            ResourceLeakDetector.setLevel(getOrDefault(TransportOptions.RESOURCE_LEAK_DETECTOR_LEVEL));
    }
    //</editor-fold>

    @Override
    public final CompletableFuture<Channel> send(final Envelope<AbstractRequest> envelope) {
        return send(envelope, null);
    }

    public final CompletableFuture<Channel> send(final Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> channelFuture) {
        checkEnvelope(envelope);
        return initialize(envelope, channelFuture).thenCompose(this::write).handle(this::release);
        //return prepare(envelope, channelFuture).thenCompose(this::write).handle(this::cleanup);
    }

    private Channel release(Channel channel, Throwable error) {
        if (error != null)
            throw new TransportWriteException("Failed to send request via transport", ConcurrentUtil.unwrap(error));
        if (channel == null)
            throw new IllegalStateException("Channel is null");
        return channel;
    }

    private CompletableFuture<Channel> initialize(final Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> channelFuture) {
        channelFuture = channelFuture == null ? newChannel(envelope) : channelFuture;
        if (channelFuture == null)
            throw new IllegalStateException("No channel is available for transport");
        return channelFuture.thenCombine(CompletableFuture.completedFuture(envelope), this::updateAttributes);
    }

    /**
     * Update the {@link Channel}'s attributes and envelope properties
     *
     * @param channel
     *         The {@link Channel} to update
     * @param envelope
     *         The {@link Envelope} to be attached to the channel
     *
     * @return The {@link Channel} instance
     */
    private Channel updateAttributes(final Channel channel, final Envelope<? extends AbstractRequest> envelope) {
        Objects.requireNonNull(channel, "Channel must not be null");
        //update sender address
        envelope.sender(channel.localAddress());

        if (!channel.hasAttr(NettyChannelAttributes.REQUEST) || channel.attr(NettyChannelAttributes.REQUEST).get() == null) {
            //update request channel attribute
            //noinspection unchecked
            channel.attr(NettyChannelAttributes.REQUEST).set((Envelope<AbstractRequest>) envelope);
        }
        return channel;
    }

    private CompletableFuture<Channel> write(final Channel channel) {
        Envelope<? extends AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
        if (envelope == null)
            throw new IllegalStateException("Request envelope is not present in channel");
        final CompletableFuture<Channel> promise = new CompletableFuture<>();
        if (channel.eventLoop().inEventLoop()) {
            writeAndNotify(channel, promise);
        } else {
            channel.eventLoop().execute(() -> writeAndNotify(channel, promise));
        }
        return promise;
    }

    private void writeAndNotify(final Channel channel, CompletableFuture<Channel> promise) {
        Objects.requireNonNull(channel, "Channel is null");
        assert channel.eventLoop().inEventLoop();
        try {
            Envelope<? extends AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
            if (envelope == null)
                throw new IllegalStateException("Request envelope is not present in channel");
            ChannelFuture writeFuture = channel.writeAndFlush(envelope);
            runWhenComplate(writeFuture, future -> {
                if (!future.isSuccess()) {
                    log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", NettyUtil.id(channel), future.cause());
                    promise.completeExceptionally(future.cause());
                } else
                    log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline", NettyUtil.id(channel));
                if (!promise.complete(channel))
                    log.warn("{} TRANSPORT => Failed to mark writeAndNotify promise as completed (Reason: Already completed, Promise: {})", NettyUtil.id(channel), promise);
            });
        } catch (Throwable e) {
            log.debug("{} TRANSPORT => Error occured during writeAndNotify operation", NettyUtil.id(channel), e);
            if (!promise.isDone())
                promise.completeExceptionally(ConcurrentUtil.unwrap(e));
        }
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
     * Use {@link Parcel#hasError()} to check if the writeAndNotify operation failed.
     *
     * @param parcel
     *         The {@link Parcel} to send
     *
     * @return A {@link CompletableFuture} which will be notified once the parcel's message has been successfully delivered through its channel's pipeline
     */
    private CompletableFuture<Parcel> write(final Parcel parcel) {
        log.debug("{} TRANSPORT => Writing parcel to channel", NettyUtil.id(parcel.channel()));

        Objects.requireNonNull(parcel, "Parcel is null");
        //ensure we have a channel attached to this parcel
        if (!parcel.hasChannel())
            throw new IllegalStateException("No channel is attached to the parcel");

        //if parcel is in error, return immediately
        if (parcel.hasError()) {
            log.debug("{} Parcel is currently in-error. Returning completed future (Error: {}).", NettyUtil.id(parcel.channel()), parcel.error.getMessage());
            return CompletableFuture.completedFuture(parcel);
        }

        final CompletableFuture<Parcel> promise = new CompletableFuture<>();
        if (parcel.channel().eventLoop().inEventLoop()) {
            writeAndNotify(parcel, promise);
        } else {
            parcel.channel().eventLoop().execute(() -> writeAndNotify(parcel, promise));
        }

        return promise.exceptionally(parcel::error);
    }

    /**
     * Writes the parcel to it's attached channel
     *
     * @param parcel
     *         The {@link Parcel} to be sent
     * @param promise
     *         The {@link CompletableFuture} to notify on write completion or failure
     */
    private void writeAndNotify(final Parcel parcel, CompletableFuture<Parcel> promise) {
        checkParcel(parcel);
        assert parcel.channel().eventLoop().inEventLoop();
        try {
            Channel channel = parcel.channel();
            ChannelFuture writeFuture = channel.writeAndFlush(parcel.envelope());
            runWhenComplate(writeFuture, future -> {
                Channel ch = future.channel();//parcel.channel();
                if (!future.isSuccess()) {
                    parcel.error(future.cause());
                    log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", NettyUtil.id(ch), parcel.error());
                } else
                    log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline", NettyUtil.id(ch));
                if (!promise.complete(parcel))
                    log.warn("{} TRANSPORT => Failed to mark writeAndNotify promise as completed (Reason: Already completed, Promise: {})", NettyUtil.id(ch), promise);
            });
        } catch (Throwable e) {
            log.debug("{} TRANSPORT => Error occured during writeAndNotify operation", NettyUtil.id(parcel.channel()), e);
            if (!promise.isDone()) {
                promise.complete(parcel.error(e));
            }
        }
    }

    /**
     * Convenience utility method which runs the callback once the provided future has transitioned to a completed state
     *
     * @param future
     *         The {@link ChannelFuture} to track
     * @param consumer
     *         The callback to be notified oncee the future has been marked as completed
     * @param <V>
     *         The type passed to the callback
     */
    private <V> void runWhenComplate(ChannelFuture future, Consumer<ChannelFuture> consumer) {
        if (future.isDone()) {
            consumer.accept(future);
        } else {
            future.addListener((ChannelFutureListener) consumer::accept);
        }
    }

    /**
     * Perform clean-up operations after writeAndNotify. If a parcel was in-error, the exception will be propagated down the chain.
     *
     * @param parcel
     *         The {@link Parcel} containing transport data
     * @param error
     *         Error that occured during send. {@code null} if no error occured.
     *
     * @return Thee {@link Channel} that was used for the writeAndNotify operation
     */
    private Channel cleanup(Parcel parcel, Throwable error) {
        try {
            //check error
            Throwable cError = ConcurrentUtil.unwrap((parcel != null && parcel.hasError()) ? parcel.error() : error);
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

    /**
     * Perform cleanup operations after write
     *
     * @param parcel
     *         The parcel to clean up
     */
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
                    if (NettyChannelPool.isPooled(channel)) {
                        log.debug("{} TRANSPORT (CLEANUP) => Found error ({}) during parcel send. Requesting to release channel from pool", NettyUtil.id(channel), parcel.error().getClass().getSimpleName());
                        //NettyUtil.release(channel);
                        if (channel instanceof PooledChannel) {
                            ((PooledChannel) channel).release();
                        } else {
                            NettyUtil.release(channel);
                        }
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
    public void close() throws IOException {
        //channelFactory.close();
    }

    @Override
    public final Options getOptions() {
        return options;
    }

    /**
     * @return The underlying {@link NettyChannelFactory} for this transport.
     */
    public NettyChannelFactory getChannelFactory() {
        return this.channelFactory;
    }

    protected CompletableFuture<Channel> newChannel(final Envelope<? extends AbstractRequest> envelope) {
        Objects.requireNonNull(envelope, "Envelope is null");
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
        private final Envelope<? extends AbstractRequest> envelope;

        private WeakReference<Channel> channelRef;

        private Throwable error;
        //</editor-fold>

        Parcel(Envelope<? extends AbstractRequest> envelope) {
            this.envelope = envelope;
        }

        //<editor-fold desc="Convenience methods">
        boolean hasChannel() {
            return channelRef != null && channelRef.get() != null;
        }

        Channel channel() {
            if (this.channelRef == null)
                return null;
            return this.channelRef.get();
        }

        Envelope<? extends AbstractRequest> envelope() {
            return envelope;
        }

        Throwable error() {
            return error;
        }

        Parcel error(Throwable error) {
            log.debug(String.format("%s Capturing error into parcel (%s)", NettyUtil.id(channel()), error == null ? "N/A" : error.getClass().getSimpleName()), error);
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
            if (hasChannel())
                throw new IllegalStateException("A channel is already attached to this parcel");

            this.channelRef = new WeakReference<>(Objects.requireNonNull(channel, "Channel must not be null"));

            //update request channel attribute
            //noinspection unchecked
            channel.attr(NettyChannelAttributes.REQUEST).set((Envelope<AbstractRequest>) envelope());

            //update sender address
            envelope().sender(channel.localAddress());

            log.debug("{} TRANSPORT => Successfully attached channel '{}' to parcel '{}'", NettyUtil.id(channel()), this, channel());
            return this;
        }

        void release() {
            if (hasChannel())
                this.channelRef.clear();
            this.channelRef = null;
            this.error = null;
        }
        //</editor-fold>

        @Override
        public String toString() {
            return String.format("[PARCEL] %s", envelope());
        }

        private Parcel attach(Channel channel, Parcel parcel) {
            assert parcel == this;
            return parcel.attach(channel);
        }
    }

    private static void checkEnvelope(final Envelope<? extends AbstractRequest> envelope) {
        //fail-fast
        if (envelope == null)
            throw new IllegalArgumentException("Envelope is null");
        if (envelope.content() == null)
            throw new IllegalStateException("Request is null");
    }
}
