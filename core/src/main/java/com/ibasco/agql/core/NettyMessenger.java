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

import com.ibasco.agql.core.exceptions.MessengerException;
import com.ibasco.agql.core.transport.DefaultNettyChannelFactoryProvider;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactoryProvider;
import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.Functions;
import com.ibasco.agql.core.util.MessengerProperties;
import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * <p>Constructor for NettyMessenger.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    protected NettyMessenger(Options options) {
        if (options == null) {
            if (!getClass().isAnnotationPresent(MessengerProperties.class))
                throw new IllegalStateException("Missing MessengerProperties annotation for class " + getClass());
            MessengerProperties properties = getClass().getDeclaredAnnotation(MessengerProperties.class);
            options = OptionBuilder.newBuilder(properties.optionClass()).build();//createOptions();
        }
        assert options != null;
        this.options = options;
        //Apply messenger specific configuration
        configure(options);
        Option.consolidate(options, getClass());

        //Initialize members
        this.factoryProvider = createFactoryProvider();
        this.channelFactory = createChannelFactory();
        this.transport = new NettyTransport(options);
    }

    //</editor-fold>

    //<editor-fold desc="Abstract/Protected Methods">

    /**
     * Populate configuration options. Subclasses should override this method. This is called right before the underlying transport is initialized.
     *
     * @param options
     *         The {@link com.ibasco.agql.core.util.Options} instance holding the configuration data
     */
    abstract protected void configure(final Options options);
    //</editor-fold>

    //<editor-fold desc="Public methods">

    /**
     * <p>createFactoryProvider.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactoryProvider} object
     */
    protected NettyChannelFactoryProvider createFactoryProvider() {
        return DEFAULT_FACTORY_PROVIDER;
    }
    //</editor-fold>

    /**
     * <p>createChannelFactory.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    abstract protected NettyChannelFactory createChannelFactory();

    /** {@inheritDoc} */
    @Override
    public Options getOptions() {
        return this.options;
    }

    /** {@inheritDoc} */
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

    /**
     * <p>Acquire context</p>
     *
     * @param data
     *         a {@link java.lang.Object} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    protected final CompletableFuture<NettyChannelContext> acquireContext(Object data) {
        if (data == null)
            throw new IllegalStateException("No data provided for channel acquisition");
        return channelFactory.create(data).thenApply(NettyChannelContext::getContext);
    }

    /**
     * <p>transformProperties.</p>
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param request
     *         a R object
     *
     * @return a {@link java.lang.Object} object
     */
    protected Object transformProperties(InetSocketAddress address, R request) {
        return address;
    }

    /**
     * <p>Attach a request to the context</p>
     *
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     * @param request
     *         a {@link com.ibasco.agql.core.AbstractRequest} object
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    protected static NettyChannelContext attach(NettyChannelContext context, AbstractRequest request) {
        log.debug("{} MESSENGER => Attaching new request '{}' to context", context.id(), request);
        //update the request in envelope
        context.properties().request(request);
        return context;
    }

    /**
     * <p>Send context to the underlying {@link com.ibasco.agql.core.Transport}</p>
     *
     * @param context
     *         The context containing the transaction details
     * @param <C>
     *         A type of {@link com.ibasco.agql.core.NettyChannelContext}
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public final <C extends NettyChannelContext> CompletableFuture<C> send(C context) {
        assert context != null;
        assert context.properties().request() != null;
        log.debug("{} MESSENGER => Preparing context for transport (Request: {})", context.id(), context.properties().request());
        CompletableFuture<C> future;
        if (context.inEventLoop()) {
            future = context.future()
                            .thenApply(NettyMessenger::initialize)
                            .thenCompose(transport::send)
                            .thenCompose(NettyChannelContext::composedFuture)
                            .thenApply(Functions::convert);
        } else {
            future = context.future()
                            .thenApplyAsync(NettyMessenger::initialize, context.eventLoop())
                            .thenComposeAsync(transport::send, context.eventLoop())
                            .thenComposeAsync(NettyChannelContext::composedFuture, context.eventLoop())
                            .thenApplyAsync(Functions::convert, context.eventLoop());
        }
        //- use handle so we don't complete exceptionally yet as we need to wrap this into a MessageException
        return future.handle(Functions::selectSecond).thenCombine(CompletableFuture.completedFuture(context), this::wrapException);
    }

    private static <V extends AbstractResponse> CompletableFuture<V> response(NettyChannelContext context) {
        return context.properties().responsePromise();
    }

    private static NettyChannelContext initialize(final NettyChannelContext context) {
        assert context.inEventLoop();

        //Ensure envelope is not null
        if (context.properties().envelope() == null)
            throw new IllegalStateException("No request is attached to the channel");

        //Ensure that envelope's sender address is up to date
        if (context.channel().localAddress() != null && (context.channel().localAddress() != context.properties().envelope().sender()))
            context.properties().envelope().sender(context.channel().localAddress());
        else
            log.debug("{} MESSENGER => Local address not updated for envelope {}", context.id(), context.properties().envelope());

        //Reset context properties/promises if necessary
        if (context.properties().responsePromise() != null && context.properties().responsePromise().isDone()) {
            log.debug("{} MESSENGER => Resetting response promise for request '{}'", context.id(), context.properties().request());
            context.properties().reset();
        }
        return context;
    }

    /**
     * Wrap an exception to a {@link MessengerException}
     *
     * @param error
     *         The exception to be wrapped
     * @param context
     *         The {@link NettyChannelContext} associated with this exception
     * @param <C>
     *         A captured type of a {@link NettyChannelContext}
     *
     * @return The underlying {@link NettyChannelContext} used for this transaction
     */
    private <C extends NettyChannelContext> C wrapException(Throwable error, C context) {
        assert context != null;
        //wrap all exceptions with MessageException
        if (error != null) {
            Throwable cause = Errors.unwrap(error);
            throw new MessengerException(cause, context);
        }
        return context;
    }

    /** {@inheritDoc} */
    @Override
    public final NettyTransport getTransport() {
        return transport;
    }

    /** {@inheritDoc} */
    @Override
    public final EventLoopGroup getExecutor() {
        return channelFactory.getExecutor();
    }

    //<editor-fold desc="Private/Protected Methods">

    /**
     * The method that will be called by the last {@link io.netty.channel.ChannelHandler} once a response has been received from the remote server.
     *
     * @param context
     *         The {@link com.ibasco.agql.core.NettyChannelContext} contianing all the important transaction details.
     * @param error
     *         The error that occured during send/receive operation. {@code null} if no error occured.
     * @param response
     *         The decoded {@link com.ibasco.agql.core.AbstractResponse} received
     */
    @ApiStatus.Internal
    protected void receive(@NotNull final NettyChannelContext context, AbstractResponse response, Throwable error) {
        final Envelope<R> envelope = context.properties().envelope();
        final CompletableFuture<S> promise = context.properties().responsePromise();

        assert context.channel().eventLoop().inEventLoop();
        assert promise != null;

        if (response != null) {
            //Update address
            if (response.getAddress() == null)
                response.setAddress(context.properties().envelope().recipient());
            //attach request to response
            if (response.getRequest() == null)
                response.setRequest(context.properties().request());
        }

        try {
            if (log.isDebugEnabled())
                log.debug("{} MESSENGER => Received response for request '{}' (Error: {})", context.id(), envelope, error == null ? "N/A" : error.getClass().getSimpleName());
            if (promise.isDone()) {
                log.debug("{} MESSENGER => [INVALID] Response '{}' has already been marked as completed. Not notifying client (Promise: {})", context.id(), envelope, promise);
                return;
            }
            if (error != null) {
                log.debug("{} MESSENGER => [ERROR] Received response in error (Request: '{}', Error: {})", context.id(), envelope, error.getClass().getSimpleName(), error);
                context.markInError(error);
            } else {
                assert response != null;
                if (response.getAddress() == null)
                    response.setAddress(context.properties().remoteAddress()); //source address
                //attach response to context and mark as completed
                if (context.markSuccess(response))
                    log.debug("{} MESSENGER => [SUCCESS] Received response successfully (Request: '{}')", context.id(), envelope);
            }
            assert promise.isDone();
        } catch (Exception e) {
            if (!promise.isDone())
                promise.completeExceptionally(e); //new ResponseException(e, context)
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (this.channelFactory != null)
            this.channelFactory.close();
        //it's possible that the transport has not been initialized yet, if the client did not call send yet
        if (transport != null)
            transport.close();
    }

    /**
     * <p>Getter for the field <code>channelFactory</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactory} object
     */
    public final NettyChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * <p>Getter for the field <code>factoryProvider</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.transport.NettyChannelFactoryProvider} object
     */
    protected final NettyChannelFactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    /**
     * <p>Lock an option and prevent future updates for this option</p>
     *
     * @param map
     *         a {@link com.ibasco.agql.core.util.Options} object
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param value
     *         a X object
     * @param <X>
     *         a X class
     */
    @SuppressWarnings("SameParameterValue")
    protected final <X> void lockedOption(Options map, Option<X> option, X value) {
        if (map.contains(option))
            map.remove(option);
        map.put(option, value, true);
    }

    /**
     * <p>Apply a default option value if not specified by the user</p>
     *
     * @param option
     *         a {@link com.ibasco.agql.core.util.Option} object
     * @param value
     *         a X object
     * @param <X>
     *         a X class
     */
    protected final <X> void applyDefault(Option<X> option, X value) {
        Options map = getOptions();
        map.putIfAbsent(option, value);
    }
    //</editor-fold>
}
