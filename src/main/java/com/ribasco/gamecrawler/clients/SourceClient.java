/*
 * MIT License
 *
 * Copyright (c) [year] [fullname]
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
 */

package com.ribasco.gamecrawler.clients;

import com.ribasco.gamecrawler.exceptions.GameCrawlerException;
import com.ribasco.gamecrawler.protocols.ResponseCallback;
import com.ribasco.gamecrawler.protocols.handlers.ErrorHandler;
import com.ribasco.gamecrawler.protocols.valve.server.SourceMasterFilter;
import com.ribasco.gamecrawler.protocols.valve.server.SourcePlayer;
import com.ribasco.gamecrawler.protocols.valve.server.SourceServer;
import com.ribasco.gamecrawler.protocols.valve.server.handlers.SourceRequestEncoder;
import com.ribasco.gamecrawler.protocols.valve.server.handlers.SourceResponseDecoder;
import com.ribasco.gamecrawler.protocols.valve.server.handlers.SourcePacketHandler;
import com.ribasco.gamecrawler.protocols.valve.server.handlers.SourceResponseHandler;
import com.ribasco.gamecrawler.protocols.valve.server.packets.requests.*;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.ribasco.gamecrawler.protocols.valve.server.SourceConstants.REQUEST_PLAYER_HEADER;

public class SourceClient extends GameClient<NioDatagramChannel> {
    private static final Logger log = LoggerFactory.getLogger(SourceClient.class);
    private Map<String, Integer> challengeMap;

    public SourceClient() {
        super(new NioEventLoopGroup(1));
        challengeMap = new HashMap<>();
    }

    @Override
    protected void configureChannel(NioDatagramChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new WriteTimeoutHandler(3));
        p.addLast(new SourcePacketHandler());
        p.addLast(new SourceRequestEncoder());
        p.addLast(new SourceResponseDecoder());
        p.addLast(new SourceResponseHandler());
        p.addLast(new ErrorHandler());
    }

    public Promise<Vector<InetSocketAddress>> getServersFromMaster(byte region, SourceMasterFilter filter, ResponseCallback<InetSocketAddress> responseCallback) {
        //Send and receive a batch of master server list
        log.debug("Sending Master Server Request");

        Promise<Vector<InetSocketAddress>> sdPromise = createPromise();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();

        //As per protocol specs, this is required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        boolean isDone = false;

        //Create our task listener
        GenericFutureListener<Future<? super Vector<InetSocketAddress>>> listener = future -> {
            if (!future.isSuccess() && future.cause() != null) {
                sdPromise.tryFailure(future.cause());
                //responseCallback.onComplete(null, future.cause());
            }
        };

        final InetSocketAddress destination = SourceMasterRequestPacket.SOURCE_MASTER;

        while (!isDone) {
            try {
                log.debug("Sending master server with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter.toString());

                //Send initial query to the master server
                Promise<Vector<InetSocketAddress>> p = sendGameServerRequest(destination, new SourceMasterRequestPacket(region, filter, startAddress));

                p.addListener(listener);

                //Retrieve the first batch, timeout after 2.5 seconds
                Vector<InetSocketAddress> serverList = p.get(2500, TimeUnit.MILLISECONDS);

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last server ip received
                final InetSocketAddress lastServerIp = startAddress;

                //With streams, we can easily filter out the unwanted entries. (e.g. Not including the last server ip we just received)
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEach(ip -> {
                    responseCallback.onComplete(ip, destination, null);
                    serverMasterList.add(ip);
                });

                //Retrieve the last element of the server list and use it as the next seed for the next query
                startAddress = serverList.lastElement();

                log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                //Did we reach the end? (Master Server sometimes sends a 0.0.0.0:0 address if we have reached the end)
                if ("0.0.0.0".equals(startAddress.getAddress().getHostAddress()) && startAddress.getPort() == 0)
                    isDone = true;

                //Sleep
                Thread.sleep(50);
            } catch (TimeoutException e) {
                log.warn("Timeout/Thread Interruption Occured during retrieval of server list");
                isDone = true;
            } catch (ExecutionException e) {
                if (!sdPromise.isDone())
                    sdPromise.setFailure(e.getCause());
                responseCallback.onComplete(null, destination, e);
                return sdPromise;
            } catch (InterruptedException e) {
                log.error("Sleep interrupted", e);
            }
        } //while

        //If this has not been marked yet and we have reached this point, then we can safely assume the operation was a success?
        if (!sdPromise.isDone()) {
            //Mark the operation as isComplete, send back the master list
            //sdPromise.setSuccess(serverMasterList);
            sdPromise.setSuccess(null);
        }

        //Notify our callback that we have completed the operation
        //responseCallback.onComplete(null, null);

        return sdPromise;
    }

    public Promise<Integer> getServerChallenge(InetSocketAddress address, byte type, ResponseCallback<Integer> responseCallback) {
        return sendGameServerRequest(address, new SourceChallengeRequestPacket(type), responseCallback);
    }

    public Promise<SourceServer> getServerDetails(InetSocketAddress address, ResponseCallback<SourceServer> responseCallback) {
        return sendGameServerRequest(address, new SourceInfoRequestPacket(), responseCallback);
    }

    public Promise<List<SourcePlayer>> getPlayerDetails(InetSocketAddress address, ResponseCallback<List<SourcePlayer>> responseCallback) {
        //TODO: retrieve from the cache or retrieve directly from the server
        Promise<List<SourcePlayer>> spPromise = createPromise();
        try {
            final int[] challengeRes = {-1};
            getServerChallenge(address, REQUEST_PLAYER_HEADER, (challenge, sender, error) -> {
                if (error != null) {
                    spPromise.tryFailure(error);
                    responseCallback.onComplete(null, address, error);
                    return;
                }
                challengeRes[0] = challenge;
            }).await(30, TimeUnit.SECONDS);

            if (!spPromise.isDone()) {
                //Wait for result
                if (challengeRes[0] > -1) {
                    getPlayerDetails(challengeRes[0], address, (msg, sender, error) -> {
                        if (error != null)
                            spPromise.tryFailure(error);
                        responseCallback.onComplete(msg, sender, error);
                    });
                } else {
                    Throwable e = new GameCrawlerException("Invalid Challenge Number. Cancelling Request.");
                    spPromise.tryFailure(e);
                    responseCallback.onComplete(null, address, e);
                }
            }
        } catch (InterruptedException e) {
            spPromise.tryFailure(e);
            responseCallback.onComplete(null, address, e);
        }
        return spPromise;
    }

    public Promise<List<SourcePlayer>> getPlayerDetails(int challenge, InetSocketAddress address, ResponseCallback<List<SourcePlayer>> responseCallback) {
        return sendGameServerRequest(address, new SourcePlayerRequestPacket(challenge), responseCallback);
    }

    public Promise<Map<String, String>> getServerRules(InetSocketAddress address, ResponseCallback<Map<String, String>> responseCallback) {
        throw new IllegalStateException("Not yet implemented");
    }

    public Promise<Map<String, String>> getServerRules(int challenge, InetSocketAddress address, ResponseCallback<Map<String, String>> responseCallback) {

        return sendGameServerRequest(address, new SourceRulesRequestPacket(challenge), responseCallback);
    }

    public int getCancelledTaskCount() {
        return cancelledTasks.get();
    }
}
