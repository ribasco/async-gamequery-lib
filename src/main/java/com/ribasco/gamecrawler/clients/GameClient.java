package com.ribasco.gamecrawler.clients;

import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.GameRequestEnvelope;
import com.ribasco.gamecrawler.protocols.Session;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by raffy on 8/27/2016.
 */
public abstract class GameClient<T extends Channel> implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(GameClient.class);

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private T channel;

    public GameClient(EventLoopGroup group) {
        this.group = group;
        this.bootstrap = new Bootstrap();

        //Initialize the Client
        initialize();
    }

    protected void initialize() {
        //Configure our bootstrap
        configureBootstrap(bootstrap);

        try {
            if (log.isDebugEnabled())
                ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            //Start the client by initiating a local bind
            channel = (T) bootstrap.bind(0).sync().channel();

            log.info("Now listening in local port: {}", ((InetSocketAddress) channel.localAddress()).getPort());
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }

    protected void configureBootstrap(Bootstrap bootstrap) {
        //Configure our bootstrap
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_SNDBUF, 1048576)
                .option(ChannelOption.SO_RCVBUF, 1048576)
                .handler(new ChannelInitializer<T>() {
                    @Override
                    protected void initChannel(T ch) throws Exception {
                        initializeChannels(ch);
                    }
                });
    }

    protected Promise sendQuery(InetSocketAddress destination, GameRequest query) {
        Promise promise = channel.eventLoop().newPromise();
        try {
            //Store the request/promise instance to the registry
            getRequestMap().put(createSessionId(destination, query.getClass()), promise);

            //If not writable, wait till it becomes available
            while (!channel.isWritable()) {
                log.debug("Waiting until outbound buffer is available for writing");
                Thread.sleep(1000);
            }

            //Send the messenger/mailman to it's designated handler
            channel.writeAndFlush(new GameRequestEnvelope<>(query, destination)).addListener(
                    (ChannelFutureListener) future -> {
                        //Write has failed
                        if (!future.isSuccess())
                            promise.setFailure(future.cause());
                    }
            ).await();
        } catch (InterruptedException e) {
            promise.setFailure(e);
        }
        return promise;
    }

    protected void waitForAll(int timeout) {
        if (getRequestMap().size() > 0) {
            log.debug("There are still {} pending requests that have not received any reply from the server. Channel ", getRequestMap().size());

            int ctr = 0;
            for (Map.Entry<String, Promise> entry : getRequestMap().entrySet()) {
                log.debug("--> {}) REQUEST: {}", ++ctr, entry.getKey());
            }

            try {
                int timeoutCtr = 0;
                while (getRequestMap().size() > 0) {
                    Thread.sleep(1000);
                    System.out.print(".");
                    if (++timeoutCtr > timeout) {
                        System.out.println();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                log.error("Interrupted", e.getMessage());
            }
        }
    }

    protected Promise createPromise() {
        return this.channel.newPromise();
    }

    protected String createSessionId(InetSocketAddress destination, Class<? extends GameRequest> requestClass) {
        return Session.getSessionId(destination, requestClass);
    }

    protected Map<String, Promise> getRequestMap() {
        return Session.getRegistry();
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing Client");
        group.shutdownGracefully();
    }

    protected abstract void initializeChannels(T channel);
}
