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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocTypes;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import com.ibasco.agql.protocols.supercell.coc.webapi.adapters.CocLeagueSeasonDeserializer;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasonRankings;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasons;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagues;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the League interface</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/leagues">Clash of Clans API - Leagues</a>
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLeagues extends CocWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient} instance
     */
    public CocLeagues(CocWebApiClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    protected void configureBuilder(GsonBuilder builder) {
        super.configureBuilder(builder);
        builder.registerTypeAdapter(CocLeagueSeason.class, new CocLeagueSeasonDeserializer());
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which contains a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague}
     */
    public CompletableFuture<List<CocLeague>> getLeagueList() {
        return getLeagueList(-1);
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which contains a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague}
     */
    public CompletableFuture<List<CocLeague>> getLeagueList(int limit) {
        return getLeagueList(limit, -1, -1);
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *         Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both. Otherwise use
     *         -1 to disregard.
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which contains a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague}
     */
    public CompletableFuture<List<CocLeague>> getLeagueList(int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagues(VERSION_1, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocLeague>>() {
            @Override
            public List<CocLeague> apply(JsonObject root) {
                return builder().fromJson(root.getAsJsonArray("items"), CocTypes.COC_LIST_LEAGUE);
            }
        });
    }

    /**
     * <p>Get league information</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague}
     */
    public CompletableFuture<CocLeague> getLeagueInfo(int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueInfo(VERSION_1, leagueId));
        return json.thenApply(root -> builder().fromJson(root, CocLeague.class));
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason}
     */
    public CompletableFuture<List<CocLeagueSeason>> getLeagueSeasons(int leagueId) {
        return getLeagueSeasons(leagueId, -1);
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason}
     */
    public CompletableFuture<List<CocLeagueSeason>> getLeagueSeasons(int leagueId, int limit) {
        return getLeagueSeasons(leagueId, limit, -1, -1);
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason}
     */
    public CompletableFuture<List<CocLeagueSeason>> getLeagueSeasons(int leagueId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueSeasons(VERSION_1, leagueId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocLeagueSeason>>() {
            @Override
            public List<CocLeagueSeason> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocLeagueSeason>>() {
                }.getType());
            }
        });
    }

    /**
     * <p>Get league season player rankings. Note that league season information is available only for Legend
     * League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans Season Id
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo}
     *
     * @see CocLeagues#getLeagueSeasons(int)
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getLeagueSeasonsPlayerRankings(int leagueId, String seasonId) {
        return getLeagueSeasonsPlayerRankings(leagueId, seasonId, -1);
    }

    /**
     * <p>Get league season player rankings. Note that league season information is available only for Legend
     * League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans Season Id
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo}
     *
     * @see CocLeagues#getLeagueSeasons(int)
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getLeagueSeasonsPlayerRankings(int leagueId, String seasonId, int limit) {
        return getLeagueSeasonsPlayerRankings(leagueId, seasonId, limit, -1, -1);
    }

    /**
     * <p>Get league season player rankings. Note that league season information is available only for Legend
     * League.</p>
     *
     * @param leagueId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link java.lang.Integer} representing a valid Clash of Clans Season Id
     * @param limit
     *         An {@link java.lang.Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link java.lang.Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result for a {@link java.util.List} of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo}
     *
     * @see CocLeagues#getLeagueSeasons(int)
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getLeagueSeasonsPlayerRankings(int leagueId, String seasonId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueSeasonRankings(VERSION_1, leagueId, seasonId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocPlayerRankInfo>>() {
            @Override
            public List<CocPlayerRankInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, CocTypes.COC_LIST_PLAYER_RANK_INFO);
            }
        });
    }
}
