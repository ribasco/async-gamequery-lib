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
 * A client used for querying information on Source servers. Based on the Valve Source Query Protocol.
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Server_Queries#Source_Server">Valve Source Server Query Protocol</a>
 */
public class SourceQueryClient extends GameServerQueryClient<SourceServerRequest, SourceServerResponse, SourceServerMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    private LoadingCache<InetSocketAddress, Integer> challengeCache;
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cache-pool-%d").setDaemon(true).build();
    private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(new ScheduledThreadPoolExecutor(32, threadFactory));
    private static final int MAX_CHALLENGE_CACHE_SIZE = 32000;
    private int maxCacheSize = MAX_CHALLENGE_CACHE_SIZE;
    private Duration cacheExpiration = Duration.ofMinutes(10);
    private Duration cacheRefreshInterval = Duration.ofMinutes(5);

    /**
     * Default Constructor using the {@link SourceServerMessenger}
     */
    public SourceQueryClient() {
        super(new SourceServerMessenger());
    }

    /**
     * <p>Retrieves a Server Challenge number from the server. This is used for some requests (such as PLAYERS and RULES) that requires a challenge number.</p>
     *
     * @param type    A {@link SourceChallengeType}
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     */
    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address) {
        return getServerChallenge(type, address, null);
    }

    /**
     * <p>Retrieves a Server Challenge number from the server. This is used for some requests (such as PLAYERS and RULES) that requires a challenge number.</p>
     *
     * @param type     A {@link SourceChallengeType}
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     *
     * @see #getPlayers(Integer, InetSocketAddress)
     * @see #getServerRules(int, InetSocketAddress)
     */
    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address, Callback<Integer> callback) {
        return sendRequest(new SourceChallengeRequest(type, address), callback, RequestPriority.REALTIME);
    }

    /**
     * <p>Retrieves a challenge number from the internal cache if available.  If the challenge number does not yet exist in the cache then a
     * request (using: {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}) will be issued to the server to obtain the latest challenge number.</p>
     * <p>This is considered to be thread-safe.</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} containing the resulting challenge {@link Integer}
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     */
    public synchronized CompletableFuture<Integer> getServerChallengeFromCache(InetSocketAddress address) {
        return CompletableFuture.<Integer>supplyAsync(() -> {
            try {
                return getChallengeCache().get(address);
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            }
        }, executorService);
    }

    /**
     * <p>Retrieve source server rules information. Please note that this method sends an initial challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link #getServerRulesCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerRules(int, InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRules(InetSocketAddress address) {
        return getServerRules(address, null);
    }

    /**
     * <p>Retrieve source server rules information. Please note that this method sends an initial challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link #getServerRulesCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerRules(int, InetSocketAddress, Callback)
     */
    public CompletableFuture<Map<String, String>> getServerRules(InetSocketAddress address, Callback<Map<String, String>> callback) {
        return getServerChallenge(SourceChallengeType.RULES, address)
                .thenCompose(challenge -> getServerRules(challenge, address))
                .whenComplete((rulesMap, error) -> {
                    if (callback != null)
                        callback.onComplete(rulesMap, address, error);
                });
    }

    /**
     * <p>
     * Retrieve source server rules information. You NEED to obtain a valid challenge number from the server first.
     * </p>
     *
     * @param challenge The challenge number to be used for the request
     * @param address   The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRules(int challenge, InetSocketAddress address) {
        return getServerRules(challenge, address, null);
    }

    /**
     * <p>
     * Retrieve source server rules information. You NEED to obtain a valid challenge number from the server first.
     * </p>
     *
     * @param challenge The challenge number to be used for the request
     * @param address   The {@link InetSocketAddress} of the source server
     * @param callback  A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRules(int challenge, InetSocketAddress address, Callback<Map<String, String>> callback) {
        return sendRequest(new SourceRulesRequest(challenge, address), callback);
    }

    /**
     * <p>Retrieve source server rules information. Uses the internal cache to retrieve the challenge number (if available).</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRulesCached(InetSocketAddress address) {
        return getServerRulesCached(address, null);
    }

    /**
     * <p>Retrieve source server rules information. Uses the internal cache to retrieve the challenge number (if available).</p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRulesCached(InetSocketAddress address, Callback<Map<String, String>> callback) {
        return getServerChallengeFromCache(address)
                .thenCompose(challengeFromCache -> getServerRules(challengeFromCache, address))
                .whenComplete((rulesMap, error) -> {
                    if (callback != null)
                        callback.onComplete(rulesMap, address, error);
                });
    }

    /**
     * <p>
     * Retrieve players currently residing on the specified server. Please note that this method sends an initial
     * challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link #getPlayersCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(int, InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(InetSocketAddress address) {
        return getPlayers(address, null);
    }

    /**
     * <p>
     * Retrieve players currently residing on the specified server. Please note that this method sends an initial
     * challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link #getPlayersCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(int, InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return getServerChallenge(SourceChallengeType.PLAYER, address)
                .thenCompose(challenge -> getPlayers(challenge, address))
                .whenComplete((playerList, error) -> {
                    if (callback != null)
                        callback.onComplete(playerList, address, error);
                });
    }

    /**
     * <p>Retrieve players currently residing on the specified server. You NEED to obtain a valid challenge number from the server first.</p>
     *
     * @param challenge The challenge number to be used for the request
     * @param address   The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(int challenge, InetSocketAddress address) {
        return getPlayers(challenge, address, null);
    }

    /**
     * <p>Retrieve players currently residing on the specified server. You NEED to obtain a valid challenge number from the server first.</p>
     *
     * @param challenge The challenge number to be used for the request
     * @param address   The {@link InetSocketAddress} of the source server
     * @param callback  A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(int challenge, InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return sendRequest(new SourcePlayerRequest(challenge, address), callback);
    }

    /**
     * <p>Retrieve players currently residing on the specified server. This uses the internal cache to retrieve the challenge number (if available).</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(int, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayersCached(InetSocketAddress address) {
        return getPlayersCached(address, null);
    }

    /**
     * <p>Retrieve players currently residing on the specified server. This uses the internal cache to retrieve the challenge number (if available).</p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on the server
     *
     * @see #getPlayers(int, InetSocketAddress, Callback)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayersCached(InetSocketAddress address, Callback<List<SourcePlayer>> callback) {
        return getServerChallengeFromCache(address)
                .thenCompose(challenge -> getPlayers(challenge, address))
                .whenComplete((playerList, error) -> {
                    if (callback != null)
                        callback.onComplete(playerList, address, error);
                });
    }

    /**
     * <p>Retrieves information of the Source Server</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains {@link SourceServer} instance
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address) {
        return sendRequest(new SourceInfoRequest(address));
    }

    /**
     * <p>Retrieves information of the Source Server</p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains {@link SourceServer} instance
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address, Callback<SourceServer> callback) {
        return sendRequest(new SourceInfoRequest(address), callback);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region A {@link SourceMasterServerRegion} value that specifies which server region the master server should return
     * @param filter A {@link SourceMasterFilter} representing a set of filters to be used by the query
     *
     * @return A {@link CompletableFuture} that contains a {@link java.util.Set} of servers retrieved from the master
     *
     * @see #getMasterServerList(SourceMasterServerRegion, SourceMasterFilter, Callback)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getMasterServerList(final SourceMasterServerRegion region, final SourceMasterFilter filter) {
        return getMasterServerList(region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region   A {@link SourceMasterServerRegion} value that specifies which server region the master server should return
     * @param filter   A {@link SourceMasterFilter} representing a set of filters to be used by the query
     * @param callback A {@link Callback} that will be invoked when a response has been received
     *
     * @return A {@link CompletableFuture} that contains a {@link java.util.Set} of servers retrieved from the master
     *
     * @see #getMasterServerList(SourceMasterServerRegion, SourceMasterFilter)
     */
    //TODO: Move this to it's own client interface since this is not source protocol specific
    public CompletableFuture<Vector<InetSocketAddress>> getMasterServerList(final SourceMasterServerRegion region, final SourceMasterFilter filter, final Callback<InetSocketAddress> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);

        final CompletableFuture<Vector<InetSocketAddress>> masterPromise = new CompletableFuture<>();
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = SourceMasterRequestPacket.SOURCE_MASTER;
        final AtomicBoolean done = new AtomicBoolean();

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + startAddress);
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", startAddress.getAddress().getHostAddress(), startAddress.getPort(), filter);

                //Send initial query to the master source
                final CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new SourceMasterServerRequest(destination, region, filter, startAddress), RequestPriority.HIGH);

                //Retrieve the first batch, timeout after 3 seconds
                final Vector<InetSocketAddress> serverList = p.get(3000, TimeUnit.MILLISECONDS);

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = startAddress;

                //With streams, we can easily filter out the unwanted entries. (e.g. Excluding the last source ip received)
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    if (callback != null && !isIpTerminator(ip))
                        callback.onComplete(ip, destination, null);
                    serverMasterList.add(ip);
                    //Add a delay here. We shouldn't send requests too fast to the master server
                    // there is a high chance that we might not receive the end of the list.
                    ConcurrentUtils.sleepUninterrupted(13);
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                startAddress = serverList.lastElement();

                log.debug("Last Server Received: {}:{}", startAddress.getAddress().getHostAddress(), startAddress.getPort());

                //Did the master send a terminator address?
                // If so, mark as complete
                if (isIpTerminator(startAddress)) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }

                //Thread.sleep(serverList.size() * 15);
            } catch (InterruptedException | TimeoutException e) {
                log.error("Timeout/Thread Interruption Occured during retrieval of server list from master");
                done.set(true);
            } catch (ExecutionException e) {
                masterPromise.completeExceptionally(e);
                callback.onComplete(null, destination, e);
                return masterPromise;
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        masterPromise.complete(serverMasterList);

        return masterPromise;
    }

    /**
     * <p>A helper to determine if the address is a terminator type address</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     *
     * @return true if the {@link InetSocketAddress} supplied is a terminator address
     */
    private static boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }

    /**
     * @return The maxmimum size allowable for the internal challenge cache
     */
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Sets the maximum allowable size for the internal challenge cache
     *
     * @param maxCacheSize An int representing the cache size
     */
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * @return Returns the duration (in minutes) used by the internal cache service to expire entries.
     */
    public long getCacheExpiration() {
        return cacheExpiration.toMinutes();
    }

    /**
     * @param cacheExpiration A number representing the duration (in minutes) to expire cached entries
     */
    public void setCacheExpiration(long cacheExpiration) {
        setCacheExpiration(Duration.ofMinutes(cacheExpiration));
    }

    /**
     * @param cacheExpiration A {@link Duration} instance representing the time it will take to expire cached entries
     */
    public void setCacheExpiration(Duration cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
    }

    /**
     * @return A {@link Duration} representing the time it will take to refersh a challenge in the internal cache
     */
    public Duration getCacheRefreshInterval() {
        return cacheRefreshInterval;
    }

    /**
     * Sets the refresh interval of each cached entry
     *
     * @param cacheRefreshInterval The {@link Duration} representing the refresh interval for each cached entry
     */
    public void setCacheRefreshInterval(Duration cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    /**
     * Retrieve the internal challenge cache of this instance
     *
     * @return A {@link LoadingCache} containing a map of {@link InetSocketAddress} keys and {@link Integer} values.
     */
    public synchronized LoadingCache<InetSocketAddress, Integer> getChallengeCache() {
        //lazy-loaded
        if (challengeCache == null) {
            log.debug("Building new cache...");
            challengeCache = CacheBuilder.newBuilder()
                    .maximumSize(maxCacheSize)
                    .expireAfterWrite(cacheExpiration.toMillis(), TimeUnit.MILLISECONDS)
                    .refreshAfterWrite(cacheRefreshInterval.toMillis(), TimeUnit.MILLISECONDS)
                    .recordStats()
                    .build(new CacheLoader<InetSocketAddress, Integer>() {
                        @Override
                        public Integer load(InetSocketAddress key) throws Exception {
                            return getServerChallenge(SourceChallengeType.ANY, key).get();
                        }

                        @Override
                        public ListenableFuture<Integer> reload(InetSocketAddress key, Integer oldValue) throws Exception {
                            log.debug("Refreshing challenge number for : {}, Old Value = {}", key, oldValue);
                            return executorService.submit(() -> load(key));
                        }
                    });

        }
        return challengeCache;
    }

    /**
     * Method that tries to perform a graceful shutdown of the client
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
        getChallengeCache().cleanUp();
        executorService.shutdown();
    }
}
