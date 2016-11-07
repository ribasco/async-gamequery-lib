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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import org.ribasco.agql.protocols.valve.steam.webapi.enums.VanityUrlType;
import org.ribasco.agql.protocols.valve.steam.webapi.interfaces.user.*;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamBanStatus;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamFriend;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamGroupId;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamUser extends SteamWebApiInterface {
    public SteamUser(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId) {
        return getFriendList(steamId, "friend");
    }

    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId, String relationship) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFriendList(VERSION_1, steamId, relationship));
        return json.thenApply(new Function<JsonObject, List<SteamFriend>>() {
            @Override
            public List<SteamFriend> apply(JsonObject root) {
                JsonArray friendsList = root.getAsJsonObject("friendslist").getAsJsonArray("friends");
                Type type = new TypeToken<List<SteamFriend>>() {
                }.getType();
                return builder().fromJson(friendsList, type);
            }
        });
    }

    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(List<Long> steamIds) {
        return getPlayerBans(steamIds.toArray(new Long[0]));
    }

    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(Long... steamIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerBans(VERSION_1, steamIds));
        return json.thenApply(new Function<JsonObject, List<SteamBanStatus>>() {
            @Override
            public List<SteamBanStatus> apply(JsonObject root) {
                JsonArray players = root.getAsJsonArray("players");
                Type type = new TypeToken<List<SteamBanStatus>>() {
                }.getType();
                return builder().fromJson(players, type);
            }
        });
    }

    public CompletableFuture<SteamPlayerProfile> getPlayerProfile(Long steamId) {
        return getPlayerProfiles(steamId).thenApply(steamPlayerProfiles -> {
            if (steamPlayerProfiles != null && steamPlayerProfiles.size() == 1)
                return steamPlayerProfiles.get(0);
            return null;
        });
    }

    public CompletableFuture<List<SteamPlayerProfile>> getPlayerProfiles(Long... steamIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerSummaries(VERSION_2, steamIds));
        return json.thenApply(new Function<JsonObject, List<SteamPlayerProfile>>() {
            @Override
            public List<SteamPlayerProfile> apply(JsonObject root) {
                JsonArray players = root.getAsJsonObject("response").getAsJsonArray("players");
                Type type = new TypeToken<List<SteamPlayerProfile>>() {
                }.getType();
                if (players != null) {
                    return builder().fromJson(players, type);
                }
                return null;
            }
        });
    }

    public CompletableFuture<List<SteamGroupId>> getUserGroupList(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetUserGroupList(VERSION_1, steamId));
        return json.thenApply((JsonObject root) -> {
            JsonArray groups = root.getAsJsonObject("response").getAsJsonArray("groups");
            Type type = new TypeToken<List<SteamGroupId>>() {
            }.getType();
            return builder().fromJson(groups, type);
        });
    }

    public CompletableFuture<Long> getSteamIdFromVanityUrl(String urlPath, VanityUrlType type) {
        CompletableFuture<JsonObject> json = sendRequest(new ResolveVanityURL(VERSION_1, urlPath, type));
        return json.thenApply(root -> {
            JsonObject response = root.getAsJsonObject("response");
            int success = response.getAsJsonPrimitive("success").getAsInt();
            if (success == 1) {
                String steamId = response.getAsJsonPrimitive("steamid").getAsString();
                if (!StringUtils.isEmpty(steamId) && StringUtils.isNumeric(steamId)) {
                    return Long.valueOf(steamId);
                }
            }
            return null;
        });
    }
}
