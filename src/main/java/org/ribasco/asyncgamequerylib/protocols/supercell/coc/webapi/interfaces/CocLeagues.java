/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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

package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.CocTypes;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.CocWebApiClient;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.CocWebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueInfo;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasonRankings;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagueSeasons;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces.leagues.GetLeagues;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos.CocLeague;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CocLeagues extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link CocWebApiClient} instance
     */
    public CocLeagues(CocWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<CocLeague>> getLeagueList() {
        return getLeagueList(-1);
    }

    public CompletableFuture<List<CocLeague>> getLeagueList(int limit) {
        return getLeagueList(limit, -1, -1);
    }

    public CompletableFuture<List<CocLeague>> getLeagueList(int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagues(VERSION_1, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocLeague>>() {
            @Override
            public List<CocLeague> apply(JsonObject root) {
                return builder().fromJson(root.getAsJsonArray("items"), CocTypes.COC_LIST_LEAGUE);
            }
        });
    }

    public CompletableFuture<CocLeague> getLeagueInfo(int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueInfo(VERSION_1, leagueId));
        return json.thenApply(root -> builder().fromJson(root, CocLeague.class));
    }

    public CompletableFuture<List<Map<String, String>>> getLeagueSeasons(int leagueId) {
        return getLeagueSeasons(leagueId, -1);
    }

    public CompletableFuture<List<Map<String, String>>> getLeagueSeasons(int leagueId, int limit) {
        return getLeagueSeasons(leagueId, limit, -1, -1);
    }

    public CompletableFuture<List<Map<String, String>>> getLeagueSeasons(int leagueId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLeagueSeasons(VERSION_1, leagueId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<Map<String, String>>>() {
            @Override
            public List<Map<String, String>> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
        });
    }

    public CompletableFuture<List<CocPlayerRankInfo>> getLeagueSeasonsPlayerRankings(int leagueId, String seasonId) {
        return getLeagueSeasonsPlayerRankings(leagueId, seasonId, -1);
    }

    public CompletableFuture<List<CocPlayerRankInfo>> getLeagueSeasonsPlayerRankings(int leagueId, String seasonId, int limit) {
        return getLeagueSeasonsPlayerRankings(leagueId, seasonId, limit, -1, -1);
    }

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
