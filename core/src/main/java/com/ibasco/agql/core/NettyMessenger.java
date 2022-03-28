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
import com.ibasco.agql.core.transport.DefaultNettyChannelFactoryProvider;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryProvider;
import com.ibasco.agql.core.util.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for modules utilizing Netty as the mode of transport
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyMessenger<R extends AbstractRequest, S extends AbstractResponse> implements Messenger<R, S> {

    private static final Logger log = LoggerFactory.getLogger(NettyMessenger.class);

    private static final NettyChannelFactoryProvider DEFAULT_FACTORY_PROVIDER = new DefaultNettyChannelFactoryProvider();

    //<editor-fold desc="Class Members">
    private final Options options;

    private final NettyTransport transport;

    private final NettyChannelFactory channelFactory;

    private final NettyChannelFactoryProvider factoryProvider;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    protected NettyMessenger(Options options) {
        if (options == null)
            options = new Options(getClass());
        //Apply messenger specific configuration parameters
        configure(options);
        //Initialize members
        this.options = options;
        this.factoryProvider = createFactoryProvider();
        this.channelFactory = createChannelFactory();
        this.transport = new NettyTransport(options);
    }

    //</editor-fold>

    //<editor-fold desc="Abstract/Protected Methods">
    abstract protected NettyChannelFactory createChannelFactory();

    protected NettyChannelFactoryProvider createFactoryProvider() {
        return DEFAULT_FACTORY_PROVIDER;
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    @Override
    public CompletableFuture<S> send(InetSocketAddress address, R request) {
        if (address == null)
            throw new IllegalArgumentException("Address not provided");
        if (request == null)
            throw new IllegalArgumentException("Request not provided");
        return acquireContext(transformProperties(address, request))
                .thenCombine(CompletableFuture.completedFuture(request), NettyMessenger::attach)
                .thenCompose(this::send)
                .thenCompose(NettyMessenger::response);
    }

    public <C extends NettyChannelContext> CompletableFuture<C> send(C context) {
        assert context != null;
        assert context.properties().request() != null;
        log.debug("{} MESSENGER => Preparing context for transport (Request: {})", context.id(), context.properties().request());
        if (context.inEventLoop()) {
            return context.future()
                          .thenApply(NettyMessenger::initialize)
                          .thenCompose(transport::send)
                          .thenApply(Functions::convert);
        } else {
            return context.future()
                          .thenApplyAsync(NettyMessenger::initialize, context.eventLoop())
                          .thenComposeAsync(transport::send, context.eventLoop())
                          .thenApplyAsync(Functions::convert, context.eventLoop());
        }
    }
    //</editor-fold>

    protected Object transformProperties(InetSocketAddress address, R request) {
        return address;
    }

    protected CompletableFuture<NettyChannelContext> acquireContext(Object data) {
        if (data == null)
            throw new IllegalStateException("No data provided for channel acquisition");
        return channelFactory.create(data).thenApply(NettyChannelContext::getContext);
    }

    private static NettyChannelContext initialize(final NettyChannelContext context) {
        assert context.inEventLoop();

        //Check request
        if (context.properties().envelope() == null)
            throw new IllegalStateException("No request is attached to the channel");

        //Update sender address
        if (context.channel().localAddress() != null && (context.channel().localAddress() != context.properties().envelope().sender()))
            context.properties().envelope().sender(context.channel().localAddress());
        else
            log.debug("{} MESSENGER => Local address not updated for envelope {}", context.id(), context.properties().envelope());

        //Reset context properties/promises if necessary
        if (context.properties().responsePromise() != null && context.properties().responsePromise().isDone()) {
            log.info("{} MESSENGER => Resetting response promise for request '{}'", context.id(), context.properties().request());
            context.properties().reset();
        }
        return context;
    }

    protected static NettyChannelContext attach(NettyChannelContext context, AbstractRequest request) {
        log.debug("{} MESSENGER => Attaching request '{}' to context", context.id(), request);
        //update the request in envelope
        context.properties().request(request);
        return context;
    }

    private static <V extends AbstractResponse> CompletableFuture<V> response(NettyChannelContext context) {
        return context.properties().responsePromise();
    }

    /**
     * The method that will be called by the last {@link ChannelHandler} once a response has been received from the remote server.
     *
     * @param context
     *         The {@link NettyChannelContext} contianing all the important transaction details.
     * @param error
     *         The error that occured during send/receive operation. {@code null} if no error occured.
     */
    @ApiStatus.Internal
    protected void receive(@NotNull final NettyChannelContext context, AbstractResponse response, Throwable error) {
        final Envelope<R> envelope = context.properties().envelope();
        final CompletableFuture<S> promise = context.properties().responsePromise();
        assert context.channel().eventLoop().inEventLoop();
        if (promise == null)
            throw new IllegalStateException("Response promise not initialized");

        try {
            if (log.isDebugEnabled())
                log.debug("{} MESSENGER => Received response '{}' (Error: {})", context.id(), envelope, error == null ? "N/A" : error.getClass().getSimpleName());
            if (promise.isDone()) {
                log.debug("{} MESSENGER => [INVALID] Response '{}' has already been marked as completed. Not notifying client (Promise: {})", context.id(), envelope, promise);
                return;
            }
            if (error != null) {
                log.debug("{} MESSENGER => [ERROR] Notified client with error (Envelope: '{}', Error: {})", context.id(), envelope, error.getClass().getSimpleName(), error);
                context.markInError(new ResponseException(error, envelope.recipient()));
            } else {
                assert response != null;
                if (response.getAddress() == null)
                    response.setAddress(context.properties().remoteAddress()); //source address
                //attach response to context and mark as completed
                if (context.markSuccess(response))
                    log.debug("{} MESSENGER => [SUCCESS] Notified client with response (Envelope: '{}')", context.id(), envelope);
            }
            assert promise.isDone();
        } catch (Throwable e) {
            if (!promise.isDone())
                promise.completeExceptionally(e);
        }
    }

    /**
     * @return The underlying {@link Transport} used by this messenger
     */
    @Override
    public final NettyTransport getTransport() {
        return transport;
    }

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
    public final Envelope<R> newEnvelope(InetSocketAddress address, R request) {
        log.debug("{} SEND => Packaging request '{} (id: {})' for '{}'", NettyUtil.id(request), request.getClass().getSimpleName(), request.id(), address);
        return MessageEnvelopeBuilder.createNew()
                                     .fromAnyAddress()
                                     .recipient(address)
                                     .message(request)
                                     .build();
    }

    @Override
    public Options getOptions() {
        return this.options;
    }

    @Override
    public final EventLoopGroup getExecutor() {
        return channelFactory.getExecutor();
    }

    @Override
    public void close() throws IOException {
        if (this.channelFactory != null)
            this.channelFactory.close();
        //it's possible that the transport has not been initialized yet, if the client did not call send yet
        if (transport != null)
            transport.close();
    }

    public final NettyChannelFactory getChannelFactory() {
        return channelFactory;
    }

    //<editor-fold desc="Private/Protected Methods">
    protected final NettyChannelFactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    private static <V extends AbstractResponse> CompletableFuture<V> fromResponse(final NettyChannelContext context) {
        assert context.properties().responsePromise() != null;
        //assert context.properties().responsePromise().isDone();
        return context.properties().responsePromise();
    }

    /**
     * Populate with configuration options. Subclasses should override this method. This is called right before the underlying transport is initialized.
     *
     * @param options
     *         The {@link Options} instance holding the configuration data
     */
    protected void configure(final Options options) {}

    @SuppressWarnings("SameParameterValue")
    protected final <X> void lockedOption(Options map, Option<X> option, X value) {
        if (map.contains(option))
            map.remove(option);
        map.add(option, value, true);
    }

    protected final <X> void defaultOption(Options map, Option<X> option, X value) {
        if (!map.contains(option))
            map.add(option, value);
    }
    //</editor-fold>
}
