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

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * <p>Listens for log messages from a Source Based Server</p>
 * <p>Note: You need to issue the RCON Command <code>'logaddress_add [ip:port]'</code> in the source server to use this service</p>
 */
public class SourceLogListenService {
    private static final Logger log = LoggerFactory.getLogger(SourceLogListenService.class);
    private InetSocketAddress listenAddress;
    private Bootstrap bootstrap;
    private static final NioEventLoopGroup listenWorkGroup = new NioEventLoopGroup();
    private Consumer<RawLogEntry> rawLogEventCallback;

    public SourceLogListenService(InetSocketAddress listenAddress) {
        this(listenAddress, null);
    }

    public SourceLogListenService(InetSocketAddress listenAddress, Consumer<RawLogEntry> rawLogEventCallback) {
        this.listenAddress = listenAddress;
        bootstrap = new Bootstrap()
                .localAddress(this.listenAddress)
                .channel(NioDatagramChannel.class)
                .group(listenWorkGroup)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new SourceLogListenHandler(rawLogEventCallback));
                    }
                });
    }

    /**
     * <p>Retrieve the callback assigned for Raw Log Events</p>
     *
     * @return A {@link Consumer} representing the raw log event callback
     */
    public Consumer<RawLogEntry> getRawLogEventCallback() {
        return rawLogEventCallback;
    }

    /**
     * <p>Sets the callback for listening on Raw Log Events</p>
     *
     * @param rawLogEventCallback A {@link Consumer} callback for raw log events
     */
    public void setRawLogEventCallback(Consumer<RawLogEntry> rawLogEventCallback) {
        this.rawLogEventCallback = rawLogEventCallback;
    }

    public InetSocketAddress getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(InetSocketAddress listenAddress) {
        this.listenAddress = listenAddress;
    }

    public void listen() {
        final ChannelFuture bindFuture = bootstrap.localAddress(listenAddress).bind().syncUninterruptibly();
        bindFuture.addListener((ChannelFuture future) -> {
            log.debug("Log Service Started using {}", future.channel().localAddress());
            future.channel().closeFuture().addListener(future1 -> log.debug("Service Shutting Down"));
        });
    }

    public void shutdown() throws InterruptedException {
        listenWorkGroup.shutdownGracefully();
        listenWorkGroup.awaitTermination(10, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        SourceLogListenService logListenService = new SourceLogListenService(new InetSocketAddress("192.168.1.10", 27500));
        logListenService.setRawLogEventCallback(SourceLogListenService::processLogData);
        logListenService.listen();
    }

    private static void processLogData(RawLogEntry message) {
        log.info("Got Data : {}", message);
    }
}
