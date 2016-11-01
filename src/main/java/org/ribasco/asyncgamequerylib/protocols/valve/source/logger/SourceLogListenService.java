/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.protocols.valve.source.logger;

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
 * <strong>NOTE:</strong> You need to issue the RCON Command <code>'logaddress_add [ip:port]'</code> to receive log messages from the game server.
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
    private InetSocketAddress listenAddress;
    private Bootstrap bootstrap;
    private static final NioEventLoopGroup listenWorkGroup = new NioEventLoopGroup();
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
     * @param listenAddress An {@link InetSocketAddress} where the listen service will bind or listen on
     */
    public SourceLogListenService(InetSocketAddress listenAddress) {
        this(listenAddress, null);
    }

    /**
     * <p>Creates a new service using the specified {@link InetSocketAddress} to listen on and utilizing
     * the callback specified to notify listeners of source log events</p>
     *
     * @param listenAddress    An {@link InetSocketAddress} where the listen service will bind or listen on
     * @param logEventCallback A {@link Consumer} callback that will be called once a log event has been received
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

        SourceLogListenService service = this;
        //Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    log.info("Interrupt Found. Trying to shutdown gracefully");
                    service.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
     * @param logEventCallback A {@link Consumer} callback for raw log events
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
     */
    public void listen() {
        final ChannelFuture bindFuture = bootstrap.localAddress(listenAddress).bind().syncUninterruptibly();
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
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        listenWorkGroup.shutdownGracefully();
        listenWorkGroup.awaitTermination(10, TimeUnit.SECONDS);
    }

    /**
     * Calls the {@link #shutdown()} method
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
