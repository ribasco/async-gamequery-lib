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

package com.ibasco.agql.protocols.valve.source.query.logger;

import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Platform;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * <p>Listens for log messages produced by a Source Server.</p>
 * <br />
 *
 * <p>
 * <strong>NOTE:</strong> Ensure that the address of this service is registered to the source server, do this by registering it with  <code>'logaddress_add [ip:port]'</code> to receive log
 * messages from the game server.
 * Make sure that the port specified is not being blocked by an external service or firewall
 * </p>
 * <br />
 * <h3>Example Usage</h3>
 * <pre>
 * {@code
 *  SourceLogListenService service = new SourceLogListenService(new InetSocketAddress(0));
 *
 *     service.setLogEventCallback(SourceLogEventHandler::handleLogMessages);
 *     service.listen(); //non-blocking
 *     service.listen().join(); //blocking (will wait until close() is called or until the service is closed)
 *
 *     public class SourceLogEventPrinter {
 *         public static void handleLogMessages(SourceLogEntry logEntry) {
 *             System.out.println(logEntry.getMessage());
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Rafael Luis Ibasco
 */
public class SourceLogListenService implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceLogListenService.class);

    private final Bootstrap bootstrap;

    private final EventLoopGroup group;

    private final AtomicReference<Consumer<SourceLogEntry>> callbackRef = new AtomicReference<>();

    private InetSocketAddress listenAddress;

    private volatile boolean bindInProgress;

    private volatile boolean started;

    private volatile CompletableFuture<Void> listenFuture;

    /**
     * <p>Creates a new service that will listen to any ip address
     * and bind to a random local port number (Similar to 0.0.0.0)</p>
     */
    public SourceLogListenService() {
        this(new InetSocketAddress(0));
    }

    /**
     * <p>Creates a new listen service for the provided {@link java.net.InetSocketAddress}. You will need to explicitly call {@link #listen()} to start listening for source log events</p>
     *
     * @param listenAddress An {@link java.net.InetSocketAddress} where the listen service will bind or listen on
     */
    public SourceLogListenService(InetSocketAddress listenAddress) {
        this(listenAddress, null);
    }

    /**
     * <p>Creates a new listen service for the provided {@link java.net.InetSocketAddress}. You will need to explicitly call {@link #listen()} to start listening for source log events</p>
     *
     * @param listenAddress The {@link java.net.InetSocketAddress} to listen on
     * @param callback      The {@link java.util.function.Consumer} callback that will be notified once a log event has been received
     */
    public SourceLogListenService(InetSocketAddress listenAddress, Consumer<SourceLogEntry> callback) {
        this(listenAddress, callback, null, 0, true);
    }

    /**
     * <p>Creates a new listen service for the provided {@link java.net.InetSocketAddress}. You will need to explicitly call {@link #listen()} to start listening for source log events</p>
     *
     * @param listenAddress   The {@link java.net.InetSocketAddress} to listen on
     * @param callback        The {@link java.util.function.Consumer} callback that will be notified once a log event has been received
     * @param executorService The {@link java.util.concurrent.ExecutorService} that will be used by the service. {@code null} to use the global executor provided by the library.
     * @param nThreads        The number of threads that will be used by the underlying {@link io.netty.channel.EventLoopGroup} (a special executor servicee used by netty). The value should normally be less than or equals to the core pool size of the executor service.
     * @param useNative       {@code true} if you prefer to use netty's <a href="https://netty.io/wiki/native-transports.html">native transports</a> over java's NIO (e.g. epoll on linux, kqueue on osx)
     */
    public SourceLogListenService(InetSocketAddress listenAddress, Consumer<SourceLogEntry> callback, ExecutorService executorService, int nThreads, boolean useNative) {
        EventLoopGroup group;
        if (executorService == null)
            executorService = Platform.getDefaultExecutor();
        //ensure arguments are available
        if (nThreads < 0) {
            log.debug("LOG SERVICE => Using default nThreads parameter = 0");
            nThreads = 0;
        }
        if (Platform.isDefaultExecutor(executorService)) {
            group = Platform.getDefaultEventLoopGroup();
        } else {
            group = Platform.getOrCreateEventLoopGroup(executorService, nThreads, useNative);
        }
        final Class<? extends Channel> channelClass = Platform.getChannelClass(TransportType.UDP, group);
        log.debug("LOG SERVICE => Executor: {}, Event loop group: {}, Channel class: {}, nThreads: {}, useNative: {}", executorService, group, channelClass, nThreads, useNative);
        this.listenAddress = listenAddress;
        this.group = group;
        setLogEventCallback(callback);
        bootstrap = new Bootstrap()
                .localAddress(listenAddress)
                .channel(channelClass)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .attr(SourceLogListenHandler.CALLBACK, callbackRef)
                .group(group)
                .handler(new ChannelInitializer<DatagramChannel>() {
                    @Override
                    protected void initChannel(@NotNull DatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new SourceLogListenHandler());
                    }
                });
    }

    /**
     * <p>Retrieve the callback assigned for Raw Log Events</p>
     *
     * @return A {@link java.util.function.Consumer} representing the raw log event callback
     */
    public Consumer<SourceLogEntry> getLogEventCallback() {
        return callbackRef.get();
    }

    /**
     * <p>Sets the callback for listening on Raw Log Events</p>
     *
     * @param callback A {@link java.util.function.Consumer} callback for raw log events
     */
    public void setLogEventCallback(Consumer<SourceLogEntry> callback) {
        this.callbackRef.set(callback);
    }

    /**
     * <p>Getter for the field <code>listenAddress</code>.</p>
     *
     * @return The {@link java.net.InetSocketAddress} where the service is binded to
     */
    public InetSocketAddress getListenAddress() {
        return listenAddress;
    }

    /**
     * Sets the listen address.
     *
     * @param listenAddress The {@link java.net.InetSocketAddress} to listen on
     * @throws java.lang.IllegalStateException If the service has been started already
     */
    public void setListenAddress(InetSocketAddress listenAddress) {
        if (started)
            throw new IllegalStateException("Could not set address once the service has started");
        this.listenAddress = listenAddress;
    }

    /**
     * Start listening for log messages. Please note that this is a non-blocking operation. If you need to block until the service is closed, then use the returned future which is notified once the underlying connection is closed.
     *
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once the connection is closed. This is notified either by an interrupt signal (SIGINT) or by invoking {@link #close()}
     * @see #setListenAddress(InetSocketAddress)
     * @see #close()
     */
    public CompletableFuture<Void> listen() {
        Objects.requireNonNull(this.listenAddress, "No listen address provided");
        return listen(this.listenAddress);
    }

    /**
     * Start listening for log messages. Please note that this is a non-blocking operation. If you need to block until the service is closed, then use the returned future which is notified once the underlying connection is closed.
     *
     * @param address The {@link java.net.InetSocketAddress} to listen on
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once the connection is closed. This is notified either by an interrupt signal (SIGINT) or by invoking {@link #close()}
     * @see #close()
     */
    public CompletableFuture<Void> listen(InetSocketAddress address) {
        this.listenAddress = Objects.requireNonNull(address, "Listen address cannot bee null");
        if (bindInProgress)
            throw new IllegalStateException("A bind is already in progreess for address: " + address);
        if (started)
            throw new IllegalStateException("Service has already been started");
        ChannelFuture bindFuture = bootstrap.localAddress(address).bind();
        this.bindInProgress = true;
        CompletableFuture<Void> promise = new CompletableFuture<>();
        if (bindFuture.isDone())
            initialize(bindFuture, promise);
        else
            bindFuture.addListener((ChannelFutureListener) future -> initialize(future, promise));
        return this.listenFuture = promise.whenComplete((unused, throwable) -> bindInProgress = false);
    }

    /**
     * @return The current future instance returned by {@link #listen(InetSocketAddress)} or {@code null} if service is not bounded to an address yet.
     */
    public CompletableFuture<Void> getListenFuture() {
        return this.listenFuture;
    }

    /**
     * @return {@code true} if service is currently bounded to an address
     */
    public boolean isListening() {
        return this.listenFuture != null && !this.listenFuture.isDone();
    }

    private void initialize(ChannelFuture future, CompletableFuture<Void> promise) {
        if (this.started) {
            throw new IllegalStateException("Service has already been started");
        }
        if (future.isSuccess()) {
            Channel channel = future.channel();
            this.started = true;
            this.bindInProgress = false;
            String hostAddress = ((InetSocketAddress) channel.localAddress()).getAddress().getHostAddress();
            int port = ((InetSocketAddress) channel.localAddress()).getPort();
            log.debug("LOG SERVICE => Log Service listening on port {} via address {} ({})", port, hostAddress, channel);
            ChannelFuture closeFuture = channel.closeFuture();
            if (closeFuture.isDone()) {
                notifyOnClose(closeFuture, promise);
            } else {
                closeFuture.addListener((ChannelFutureListener) cFuture -> notifyOnClose(cFuture, promise));
            }
        } else {
            throw new IllegalStateException(future.cause());
        }
    }

    private void notifyOnClose(ChannelFuture future, CompletableFuture<Void> promise) {
        if (future.isSuccess()) {
            log.debug("LOG SERVICE => Service Shutting Down");
            promise.complete(null);
        } else {
            promise.completeExceptionally(future.cause());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        try {
            log.debug("LOG SERVICE => Requesting to shutdown log service");
            group.shutdownGracefully(10, GeneralOptions.CLOSE_TIMEOUT.getDefaultValue(), TimeUnit.SECONDS).sync();
            log.debug("LOG SERVICE => Event loop group shutdown gracefully");
        } catch (InterruptedException e) {
            log.debug("LOG SERVICE => Shutdown interrupted");
            throw new IOException(e);
        }
    }
}
