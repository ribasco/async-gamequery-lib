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
import com.google.gson.JsonObject;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.core.client.AbstractRestClient;
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
            if (appObject.getAsJsonPrimitive("success").getAsBoolean()) {
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
