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
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news.GetNewsForApp;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamNewsItem;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>SteamNews class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamNews extends SteamWebApiInterface {
    /**
     * <p>Constructor for SteamNews.</p>
     *
     * @param client a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public SteamNews(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getNewsForApp.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId) {
        return getNewsForApp(appId, -1, -1, -1, "");
    }

    /**
     * <p>getNewsForApp.</p>
     *
     * @param appId a int
     * @param maxLength a int
     * @param endDate a int
     * @param count a int
     * @param feeds a {@link java.lang.String} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId, int maxLength, int endDate, int count, String feeds) {
        GetNewsForApp request = new GetNewsForApp(2, appId).maxLength(maxLength).endDate(endDate).count(count).feeds(feeds);
        CompletableFuture<JsonObject> newsItems = sendRequest(request);
        return newsItems.thenApply(root -> {
            JsonObject appNews = root.getAsJsonObject("appnews");
            JsonArray newsItems1 = appNews.getAsJsonArray("newsitems");
            Type newsListType = new TypeToken<List<SteamNewsItem>>() {}.getType();
            return builder().fromJson(newsItems1, newsListType);
        });
    }
}
