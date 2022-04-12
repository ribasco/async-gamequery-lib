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
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.econitems.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsStoreMeta;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconPlayerItem;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchema;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Methods relating to in-game items for supported games.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItems extends SteamWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} instance
     */
    public SteamEconItems(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getPlayerItems.</p>
     *
     * @param appId a int
     * @param steamId a long
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamEconPlayerItem>> getPlayerItems(int appId, long steamId) {
        return getPlayerItems(appId, steamId, VERSION_1);
    }

    /**
     * <p>getPlayerItems.</p>
     *
     * @param appId a int
     * @param steamId a long
     * @param apiVersion a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<SteamEconPlayerItem>> getPlayerItems(int appId, long steamId, int apiVersion) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerItems(appId, steamId, apiVersion));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            int status = result.getAsJsonPrimitive("status").getAsInt();
            JsonArray items = result.getAsJsonArray("items");
            Type type = new TypeToken<List<SteamEconPlayerItem>>() {
            }.getType();
            return fromJson(items, type);
        });
    }

    /**
     * <p>getSchema.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamEconSchema> getSchema(int appId) {
        return getSchema(appId, VERSION_1);
    }

    /**
     * <p>getSchema.</p>
     *
     * @param appId a int
     * @param version a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamEconSchema> getSchema(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSchema(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            int status = result.getAsJsonPrimitive("status").getAsInt();
            return fromJson(result, SteamEconSchema.class);
        });
    }

    /**
     * <p>getSchemaUrl.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<String> getSchemaUrl(int appId) {
        return getSchemaUrl(appId, VERSION_1);
    }

    /**
     * <p>getSchemaUrl.</p>
     *
     * @param appId a int
     * @param version a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<String> getSchemaUrl(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSchemaUrl(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            int status = result.getAsJsonPrimitive("status").getAsInt();
            return result.getAsJsonPrimitive("items_game_url").getAsString();
        });
    }

    /**
     * <p>getStoreMetadata.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamEconItemsStoreMeta> getStoreMetadata(int appId) {
        return getStoreMetadata(appId, VERSION_1);
    }

    /**
     * <p>getStoreMetadata.</p>
     *
     * @param appId a int
     * @param version a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<SteamEconItemsStoreMeta> getStoreMetadata(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetStoreMetadata(appId, version));
        return json.thenApply(root -> fromJson(getSteamResult(root), SteamEconItemsStoreMeta.class));
    }

    /**
     * <p>getStoreStatus.</p>
     *
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Integer> getStoreStatus(int appId) {
        return getStoreStatus(appId, VERSION_1);
    }

    /**
     * <p>getStoreStatus.</p>
     *
     * @param appId a int
     * @param version a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Integer> getStoreStatus(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetStoreStatus(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            return result.getAsJsonPrimitive("store_status").getAsInt();
        });
    }
}
