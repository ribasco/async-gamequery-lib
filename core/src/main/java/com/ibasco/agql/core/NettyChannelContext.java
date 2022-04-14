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
import com.ibasco.agql.core.exceptions.NoChannelContextException;
import com.ibasco.agql.core.exceptions.WriteInProgressException;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
import com.ibasco.agql.core.transport.handlers.ReadTimeoutHandler;
import com.ibasco.agql.core.transport.handlers.WriteTimeoutHandler;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.Functions;
import com.ibasco.agql.core.util.MessageEnvelopeBuilder;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.Options;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The context attached to a {@link io.netty.channel.Channel} instance
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("unchecked")
@ApiStatus.Internal
public class NettyChannelContext implements Closeable, Cloneable {

    private static final Logger log = LoggerFactory.getLogger(NettyChannelContext.class);

    private final Channel channel;

    private final NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse, ? extends Options> messenger;

    private final Deque<Properties> propertiesStack = new ArrayDeque<>(10);

    private Properties properties;

    private static final ChannelFutureListener CLEANUP_ON_CLOSE = future -> {
        NettyChannelContext context = NettyChannelContext.getContext(future.channel());
        log.debug("{} CONTEXT (CLOSE) => Context closed (Error: {}, Response: {})", context.id(), context.properties().error(), context.properties().response());
        context.cleanup();
    };

    /**
     * Called when the channel has been pre-maturely closed
     */
    private static final ChannelFutureListener FAIL_ON_CLOSE = future -> {
        final NettyChannelContext context = NettyChannelContext.getContext(future.channel());
        final CompletableFuture<?> responsePromise = context.properties().responsePromise();
        if (responsePromise == null) {
            log.debug("{} CONTEXT => Skipping (Reason: No promise attached to channel: {})", context.id(), context.channel());
            return;
        }
        //if the response promise has been completed already, do nothing
        if (responsePromise.isDone())
            return;
        log.debug("{} CONTEXT => Connection dropped by the remote server. Completing request with error (Request: {})", context.id(), context.properties().request());
        if (future.isSuccess())
            responsePromise.completeExceptionally(new ChannelClosedException("Connection was dropped by the remote server", context.channel()));
        else
            responsePromise.completeExceptionally(new ChannelClosedException("Connection was dropped by the remote server", future.cause(), context.channel()));
    };

    /**
     * <p>Constructor for NettyChannelContext.</p>
     *
     * @param channel
     *         a {@link io.netty.channel.Channel} object
     * @param messenger
     *         a {@link com.ibasco.agql.core.NettyMessenger} object
     */
    public NettyChannelContext(final Channel channel, final NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse, ? extends Options> messenger) {
        if (channel == null)
            throw new IllegalArgumentException("Channel must not be null");
        if (!channel.isActive())
            throw new IllegalStateException("Channel must be active");
        if (messenger == null)
            throw new IllegalStateException("Messenger must not be null");
        this.channel = channel;
        this.messenger = messenger;
        this.properties = newProperties(null);

        //mark response promise exceptionally if the channel was closed before a response was received
        failOnClose();
        cleanupOnClose();
    }

    /**
     * <p>Constructor for NettyChannelContext.</p>
     *
     * @param context a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    protected NettyChannelContext(NettyChannelContext context) {
        this.channel = context.channel;
        this.messenger = context.messenger;
        this.properties = new Properties(context.properties);
        this.properties.reset();
        failOnClose();
        cleanupOnClose();
    }

    /**
     * <p>future.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public final CompletableFuture<NettyChannelContext> future() {
        return CompletableFuture.completedFuture(this);
    }

    /**
     * <p>composedFuture.</p>
     *
     * @param <C> a C class
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public final <C extends NettyChannelContext> CompletableFuture<C> composedFuture() {
        return (CompletableFuture<C>) future().thenCombineAsync(properties().responsePromise(), Functions::selectFirst, eventLoop());
    }

    /**
     * <p>isValid.</p>
     *
     * @return a boolean
     */
    public boolean isValid() {
        return this.channel.isActive();
    }

    /**
     * <p>id.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public final String id() {
        return Netty.id(channel);
    }

    /**
     * <p>channel.</p>
     *
     * @return a {@link io.netty.channel.Channel} object
     */
    public final Channel channel() {
        return channel;
    }

    /**
     * <p>messenger.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyMessenger} object
     */
    public NettyMessenger<? extends AbstractRequest, ? extends AbstractResponse, ? extends Options> messenger() {
        return this.messenger;
    }

    /**
     * <p>inEventLoop.</p>
     *
     * @return a boolean
     */
    public final boolean inEventLoop() {
        return channel.eventLoop().inEventLoop();
    }

    /**
     * <p>eventLoop.</p>
     *
     * @return a {@link io.netty.channel.EventLoop} object
     */
    public final EventLoop eventLoop() {
        return channel.eventLoop();
    }

