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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.adapters.SteamAssetClassInfoMapDeserializer;
import com.ibasco.agql.protocols.valve.steam.webapi.adapters.SteamAssetDescDeserializer;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.economy.GetAssetClassInfo;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.economy.GetAssetPrices;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamAssetClassInfo;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamAssetDescription;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamAssetPriceInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Created by raffy on 10/26/2016.
 */
public class SteamEconomy extends SteamWebApiInterface {
    public SteamEconomy(SteamWebApiClient client) {
        super(client);
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(new TypeToken<Map<String, SteamAssetClassInfo>>() {
        }.getType(), new SteamAssetClassInfoMapDeserializer());
        builder.registerTypeAdapter(SteamAssetDescription.class, new SteamAssetDescDeserializer());
    }

    public CompletableFuture<List<SteamAssetPriceInfo>> getAssetPrices(int appId) {
        return getAssetPrices(appId, null, null);
    }

    public CompletableFuture<List<SteamAssetPriceInfo>> getAssetPrices(int appId, String currency, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAssetPrices(VERSION_1, appId, currency, language));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            boolean success = result.getAsJsonPrimitive("success").getAsBoolean();
            if (success) {
                JsonArray assets = result.getAsJsonArray("assets");
                Type type = new TypeToken<List<SteamAssetPriceInfo>>() {
                }.getType();
                return builder().fromJson(assets, type);
            }
            return new ArrayList<SteamAssetPriceInfo>();
        });
    }

    public CompletableFuture<Map<String, SteamAssetClassInfo>> getAssetClassInfo(int appId, String language, List<Long> classIds) {
        return getAssetClassInfo(appId, language, classIds.toArray(new Long[0]));
    }

    public CompletableFuture<Map<String, SteamAssetClassInfo>> getAssetClassInfo(int appId, String language, Long... classIds) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAssetClassInfo(VERSION_1, appId, language, classIds));
        return json.thenApply(new Function<JsonObject, Map<String, SteamAssetClassInfo>>() {
            @Override
            public Map<String, SteamAssetClassInfo> apply(JsonObject root) {
                JsonObject result = root.getAsJsonObject("result");
                Type type = new TypeToken<Map<String, SteamAssetClassInfo>>() {
                }.getType();
                return builder().fromJson(result, type);
            }
        });
    }
}
