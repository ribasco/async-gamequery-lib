/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.NettySocketClient;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryMessenger;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryOptions;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceChallengeException;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.source.query.protocols.challenge.SourceQueryChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.challenge.SourceQueryChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.protocols.players.SourceQueryPlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.players.SourceQueryPlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesRequest;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesResponse;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A client used for querying information on Source servers. Based on the Valve Source Query Protocol.
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.valvesoftware.com/wiki/Server_Queries#Source_Server">Valve Source Server Query
 * Protocol</a>
 * @see SourceQueryOptions
 */
public final class SourceQueryClient extends NettySocketClient<SourceQueryRequest, SourceQueryResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

    //<editor-fold desc="Deprecated Members">

    /**
     * @deprecated To be removed in the next major release
     */
    @Deprecated
    private static final int MAX_CHALLENGE_CACHE_SIZE = 32000;

    /**
     * @deprecated To be removed in the next major release
     */
    @Deprecated
    private LoadingCache<ChallengeKey, Integer> challengeCache;

    /**
     * @deprecated To be removed in the next major release
     */
    @Deprecated
    private int maxCacheSize = MAX_CHALLENGE_CACHE_SIZE;

    /**
     * @deprecated To be removed in the next major release
     */
    @Deprecated
    private Duration cacheExpiration = Duration.ofMinutes(15);

    /**
     * @deprecated To be removed in the next major release
     */
    @Deprecated
    private Duration cacheRefreshInterval = Duration.ofMinutes(10);
    //</editor-fold>

    //<editor-fold desc="Public Constructors">

    /**
     * Create a new {@link SourceQueryClient} instance using the default {@link Options} configured for this module
     */
    public SourceQueryClient() {
        this(null);
    }

    /**
     * Create a new {@link SourceQueryClient} instance using the provided user-defined configuration options
     *
     * @param options
     *         The {@link Options} containing the configuration options for this client
     *
     * @see OptionBuilder
     */
    public SourceQueryClient(Options options) {
        super(options);
    }
    //</editor-fold>

    //<editor-fold desc="New API">

    /**
     * <p>Retrieves information about the specified Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link CompletableFuture} returns a value of {@link SourceQueryInfoResponse} which provides additional details on the response.
     *
     * @since 0.2.0
     */
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address) {
        return getInfo(address, null);
    }

    /**
     * <p>Retrieves information about the specified Source Server using the provided challenge number <em>(optional)</em></p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     * @param challenge
     *         A 32-bit signed integer anti-spoofing challenge number. Set to {@code null} to let the library obtain one automatically (this is similar to calling {@link #getInfo(InetSocketAddress)})
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link CompletableFuture} returns a value of {@link SourceQueryInfoResponse} which provides additional details on the response.
     *
     * @see <a href="https://steamcommunity.com/discussions/forum/14/2974028351344359625/?ctp=2">Changes to A2S_INFO protocol</a>
     * @see <a href="https://store.steampowered.com/oldnews/78652">Steam Client Updates as of 12/08/2020</a>
     * @since 0.2.0
     */
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address, Integer challenge) {
        return send(address, new SourceQueryInfoRequest(challenge), SourceQueryInfoResponse.class);
    }

    /**
     * <p>Retrieves information about the specified Source Server.</p>
     *
     * <blockquote>
     * <strong>Note:</strong> If the server requires a challenge number, it will automatically be obtained IF {@code autoUpdate} is set. Otherwise an exception will be thrown and will mark the resulting {@link CompletableFuture} in failure. You will then have to obtain the challenge number manually and execute the query via {@link #getInfo(InetSocketAddress, Integer)}
     * </blockquote>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     * @param autoUpdate
     *         Set to {@code true} if the process of obtaining a new challenge number from the server should be done automatically. Otherwise the {@link CompletableFuture} will be marked as failed and return a {@link SourceChallengeException}
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link CompletableFuture} returns a value of {@link SourceQueryInfoResponse} which provides additional details on the response.
     *
     * @throws SourceChallengeException
     *         When autoUpdate is {@code false}, this exception is thrown when the server requires a challenge number. Attached to the exception is the challenge number to be sent to the server.
     * @see <a href="https://steamcommunity.com/discussions/forum/14/2974028351344359625/?ctp=2">Changes to A2S_INFO protocol</a>
     * @see <a href="https://store.steampowered.com/oldnews/78652">Steam Client Updates as of 12/08/2020</a>
     * @see #getInfo(InetSocketAddress, Integer)
     * @see #getInfo(InetSocketAddress, boolean, boolean)
     * @since 0.2.0
     */
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address, boolean autoUpdate) {
        return getInfo(address, autoUpdate, false);
    }

    /**
     * <p>Retrieves information about the specified Source Server</p>
     *
     * <blockquote>
     * <strong>Note:</strong> If the server requires a challenge number, it will automatically be obtained IF {@code autoUpdate} is set. Otherwise an exception will be thrown and will mark the resulting {@link CompletableFuture} in failure. You will then have to obtain the challenge number manually and execute the query via {@link #getInfo(InetSocketAddress, Integer)}
     * </blockquote>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     * @param autoUpdate
     *         Set to {@code true} if the process of obtaining a new challenge number from the server should be done automatically. Otherwise the {@link CompletableFuture} will be marked as failed and return a {@link SourceChallengeException}
     * @param bypassChallenge
     *         Attempts to bypass challenge number (even if server requires it). Please note that his is experimental and it may or may not work and might be removed in future versions.
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link CompletableFuture} returns a value of {@link SourceQueryInfoResponse} which provides additional details on the response.
     *
     * @throws SourceChallengeException
     *         When autoUpdate is {@code false}, this exception is thrown when the server requires a challenge number. Attached to the exception is the challenge number to be sent to the server.
     * @see <a href="https://steamcommunity.com/discussions/forum/14/2974028351344359625/?ctp=2">Changes to A2S_INFO protocol</a>
     * @see <a href="https://store.steampowered.com/oldnews/78652">Steam Client Updates as of 12/08/2020</a>
     * @since 0.2.0
     */
    @ApiStatus.Experimental
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address, boolean autoUpdate, boolean bypassChallenge) {
        SourceQueryInfoRequest request = new SourceQueryInfoRequest();
        request.setAutoUpdate(autoUpdate);
        request.setBypassChallenge(bypassChallenge);
        return send(address, request, SourceQueryInfoResponse.class);
    }

    /**
     * <p>Retrieve server rules information</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link CompletableFuture} returns a value of {@link SourceQueryRulesResponse} which provides additional details on the response.
     *
     * @see #getRules(InetSocketAddress, Integer)
     * @see #getChallenge(InetSocketAddress, SourceChallengeType)
     * @since 0.2.0
     */
    public CompletableFuture<SourceQueryRulesResponse> getRules(InetSocketAddress address) {
        return getRules(address, null);
    }

    /**
     * <p>Retrieve server rules information</p>
     *
     * <blockquote>
     * <em><strong>Note:</strong> This method requires a valid challenge number which can be obtained via {@link #getChallenge(InetSocketAddress, SourceChallengeType)}</em>
     * </blockquote>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     * @param challenge
     *         A 32-bit signed integer anti-spoofing challenge number. Set to {@code null} to let the library obtain one automatically (this is similar to calling {@link #getRules(InetSocketAddress)})
     *
     * @return A {@link CompletableFuture} that is notified once a response has been received from the server. If successful, the future returns a value of {@link SourceQueryRulesResponse} which provides additional details on the response.
     *
     * @see #getRules(InetSocketAddress, Integer)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @since 0.2.0
     */
    public CompletableFuture<SourceQueryRulesResponse> getRules(InetSocketAddress address, Integer challenge) {
        return send(address, new SourceQueryRulesRequest(challenge), SourceQueryRulesResponse.class);
    }

    /**
     * <p>Obtains a 4-byte (32-bit) anti-spoofing integer from the server. This is used for some requests (such as PLAYERS, RULES or INFO) that requires a challenge number.</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     * @param type
     *         The {@link SourceChallengeType} enumeration which identifies the type of server challenge.
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     */
    public CompletableFuture<SourceQueryChallengeResponse> getChallenge(InetSocketAddress address, SourceChallengeType type) {
        return send(address, new SourceQueryChallengeRequest(type), SourceQueryChallengeResponse.class);
    }
    //</editor-fold>

    //<editor-fold desc="Deprecated API">

    /**
     * <p>Retrieves a Server Challenge number from the server. This is used for some requests (such as PLAYERS and
     * RULES) that requires a challenge number.</p>
     *
     * @param type
     *         The {@link SourceChallengeType} enumeration which identifies the type of server challenge.
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address) {
        return send(address, new SourceQueryChallengeRequest(type), SourceQueryChallengeResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /**
     * <p>Retrieves information of the Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains {@link SourceServer} instance
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address) {
        return send(address, new SourceQueryInfoRequest(), SourceQueryInfoResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /**
     * <p>
     * Retrieve a list of active players in the server. Please note that this method sends an initial
     * challenge request (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType,
     * InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link
     * #getPlayersCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(Integer, InetSocketAddress)
     * @deprecated The return value of the future will be replaced by {@link SourceQueryPlayerResponse}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Collection<SourcePlayer>> getPlayers(InetSocketAddress address) {
        return send(address, new SourceQueryPlayerRequest(), SourceQueryPlayerResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /**
     * <p>Retrieve a list of active players in the server. You NEED to obtain a valid challenge number from the server
     * first.</p>
     *
     * @param challenge
     *         A 32-bit signed integer anti-spoofing challenge number. Set to {@code null} to let the library obtain one automatically (this is similar to calling {@link #getPlayers(InetSocketAddress)})
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     * @deprecated The return value of the future will be replaced by {@link SourceQueryPlayerResponse}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<List<SourcePlayer>> getPlayers(Integer challenge, InetSocketAddress address) {
        return send(address, new SourceQueryPlayerRequest(challenge), SourceQueryPlayerResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /**
     * <p>Retrieve source server rules information. Please note that this method sends an initial challenge request
     * (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link
     * #getServerRulesCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Map<String, String>> getServerRules(InetSocketAddress address) {
        return getServerRules(null, address);
    }

    /**
     * <p>
     * Retrieve source server rules information. You NEED to obtain a valid challenge number from the server first.
     * </p>
     *
     * @param challenge
     *         The challenge number to be used for the request
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Map<String, String>> getServerRules(Integer challenge, InetSocketAddress address) {
        return send(address, new SourceQueryRulesRequest(challenge), SourceQueryRulesResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /**
     * @return The maxmimum size allowable for the internal challenge cache
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Sets the maximum allowable size for the internal challenge cache
     *
     * @param maxCacheSize
     *         An int representing the cache size
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * @return Returns the duration (in minutes) used by the internal cache service to expire entries.
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public long getCacheExpiration() {
        return cacheExpiration.toMinutes();
    }

    /**
     * @param cacheExpiration
     *         A {@link Duration} instance representing the time it will take to expire cached entries
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setCacheExpiration(Duration cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
    }

    /**
     * @param cacheExpiration
     *         A number representing the duration (in minutes) to expire cached entries
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setCacheExpiration(long cacheExpiration) {
        setCacheExpiration(Duration.ofMinutes(cacheExpiration));
    }

    /**
     * @return A {@link Duration} representing the time it will take to refersh a challenge in the internal cache
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public Duration getCacheRefreshInterval() {
        return cacheRefreshInterval;
    }

    /**
     * Sets the refresh interval of each cached entry
     *
     * @param cacheRefreshInterval
     *         The {@link Duration} representing the refresh interval for each cached entry
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setCacheRefreshInterval(Duration cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    /**
     * <p>Retrieve a list of active players in the server. This uses the internal cache to retrieve the challenge number
     * (if available).</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(Integer, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     * @deprecated This will be removed in the next major release. Please use {@link #getPlayers(InetSocketAddress)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<List<SourcePlayer>> getPlayersCached(InetSocketAddress address) {
        return getServerChallengeFromCache(address).thenCompose(challenge -> getPlayers(challenge, address));
    }

    /**
     * <p>Retrieves a challenge number from the internal cache if available.  If the challenge number does not yet exist
     * in the cache then a
     * request (using: {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}) will be issued to the server
     * to obtain the latest challenge number.</p>
     * <p>This is considered to be thread-safe.</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} containing the resulting challenge {@link Integer}
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @deprecated This will be removed in the next major version. Please use {@link #getChallenge(InetSocketAddress, SourceChallengeType)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Integer> getServerChallengeFromCache(InetSocketAddress address) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getChallengeCache().get(new ChallengeKey(SourceChallengeType.CHALLENGE, address));
            } catch (ExecutionException e) {
                throw new CompletionException(e);
            }
        }, getExecutor());
    }

    /**
     * <p>Retrieve source server rules information. Uses the internal cache to retrieve the challenge number (if
     * available).</p>
     *
     * @param address
     *         The {@link InetSocketAddress} containing the ip address and port number information of the target server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallengeFromCache(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @deprecated This will be removed in the next major release. Please use {@link #getRules(InetSocketAddress)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public CompletableFuture<Map<String, String>> getServerRulesCached(InetSocketAddress address) {
        return getServerChallengeFromCache(address).thenCompose(challenge -> getServerRules(challenge, address));
    }

    /**
     * Retrieve the internal challenge cache of this instance
     *
     * @return A {@link LoadingCache} containing a map of {@link InetSocketAddress} keys and {@link Integer} values.
     *
     * @deprecated This will be removed in the next major release
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public LoadingCache<ChallengeKey, Integer> getChallengeCache() {
        //lazy-loaded
        if (this.challengeCache == null) {
            log.debug("Building new cache...");
            this.challengeCache = CacheBuilder.newBuilder()
                                              .maximumSize(getMaxCacheSize())
                                              .expireAfterWrite(getCacheExpiration(), TimeUnit.MINUTES)
                                              .refreshAfterWrite(getCacheRefreshInterval())
                                              .build(new CacheLoader<ChallengeKey, Integer>() {
                                                  @Override
                                                  public @NotNull Integer load(@NotNull ChallengeKey key) {
                                                      return getServerChallenge(key.type, key.address).join();
                                                  }
                                              });
        }
        return this.challengeCache;
    }

    private static class ChallengeKey {

        private final SourceChallengeType type;

        private final InetSocketAddress address;

        private ChallengeKey(SourceChallengeType type, InetSocketAddress address) {
            this.type = type;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChallengeKey that = (ChallengeKey) o;
            return type == that.type && address.equals(that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, address);
        }
    }
    //</editor-fold>

    @Override
    protected NettyMessenger<SourceQueryRequest, SourceQueryResponse> createMessenger(Options options) {
        return new SourceQueryMessenger(options);
    }
}
