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
 */
public final class SourceQueryClient extends NettySocketClient<SourceQueryRequest, SourceQueryResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryClient.class);

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

    /**
     * Create a new {@link SourceQueryClient} instance
     */
    public SourceQueryClient() {
        this(null);
    }

    /**
     * Create a new {@link SourceQueryClient} instance with custom configuration options
     *
     * @param options
     *         The {@link Options} containing the configuration options for this client
     *
     * @see OptionBuilder
     */
    public SourceQueryClient(Options options) {
        super(options);
    }

    /**
     * <p>Retrieves a Server Challenge number from the server. This is used for some requests (such as PLAYERS and
     * RULES) that requires a challenge number.</p>
     *
     * @param type
     *         A {@link SourceChallengeType}
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} returning a value of {@link Integer} representing the server challenge number
     */
    public CompletableFuture<Integer> getServerChallenge(SourceChallengeType type, InetSocketAddress address) {
        return send(address, new SourceQueryChallengeRequest(type), SourceQueryChallengeResponse.class).thenApply(SourceQueryChallengeResponse::getChallenge);
    }

    /**
     * <p>Retrieves information of the Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains {@link SourceServer} instance
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address) {
        return send(address, new SourceQueryInfoRequest(), SourceQueryInfoResponse.class).thenApply(SourceQueryInfoResponse::getServer);
    }

    /**
     * <p>Retrieves information about the Source Server.</p>
     *
     * @param challenge
     *         The challenge number to be used for the request
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that is notified once a reply has been received from the remote server. If successful, a {@link SourceServer} is returned.
     */
    public CompletableFuture<SourceServer> getServerInfo(Integer challenge, InetSocketAddress address) {
        return send(address, new SourceQueryInfoRequest(challenge), SourceQueryInfoResponse.class).thenApply(SourceQueryInfoResponse::getServer);
    }

    /**
     * <p>Retrieves information about the Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param autoUpdate
     *         Set to {@code true} if the challenge number will automatically be queried. Otherwise an exception will be thrown indicating that the server requires a new/updated challenge number.
     *
     * @return A {@link CompletableFuture} that is notified once a reply has been received from the remote server. If successful, a {@link SourceServer} is returned.
     *
     * @throws SourceChallengeException
     *         When autoUpdate is {@code false}, this exception is thrown when the server requires a challenge number. Attached to the exception is the challenge number to be sent to the server.
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address, boolean autoUpdate) {
        return getServerInfo(address, autoUpdate, false);
    }

    /**
     * <p>Retrieves information about the Source Server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param autoUpdate
     *         Set to {@code true} if the challenge number will automatically be queried. Otherwise the future will be marked as failed and return a {@link SourceChallengeException}
     *
     * @return A {@link CompletableFuture} that is notified once a reply has been received from the remote server. If successful, a {@link SourceServer} is returned.
     *
     * @throws SourceChallengeException
     *         When autoUpdate is {@code false}, this exception is thrown when the server requires a challenge number. Attached to the exception is the challenge number to be sent to the server.
     */
    public CompletableFuture<SourceServer> getServerInfo(InetSocketAddress address, boolean autoUpdate, boolean bypassChallenge) {
        SourceQueryInfoRequest request = new SourceQueryInfoRequest();
        request.setAutoUpdate(autoUpdate);
        request.setBypassChallenge(bypassChallenge);
        return send(address, request, SourceQueryInfoResponse.class).thenApply(SourceQueryInfoResponse::getServer);
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
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(Integer, InetSocketAddress)
     */
    public CompletableFuture<Collection<SourcePlayer>> getPlayers(InetSocketAddress address) {
        return send(address, new SourceQueryPlayerRequest(), SourceQueryPlayerResponse.class).thenApply(SourceQueryPlayerResponse::getPlayers);
    }

    /**
     * <p>Retrieve a list of active players in the server. You NEED to obtain a valid challenge number from the server
     * first.</p>
     *
     * @param challenge
     *         The challenge number to be used for the request
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(Integer challenge, InetSocketAddress address) {
        return send(address, new SourceQueryPlayerRequest(challenge), SourceQueryPlayerResponse.class).thenApply(SourceQueryPlayerResponse::getPlayers);
    }

    /**
     * <p>Retrieve source server rules information. Please note that this method sends an initial challenge request
     * (Total of 5 bytes) to the server using {@link #getServerChallenge(SourceChallengeType, InetSocketAddress)}.
     * If you plan to use this method more than once for the same address, please consider using {@link
     * #getServerRulesCached(InetSocketAddress)} instead.
     * </p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     */
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
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     */
    public CompletableFuture<Map<String, String>> getServerRules(Integer challenge, InetSocketAddress address) {
        return send(address, new SourceQueryRulesRequest(challenge), SourceQueryRulesResponse.class).thenApply(SourceQueryRulesResponse::getRules);
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
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link List} of {@link SourcePlayer} currently residing on
     * the server
     *
     * @see #getPlayers(Integer, InetSocketAddress)
     * @see #getServerChallengeFromCache(InetSocketAddress)
     * @deprecated This will be removed in the next major release
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
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} containing the resulting challenge {@link Integer}
     *
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @deprecated This will be removed in the next major version
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
     *         The {@link InetSocketAddress} of the source server
     *
     * @return A {@link CompletableFuture} that contains a {@link Map} of server rules
     *
     * @see #getServerChallengeFromCache(InetSocketAddress)
     * @see #getServerChallenge(SourceChallengeType, InetSocketAddress)
     * @deprecated This will be removed in the next major release
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

    @Override
    protected NettyMessenger<SourceQueryRequest, SourceQueryResponse> createMessenger(Options options) {
        return new SourceQueryMessenger(options);
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
}
