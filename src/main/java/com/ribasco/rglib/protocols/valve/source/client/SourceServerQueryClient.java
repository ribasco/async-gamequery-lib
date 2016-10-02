/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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

package com.ribasco.rglib.protocols.valve.source.client;

import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.client.GameServerQueryClient;
import com.ribasco.rglib.protocols.valve.source.SourceMasterFilter;
import com.ribasco.rglib.protocols.valve.source.SourceServerMessenger;
import com.ribasco.rglib.protocols.valve.source.SourceServerRequest;
import com.ribasco.rglib.protocols.valve.source.SourceServerResponse;
import com.ribasco.rglib.protocols.valve.source.enums.SourceChallengeType;
import com.ribasco.rglib.protocols.valve.source.enums.SourceMasterServerRegion;
import com.ribasco.rglib.protocols.valve.source.packets.request.SourceMasterRequestPacket;
import com.ribasco.rglib.protocols.valve.source.pojos.SourcePlayer;
import com.ribasco.rglib.protocols.valve.source.pojos.SourceServer;
import com.ribasco.rglib.protocols.valve.source.request.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceServerQueryClient extends GameServerQueryClient<SourceServerRequest, SourceServerResponse, SourceServerMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceServerQueryClient.class);

    private Map<Integer, InetSocketAddress> challengeMap = new HashMap<>();

    private final class MasterErrorListener implements GenericFutureListener<Promise<Vector<InetSocketAddress>>> {

        private Promise sdPromise;
        private Callback callback;
        private InetSocketAddress destination;

        public MasterErrorListener(Promise sdPromise, Callback callback, InetSocketAddress destination) {
            this.sdPromise = sdPromise;
            this.callback = callback;
            this.destination = destination;
        }

        @Override
        public void operationComplete(Promise<Vector<InetSocketAddress>> future) throws Exception {
            if (future.isDone()) {
                if (!future.isSuccess() && future.cause() != null) {
                    sdPromise.tryFailure(future.cause());
                    callback.onComplete(null, destination, future.cause());
                }
            }
        }
    }

    public SourceServerQueryClient() {
        super(new SourceServerMessenger());
    }

    public Future<Map<String, String>> getServerRules(int challenge, InetSocketAddress address, Callback<Map<String, String>> callback) {
        return sendRequest(new SourceRulesRequest(challenge, address), callback);
    }

    public Future<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address, Callback<Integer> callback) {
        return sendRequest(new SourceChallengeRequest(type, address), callback);
    }

    public Future<List<SourcePlayer>> getPlayers(int challenge, InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return sendRequest(new SourcePlayerRequest(challenge, address), callback);
    }

    public Future<SourceServer> getServerInfo(InetSocketAddress address, Callback<SourceServer> callback) {
        return sendRequest(new SourceInfoRequest(address), callback);
    }

    public Future<Vector<InetSocketAddress>> getMasterServerList(SourceMasterServerRegion region, SourceMasterFilter filter, Callback<InetSocketAddress> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        final Promise<Vector<InetSocketAddress>> sdPromise = getMessenger().getTransport().newPromise();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = SourceMasterRequestPacket.SOURCE_MASTER;

        boolean isDone = false;

        final AtomicBoolean done = new AtomicBoolean();

        while (!done.get()) {
            System.err.println("Getting from master server...");
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter);

                //Send initial query to the master source
                final Promise<Vector<InetSocketAddress>> p = sendRequest(new SourceMasterServerRequest(destination, region, filter, startAddress));
                p.addListener(new MasterErrorListener(sdPromise, callback, destination));

                //Retrieve the first batch, timeout after 2.5 seconds
                //final SourceMasterServerResponse response = p.get(2500, TimeUnit.MILLISECONDS);
                final Vector<InetSocketAddress> serverList = p.get(2500, TimeUnit.MILLISECONDS);//response.getMessage();

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = startAddress;

                //With streams, we can easily filter out the unwanted entries. (e.g. Excluding the last source ip we just received)
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    try {
                        callback.onComplete(ip, destination, null);
                        //Add a delay here. We shouldn't send requests too fast to the master server
                        // there is a high chance that we might not receive the end of the list.
                        Thread.sleep(13);
                        serverMasterList.add(ip);
                    } catch (InterruptedException ignored) {
                    }
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                startAddress = serverList.lastElement();

                log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                //Did we reach the end? (Master Server sometimes sends a 0.0.0.0:0 address if we have reached the end)
                if ("0.0.0.0".equals(startAddress.getAddress().getHostAddress()) && startAddress.getPort() == 0) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }

                //Thread.sleep(serverList.size() * 15);
            } catch (InterruptedException | TimeoutException e) {
                log.debug("Timeout/Thread Interruption Occured during retrieval of source list");
                done.set(true);
            } catch (ExecutionException e) {
                sdPromise.tryFailure(e.getCause());
                callback.onComplete(null, destination, e);
                return sdPromise;
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        sdPromise.trySuccess(serverMasterList);

        return sdPromise;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
