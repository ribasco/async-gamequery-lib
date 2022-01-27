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

import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.transport.*;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Option;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
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

    private final Logger log = LoggerFactory.getLogger(NettyMessenger.class);

    private static final AttributeKey<CompletableFuture<?>> PROMISE = AttributeKey.valueOf("messengerPromise");

    //<editor-fold desc="Class Members">
    private final Options options;

    private final NettyTransport transport;

    private final NettyChannelFactory channelFactory;

    private final ThreadLocal<ChannelContext> channelContextMap = new ThreadLocal<>();

    private static final ChannelFutureListener FAIL_ON_CLOSE = future -> {
        final Channel ch = future.channel();
        try {
            CompletableFuture<?> p = ch.attr(PROMISE).get();
            if (p == null)
                return;
            if (p.isDone())
                return;
            if (future.isSuccess())
                p.completeExceptionally(new ChannelClosedException("Connection was dropped by the remote server"));
            else
                p.completeExceptionally(new ChannelClosedException("Connection was dropped by the remote server", future.cause()));
        } finally {
            ch.attr(PROMISE).set(null);
        }
    };

    private final ChannelFutureListener UNREGISTER_ON_CLOSE = future -> {
        Channel channel = future.channel();
        if (channel.eventLoop().inEventLoop()) {
            channelContextMap.remove();
        } else {
            channel.eventLoop().execute(channelContextMap::remove);
        }
    };
    //</editor-fold>

    //<editor-fold desc="Constructor">
    protected NettyMessenger(Options options, final NettyChannelFactoryProvider factoryProvider) {
        Objects.requireNonNull(factoryProvider, "Transport factory provider is missing");
        if (options == null)
            options = new Options(getClass());
        //Apply messenger specific configuration parameters
        configure(options);
        this.options = options;
        this.channelFactory = factoryProvider.getFactory(options, this);
        assert this.channelFactory != null;
        this.transport = new NettyTransport(channelFactory, options);

    }
    //</editor-fold>

    //<editor-fold desc="Abstract Methods">
    abstract public void registerInboundHandlers(LinkedList<ChannelInboundHandler> handlers);

    abstract public void registerOutboundHandlers(LinkedList<ChannelOutboundHandler> handlers);
    //</editor-fold>

    /**
     * Populate with configuration options. Subclasses should override this method. This is called right before the underlying transport is initialized.
     *
     * @param options
     *         The {@link Options} instance holding the configuration data
     */
    protected void configure(final Options options) {}

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
        return send(newEnvelope(address, request), channelFuture);
    }

    public CompletableFuture<S> send(Envelope<R> envelope, Channel channel) {
        return send(envelope, CompletableFuture.completedFuture(channel));
    }

    public CompletableFuture<S> send(Envelope<R> envelope, CompletableFuture<Channel> channelFuture) {
        Objects.requireNonNull(envelope, "Envelope cannot be null");
        Objects.requireNonNull(envelope.promise(), "Promise is null");

        /*SingleThreadEventLoop el = (SingleThreadEventLoop) getExecutor().next();

        if (channelFuture == null) {
            log.info("Using event loop: '{}' for new channel", el.threadProperties().name());
            channelFuture = channelFactory.create(envelope, el);
        }*/

        log.debug("{} SEND => Sending request to transport '{}' (Envelope: {})", NettyUtil.id(envelope.content()), envelope.content().getClass().getSimpleName(), envelope);
        return transport.send(envelope, channelFuture)
                        .thenApply(this::failOnClose)
                        .thenApply(this::initialize)
                        .thenCompose(this::toResult);
    }

    private CompletableFuture<S> toResult(Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        if (!channel.eventLoop().inEventLoop()) {
            throw new IllegalStateException(String.format("Channel '%s' not in event loop (Expected: %s, Actual: %s)", channel, NettyUtil.getThreadName(channel), Thread.currentThread().getName()));
        }
        //note: messages sent via transport should always have a request envelope instance present in it's channel's attributes
        Envelope<AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
        if (envelope == null)
            throw new IllegalStateException("Missing request envelope");
        Objects.requireNonNull(envelope.promise(), "Response promise is null");
        CompletableFuture<S> promise = envelope.promise();
        ChannelContext context = channelContextMap.get();
        if (context == null)
            throw new IllegalStateException("No channel context found for: " + channel);
        //ensure we are dealing with thee same channel
        assert channel.id().equals(context.channel().id());
        //when the promise completes, then release resources (if any)
        promise.whenComplete(context::cleanup);
        return promise;
    }

    @Override
    public void receive(Envelope<S> envelope, Throwable error) {
        try {
            if (log.isDebugEnabled())
                log.debug("MESSENGER => Received response '{}' (Error: {})", envelope, error == null ? "N/A" : error.getClass().getSimpleName());
            if (envelope.isCompleted()) {
                log.debug("MESSENGER => [INVALID] Response '{}' has already been marked as completed. Not notifying client (Promise: {})", envelope, envelope.promise());
                return;
            }
            if (error != null) {
                log.debug("MESSENGER => [ERROR] Notified client with error (Envelope: '{}', Error: {})", envelope, error.getClass().getSimpleName());
                envelope.promise().completeExceptionally(new ResponseException(error, envelope.sender()));
            } else {
                S response = envelope.content();
                if (response.getAddress() == null)
                    response.setAddress(envelope.sender());
                envelope.promise().complete(response);
                log.debug("MESSENGER => [SUCCESS] Notified client with response (Envelope: '{}')", envelope);
            }
        } catch (Throwable e) {
            if (envelope != null) {
                if (!envelope.isCompleted())
                    envelope.promise().completeExceptionally(e);
            } else {
                log.error("Error occured during receive() operation", e);
            }
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
        //log.debug("{} SEND => Packaging request '{} (id: {})' for '{}'", NettyUtil.id(request), request.getClass().getSimpleName(), request.id(), address);
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
        return transport;
    }
    //</editor-fold>

    @Override
    public Options getOptions() {
        return this.options;
    }

    @Override
    public final EventLoopGroup getExecutor() {
        //return (EventLoopGroup) transport.getExecutor();
        return (EventLoopGroup) channelFactory.getExecutor();
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

    /**
     * Fail when {@link Channel} has been closed pre-maturely
     *
     * @param channel
     *         The {@link Channel} to monitor for closure
     *
     * @return The {@link Channel}
     */
    private Channel failOnClose(Channel channel) {
        CompletableFuture<?> promise = channel.attr(PROMISE).get();
        if (promise == null) {
            log.debug("Promise for channel {} is null", channel);
            return channel;
        }
        ChannelFuture closeFuture = channel.closeFuture();
        if (closeFuture.isDone()) {
            try {
                if (promise.isDone())
                    return channel;
                if (closeFuture.isSuccess()) {
                    promise.completeExceptionally(new ChannelClosedException("Connection was dropped by the server"));
                } else {
                    promise.completeExceptionally(new ChannelClosedException("Connection was dropped by the server", closeFuture.cause()));
                }
            } finally {
                channel.attr(PROMISE).set(null);
            }
        } else {
            closeFuture.addListener(FAIL_ON_CLOSE);
        }
        return channel;
    }

    private Channel initialize(Channel channel) {
        Envelope<AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
        channel.attr(PROMISE).set(envelope.promise());
        registerContext(channel);
        return channel;
    }

    private void registerContext(Channel channel) {
        if (channel.eventLoop().inEventLoop()) {
            registerContextIfEmpty(channel);
        } else {
            channel.eventLoop().execute(() -> registerContextIfEmpty(channel));
        }
    }

    private void registerContextIfEmpty(Channel channel) {
        assert channel.eventLoop().inEventLoop();
        ChannelContext context = channelContextMap.get();
        if (context != null) {
            if (!context.channel().id().equals(channel.id()))
              log.debug(String.format("Channel id mismatch (Expeected: %s, Actual: %s)", context.channel(), channel));
        }
        channelContextMap.set(new ChannelContext(channel));
    }

    private class ChannelContext {

        private final Channel channel;

        private ChannelContext(Channel channel) {
            this.channel = Objects.requireNonNull(channel, "Channel must not be null");
            channel.closeFuture().addListener(UNREGISTER_ON_CLOSE);
        }

        public final Channel channel() {
            return channel;
        }

        public void cleanup(S response, Throwable error) {
            Boolean autoRelease = channel.attr(NettyChannelAttributes.AUTO_RELEASE).get();
            if (autoRelease) {
                log.debug("MESSENGER => Releasing channel '{}' (Has Error: {})", response, error != null, error);
                NettyChannelPool.tryRelease(channel);
            }
        }

        @Override
        public String toString() {
            if (channel == null)
                return "N/A";
            return channel.toString();
        }
    }
}