    /**
     * <p>isCompleted.</p>
     *
     * @return a boolean
     */
    public final boolean isCompleted() {
        if (properties.responsePromise == null)
            throw new IllegalStateException("Context not initialized");
        return properties.responsePromise.isDone();
    }

    /**
     * <p>hasResponse.</p>
     *
     * @return a boolean
     */
    public final boolean hasResponse() {
        return isCompleted() && properties().response() != null;
    }

    /**
     * <p>hasError.</p>
     *
     * @return a boolean
     */
    public final boolean hasError() {
        if (properties.responsePromise == null)
            throw new IllegalStateException("Context not initialized");
        return properties.responsePromise.isCompletedExceptionally();
    }

    /**
     * <p>markSuccess.</p>
     *
     * @param response a {@link com.ibasco.agql.core.AbstractResponse} object
     * @return a boolean
     */
    public final boolean markSuccess(AbstractResponse response) {
        checkResponse();
        return this.properties.responsePromise.complete(response);
    }

    /**
     * <p>markInError.</p>
     *
     * @param error a {@link java.lang.Throwable} object
     */
    public final void markInError(Throwable error) {
        checkResponse();
        if (this.properties.responsePromise.completeExceptionally(error))
            this.properties.responseError = error;
    }

    /**
     * Pass the message back to the messenger. Note this will not mark the promise as completed.
     *
     * @param response
     *         The {@link com.ibasco.agql.core.AbstractResponse} to receive
     */
    public final void receive(AbstractResponse response) {
        if (this.messenger == null)
            throw new IllegalStateException("No messenger is assigned to this channel context: " + this);
        try {
            this.messenger.receive(this, response, null);
        } catch (Throwable e) {
            log.error("{} CONTEXT => Messenger receive() has thrown an error", id(), e);
            markInError(e);
        }
    }

    /**
     * Pass the error back to the messenger. Note this will not mark the promise as completed.
     *
     * @param error
     *         The {@link java.lang.Throwable} to receive
     */
    public final void receive(Throwable error) {
        if (this.messenger == null)
            throw new IllegalStateException("No messenger is assigned to this channel context: " + this);
        try {
            this.messenger.receive(this, null, error);
        } catch (Throwable e) {
            log.error("{} CONTEXT => Messenger receive() has thrown an error", id(), e);
            markInError(e);
        }
    }

    /**
     * <p>exists.</p>
     *
     * @param key a {@link io.netty.util.AttributeKey} object
     * @param <V> a V class
     * @return a boolean
     */
    public final <V> boolean exists(AttributeKey<V> key) {
        return this.channel.hasAttr(key) && this.channel.attr(key).get() != null;
    }

    /**
     * <p>get.</p>
     *
     * @param key a {@link io.netty.util.AttributeKey} object
     * @param <V> a V class
     * @return a V object
     */
    public final <V> V get(AttributeKey<V> key) {
        return this.channel.attr(key).get();
    }

    /**
     * <p>set.</p>
     *
     * @param key a {@link io.netty.util.AttributeKey} object
     * @param value a V object
     * @param <V> a V class
     */
    public final <V> void set(AttributeKey<V> key, V value) {
        this.channel.attr(key).set(value);
    }

    /**
     * <p>remoteAddress.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public InetSocketAddress remoteAddress() {
        if (channel.remoteAddress() == null)
            return properties().envelope().recipient();
        return (InetSocketAddress) channel.remoteAddress();
    }

    /**
     * <p>localAddress.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    /**
     * <p>properties.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext.Properties} object
     */
    public Properties properties() {
        return properties;
    }

    /**
     * <p>send.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<? extends NettyChannelContext> send() {
        return messenger.send(this).thenCompose(NettyChannelContext::composedFuture);
    }

    /**
     * Save the current state of this context
     *
     * @return This {@link com.ibasco.agql.core.NettyChannelContext}
     */
    public NettyChannelContext save() {
        propertiesStack.addFirst(newProperties(properties));
        properties.reset();
        return this;
    }

    /**
     * Restore the previously saved context
     *
     * @return This {@link com.ibasco.agql.core.NettyChannelContext}
     */
    public NettyChannelContext restore() {
        this.properties = propertiesStack.removeFirst();
        return this;
    }

    /**
     * Clear the properties stack
     */
    public void clear() {
        this.propertiesStack.clear();
    }

    /**
     * <p>newProperties.</p>
     *
     * @param copy a {@link com.ibasco.agql.core.NettyChannelContext.Properties} object
     * @return a {@link com.ibasco.agql.core.NettyChannelContext.Properties} object
     */
    protected Properties newProperties(Properties copy) {
        if (copy != null)
            return new Properties(copy);
        return new Properties();
    }

