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
import com.ribasco.rglib.protocols.valve.steam.api.news.GetNewsForApp;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamNewsItem;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/26/2016.
 */
public class SteamNews extends SteamInterface {
    public SteamNews(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId) {
        return getNewsForApp(appId, -1, -1, -1, "");
    }

    public CompletableFuture<List<SteamNewsItem>> getNewsForApp(int appId, int maxLength, int endDate, int count, String feeds) {
        GetNewsForApp request = new GetNewsForApp(2, appId);
        request.setMaxLength(maxLength);
        request.setEndDate(endDate);
        request.setCount(count);
        request.setFeeds(feeds);
        CompletableFuture<JsonObject> newsItems = client.sendRequest(request);
        return newsItems.thenApply(new Function<JsonObject, List<SteamNewsItem>>() {
            @Override
            public List<SteamNewsItem> apply(JsonObject root) {
                JsonObject appNews = root.getAsJsonObject("appnews");
                JsonArray newsItems = appNews.getAsJsonArray("newsitems");
                Type newsListType = new TypeToken<List<SteamNewsItem>>() {
                }.getType();
                return client.getJsonBuilder().fromJson(newsItems, newsListType);
            }
        });
    }
}
