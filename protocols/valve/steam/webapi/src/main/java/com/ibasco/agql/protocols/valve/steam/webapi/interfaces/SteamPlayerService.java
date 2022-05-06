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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetBadges;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetCommunityBadgeProgress;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetOwnedGames;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetRecentlyPlayedGames;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetSteamGameLenderId;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player.GetSteamLevel;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerBadgeInfo;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerRecentPlayed;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamQuestStatus;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 10/26/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerService extends SteamWebApiInterface {

    /**
     * <p>Constructor for SteamPlayerService.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public SteamPlayerService(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getRecentlyPlayedGames.</p>
     *
     * @param steamId
     *         a long
     * @param count
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamPlayerRecentPlayed>> getRecentlyPlayedGames(long steamId, int count) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRecentlyPlayedGames(VERSION_1, steamId, count));
        return json.thenApply(root -> {
            JsonArray games = root.getAsJsonObject("response").getAsJsonArray("games");
            Type type = new TypeToken<List<SteamPlayerRecentPlayed>>() {}.getType();
            return builder().fromJson(games, type);
        });
    }

    /**
     * <p>getOwnedGames.</p>
     *
     * @param steamId
     *         a long
     * @param includeAppInfo
     *         a boolean
     * @param includePlayedFreeGames
     *         a boolean
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamPlayerOwnedGame>> getOwnedGames(long steamId, boolean includeAppInfo, boolean includePlayedFreeGames) {
        CompletableFuture<JsonObject> json = sendRequest(new GetOwnedGames(VERSION_1, steamId, includeAppInfo, includePlayedFreeGames));
        return json.thenApply(root -> {
            JsonArray games = root.getAsJsonObject("response").getAsJsonArray("games");
            Type type = new TypeToken<List<SteamPlayerOwnedGame>>() {
            }.getType();
            return builder().fromJson(games, type);
        });
    }

    /**
     * <p>getBadges.</p>
     *
     * @param steamId
     *         a long
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamPlayerBadgeInfo> getBadges(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetBadges(VERSION_1, steamId));
        return json.thenApply(root -> {
            JsonElement playerBadgeInfo = root.getAsJsonObject("response");
            return builder().fromJson(playerBadgeInfo, SteamPlayerBadgeInfo.class);
        });
    }

    /**
     * <p>getCommunityBadgeProgress.</p>
     *
     * @param steamId
     *         a long
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamQuestStatus>> getCommunityBadgeProgress(long steamId) {
        return getCommunityBadgeProgress(steamId, -1);
    }

    /**
     * <p>getCommunityBadgeProgress.</p>
     *
     * @param steamId
     *         a long
     * @param badgeId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamQuestStatus>> getCommunityBadgeProgress(long steamId, int badgeId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetCommunityBadgeProgress(VERSION_1, steamId, badgeId));
        return json.thenApply(root -> {
            JsonArray quests = root.getAsJsonObject("response").getAsJsonArray("quests");
            if (quests != null) {
                return builder().fromJson(quests, new TypeToken<List<SteamQuestStatus>>() {}.getType());
            }
            return new ArrayList<>();
        });
    }

    /**
     * <p>getSteamLevel.</p>
     *
     * @param steamId
     *         a long
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Integer> getSteamLevel(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSteamLevel(VERSION_1, steamId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("player_level").getAsInt());
    }

    /**
     * <p>getSteamGameLenderId.</p>
     *
     * @param steamId
     *         a long
     * @param appId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<String> getSteamGameLenderId(long steamId, int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSteamGameLenderId(VERSION_1, steamId, appId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("lender_steamid").getAsString());
    }
}