    private void checkResponse() {
        if (this.properties.responsePromise == null)
            throw new IllegalStateException("Failed to set response. Response promise was not initialized");
        if (this.properties.responsePromise.isDone())
            throw new IllegalStateException("A response was already received for this context. Make sure reset() is called before updating this property");
    }

    /**
     * Get the channel context attached to the provided {@link io.netty.channel.Channel}
     *
     * @param channel
     *         The {@link io.netty.channel.Channel} to retrieve the context from
     * @return The {@link com.ibasco.agql.core.NettyChannelContext} associated with the {@link io.netty.channel.Channel}
     */
    public static NettyChannelContext getContext(Channel channel) {
        if (channel == null)
            throw new IllegalArgumentException("Channel is null");
        NettyChannelContext context = channel.attr(NettyChannelAttributes.CHANNEL_CONTEXT).get();
        if (context == null)
            throw new NoChannelContextException("Missing channel context", channel);
        return context;
    }

    /**
     * <p>attach.</p>
     *
     * @param request a {@link com.ibasco.agql.core.AbstractRequest} object
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext attach(AbstractRequest request) {
        properties().request(request);
        return this;
    }

    /**
     * <p>enableAutoRelease.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext enableAutoRelease() {
        properties().autoRelease(true);
        return this;
    }

    /**
     * <p>disableAutoRelease.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext disableAutoRelease() {
        properties().autoRelease(false);
        return this;
    }

    /**
     * <p>disableWriteTimeout.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext disableWriteTimeout() {
        channel().pipeline().remove(WriteTimeoutHandler.class);
        return this;
    }

    /**
     * <p>disableReadTimeout.</p>
     *
     * @return a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public NettyChannelContext disableReadTimeout() {
        channel().pipeline().remove(ReadTimeoutHandler.class);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        if (NettyChannelPool.isPooled(channel))
            NettyChannelPool.tryRelease(channel).thenAccept(success -> {
                if (!success) {
                    log.warn("{} CONTEXT (CLOSE) => Failed to release a pooled channel. Closing channel", id());
                    channel.close();
                    return;
                }
                log.debug("{} CONTEXT (RELEASE) => Context released (Pooled)", id());
            });
        else {
            channel.close();
            log.debug("{} CONTEXT (RELEASE) => Context released", id());
        }
    }

    /**
     * Marks the response promise exceptionally if the connection was dropped before we receive a response from the remote server
     */
    private void failOnClose() {
        CompletableFuture<?> promise = properties().responsePromise();
        if (promise == null)
            throw new IllegalStateException("Missing envelope promise");
        ChannelFuture closeFuture = channel().closeFuture();
        if (closeFuture.isDone()) {
            if (promise.isDone())
                return;
            if (closeFuture.isSuccess()) {
                promise.completeExceptionally(new ChannelClosedException("Connection was dropped by the server", channel()));
            } else {
                promise.completeExceptionally(new ChannelClosedException("Connection was dropped by the server", closeFuture.cause(), channel()));
            }
            assert promise.isDone();
        } else {
            closeFuture.addListener(FAIL_ON_CLOSE);
        }
    }

    private void cleanupOnClose() {
        ChannelFuture closeFuture = channel().closeFuture();
        if (closeFuture.isDone()) {
            if (!closeFuture.isSuccess())
                log.error("Error occured while attempting to close channel (context: {})", this, closeFuture.cause());
            //perform cleanup operations
            cleanup();
        } else {
            closeFuture.addListener(CLEANUP_ON_CLOSE);
        }
    }

    /**
     * Called once the underlying {@link io.netty.channel.Channel}/Connection has been closed.
     */
    protected void cleanup() {
        //no-op. meant to be overriden by sub-classes
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + this.hashCode() + " :: " + channel().id().asShortText() + " :: " + properties().request();
    }

    /**
     * Contains all the properties associated with the channel
     *
     * @author Rafael Luis Ibasco
     */
    public class Properties implements ContextProperties {

        private final Envelope<? extends AbstractRequest> envelope;

        private volatile CompletableFuture<NettyChannelContext> writePromise;

        private CompletableFuture<AbstractResponse> responsePromise;

        private Throwable responseError;

        private boolean autoRelease = true;

        protected Properties() {
            log.debug("{} CONTEXT => Initializing context properties for channel '{}' (Local: {}, Remote: {})", id(), channel, channel.localAddress(), channel.remoteAddress());
            this.envelope = MessageEnvelopeBuilder.createNew().fromAnyAddress().recipient(channel().remoteAddress()).build();
            this.responseError = null;
            this.responsePromise = new CompletableFuture<>();
            attachListeners();
        }

        public Properties(Properties properties) {
            this.envelope = MessageEnvelopeBuilder.createFrom(properties.envelope).build();
            this.responsePromise = properties.responsePromise;
            this.responseError = properties.responseError;
            this.autoRelease = properties.autoRelease;
            this.writePromise = properties.writePromise;
        }

