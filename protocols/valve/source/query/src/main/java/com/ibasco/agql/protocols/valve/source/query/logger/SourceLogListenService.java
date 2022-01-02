/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.logger;

import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * <p>Listens for raw log messages from a Source Based Server.</p>
 * <p>
 * <p>
 * <strong>NOTE:</strong> You need to issue the RCON Command <code>'logaddress_add [ip:port]'</code> to receive log
 * messages from the game server.
 * Make sure that the port specified is not being blocked by an external service or firewall
 * </p>
 * <h3>Example Usage</h3>
 * <pre>
 * {@code
 *  SourceLogListenService service = new SourceLogListenService(new InetSocketAddress(0));
 *
 *     service.setLogEventCallback(SourceLogEventHandler::handleLogMessages);
 *     service.listen();
 *
 *     public class SourceLogEventPrinter {
 *         public static void handleLogMessages(SourceLogEntry logEntry) {
 *             System.out.println(logEntry.getMessage());
 *         }
 *     }
 * }
 * </pre>
 */
public class SourceLogListenService implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceLogListenService.class);

    private static final NioEventLoopGroup listenWorkGroup = new NioEventLoopGroup();

    private InetSocketAddress listenAddress;

    private final Bootstrap bootstrap;

    private Consumer<SourceLogEntry> logEventCallback;

    /**
     * <p>Creates a new service that will listen to any ip address
     * and bind to a random local port number (Similar to 0.0.0.0</p>
     */
    public SourceLogListenService() {
        this(new InetSocketAddress(0));
    }

    /**
     * <p>Creates a new service using the specified {@link InetSocketAddress} to listen on.</p>
     *
     * @param listenAddress
     *         An {@link InetSocketAddress} where the listen service will bind or listen on
     */
    public SourceLogListenService(InetSocketAddress listenAddress) {
        this(listenAddress, null);
    }

    /**
     * <p>Creates a new service using the specified {@link InetSocketAddress} to listen on and utilizing
     * the callback specified to notify listeners of source log events</p>
     *
     * @param listenAddress
     *         An {@link InetSocketAddress} where the listen service will bind or listen on
     * @param logEventCallback
     *         A {@link Consumer} callback that will be called once a log event has been received
     */
    public SourceLogListenService(InetSocketAddress listenAddress, Consumer<SourceLogEntry> logEventCallback) {
        this.listenAddress = listenAddress;
        bootstrap = new Bootstrap()
                .localAddress(this.listenAddress)
                .channel(NioDatagramChannel.class)
                .group(listenWorkGroup)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new SourceLogListenHandler(logEventCallback));
                    }
                });

        //Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.debug("Service Interrupted. Shutting down gracefully.");
                SourceLogListenService.this.shutdown();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

    /**
     * <p>Retrieve the callback assigned for Raw Log Events</p>
     *
     * @return A {@link Consumer} representing the raw log event callback
     */
    public Consumer<SourceLogEntry> getLogEventCallback() {
        return logEventCallback;
    }

    /**
     * <p>Sets the callback for listening on Raw Log Events</p>
     *
     * @param logEventCallback
     *         A {@link Consumer} callback for raw log events
     */
    public void setLogEventCallback(Consumer<SourceLogEntry> logEventCallback) {
        this.logEventCallback = logEventCallback;
    }

    public InetSocketAddress getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(InetSocketAddress listenAddress) {
        this.listenAddress = listenAddress;
    }

    /**
     * Start listening for log messages
     *
     * @throws InterruptedException
     *         When the service is interrupted
     */
    public void listen() throws InterruptedException {
        ChannelFuture bindFuture = bootstrap.localAddress(listenAddress).bind().await();
        bindFuture.addListener((ChannelFuture future) -> {
            String hostAddress = ((InetSocketAddress) future.channel().localAddress()).getAddress().getHostAddress();
            int port = ((InetSocketAddress) future.channel().localAddress()).getPort();
            log.debug("Log Service listening on port {} via address {}", port, hostAddress);
            future.channel().closeFuture().addListener(future1 -> log.debug("Service Shutting Down"));
        });
    }

    /**
     * <p>Tries to shutdown the listener gracefully</p>
     *
     * @deprecated Use {@link #close()}
     */
    @Deprecated
    public void shutdown() throws InterruptedException {
        listenWorkGroup.shutdownGracefully(10, 10, TimeUnit.SECONDS);
    }

    /**
     * Calls the {@link #shutdown()} method
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            log.debug("Closing service");
            shutdown();
        } catch (InterruptedException e) {
            throw new AsyncGameLibUncheckedException(e);
        }
    }
}
