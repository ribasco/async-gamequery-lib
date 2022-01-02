/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ibasco.agql.core.AbstractRestClient;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;
import com.ibasco.agql.protocols.valve.steam.webapi.adapters.StoreAppPcRequirementsDeserializer;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;

import java.util.concurrent.CompletableFuture;

public class SteamStorefront extends AbstractWebApiInterface<SteamWebApiClient, SteamStoreApiRequest, SteamWebApiResponse> {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link AbstractRestClient} instance
     */
    public SteamStorefront(SteamWebApiClient client) {
        super(client);
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(StoreAppPcRequirements.class, new StoreAppPcRequirementsDeserializer());
    }

    public CompletableFuture<StoreFeaturedApps> getFeaturedApps() {
        return getFeaturedApps(null, null);
    }

    public CompletableFuture<StoreFeaturedApps> getFeaturedApps(String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFeaturedGames(VERSION_1, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreFeaturedApps.class));
    }

    public CompletableFuture<StoreFeaturedCategories> getFeaturedCategories() {
        return getFeaturedCategories(null, null);
    }

    public CompletableFuture<StoreFeaturedCategories> getFeaturedCategories(String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFeaturedCategories(VERSION_1, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreFeaturedCategories.class));
    }

    public CompletableFuture<StoreAppDetails> getAppDetails(int appId) {
        return getAppDetails(appId, null, null);
    }

    public CompletableFuture<StoreAppDetails> getAppDetails(int appId, String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppDetails(VERSION_1, appId, countryCode, language));
        return json.thenApply(root -> {
            JsonObject appObject = root.getAsJsonObject(String.valueOf(appId));
            JsonPrimitive success = appObject.getAsJsonPrimitive("success");
            if (success != null && success.getAsBoolean()) {
                JsonObject appData = appObject.getAsJsonObject("data");
                return fromJson(appData, StoreAppDetails.class);
            }
            return null;
        });
    }

    public CompletableFuture<StorePackageDetails> getPackageDetails(int packageId) {
        return getPackageDetails(packageId, null, null);
    }

    public CompletableFuture<StorePackageDetails> getPackageDetails(int packageId, String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPackageDetails(VERSION_1, packageId, countryCode, language));
        return json.thenApply(root -> {
            JsonObject packageObj = root.getAsJsonObject(String.valueOf(packageId));
            JsonObject packageData = packageObj.getAsJsonObject("data");
            return fromJson(packageData, StorePackageDetails.class);
        });
    }

    public CompletableFuture<StoreSaleDetails> getSaleDetails(int saleId) {
        return getSaleDetails(saleId, null, null);
    }

    public CompletableFuture<StoreSaleDetails> getSaleDetails(int saleId, String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSaleDetails(VERSION_1, saleId, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreSaleDetails.class));
    }
}
