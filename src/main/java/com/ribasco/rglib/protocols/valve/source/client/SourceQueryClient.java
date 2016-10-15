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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.client.GameServerQueryClient;
import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.core.utils.ConcurrentUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceQueryClient extends GameServerQueryClient<SourceServerRequest, SourceServerResponse, SourceServerMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    private LoadingCache<InetSocketAddress, Integer> challengeCache;
    final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cache-pool-%d").setDaemon(true).build();
    final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(new ScheduledThreadPoolExecutor(32, threadFactory));
    private static final int MAX_CHALLENGE_CACHE_SIZE = 32000;

    public SourceQueryClient() {
        super(new SourceServerMessenger());
        challengeCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_CHALLENGE_CACHE_SIZE)
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build(new CacheLoader<InetSocketAddress, Integer>() {
                    @Override
                    public Integer load(InetSocketAddress key) throws Exception {
                        try {
                            return getServerChallenge(SourceChallengeType.ANY, key, null).get();
                        } catch (ExecutionException e) {
                            throw e;
                        }
                    }
                });
    }

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
                    if (callback != null)
                        callback.onComplete(null, destination, future.cause());
                }
            }
        }
    }

    public Future<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address, Callback<Integer> callback) {
        return sendRequest(new SourceChallengeRequest(type, address), callback, RequestPriority.REALTIME);
    }

    private final ReentrantLock lock = new ReentrantLock();

    public void getChallengeFromCache(InetSocketAddress address, Callback<Integer> callback) {
        log.debug("Obtaining lock from thread {}", Thread.currentThread().getName());
        //lock.lock();
        try {
            ListenableFuture<Integer> lFuture = executorService.submit(() -> challengeCache.get(address));
            lFuture.addListener(() -> {
                try {
                    int challenge = lFuture.get();
                    callback.onComplete(challenge, address, null);
                } catch (ExecutionException e) {
                    callback.onComplete(null, address, e.getCause());
                } catch (InterruptedException e) {
                    callback.onComplete(null, address, e);
                }
            }, executorService);
        } finally {
            //lock.unlock();
        }
    }

    public Future<Map<String, String>> getServerRules(int challenge, InetSocketAddress address, Callback<Map<String, String>> callback) {
        return sendRequest(new SourceRulesRequest(challenge, address), callback);
    }

    public Future<Map<String, String>> getServerRules(InetSocketAddress address, Callback<Map<String, String>> callback) {
        final Promise<Map<String, String>> pPromise = getMessenger().getTransport().newPromise();
        getServerChallenge(SourceChallengeType.RULES, address, (challenge, sender, error) -> {
            if (error != null) {
                callback.onComplete(null, sender, error);
                pPromise.tryFailure(error);
                return;
            }
            getServerRules(challenge, address, (rules, rulesSender, rulesError) -> {
                if (rulesError != null) {
                    callback.onComplete(null, address, rulesError);
                    pPromise.tryFailure(rulesError);
                    return;
                }
                callback.onComplete(rules, address, null);
                pPromise.trySuccess(rules);
            });
        });
        return pPromise;
    }

    public Future<Map<String, String>> getServerRulesCached(InetSocketAddress address, Callback<Map<String, String>> callback) {
        final Promise<Map<String, String>> pPromise = getMessenger().getTransport().newPromise();

        getChallengeFromCache(address, (challengeNumber, sender, error) -> {
            if (error != null) {
                Throwable err = null;
                if (error instanceof ExecutionException)
                    err = error.getCause();
                else
                    err = error;
                callback.onComplete(null, address, err);
                pPromise.tryFailure(err);
                return;
            }
            //We have successfully received a challenge number
            getServerRules(challengeNumber, address, (response, sender1, error1) -> {
                if (error1 != null) {
                    callback.onComplete(null, address, error1);
                    pPromise.tryFailure(error1);
                    return;
                }
                callback.onComplete(response, address, null);
                pPromise.trySuccess(response);
            });
        });
        return pPromise;
    }

    public Future<List<SourcePlayer>> getPlayers(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        final Promise<List<SourcePlayer>> pPromise = getMessenger().getTransport().newPromise();
        getServerChallenge(SourceChallengeType.PLAYER, address, (challenge, sender, error) -> {
            if (error != null) {
                callback.onComplete(null, address, error);
                pPromise.tryFailure(error);
                return;
            }
            getPlayers(challenge, address, (playerList, playerSender, playerError) -> {
                if (playerError != null) {
                    callback.onComplete(null, address, playerError);
                    pPromise.tryFailure(playerError);
                    return;
                }
                callback.onComplete(playerList, address, null);
                pPromise.trySuccess(playerList);
            });
        });
        return pPromise;
    }

    public Future<List<SourcePlayer>> getPlayers(Integer challenge, InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return sendRequest(new SourcePlayerRequest(challenge, address), callback);
    }

    public Future<List<SourcePlayer>> getPlayersCached(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        final Promise<List<SourcePlayer>> pPromise = getMessenger().getTransport().newPromise();
        getChallengeFromCache(address, (challengeNumber, sender, error) -> {
            if (error != null) {
                Throwable err = null;
                if (error instanceof ExecutionException)
                    err = error.getCause();
                else
                    err = error;
                callback.onComplete(null, address, err);
                pPromise.tryFailure(err);
                return;
            }
            getPlayers(challengeNumber, address, (response, sender1, error1) -> {
                if (error1 != null) {
                    callback.onComplete(null, address, error1);
                    pPromise.tryFailure(error1);
                    return;
                }
                callback.onComplete(response, address, null);
                pPromise.trySuccess(response);
            });
        });

        return pPromise;
    }

    public Future<SourceServer> getServerInfo(InetSocketAddress address, Callback<SourceServer> callback) {
        return sendRequest(new SourceInfoRequest(address), callback);
    }

    private boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }

    public Future<Vector<InetSocketAddress>> getMasterServerList(SourceMasterServerRegion region, SourceMasterFilter filter, Callback<InetSocketAddress> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        final Promise<Vector<InetSocketAddress>> sdPromise = getMessenger().getTransport().newPromise();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = SourceMasterRequestPacket.SOURCE_MASTER;
        final AtomicBoolean done = new AtomicBoolean();

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + startAddress);
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter);

                //Send initial query to the master source
                final Future<Vector<InetSocketAddress>> p = sendRequest(new SourceMasterServerRequest(destination, region, filter, startAddress), RequestPriority.HIGH);
                p.addListener(new MasterErrorListener(sdPromise, callback, destination));

                //Retrieve the first batch, timeout after 3 seconds
                final Vector<InetSocketAddress> serverList = p.get(3000, TimeUnit.MILLISECONDS);//response.getMessage();

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = startAddress;

                //With streams, we can easily filter out the unwanted entries. (e.g. Excluding the last source ip received)
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    if (callback != null && !isIpTerminator(ip))
                        callback.onComplete(ip, destination, null);
                    //Add a delay here. We shouldn't send requests too fast to the master server
                    // there is a high chance that we might not receive the end of the list.
                    ConcurrentUtils.sleepUninterrupted(13);
                    serverMasterList.add(ip);
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                startAddress = serverList.lastElement();

                log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                //Did we reach the end? (Master Server sometimes sends a 0.0.0.0:0 address if we have reached the end)
                if (isIpTerminator(startAddress)) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }

                //Thread.sleep(serverList.size() * 15);
            } catch (InterruptedException | TimeoutException e) {
                log.debug("Timeout/Thread Interruption Occured during retrieval of source list");
                done.set(true);
                //sdPromise.tryFailure(new ReadTimeoutException(null, "Timeout occured"));
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

    public Cache<InetSocketAddress, Integer> getChallengeCache() {
        return challengeCache;
    }

    @Override
    public void close() throws IOException {
        super.close();
        challengeCache.invalidateAll();
        challengeCache.cleanUp();
        executorService.shutdown();
    }
}
