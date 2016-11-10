/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocTypes;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import org.ribasco.agql.protocols.supercell.coc.webapi.adapters.CocLeagueSeasonDeserializer;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueInfo;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasonRankings;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasons;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagues;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the League interface</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/leagues">Clash of Clans API - Leagues</a>
 */
public class CocLeagues extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link CocWebApiClient} instance
     */
    public CocLeagues(CocWebApiClient client) {
        super(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureBuilder(GsonBuilder builder) {
        super.configureBuilder(builder);
        builder.registerTypeAdapter(CocLeagueSeason.class, new CocLeagueSeasonDeserializer());
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocLeague}
     */
    public CompletableFuture<List<CocLeague>> getLeagueList() {
        return getLeagueList(-1);
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocLeague}
     */
    public CompletableFuture<List<CocLeague>> getLeagueList(int limit) {
        return getLeagueList(limit, -1, -1);
    }

    /**
     * <p>Get list of leagues</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *         Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both. Otherwise use
     *         -1 to disregard.
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocLeague}
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
     *         An {@link Integer} representing a valid Clash of Clans League Id
     *
     * @return A {@link CompletableFuture} containing a future result for {@link CocLeague}
     */
    public CompletableFuture<CocLeague> getLeagueInfo(int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueInfo(VERSION_1, leagueId));
        return json.thenApply(root -> builder().fromJson(root, CocLeague.class));
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link Integer} representing a valid Clash of Clans League Id
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLeagueSeason}
     */
    public CompletableFuture<List<CocLeagueSeason>> getLeagueSeasons(int leagueId) {
        return getLeagueSeasons(leagueId, -1);
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link Integer} representing a valid Clash of Clans League Id
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLeagueSeason}
     */
    public CompletableFuture<List<CocLeagueSeason>> getLeagueSeasons(int leagueId, int limit) {
        return getLeagueSeasons(leagueId, limit, -1, -1);
    }

    /**
     * <p>Get league seasons. Note that league season information is available only for Legend League.</p>
     *
     * @param leagueId
     *         An {@link Integer} representing a valid Clash of Clans League Id
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLeagueSeason}
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
     *         An {@link Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link Integer} representing a valid Clash of Clans Season Id
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocPlayerRankInfo}
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
     *         An {@link Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link Integer} representing a valid Clash of Clans Season Id
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocPlayerRankInfo}
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
     *         An {@link Integer} representing a valid Clash of Clans League Id
     * @param seasonId
     *         An {@link Integer} representing a valid Clash of Clans Season Id
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocPlayerRankInfo}
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
