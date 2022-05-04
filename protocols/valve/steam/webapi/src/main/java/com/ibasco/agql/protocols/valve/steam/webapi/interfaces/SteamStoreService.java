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

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.GetAppList;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.GetLocalizedNameForTags;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.GetMostPopularTags;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.LocalizedNameTag;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.PopularTag;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.SteamStoreAppResponse;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SteamStoreService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamStoreService(SteamWebApiClient client) {
        super(client);
    }

    /**
     * Gets a list of apps available on the Steam Store.
     *
     * @return A {@link CompletableFuture} with return type of {@link SteamStoreAppResponse}
     */
    public CompletableFuture<SteamStoreAppResponse> getAppList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppList(VERSION_1));
        return json.thenApply(res -> res.get("response").getAsJsonObject()).thenApply(jsonObject -> builder().fromJson(jsonObject, SteamStoreAppResponse.class));
    }

    /**
     * Gets a list of apps available on the Steam Store.
     *
     * @return A {@link CompletableFuture} with return type of {@link SteamStoreAppResponse}
     */
    public CompletableFuture<SteamStoreAppResponse> getAppList(int lastAppId, int maxResults) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppList(VERSION_1, lastAppId, maxResults));
        return json.thenApply(res -> res.get("response").getAsJsonObject()).thenApply(jsonObject -> builder().fromJson(jsonObject, SteamStoreAppResponse.class));
    }

    /**
     * Gets a list of apps available on the Steam Store.
     *
     * @return A {@link CompletableFuture} with return type of {@link SteamStoreAppResponse}
     */
    public CompletableFuture<SteamStoreAppResponse> getAppList(int maxResults) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppList(VERSION_1, maxResults));
        return json.thenApply(res -> res.get("response").getAsJsonObject()).thenApply(jsonObject -> builder().fromJson(jsonObject, SteamStoreAppResponse.class));
    }

    /**
     * Gets a list of apps available on the Steam Store.
     *
     * @param ifModifiedSince
     *         Return only items that have been modified since this date.
     * @param haveDescriptionLanguage
     *         Return only items that have a description in this language.
     * @param includeGames
     *         Include games (defaults to enabled)
     * @param includeDlc
     *         Include DLC
     * @param includeSoftware
     *         Include software items
     * @param includeVideos
     *         Include videos and series
     * @param includeHardware
     *         Include hardware
     * @param lastAppId
     *         For continuations, this is the last appid returned from the previous call.
     * @param maxResults
     *         Number of results to return at a time. Default 10k, max 50k.
     *
     * @return A {@link CompletableFuture} with return type of {@link SteamStoreAppResponse}
     */
    public CompletableFuture<SteamStoreAppResponse> getAppList(int ifModifiedSince, String haveDescriptionLanguage, boolean includeGames, boolean includeDlc, boolean includeSoftware, boolean includeVideos, boolean includeHardware, int lastAppId, int maxResults) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppList(VERSION_1, ifModifiedSince, haveDescriptionLanguage, includeGames, includeDlc, includeSoftware, includeVideos, includeHardware, lastAppId, maxResults));
        return json.thenApply(res -> res.get("response").getAsJsonObject()).thenApply(jsonObject -> builder().fromJson(jsonObject, SteamStoreAppResponse.class));
    }

    /**
     * Gets tag names in a different language
     *
     * @param language
     *         The localized language (e.g. russian, english)
     * @param tagIds
     *         A collection of tag ids
     *
     * @return A {@link CompletableFuture} with return type of {@link LocalizedNameTag}
     */
    public CompletableFuture<List<LocalizedNameTag>> getLocalizedNameForTags(String language, Collection<Integer> tagIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocalizedNameForTags(VERSION_1, language, tagIds));
        return json.thenApply(r -> r.get("response").getAsJsonObject().get("tags")).thenApply(response -> {
            Type type = new TypeToken<List<LocalizedNameTag>>() {}.getType();
            return builder().fromJson(response, type);
        });
    }

    /**
     * Get all whitelisted tags, with localized names.
     *
     * @param language
     *         The language name (e.g. english)
     *
     * @return @return A {@link CompletableFuture} with return type of {@link PopularTag}
     */
    public CompletableFuture<List<PopularTag>> getPopularTag(String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetMostPopularTags(VERSION_1, language));
        return json.thenApply(j -> j.get("response").getAsJsonObject().get("tags")).thenApply(response -> {
            Type type = new TypeToken<List<PopularTag>>() {}.getType();
            return builder().fromJson(response, type);
        });
    }
}
