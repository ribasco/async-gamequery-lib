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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.userstats.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 10/26/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamUserStats extends SteamWebApiInterface {

    /**
     * <p>Constructor for SteamUserStats.</p>
     *
     * @param client a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public SteamUserStats(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getGlobalAchievementPercentagesForApp.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamGameAchievement>> getGlobalAchievementPercentagesForApp(int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetGlobalAchievementPercentagesForApp(VERSION_2, appId));
        return json.thenApply(root -> {
            JsonObject achievementPct = root.getAsJsonObject("achievementpercentages");
            JsonArray achievements = achievementPct.getAsJsonArray("achievements");
            Type type = new TypeToken<List<SteamGameAchievement>>() {
            }.getType();
            return builder().fromJson(achievements, type);
        });
    }

    /**
     * <p>getGlobalStatsForGame.</p>
     *
     * @param appId a int
     * @param count a int
     * @param name a {@link java.lang.String} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Object>> getGlobalStatsForGame(int appId, int count, String name) {
        CompletableFuture<JsonObject> json = sendRequest(new GetGlobalStatsForGame(VERSION_2, appId, count, name));
        return json.thenApply(root -> null);
    }

    /**
     * <p>getSchemaForGame.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamGameStatsSchemaInfo> getSchemaForGame(int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSchemaForGame(VERSION_2, appId));
        return json.thenApply(root -> {
            SteamGameStatsSchemaInfo info = new SteamGameStatsSchemaInfo();
            JsonObject availableGameStats = root.getAsJsonObject("game").getAsJsonObject("availableGameStats");
            Type achievementsSchemaType = new TypeToken<List<SteamGameAchievementSchema>>() {}.getType();
            Type statsSchemaType = new TypeToken<List<SteamGameStatsSchema>>() {}.getType();
            info.setAchievementSchemaList(fromJson(availableGameStats.getAsJsonArray("achievements"), achievementsSchemaType));
            info.setStatsSchemaList(fromJson(availableGameStats.getAsJsonArray("stats"), statsSchemaType));
            return info;
        });
    }

    /**
     * <p>getNumberOfCurrentPlayers.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Integer> getNumberOfCurrentPlayers(int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetNumberOfCurrentPlayers(VERSION_1, appId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("player_count").getAsInt());
    }

    /**
     * <p>getPlayerAchievements.</p>
     *
     * @param steamId a long
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamPlayerAchievement>> getPlayerAchievements(long steamId, int appId) {
        return getPlayerAchievements(steamId, appId, null);
    }

    /**
     * <p>getPlayerAchievements.</p>
     *
     * @param steamId a long
     * @param appId a int
     * @param language a {@link java.lang.String} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamPlayerAchievement>> getPlayerAchievements(long steamId, int appId, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerAchievements(VERSION_1, steamId, appId, language));
        return json.thenApply(r -> asCollectionOf(SteamPlayerAchievement.class, "achievements", r.getAsJsonObject("playerstats"), ArrayList.class, true));
    }

    /**
     * <p>getUserStatsForGame.</p>
     *
     * @param steamId a long
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamPlayerStats> getUserStatsForGame(long steamId, int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetUserStatsForGame(VERSION_2, steamId, appId));
        return json.thenApply(root -> fromJson(root.getAsJsonObject("playerstats"), SteamPlayerStats.class));
    }
}
