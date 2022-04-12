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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.NettySocketClient;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.protocols.valve.source.query.challenge.SourceQueryChallengeRequest;
import com.ibasco.agql.protocols.valve.source.query.challenge.SourceQueryChallengeResponse;
import com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryRequest;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.players.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesRequest;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesResponse;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * A query client for retreiving information on Source servers. Based on the <a href="https://developer.valvesoftware.com/wiki/Server_Queries#Source_Server">Valve Source Server Query Protocol</a>
 * </p>
 *
 * <h3>Code example:</h3>
 *
 * <h4>Asynchronous (non-blocking)</h4>
 *
 * <pre>
 * try (SourceQueryClient client = new SourceQueryClient()) {
 *     CompletableFuture<Source> resultFuture = client.getInfo(new InetSocketAddress("192.168.1.10", 27015);
 *     if (resultFuture.isDone()) {
 *         SourceQueryInfoResponse infoResponse = resultFuture.getNow(null);
 *         assert infoResponse != null;
 *         System.out.printf("INFO: %s\n", infoResponse.getResult());
 *     } else {
 *         //we have not yet received a response, register a callback once we do
 *         resultFuture.whenComplete((response, error) -> {
 *             if (error != null) {
 *                 error.printStackTrace(System.err);
 *                 return;
 *             }
 *             assert response != null;
 *             System.out.printf("INFO: %s\n", response);
 *         });
 *     }
 * }
 * </pre>
 *
 * <h4>Synchronous (blocking)</h4>
 *
 * <pre>
 * try (SourceQueryClient client = new SourceQueryClient()) {
 *     SourceQueryInfoResponse infoResponse = client.getInfo(new InetSocketAddress("192.168.1.10", 27015);
 *     assert infoResponse != null;
 *     System.out.printf("INFO: %s\n", infoResponse.getResult());
 * } catch (Exception error) {
 *     error.printStackTrace(System.err);
 * }
 * </pre>
 *
 * <h4>Modifying client configuration parameters</h4>
 *
 * <p>Enable rate-limiting (disabled by default)</p>
 *
 * <pre>
 * Options options = OptionBuilder.newBuilder().option(SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED, true)
 *                                             .option(SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
 *                                             .build();
 *
 * //pass options to client's constructor
 * try (SourceQueryClient client = new SourceQueryClient(options)) {
 *     InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
 *
 *     //execute query 100 times
 *     for (int i=0; i<100; i++) {
 *         CompletableFuture<Source> resultFuture = client.getInfo(address);
 *         if (resultFuture.isDone()) {
 *             SourceQueryInfoResponse infoResponse = resultFuture.getNow(null);
 *             assert infoResponse != null;
 *             System.out.printf("INFO: %s\n", infoResponse.getResult());
 *         } else {
 *             //we have not yet received a response, register a callback once we do
 *             resultFuture.whenComplete((response, error) -> {
 *                 if (error != null) {
 *                     error.printStackTrace(System.err);
 *                     return;
 *                 }
 *                 assert response != null;
 *                 System.out.printf("INFO: %s\n", response);
 *             });
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.valvesoftware.com/wiki/Server_Queries#Source_Server">Valve Source Server Query
 * Protocol</a>
 * @see SourceQueryOptions
 */
public final class SourceQueryClient extends NettySocketClient<SourceQueryRequest, SourceQueryResponse<?>> {

    //<editor-fold desc="Public Constructors">

    /**
     * Create a new {@link com.ibasco.agql.protocols.valve.source.query.SourceQueryClient} instance using the pre-defined configuration {@link com.ibasco.agql.core.util.Options} for this client
     */
    public SourceQueryClient() {
        this(null);
    }

    /**
     * Create a new {@link com.ibasco.agql.protocols.valve.source.query.SourceQueryClient} instance using the provided configuration {@link com.ibasco.agql.core.util.Options}
     *
     * @param options
     *         The user-defined {@link com.ibasco.agql.core.util.Options} containing the configuration settings to be used by this client.
     * @see Options
     * @see OptionBuilder
     */
    public SourceQueryClient(Options options) {
        super(options);
    }
    //</editor-fold>

    /**
     * <p>Retrieves information about the Source server. A <a href="https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO">challenge</a>number will automatically be obtained if required by the server.</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the ip address and port number information of the target server
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link java.util.concurrent.CompletableFuture} returns a value of {@link com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoResponse} which provides additional details on the server.
     * @see #getInfo(InetSocketAddress, Integer)
     */
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address) {
        return getInfo(address, null);
    }

    /**
     * <p>Retrieves information about the Source server using the provided challenge number <em>(optional)</em></p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server
     * @param challenge
     *         (optional) A 32-bit signed integer anti-spoofing challenge. Set to {@code null} to let the library obtain one automatically. This is similar to calling {@link #getInfo(InetSocketAddress)}.
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link java.util.concurrent.CompletableFuture} returns a value of {@link com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoResponse} which provides additional details on the server
     * @see <a href="https://steamcommunity.com/discussions/forum/14/2974028351344359625/?ctp=2">Changes to A2S_INFO protocol</a>
     * @see <a href="https://store.steampowered.com/oldnews/78652">Steam Client Updates as of 12/08/2020</a>
     */
    public CompletableFuture<SourceQueryInfoResponse> getInfo(InetSocketAddress address, Integer challenge) {
        return send(address, new SourceQueryInfoRequest(challenge), SourceQueryInfoResponse.class);
    }

    /**
     * <p>Retrieve rules from the Source server</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server.
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has been received from the server. If successful, the {@link java.util.concurrent.CompletableFuture} returns a value of {@link com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesResponse} which provides additional details on the response.
     * @see #getRules(InetSocketAddress, Integer)
     * @see #getChallenge(InetSocketAddress, SourceChallengeType)
     */
    public CompletableFuture<SourceQueryRulesResponse> getRules(InetSocketAddress address) {
        return getRules(address, null);
    }

    /**
     * <p>Retrieve rules from the Source server</p>
     *
     * <blockquote>
     * <em><strong>Note:</strong> This method requires a valid challenge number which can be obtained via {@link #getChallenge(InetSocketAddress, SourceChallengeType)}</em>
     * </blockquote>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server
     * @param challenge
     *         (optional) A 32-bit signed integer anti-spoofing challenge. Set to {@code null} to let the library obtain one automatically (this is similar to calling {@link #getRules(InetSocketAddress)})
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has been received from the server. If successful, the future returns a value of {@link com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesResponse} which provides additional details on the response.
     * @see #getRules(InetSocketAddress, Integer)
     */
    public CompletableFuture<SourceQueryRulesResponse> getRules(InetSocketAddress address, Integer challenge) {
        return send(address, new SourceQueryRulesRequest(challenge), SourceQueryRulesResponse.class);
    }

    /**
     * <p>Obtains a 4-byte (32-bit) anti-spoofing integer from the server. This is used for queries (such as PLAYERS, RULES or INFO) that requires a challenge number.</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server
     * @param type
     *         The {@link com.ibasco.agql.protocols.valve.source.query.common.enums.SourceChallengeType} enumeration which identifies the type of server challenge.
     * @return A {@link java.util.concurrent.CompletableFuture} returning a value of {@link java.lang.Integer} representing the server challenge number
     */
    public CompletableFuture<SourceQueryChallengeResponse> getChallenge(InetSocketAddress address, SourceChallengeType type) {
        return send(address, new SourceQueryChallengeRequest(type), SourceQueryChallengeResponse.class);
    }

    /**
     * <p>
     * Retrieve a list of active players in the server.
     * </p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server
     * @return A {@link java.util.concurrent.CompletableFuture} that contains a {@link java.util.List} of {@link com.ibasco.agql.protocols.valve.source.query.players.SourcePlayer} currently residing on
     * the server
     * @see #getPlayers(InetSocketAddress, Integer)
     */
    public CompletableFuture<SourceQueryPlayerResponse> getPlayers(InetSocketAddress address) {
        return send(address, new SourceQueryPlayerRequest(), SourceQueryPlayerResponse.class);
    }

    /**
     * <p>Retrieve a list of active players in the server. You need to obtain a valid challenge number from the server
     * first via {@link #getChallenge(InetSocketAddress, SourceChallengeType)}</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} containing the IP address and port number information of the target server
     * @param challenge
     *         (optional) A 32-bit signed integer anti-spoofing challenge. Set to {@code null} to let the library obtain one automatically (this is similar to calling {@link #getPlayers(InetSocketAddress)})
     * @return A {@link java.util.concurrent.CompletableFuture} that contains a {@link java.util.List} of {@link com.ibasco.agql.protocols.valve.source.query.players.SourcePlayer} currently residing on
     * the server
     * @see #getPlayers(InetSocketAddress)
     * @see #getChallenge(InetSocketAddress, SourceChallengeType)
     */
    public CompletableFuture<List<SourcePlayer>> getPlayers(InetSocketAddress address, Integer challenge) {
        return send(address, new SourceQueryPlayerRequest(challenge), SourceQueryPlayerResponse.class).thenApply(SourceQueryResponse::getResult);
    }

    /** {@inheritDoc} */
    @Override
    protected NettyMessenger<SourceQueryRequest, SourceQueryResponse<?>> createMessenger(Options options) {
        return new SourceQueryMessenger(options);
    }
}
