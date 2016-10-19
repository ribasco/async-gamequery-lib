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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by raffy on 9/14/2016.
 */
public class SourceQueryClient extends GameServerQueryClient<SourceServerRequest, SourceServerResponse, SourceServerMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    private LoadingCache<InetSocketAddress, Integer> challengeCache;
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cache-pool-%d").setDaemon(true).build();
    private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(new ScheduledThreadPoolExecutor(32, threadFactory));
    private static final int MAX_CHALLENGE_CACHE_SIZE = 32000;
    private int maxCacheSize = MAX_CHALLENGE_CACHE_SIZE;
    private Duration cacheExpiration = Duration.ofSeconds(3);

    public SourceQueryClient() {
        super(new SourceServerMessenger());
    }

    public LoadingCache<InetSocketAddress, Integer> getChallengeCache() {
        //lazy-loaded
        if (challengeCache == null) {
            challengeCache = CacheBuilder.newBuilder()
                    .maximumSize(maxCacheSize)
                    .expireAfterWrite(cacheExpiration.toMinutes(), TimeUnit.MINUTES)
                    .refreshAfterWrite(5, TimeUnit.MINUTES)
                    .build(new CacheLoader<InetSocketAddress, Integer>() {
                        @Override
                        public Integer load(InetSocketAddress key) throws Exception {
                            try {
                                return getServerChallenge(SourceChallengeType.ANY, key, null).get();
                            } catch (ExecutionException e) {
                                throw e;
                            }
                        }

                        @Override
                        public ListenableFuture<Integer> reload(InetSocketAddress key, Integer oldValue) throws Exception {
                            return executorService.submit(() -> load(key));
                        }
                    });
        }
        return challengeCache;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public long getCacheExpiration() {
        return cacheExpiration.toMinutes();
    }

    public void setCacheExpiration(long cacheExpiration) {
        setCacheExpiration(Duration.ofMinutes(cacheExpiration));
    }

    public void setCacheExpiration(Duration cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
    }

    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address) {
        return getServerChallenge(type, address, null);
    }

    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address, Callback<Integer> callback) {
        return sendRequest(new SourceChallengeRequest(type, address), callback, RequestPriority.REALTIME);
    }

    public CompletableFuture<Integer> getChallengeFromCache(InetSocketAddress address) {
        return CompletableFuture.<Integer>supplyAsync(() -> getChallengeCache().getUnchecked(address), executorService);
    }

    public CompletableFuture<Map<String, String>> getServerRules(int challenge, InetSocketAddress address, Callback<Map<String, String>> callback) {
        return sendRequest(new SourceRulesRequest(challenge, address), callback);
    }

    public CompletableFuture<Map<String, String>> getServerRules(InetSocketAddress address, Callback<Map<String, String>> callback) {
        return getServerChallenge(SourceChallengeType.RULES, address, null).thenCompose(challenge -> getServerRules(challenge, address, callback));
    }

    public CompletableFuture<Map<String, String>> getServerRulesCached(InetSocketAddress address, Callback<Map<String, String>> callback) {
        return getChallengeFromCache(address).thenCompose(challenge -> getServerRules(challenge, address, callback));
    }

    public CompletableFuture<List<SourcePlayer>> getPlayers(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return getServerChallenge(SourceChallengeType.PLAYER, address).thenCompose(challenge -> getPlayers(challenge, address, callback));
    }

    public CompletableFuture<List<SourcePlayer>> getPlayers(Integer challenge, InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return sendRequest(new SourcePlayerRequest(challenge, address), callback);
    }

    public CompletableFuture<List<SourcePlayer>> getPlayersCached(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return getChallengeFromCache(address).thenCompose(challenge -> getPlayers(challenge, address, callback));
    }

    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address, Callback<SourceServer> callback) {
        return sendRequest(new SourceInfoRequest(address), callback);
    }

    private boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }

    public CompletableFuture<Vector<InetSocketAddress>> getMasterServerList(SourceMasterServerRegion region, SourceMasterFilter filter, Callback<InetSocketAddress> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        final CompletableFuture<Vector<InetSocketAddress>> sdPromise = new CompletableFuture<>();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = SourceMasterRequestPacket.SOURCE_MASTER;
        final AtomicBoolean done = new AtomicBoolean();

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + startAddress);
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter);

                //Send initial query to the master source
                final CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new SourceMasterServerRequest(destination, region, filter, startAddress), RequestPriority.HIGH);
                //TODO: Listen for exceptions

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
                sdPromise.completeExceptionally(e);
                callback.onComplete(null, destination, e);
                return sdPromise;
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        sdPromise.complete(serverMasterList);

        return sdPromise;
    }

    @Override
    public void close() throws IOException {
        super.close();
        getChallengeCache().cleanUp();
        executorService.shutdown();
    }
}
