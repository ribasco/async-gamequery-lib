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

package org.ribasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import org.ribasco.agql.protocols.valve.steam.webapi.interfaces.player.*;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerBadgeInfo;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerRecentPlayed;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamQuestStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/26/2016.
 */
public class SteamPlayerService extends SteamWebApiInterface {
    public SteamPlayerService(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamPlayerRecentPlayed>> getRecentlyPlayedGames(long steamId, int count) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRecentlyPlayedGames(VERSION_1, steamId, count));
        return json.thenApply(new Function<JsonObject, List<SteamPlayerRecentPlayed>>() {
            @Override
            public List<SteamPlayerRecentPlayed> apply(JsonObject root) {
                JsonArray games = root.getAsJsonObject("response").getAsJsonArray("games");
                Type type = new TypeToken<List<SteamPlayerRecentPlayed>>() {
                }.getType();
                return builder().fromJson(games, type);
            }
        });
    }

    public CompletableFuture<List<SteamPlayerOwnedGame>> getOwnedGames(long steamId, boolean includeAppInfo, boolean includePlayedFreeGames) {
        CompletableFuture<JsonObject> json = sendRequest(new GetOwnedGames(VERSION_1, steamId, includeAppInfo, includePlayedFreeGames));
        return json.thenApply(new Function<JsonObject, List<SteamPlayerOwnedGame>>() {
            @Override
            public List<SteamPlayerOwnedGame> apply(JsonObject root) {
                JsonArray games = root.getAsJsonObject("response").getAsJsonArray("games");
                Type type = new TypeToken<List<SteamPlayerOwnedGame>>() {
                }.getType();
                return builder().fromJson(games, type);
            }
        });
    }

    public CompletableFuture<SteamPlayerBadgeInfo> getBadges(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetBadges(VERSION_1, steamId));
        return json.thenApply(root -> {
            JsonElement playerBadgeInfo = root.getAsJsonObject("response");
            return builder().fromJson(playerBadgeInfo, SteamPlayerBadgeInfo.class);
        });
    }

    public CompletableFuture<List<SteamQuestStatus>> getCommunityBadgeProgress(long steamId) {
        return getCommunityBadgeProgress(steamId, -1);
    }

    public CompletableFuture<List<SteamQuestStatus>> getCommunityBadgeProgress(long steamId, int badgeId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetCommunityBadgeProgress(VERSION_1, steamId, badgeId));
        return json.thenApply(new Function<JsonObject, List<SteamQuestStatus>>() {
            @Override
            public List<SteamQuestStatus> apply(JsonObject root) {
                JsonArray quests = root.getAsJsonObject("response").getAsJsonArray("quests");
                if (quests != null) {
                    Type type = new TypeToken<List<SteamQuestStatus>>() {
                    }.getType();
                    return builder().fromJson(quests, type);
                }
                return new ArrayList<>();
            }
        });
    }

    public CompletableFuture<Integer> getSteamLevel(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSteamLevel(VERSION_1, steamId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("player_level").getAsInt());
    }

    public CompletableFuture<String> getSteamGameLenderId(long steamId, int appId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSteamGameLenderId(VERSION_1, steamId, appId));
        return json.thenApply(root -> root.getAsJsonObject("response").getAsJsonPrimitive("lender_steamid").getAsString());
    }
}
