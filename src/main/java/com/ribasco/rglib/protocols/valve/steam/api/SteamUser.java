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
import com.ribasco.rglib.protocols.valve.steam.api.user.*;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamBanStatus;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamFriend;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamGroupId;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamPlayerProfile;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamUser extends SteamInterface {
    public SteamUser(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId) {
        return getFriendList(steamId, "friend");
    }

    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId, String relationship) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetFriendList(VERSION_1, steamId, relationship));
        return json.thenApply(new Function<JsonObject, List<SteamFriend>>() {
            @Override
            public List<SteamFriend> apply(JsonObject root) {
                JsonArray friendsList = root.getAsJsonObject("friendslist").getAsJsonArray("friends");
                Type type = new TypeToken<List<SteamFriend>>() {
                }.getType();
                return client.getJsonBuilder().fromJson(friendsList, type);
            }
        });
    }

    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(List<Long> steamIds) {
        return getPlayerBans(steamIds.toArray(new Long[0]));
    }

    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(Long... steamIds) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetPlayerBans(VERSION_1, steamIds));
        return json.thenApply(new Function<JsonObject, List<SteamBanStatus>>() {
            @Override
            public List<SteamBanStatus> apply(JsonObject root) {
                JsonArray players = root.getAsJsonArray("players");
                Type type = new TypeToken<List<SteamBanStatus>>() {
                }.getType();
                return client.getJsonBuilder().fromJson(players, type);
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
        CompletableFuture<JsonObject> json = client.sendRequest(new GetPlayerSummaries(VERSION_2, steamIds));
        return json.thenApply(new Function<JsonObject, List<SteamPlayerProfile>>() {
            @Override
            public List<SteamPlayerProfile> apply(JsonObject root) {
                JsonArray players = root.getAsJsonObject("response").getAsJsonArray("players");
                Type type = new TypeToken<List<SteamPlayerProfile>>() {
                }.getType();
                if (players != null) {
                    return jsonBuilder.fromJson(players, type);
                }
                return null;
            }
        });
    }

    public CompletableFuture<List<SteamGroupId>> getUserGroupList(long steamId) {
        CompletableFuture<JsonObject> json = client.sendRequest(new GetUserGroupList(VERSION_1, steamId));
        return json.thenApply((JsonObject root) -> {
            JsonArray groups = root.getAsJsonObject("response").getAsJsonArray("groups");
            Type type = new TypeToken<List<SteamGroupId>>() {
            }.getType();
            return jsonBuilder.fromJson(groups, type);
        });
    }

    public CompletableFuture<Long> getSteamIdFromVanityUrl(String urlPath, ResolveVanityURL.VanityUrlType type) {
        CompletableFuture<JsonObject> json = client.sendRequest(new ResolveVanityURL(VERSION_1, urlPath, type));
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
