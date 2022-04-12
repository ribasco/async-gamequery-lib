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
import com.ibasco.agql.protocols.valve.steam.webapi.enums.VanityUrlType;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.user.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamBanStatus;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamFriend;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamGroupId;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerProfile;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamUser extends SteamWebApiInterface {
    /**
     * <p>Constructor for SteamUser.</p>
     *
     * @param client a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public SteamUser(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getFriendList.</p>
     *
     * @param steamId a long
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId) {
        return getFriendList(steamId, "friend");
    }

    /**
     * <p>getFriendList.</p>
     *
     * @param steamId a long
     * @param relationship a {@link java.lang.String} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamFriend>> getFriendList(long steamId, String relationship) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFriendList(VERSION_1, steamId, relationship));
        return json.thenApply(root -> {
            JsonArray friendsList = root.getAsJsonObject("friendslist").getAsJsonArray("friends");
            Type type = new TypeToken<List<SteamFriend>>() {
            }.getType();
            return builder().fromJson(friendsList, type);
        });
    }

    /**
     * <p>getPlayerBans.</p>
     *
     * @param steamIds a {@link java.util.List} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(List<Long> steamIds) {
        return getPlayerBans(steamIds.toArray(new Long[0]));
    }

    /**
     * <p>getPlayerBans.</p>
     *
     * @param steamIds a {@link java.lang.Long} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamBanStatus>> getPlayerBans(Long... steamIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerBans(VERSION_1, steamIds));
        return json.thenApply(root -> {
            JsonArray players = root.getAsJsonArray("players");
            Type type = new TypeToken<List<SteamBanStatus>>() {
            }.getType();
            return builder().fromJson(players, type);
        });
    }

    /**
     * <p>getPlayerProfile.</p>
     *
     * @param steamId a {@link java.lang.Long} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamPlayerProfile> getPlayerProfile(Long steamId) {
        return getPlayerProfiles(steamId).thenApply(steamPlayerProfiles -> {
            if (steamPlayerProfiles != null && steamPlayerProfiles.size() == 1)
                return steamPlayerProfiles.get(0);
            return null;
        });
    }

    /**
     * <p>getPlayerProfiles.</p>
     *
     * @param steamIds a {@link java.lang.Long} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamPlayerProfile>> getPlayerProfiles(Long... steamIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerSummaries(VERSION_2, steamIds));
        return json.thenApply(root -> {
            JsonArray players = root.getAsJsonObject("response").getAsJsonArray("players");
            Type type = new TypeToken<List<SteamPlayerProfile>>() {
            }.getType();
            if (players != null) {
                return builder().fromJson(players, type);
            }
            return null;
        });
    }

    /**
     * <p>getUserGroupList.</p>
     *
     * @param steamId a long
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamGroupId>> getUserGroupList(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetUserGroupList(VERSION_1, steamId));
        return json.thenApply((JsonObject root) -> {
            JsonArray groups = root.getAsJsonObject("response").getAsJsonArray("groups");
            Type type = new TypeToken<List<SteamGroupId>>() {
            }.getType();
            return builder().fromJson(groups, type);
        });
    }

    /**
     * <p>getSteamIdFromVanityUrl.</p>
     *
     * @param urlPath a {@link java.lang.String} object
     * @param type a {@link com.ibasco.agql.protocols.valve.steam.webapi.enums.VanityUrlType} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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
