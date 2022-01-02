/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            Type type = new TypeToken<Map<String, SteamAssetClassInfo>>() {
            }.getType();
            return builder().fromJson(result, type);
        });
    }
}
