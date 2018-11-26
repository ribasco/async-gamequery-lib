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
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.econitems.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsStoreMeta;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconPlayerItem;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchema;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Methods relating to in-game items for supported games.
 */
public class SteamEconItems extends SteamWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamEconItems(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamEconPlayerItem>> getPlayerItems(int appId, long steamId) {
        return getPlayerItems(appId, steamId, VERSION_1);
    }

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

    public CompletableFuture<SteamEconSchema> getSchema(int appId) {
        return getSchema(appId, VERSION_1);
    }

    public CompletableFuture<SteamEconSchema> getSchema(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSchema(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            int status = result.getAsJsonPrimitive("status").getAsInt();
            return fromJson(result, SteamEconSchema.class);
        });
    }

    public CompletableFuture<String> getSchemaUrl(int appId) {
        return getSchemaUrl(appId, VERSION_1);
    }

    public CompletableFuture<String> getSchemaUrl(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSchemaUrl(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            int status = result.getAsJsonPrimitive("status").getAsInt();
            return result.getAsJsonPrimitive("items_game_url").getAsString();
        });
    }

    public CompletableFuture<SteamEconItemsStoreMeta> getStoreMetadata(int appId) {
        return getStoreMetadata(appId, VERSION_1);
    }

    public CompletableFuture<SteamEconItemsStoreMeta> getStoreMetadata(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetStoreMetadata(appId, version));
        return json.thenApply(root -> fromJson(getSteamResult(root), SteamEconItemsStoreMeta.class));
    }

    public CompletableFuture<Integer> getStoreStatus(int appId) {
        return getStoreStatus(appId, VERSION_1);
    }

    public CompletableFuture<Integer> getStoreStatus(int appId, int version) {
        CompletableFuture<JsonObject> json = sendRequest(new GetStoreStatus(appId, version));
        return json.thenApply(root -> {
            JsonObject result = root.getAsJsonObject("result");
            return result.getAsJsonPrimitive("store_status").getAsInt();
        });
    }
}
