package com.ribasco.gamecrawler.clients;

import com.ribasco.gamecrawler.protocols.Callback;
import com.ribasco.gamecrawler.protocols.GameRequest;
import com.ribasco.gamecrawler.protocols.GameRequestEnvelope;
import com.ribasco.gamecrawler.protocols.Session;
import com.ribasco.gamecrawler.protocols.handlers.ErrorHandler;
import com.ribasco.gamecrawler.protocols.valve.source.SourcePlayer;
import com.ribasco.gamecrawler.protocols.valve.source.SourceServer;
import com.ribasco.gamecrawler.protocols.valve.source.handlers.SourceRequestEncoder;
import com.ribasco.gamecrawler.protocols.valve.source.handlers.SourceResponseDecoder;
import com.ribasco.gamecrawler.protocols.valve.source.handlers.SourceResponseDiscardHandler;
import com.ribasco.gamecrawler.protocols.valve.source.handlers.SourceResponseHandler;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceChallengeRequest;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceInfoRequest;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourceMasterRequest;
import com.ribasco.gamecrawler.protocols.valve.source.requests.SourcePlayerRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceQueryClient extends GameQueryClient {
    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private Channel datagramChannel;
    private Map<String, Promise> requestMap;
    public final static AttributeKey<Map<String, Promise>> REGISTRY = AttributeKey.valueOf("requestMap");

    public SourceQueryClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        requestMap = Session.getRegistry();

        //Configure our bootstrap
        configureBootstrap(bootstrap);

        try {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            //Start the client by initiating a local bind
            datagramChannel = bootstrap.bind(0).sync().channel();
            //Set datagramChannel attributes
            datagramChannel.attr(REGISTRY).set(requestMap);
            log.debug("Local Address Bind: {}", datagramChannel.localAddress());
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }

    public void configureBootstrap(Bootstrap bootstrap) {

        //Configure our bootstrap
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_SNDBUF, 1048576)
                .option(ChannelOption.SO_RCVBUF, 1048576)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //pipeline.addLast(new ReadTimeoutHandler(3)); //if no inbound traffic is received for 2 seconds, signal for shutdown
                        pipeline.addLast(new ErrorHandler());
                        pipeline.addLast(new SourceResponseDiscardHandler());
                        pipeline.addLast(new SourceRequestEncoder());
                        pipeline.addLast(new SourceResponseDecoder());
                        pipeline.addLast(new SourceResponseHandler());
                    }
                });
        ;
    }

    public Promise<InetSocketAddress> getServersFromMaster(byte region, String filter, Callback<InetSocketAddress> callback) {
        //Send and receive a batch of master server list
        log.debug("Sending Master Server Request");
        Promise sdPromise = datagramChannel.eventLoop().newPromise();

        //As per protocol specs, this is required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        try {
            boolean isDone = false;

            while (!isDone) {
                try {
                    log.info("Sending master server with seed: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                    //Send initial query to the master server
                    Promise<Vector<InetSocketAddress>> p = sendQuery(SourceMasterRequest.SOURCE_MASTER, new SourceMasterRequest(region, filter, startAddress));

                    //Retrieve the first batch
                    Vector<InetSocketAddress> serverList = p.get(2500, TimeUnit.MILLISECONDS);

                    //Iterate through each address and call onReceive callback. Make sure we don't include the last server ip received
                    final InetSocketAddress lastServerIp = startAddress;
                    serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEach(callback::onReceive);

                    //Retrieve the last element of the server list and use it as the next seed for the next query
                    startAddress = serverList.lastElement();

                    log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                    //Did we reach the end? (Master Server sometimes sends a 0.0.0.0:0 address if we have reached the end)
                    if ("0.0.0.0".equals(startAddress.getAddress().getHostAddress()) && startAddress.getPort() == 0)
                        isDone = true;
                } catch (TimeoutException | InterruptedException e) {
                    log.debug("Timeout Occured during retrieval of server list");
                    isDone = true;
                }
            }

            //Mark the operation as complete
            sdPromise.setSuccess(null);
        } catch (ExecutionException e) {
            sdPromise.setFailure(e.getCause());
        }

        return sdPromise;
    }

    public Promise<Integer> getServerChallenge(InetSocketAddress address, Callback<Integer> callback) {
        Promise<Integer> p = sendQuery(address, new SourceChallengeRequest());
        p.addListener(future -> {
            if (future.isSuccess())
                callback.onReceive(p.get());
            else
                p.setFailure(future.cause());
        });
        return p;
    }

    public Promise<SourceServer> getServerDetails(InetSocketAddress address, Callback<SourceServer> callback) {
        Promise<SourceServer> p = sendQuery(address, new SourceInfoRequest());
        p.addListener(future -> {
            if (future.isSuccess()) {
                callback.onReceive(p.get());
            } else
                p.setFailure(future.cause());
        });
        return p;
    }

    public Promise<List<SourcePlayer>> getPlayerDetails(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        Promise<List<SourcePlayer>> p = sendQuery(address, new SourcePlayerRequest());
        p.addListener(future -> {
            if (future.isSuccess()) {
                callback.onReceive(p.get());
            } else
                p.setFailure(future.cause());
        });
        return p;
    }

    private Promise sendQuery(InetSocketAddress destination, GameRequest query) {
        Promise promise = datagramChannel.eventLoop().newPromise();
        try {
            //Store the request/promise instance to the registry
            requestMap.put(Session.getSessionId(destination, query.getClass()), promise);

            //If not writable, wait till it becomes available
            while (!datagramChannel.isWritable()) {
                log.debug("Waiting until outbound buffer is available for writing");
                Thread.sleep(1000);
            }

            //Send the messenger/mailman to it's designated handler
            datagramChannel.writeAndFlush(new GameRequestEnvelope<>(query, destination)).addListener(
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

    @Override
    public void close() throws IOException {
        log.debug("Closing Client");
        group.shutdownGracefully();
    }

    private void waitForAll(int timeout) {
        if (requestMap.size() > 0) {
            log.debug("There are still {} pending requests that have not received any reply from the server. Channel ", requestMap.size());

            int ctr = 0;
            for (Map.Entry<String, Promise> entry : requestMap.entrySet()) {
                log.debug("--> {}) REQUEST: {}", ++ctr, entry.getKey());
            }

            try {
                int timeoutCtr = 0;
                while (requestMap.size() > 0) {
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

    public static void main(String[] args) {

        //Run client test code
        try (SourceQueryClient client = new SourceQueryClient()) {

            final AtomicInteger ctr1 = new AtomicInteger();
            final AtomicInteger ctr2 = new AtomicInteger();
            final AtomicInteger ctr3 = new AtomicInteger();

            //Retrieve servers from master
            client.getServersFromMaster(SourceMasterRequest.REGION_ALL, "\\appid\\550", serverAddress -> {
                log.debug("{}) Received from Master Server: {}:{}", ctr1.incrementAndGet(), serverAddress.getAddress().getHostAddress(), serverAddress.getPort());


                //Get Server Details
                client.getServerDetails(serverAddress, msg -> {

                    log.debug("{}) Received Server Info from {}:{} (Name = {}, Map = {}, Players = {}, Max Players = {})", ctr2.incrementAndGet(), serverAddress.getAddress().getHostAddress(), serverAddress.getPort(), msg.getName(), msg.getMapName(), msg.getNumOfPlayers(), msg.getMaxPlayers());

                    //Retrieve challenge number
                    client.getServerChallenge(serverAddress, challengeNumber -> log.debug("{}) Received challenge number {}", ctr3.incrementAndGet(), challengeNumber));
                });
            }).sync();

            log.info("Waiting till all requests have been processed");

            //Iterate remaining requests that have not been processed
            client.waitForAll(5);

            log.info("Received a total of {} master servers, {} server details, {} server challenge numbers", ctr1.get(), ctr2.get(), ctr3.get());
            log.info("Done");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
