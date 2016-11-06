package org.ribasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.core.AbstractWebApiInterface;
import org.ribasco.agql.core.client.AbstractRestClient;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;
import org.ribasco.agql.protocols.valve.steam.webapi.interfaces.storefront.*;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.*;

import java.util.concurrent.CompletableFuture;

public class SteamStorefront extends AbstractWebApiInterface<SteamWebApiClient, SteamStoreApiRequest, SteamWebApiResponse> {

    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public SteamStorefront(SteamWebApiClient client) {
        super(client);
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
            JsonObject appData = appObject.getAsJsonObject("data");
            return fromJson(appData, StoreAppDetails.class);
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
