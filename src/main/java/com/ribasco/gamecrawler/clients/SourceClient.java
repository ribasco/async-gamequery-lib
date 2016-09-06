package com.ribasco.gamecrawler.clients;

import com.ribasco.gamecrawler.protocols.Callback;
import com.ribasco.gamecrawler.protocols.handlers.ErrorHandler;
import com.ribasco.gamecrawler.protocols.valve.source.SourceMasterFilter;
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
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
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

public class SourceClient extends GameClient<NioDatagramChannel> {
    private static final Logger log = LoggerFactory.getLogger(SourceClient.class);

    public SourceClient() {
        super(new NioEventLoopGroup());
    }

    @Override
    protected void initializeChannels(NioDatagramChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        //pipeline.addLast(new ReadTimeoutHandler(3)); //if no inbound traffic is received for 2 seconds, signal for shutdown
        pipeline.addLast(new ErrorHandler());
        pipeline.addLast(new SourceResponseDiscardHandler());
        pipeline.addLast(new SourceRequestEncoder());
        pipeline.addLast(new SourceResponseDecoder());
        pipeline.addLast(new SourceResponseHandler());
    }

    public Promise<InetSocketAddress> getServersFromMaster(byte region, SourceMasterFilter filter, Callback<InetSocketAddress> callback) {
        //Send and receive a batch of master server list
        log.debug("Sending Master Server Request");
        Promise sdPromise = createPromise();

        //As per protocol specs, this is required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        try {
            boolean isDone = false;

            while (!isDone) {
                try {
                    log.debug("Sending master server with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter.toString());

                    //Send initial query to the master server
                    Promise<Vector<InetSocketAddress>> p = sendQuery(SourceMasterRequest.SOURCE_MASTER, new SourceMasterRequest(region, filter.toString(), startAddress));

                    //Retrieve the first batch
                    Vector<InetSocketAddress> serverList = p.get(2500, TimeUnit.MILLISECONDS);

                    //Iterate through each address and call onReceive callback. Make sure we don't include the last server ip received
                    final InetSocketAddress lastServerIp = startAddress;
                    serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEach(callback::onReceive);

                    //Sleep
                    Thread.sleep(10);

                    //Retrieve the last element of the server list and use it as the next seed for the next query
                    startAddress = serverList.lastElement();

                    log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                    //Did we reach the end? (Master Server sometimes sends a 0.0.0.0:0 address if we have reached the end)
                    if ("0.0.0.0".equals(startAddress.getAddress().getHostAddress()) && startAddress.getPort() == 0)
                        isDone = true;
                } catch (TimeoutException | InterruptedException e) {
                    log.warn("Timeout Occured during retrieval of server list");
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

    public static void main(String[] args) {

        //Run client test code
        try (SourceClient client = new SourceClient()) {

            final AtomicInteger ctr1 = new AtomicInteger();
            final AtomicInteger ctr2 = new AtomicInteger();
            final AtomicInteger ctr3 = new AtomicInteger();

            SourceMasterFilter masterFilter = new SourceMasterFilter();

            //Retrieve servers from master
            client.getServersFromMaster(SourceMasterRequest.REGION_ALL, masterFilter.appId(550), serverAddress -> {
                System.out.print(".");

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
