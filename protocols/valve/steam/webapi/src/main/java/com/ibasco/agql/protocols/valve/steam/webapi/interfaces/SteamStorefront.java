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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;
import com.ibasco.agql.protocols.valve.steam.webapi.adapters.StoreAppPcRequirementsDeserializer;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.GetAppDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.GetFeaturedCategories;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.GetFeaturedGames;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.GetPackageDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront.GetSaleDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedApps;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreFeaturedCategories;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StorePackageDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreSaleDetails;
import java.util.concurrent.CompletableFuture;

/**
 * <p>SteamStorefront class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamStorefront extends AbstractWebApiInterface<SteamWebApiClient, SteamStoreApiRequest, SteamWebApiResponse> {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.core.AbstractRestClient} instance
     */
    public SteamStorefront(SteamWebApiClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(StoreAppPcRequirements.class, new StoreAppPcRequirementsDeserializer());
    }

    /**
     * <p>getFeaturedApps.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreFeaturedApps> getFeaturedApps() {
        return getFeaturedApps(null, null);
    }

    /**
     * <p>getFeaturedApps.</p>
     *
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreFeaturedApps> getFeaturedApps(String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFeaturedGames(VERSION_1, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreFeaturedApps.class));
    }

    /**
     * <p>getFeaturedCategories.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreFeaturedCategories> getFeaturedCategories() {
        return getFeaturedCategories(null, null);
    }

    /**
     * <p>getFeaturedCategories.</p>
     *
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreFeaturedCategories> getFeaturedCategories(String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetFeaturedCategories(VERSION_1, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreFeaturedCategories.class));
    }

    /**
     * <p>getAppDetails.</p>
     *
     * @param appId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreAppDetails> getAppDetails(int appId) {
        return getAppDetails(appId, null, null);
    }

    /**
     * <p>getAppDetails.</p>
     *
     * @param appId
     *         a int
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>getPackageDetails.</p>
     *
     * @param packageId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StorePackageDetails> getPackageDetails(int packageId) {
        return getPackageDetails(packageId, null, null);
    }

    /**
     * <p>getPackageDetails.</p>
     *
     * @param packageId
     *         a int
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StorePackageDetails> getPackageDetails(int packageId, String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPackageDetails(VERSION_1, packageId, countryCode, language));
        return json.thenApply(root -> {
            JsonObject packageObj = root.getAsJsonObject(String.valueOf(packageId));
            JsonObject packageData = packageObj.getAsJsonObject("data");
            return fromJson(packageData, StorePackageDetails.class);
        });
    }

    /**
     * <p>getSaleDetails.</p>
     *
     * @param saleId
     *         a int
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreSaleDetails> getSaleDetails(int saleId) {
        return getSaleDetails(saleId, null, null);
    }

    /**
     * <p>getSaleDetails.</p>
     *
     * @param saleId
     *         a int
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<StoreSaleDetails> getSaleDetails(int saleId, String countryCode, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetSaleDetails(VERSION_1, saleId, countryCode, language));
        return json.thenApply(root -> fromJson(root, StoreSaleDetails.class));
    }
}
