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

package com.ibasco.agql.core;

import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.transport.ChannelAttributes;
import com.ibasco.agql.core.transport.NettyChannelHandlerInitializer;
import com.ibasco.agql.core.transport.NettyTransport;
import com.ibasco.agql.core.transport.TransportFactory;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.core.util.OptionMap;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Base messenger class utiliting Sockets for transport
 *
 * @param <A>
 *         The type of {@link SocketAddress} which identifies the destination address of the message
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unchecked")
abstract public class NettyMessenger<A extends SocketAddress, R extends AbstractRequest, S extends AbstractResponse> implements Messenger<A, R, S>, NettyChannelHandlerInitializer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AttributeKey<CompletableFuture<S>> PROMISE = AttributeKey.valueOf("messengerPromise");

    //<editor-fold desc="Class Members">
    private final OptionMap options;

    private final Transport<Channel, Envelope<AbstractRequest>> transport;

    //</editor-fold>

    //<editor-fold desc="Constructor">
    protected NettyMessenger(final OptionMap options, final TransportFactory<Envelope<AbstractRequest>> factory) {
        Objects.requireNonNull(factory, "Transport factory is missing");

        //Apply messenger specific
        //configuration parameters
        configure(options);

        //create a new transport instance
        final NettyTransport transport = (NettyTransport) factory.create(options, this);

        //initialize transport
        initialize(transport);

        //Initialize the transport (e.g. bootstrap, executors etc)
        transport.initialize();

        onInitialized(transport);

        this.options = options == null ? new OptionMap(getClass()) : options;
        this.transport = transport;
    }
    //</editor-fold>

    //<editor-fold desc="Abstract Methods">
    abstract public void registerInboundHandlers(LinkedList<ChannelInboundHandler> handlers);

    abstract public void registerOutboundHandlers(LinkedList<ChannelOutboundHandler> handlers);
    //</editor-fold>

    /**
     * Populate with configuration options. Sub-classes should override this method. This is called right before the underlying transport is initialized.
     *
     * @param options
     *         The {@link OptionMap} instance holding the configuration data
     */
    protected void configure(final OptionMap options) {}

    /**
     * Called before the {@link Transport} is initialized. All necessary configuration operations should be performed in this method
     */
    protected void initialize(NettyTransport transport) {
        //no-op. should be overriden by subclass
    }

    /**
     * Called after a transport has been initialized
     *
     * @param transport
     *         The {@link NettyTransport} that has been initialized
     */
    protected void onInitialized(NettyTransport transport) {}

    //<editor-fold desc="Public methods">
    @Override
    public CompletableFuture<S> send(A address, R request) {
        return send(address, request, (CompletableFuture<Channel>) null);
    }

    public CompletableFuture<S> send(R request, Channel channel) {
        return send(newEnvelope((A) channel.remoteAddress(), request), channel);
    }

    public CompletableFuture<S> send(A address, R request, Channel channel) {
        return send(address, request, CompletableFuture.completedFuture(channel));
    }

    public CompletableFuture<S> send(A address, R request, CompletableFuture<Channel> channelFuture) {
        //prepare message for transport (wrap request inside an envelope)
        return send(newEnvelope(address, request), channelFuture);
    }

    public CompletableFuture<S> send(Envelope<R> envelope, Channel channel) {
        return send(envelope, CompletableFuture.completedFuture(channel));
    }

    public CompletableFuture<S> send(Envelope<R> envelope, CompletableFuture<Channel> channelFuture) {
        Objects.requireNonNull(envelope, "Envelope cannot be null");
        Objects.requireNonNull(envelope.promise(), "Promise is null");
        final CompletableFuture<S> promise = envelope.promise();
        try {
            log.debug("{} SEND => Preparing request '{}' for transport (destination: {})", NettyUtil.id(envelope.content()), envelope.content().getClass().getSimpleName(), envelope.recipient());
            CompletableFuture<Channel> writeFuture = getTransport().send(envelope, channelFuture).thenApply(this::updateAttributes);
            failOnClose(writeFuture);
            return writeFuture.thenCompose(c -> promise);
        } catch (Throwable e) {
            promise.completeExceptionally(e);
            return promise;
        }
    }

    @Override
    public void receive(Envelope<S> envelope, Throwable error) {
        if (log.isDebugEnabled())
            log.debug("MESSENGER => Received response '{}' (Error: {})", envelope, error == null ? "N/A" : error.getClass().getSimpleName());
        if (envelope.isCompleted()) {
            envelope.promise().obtrudeValue(envelope.content());
            log.warn("MESSENGER => [INVALID] Response '{}' has already been marked as completed. Not notifying client (Promise: {})", envelope, envelope.promise());
            return;
        }
        if (error != null) {
            log.debug("MESSENGER => [ERROR] Notified client with error (Envelope: '{}', Error: {})", envelope, error.getClass().getSimpleName());
            envelope.promise().completeExceptionally(new ResponseException(error, envelope.sender()));
        } else {
            envelope.promise().complete(envelope.content());
            log.debug("MESSENGER => [SUCCESS] Notified client with response (Envelope: '{}')", envelope);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Private/Protected Methods">
    /**
     * Packs the raw request into an envelope containing all the required details of the underlying {@link Transport}
     *
     * @param address
     *         The address of the destination
     * @param request
     *         The request to be delivered to the address
     *
     * @return An {@link Envelope} containing the request and other details needed by the {@link Transport}
     */
    public Envelope<R> newEnvelope(A address, R request) {
        return newEnvelope(address, request, new CompletableFuture<>());
    }

    /**
     * Packs the raw request into an envelope containing all the required details of the underlying {@link Transport}
     *
     * @param address
     *         The address of the destination
     * @param request
     *         The request to be delivered to the address
     * @param promise
     *         The {@link CompletableFuture} that will be noitified once a response has been received from the destination
     *
     * @return An {@link Envelope} containing the request and other details needed by the {@link Transport}
     */
    public Envelope<R> newEnvelope(A address, R request, CompletableFuture<S> promise) {
        log.debug("{} SEND => Packaging request '{} (id: {})' for '{}'", NettyUtil.id(request), request.getClass().getSimpleName(), request.id(), address);
        return MessageEnvelopeBuilder.createNew()
                                     .fromAnyAddress()
                                     .recipient(address)
                                     .message(request)
                                     .messenger(this)
                                     .promise(promise)
                                     .build();
    }

    /**
     * @return The underlying {@link Transport} used by this messenger
     */
    @Override
    public final NettyTransport getTransport() {
        return (NettyTransport) transport;
    }
    //</editor-fold>

    @Override
    public OptionMap getOptions() {
        return this.options;
    }

    @Override
    public final EventLoopGroup getExecutor() {
        return (EventLoopGroup) transport.getExecutor();
    }

    @Override
    public void close() throws IOException {
        //its possible that the transport has not been initialized yet, if the client did not call send yet
        if (transport != null)
            transport.close();
    }

    protected final <X> void defaultOption(OptionMap map, Option<X> option, X value) {
        if (!map.contains(option))
            map.add(option, value);
    }

    private void failOnClose(CompletableFuture<Channel> future) {
        if (future.isDone()) {
            try {
                Channel channel = future.getNow(null);
                if (channel == null)
                    throw new IllegalStateException("Channel is null");
                failOnClose(channel);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        } else {
            future.thenAccept(this::failOnClose);
        }
    }

    private void failOnClose(Channel channel) {
        CompletableFuture<S> promise = channel.attr(PROMISE).get();
        if (promise.isDone()) {
            channel.attr(PROMISE).set(null);
            return;
        }
        ChannelFuture closeFuture = channel.closeFuture();
        if (closeFuture.isDone()) {
            try {
                promise.completeExceptionally(new ClosedChannelException());
                if (!closeFuture.isSuccess()) {
                    log.debug("Failed to close channel '{}' due to error", channel, closeFuture.cause());
                }
            } finally {
                channel.attr(PROMISE).set(null);
            }
        } else {
            closeFuture.addListener((ChannelFutureListener) future -> {
                Channel ch = future.channel();
                try {
                    CompletableFuture<S> p = ch.attr(PROMISE).get();
                    if (p.isDone())
                        return;
                    if (future.isSuccess()) {
                        p.completeExceptionally(new ClosedChannelException());
                    } else {
                        p.completeExceptionally(new Exception("Connection was closed by th remote server", new ClosedChannelException()));
                    }
                } finally {
                    ch.attr(PROMISE).set(null);
                }
            });
        }
    }

    private Channel updateAttributes(Channel channel) {
        Envelope<AbstractRequest> envelope1 = channel.attr(ChannelAttributes.REQUEST).get();
        channel.attr(PROMISE).set(envelope1.promise());
        return channel;
    }
}