        protected void onPropertiesReset() {
            //no-op. meant to be overriden by sub-classes
        }

        @Override
        public boolean autoRelease() {
            return autoRelease;
        }

        @Override
        public void autoRelease(boolean autoRelease) {
            this.autoRelease = autoRelease;
        }

        @Override
        public InetSocketAddress localAddress() {
            checkEnvelope();
            return envelope.sender();
        }

        @Override
        public InetSocketAddress remoteAddress() {
            checkEnvelope();
            return envelope.recipient();
        }

        @Override
        public <V extends AbstractRequest> V request() {
            checkEnvelope();
            //noinspection unchecked
            return (V) envelope().content();
        }

        @Override
        public void request(AbstractRequest request) {
            checkEnvelope();
            envelope().content(request);
        }

        @Override
        public <V extends AbstractResponse> V response() {
            if (responsePromise == null)
                return null;
            try {
                return (V) responsePromise.getNow(null);
            } catch (Throwable e) {
                log.debug("{} CONTEXT => Failed to retrieve response value due to an error", id(), e);
                return null;
            }
        }

        @Override
        public Throwable error() {
            if (responsePromise == null || !responsePromise.isCompletedExceptionally())
                return null;
            try {
                responsePromise.getNow(null);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        public <A extends AbstractRequest> Envelope<A> envelope() {
            //noinspection unchecked
            return (Envelope<A>) envelope;
        }

        @Override
        public boolean writeDone() {
            if (this.writePromise == null)
                throw new IllegalStateException("No write operation is currntly in-progress");
            return this.writePromise.isDone();
        }

        @Override
        public boolean writeInError() {
            if (this.writePromise == null)
                throw new IllegalStateException("No write operation is currntly in-progress");
            return this.writePromise.isCompletedExceptionally();
        }

        @Override
        public boolean writeInProgress() {
            return this.writePromise != null && !this.writePromise.isDone();
        }

        @Override
        public CompletableFuture<NettyChannelContext> beginWrite() {
            if (writeInProgress())
                throw new WriteInProgressException("A write operation is already in-progress");
            this.writePromise = new CompletableFuture<>();
            return writePromise;
        }

        @Override
        public CompletableFuture<NettyChannelContext> endWrite() {
            return endWrite(null);
        }

        @Override
        public CompletableFuture<NettyChannelContext> endWrite(Throwable error) {
            if (!writeInProgress())
                throw new IllegalStateException("No write operation on-going");
            if (inEventLoop()) {
                try {
                    if (error != null) {
                        this.writePromise.completeExceptionally(error);
                    } else {
                        this.writePromise.complete(NettyChannelContext.this);
                    }
                } finally {
                    this.writePromise = null;
                }
                return CompletableFuture.completedFuture(NettyChannelContext.this);
            } else {
                return CompletableFuture.supplyAsync(() -> {
                    final NettyChannelContext ctx = NettyChannelContext.this;
                    try {
                        if (error != null) {
                            ctx.properties.writePromise.completeExceptionally(error);
                        } else {
                            ctx.properties.writePromise.complete(NettyChannelContext.this);
                        }
                    } finally {
                        ctx.properties.writePromise = null;
                    }
                    return NettyChannelContext.this;
                }, eventLoop());
            }
        }

        @Override
        public <V extends AbstractResponse> CompletableFuture<V> responsePromise() {
            //noinspection unchecked
            return (CompletableFuture<V>) responsePromise;
        }

        @Override
        public void reset() {
            log.debug("{} CONTEXT => Resetting context properties (Request: {})", id(), request());
            //note: envelope's content will not be cleared, hence can be re-used.
            this.responseError = null;
            this.responsePromise = new CompletableFuture<>();
            //reattach listeners
            attachListeners();
            onPropertiesReset();
        }

        private void checkEnvelope() {
            if (envelope == null)
                throw new IllegalStateException("No envelope attached to the context");
        }

        private void attachListeners() {
            //release once the response promise has been marked as completed
            this.responsePromise.whenComplete(this::releaseOnCompletion);
            log.debug("{} CONTEXT => Attached auto-release listener", id());
        }

        private void releaseOnCompletion(AbstractResponse response, Throwable error) {
            if (!autoRelease) {
                log.debug("{} CONTEXT => Skipping auto release", id());
                return;
            }
            log.debug("{} CONTEXT => Auto releasing context (Auto release: {}, Request: {})", id(), properties().autoRelease(), properties.request());
            //release or close the context
            close();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NettyChannelContext)) return false;
        NettyChannelContext context = (NettyChannelContext) o;
        return channel.id().equals(context.channel.id());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(channel.id());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public NettyChannelContext clone() {
        return new NettyChannelContext(this);
    }
}
