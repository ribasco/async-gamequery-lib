/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

public class SteamNews extends SteamWebApiInterface {
    public SteamNews(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId) {
        return getNewsForApp(appId, -1, -1, -1, "");
    }

    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId, int maxLength, int endDate, int count, String feeds) {
        GetNewsForApp request = new GetNewsForApp(2, appId).maxLength(maxLength).endDate(endDate).count(count).feeds(feeds);
        CompletableFuture<JsonObject> newsItems = sendRequest(request);
        return newsItems.thenApply(root -> {
            JsonObject appNews = root.getAsJsonObject("appnews");
            JsonArray newsItems1 = appNews.getAsJsonArray("newsitems");
            Type newsListType = new TypeToken<List<SteamNewsItem>>() {
            }.getType();
            return builder().fromJson(newsItems1, newsListType);
        });
    }
}
