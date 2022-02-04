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
import com.ibasco.agql.core.exceptions.TransportWriteException;
import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for all classee who would like to utilize the netty framework
 *
 * @author Rafael Luis Ibasco
 */
public class NettyTransport implements Transport<Channel, Envelope<AbstractRequest>> {

    private static final Logger log = LoggerFactory.getLogger(NettyTransport.class);

    //<editor-fold desc="Private Members">
    private static final AttributeKey<CompletableFuture<Channel>> WRITE_PROMISE = AttributeKey.valueOf("transportWritePromise");

    private final Options options;

    private final NettyChannelFactory channelFactory;
    //</editor-fold>

    private final ChannelFutureListener WRITE_LISTENER = future -> {
        final Channel channel = future.channel();
        try {
            CompletableFuture<Channel> promise = channel.attr(WRITE_PROMISE).get();
            if (promise == null)
                throw new IllegalStateException("Missing write promise in channel: " + channel);
            if (future.isSuccess()) {
                log.debug("{} TRANSPORT => Request has been sent and processed through the channel's pipeline", NettyUtil.id(channel));
                promise.complete(channel);
            } else {
                log.debug("{} TRANSPORT => An error occured while sending request through the channel's pipeline", NettyUtil.id(channel), future.cause());
                promise.completeExceptionally(future.cause());
            }
        } finally {
            channel.attr(WRITE_PROMISE).set(null);
        }
    };

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
        return initialize(envelope, channelFuture).thenCompose(this::write).handle(this::cleanup);
    }

    /**
     * Initialize the {@link Channel} and update the required attributes once it is readily available. If no {@link Channel} future is provided, then a nw {@link Channel} will be created using the provided {@link NettyChannelFactory}.
     *
     * @param envelope
     *         The request {@link Envelope} to be associated with the {@link Channel}
     * @param channelFuture
     *         The {@link CompletableFuture} that is completed once a {@link Channel} is available.
     *
     * @return A {@link CompletableFuture} that is notified once a {@link Channel} is available.
     */
    private CompletableFuture<Channel> initialize(final Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> channelFuture) {
        if (channelFuture == null)
            channelFuture = channelFactory.create(envelope);
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
        if (!channel.eventLoop().inEventLoop())
            log.warn("Expected event loop: {}, but in {}", NettyUtil.getThreadName(channel), Thread.currentThread().getName());
        assert channel.eventLoop().inEventLoop();
        //update sender address
        envelope.sender(channel.localAddress());
        //noinspection unchecked
        channel.attr(NettyChannelAttributes.REQUEST).set((Envelope<AbstractRequest>) envelope);
        return channel;
    }

    private CompletableFuture<Channel> write(final Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        Envelope<? extends AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
        if (envelope == null)
            throw new IllegalStateException("Missing request envelope");
        final CompletableFuture<Channel> promise = new CompletableFuture<>();
        try {
            //set write promise
            channel.attr(WRITE_PROMISE).set(promise);
            if (channel.eventLoop().inEventLoop()) {
                writeAndNotify(channel);
            } else {
                channel.eventLoop().execute(() -> writeAndNotify(channel));
            }
        } catch (Exception e) {
            promise.completeExceptionally(e);
            channel.attr(WRITE_PROMISE).set(null);
        }
        return promise;
    }

    private void writeAndNotify(final Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        assert channel.eventLoop().inEventLoop();
        final CompletableFuture<Channel> promise = channel.attr(WRITE_PROMISE).get();
        try {
            Envelope<? extends AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
            if (envelope == null)
                throw new IllegalStateException("Request envelope is not present in channel");
            ChannelFuture writeFuture = channel.writeAndFlush(envelope);
            if (writeFuture.isDone()) {
                try {
                    if (writeFuture.isSuccess()) {
                        promise.complete(channel);
                    } else {
                        promise.completeExceptionally(writeFuture.cause());
                    }
                } finally {
                    channel.attr(WRITE_PROMISE).set(null);
                }
            } else {
                writeFuture.addListener(WRITE_LISTENER);
            }
        } catch (Throwable e) {
            log.debug("{} TRANSPORT => Error occured during writeAndNotify operation", NettyUtil.id(channel), e);
            promise.completeExceptionally(ConcurrentUtil.unwrap(e));
            channel.attr(WRITE_PROMISE).set(null);
        }
    }

    private Channel cleanup(Channel channel, Throwable error) {
        if (error != null) {
            log.error("Error during write operation", error);
            throw new TransportWriteException("Failed to send request via transport", ConcurrentUtil.unwrap(error));
        }
        if (channel == null)
            throw new IllegalStateException("Channel is null");
        assert channel.eventLoop().inEventLoop();
        return channel;
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

    private static void checkEnvelope(final Envelope<? extends AbstractRequest> envelope) {
        //fail-fast
        if (envelope == null)
            throw new IllegalArgumentException("Envelope is null");
        if (envelope.content() == null)
            throw new IllegalStateException("Request is null");
    }
}
