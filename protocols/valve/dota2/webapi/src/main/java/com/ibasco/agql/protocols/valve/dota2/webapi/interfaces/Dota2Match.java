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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2WebApiInterface;
import com.ibasco.agql.protocols.valve.dota2.webapi.adapters.Dota2TeamInfoAdapter;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetLeagueListing;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetLiveLeagueGames;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetMatchDetails;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetMatchHistory;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetMatchHistoryBySequenceNum;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetTeamInfoByTeamID;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match.GetTopLiveGame;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2League;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2LiveLeagueGame;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchDetails;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistory;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistoryCriteria;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchTeamInfo;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2TopLiveGame;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Dota2Match class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2Match extends Dota2WebApiInterface {

    private static final Logger log = LoggerFactory.getLogger(Dota2Match.class);

    /**
     * <p>Constructor for Dota2Match.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public Dota2Match(SteamWebApiClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(Dota2MatchTeamInfo.class, new Dota2TeamInfoAdapter());
    }

    /**
     * <p>getLeagueListing.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2League>> getLeagueListing() {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueListing(VERSION_1));
        return json.thenApply(r -> asCollectionOf(Dota2League.class, "leagues", r));
    }

    /**
     * <p>getLiveLeagueGames.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2LiveLeagueGame>> getLiveLeagueGames() {
        return getLiveLeagueGames(-1, -1);
    }

    /**
     * <p>getLiveLeagueGames.</p>
     *
     * @param leagueId
     *         a int
     * @param matchId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2LiveLeagueGame>> getLiveLeagueGames(int leagueId, int matchId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLiveLeagueGames(VERSION_1, leagueId, matchId));
        return json.thenApply(r -> asCollectionOf(Dota2LiveLeagueGame.class, "games", r));
    }

    /**
     * <p>getTopLiveGame.</p>
     *
     * @param partner
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2TopLiveGame>> getTopLiveGame(int partner) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTopLiveGame(VERSION_1, partner));
        return json.thenApply(r -> asCollectionOf(Dota2TopLiveGame.class, "game_list", r));
    }

    /**
     * <p>getMatchDetails.</p>
     *
     * @param matchId
     *         a long
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Dota2MatchDetails> getMatchDetails(long matchId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetMatchDetails(VERSION_1, matchId));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2MatchDetails.class));
    }

    /**
     * <p>getMatchHistory.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Dota2MatchHistory> getMatchHistory() {
        return getMatchHistory(null);
    }

    /**
     * <p>getMatchHistory.</p>
     *
     * @param criteria
     *         a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistoryCriteria} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Dota2MatchHistory> getMatchHistory(Dota2MatchHistoryCriteria criteria) {
        CompletableFuture<JsonObject> json = sendRequest(new GetMatchHistory(VERSION_1, criteria));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2MatchHistory.class));
    }

    /**
     * <p>getMatchHistoryBySequenceNum.</p>
     *
     * @param startSeqNum
     *         a int
     * @param matchesRequested
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2MatchDetails>> getMatchHistoryBySequenceNum(long startSeqNum, int matchesRequested) {
        CompletableFuture<JsonObject> json = sendRequest(new GetMatchHistoryBySequenceNum(VERSION_1, startSeqNum, matchesRequested));
        return json.thenApply(r -> asCollectionOf(Dota2MatchDetails.class, "matches", r));
    }

    /**
     * <p>getTeamInfoById.</p>
     *
     * @param startTeamId
     *         a int
     * @param maxTeamCount
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2MatchTeamInfo>> getTeamInfoById(int startTeamId, int maxTeamCount) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTeamInfoByTeamID(VERSION_1, startTeamId, maxTeamCount));
        return json.thenApply(r -> asCollectionOf(Dota2MatchTeamInfo.class, "teams", r));
    }
}
