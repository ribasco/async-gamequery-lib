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

package com.ribasco.rglib.protocols.valve.steam.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ribasco.rglib.protocols.valve.steam.SteamWebApiClient;
import com.ribasco.rglib.protocols.valve.steam.api.userstats.*;
import com.ribasco.rglib.protocols.valve.steam.pojos.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/26/2016.
 */
public class SteamUserStats extends SteamInterface {
    public SteamUserStats(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamGameAchievement>> getGlobalAchievementPercentagesForApp(int appId) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetGlobalAchievementPercentagesForApp(VERSION_2, appId));
        return json.thenApply(new Function<JsonObject, List<SteamGameAchievement>>() {
            @Override
            public List<SteamGameAchievement> apply(JsonObject root) {
                JsonObject achievementPct = root.getAsJsonObject("achievementpercentages");
                JsonArray achievements = achievementPct.getAsJsonArray("achievements");
                Type type = new TypeToken<List<SteamGameAchievement>>() {
                }.getType();
                return client.getJsonBuilder().fromJson(achievements, type);
            }
        });
    }

    //TODO: Figure out what that damn name[0] parameter should be
    public CompletableFuture<List<Object>> getGlobalStatsForGame(int appId, int count, String name) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetGlobalStatsForGame(VERSION_2, appId, count, name));
        return json.thenApply(new Function<JsonObject, List<Object>>() {
            @Override
            public List<Object> apply(JsonObject root) {
                return null;
            }
        });
    }

    public CompletableFuture<SteamGameStatsSchemaInfo> getSchemaForGame(int appId) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetSchemaForGame(VERSION_2, appId));
        return json.thenApply(root -> {
            SteamGameStatsSchemaInfo info = new SteamGameStatsSchemaInfo();
            JsonObject availableGameStats = root.getAsJsonObject("game").getAsJsonObject("availableGameStats");
            Type achievementsSchemaType = new TypeToken<List<SteamGameAchievementSchema>>() {
            }.getType();
            Type statsSchemaType = new TypeToken<List<SteamGameStatsSchema>>() {
            }.getType();
            info.setAchievementSchemaList(client.getJsonBuilder().fromJson(availableGameStats.getAsJsonArray("achievements"), achievementsSchemaType));
            info.setStatsSchemaList(client.getJsonBuilder().fromJson(availableGameStats.getAsJsonArray("stats"), statsSchemaType));
            return info;
        });
    }

    public CompletableFuture<Integer> getNumberOfCurrentPlayers(int appId) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetNumberOfCurrentPlayers(VERSION_1, appId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("player_count").getAsInt());
    }

    public CompletableFuture<List<SteamPlayerAchievement>> getPlayerAchievements(long steamId, int appId) {
        return getPlayerAchievements(steamId, appId, null);
    }

    public CompletableFuture<List<SteamPlayerAchievement>> getPlayerAchievements(long steamId, int appId, String language) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetPlayerAchievements(VERSION_1, steamId, appId, language));
        return json.thenApply(new Function<JsonObject, List<SteamPlayerAchievement>>() {
            @Override
            public List<SteamPlayerAchievement> apply(JsonObject root) {
                JsonArray playerAchievements = root.getAsJsonObject("playerstats").getAsJsonArray("achievements");
                Type pAchievementType = new TypeToken<List<SteamPlayerAchievement>>() {
                }.getType();
                return client.getJsonBuilder().fromJson(playerAchievements, pAchievementType);
            }
        });
    }

    public CompletableFuture<SteamPlayerStats> getUserStatsForGame(long steamId, int appId) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetUserStatsForGame(VERSION_2, steamId, appId));
        return json.thenApply(root -> client.getJsonBuilder().fromJson(root.getAsJsonObject("playerstats"), SteamPlayerStats.class));
    }
}
