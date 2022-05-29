package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.communityservice.GetApps;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamCommunityApp;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SteamCommunityService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamCommunityService(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamCommunityApp>> getApps(Integer... appIds) {
        return getApps(Arrays.asList(appIds));
    }

    /**
     * Returns a list of apps retrieved from the community service interface.
     *
     * @param appIds
     *         A collection of steam app ids
     *
     * @return A {@link CompletableFuture} object
     */
    public CompletableFuture<List<SteamCommunityApp>> getApps(Collection<Integer> appIds) {
        CompletableFuture<JsonObject> result = sendRequest(new GetApps(VERSION_1, appIds));
        return result.thenApply(r -> {
            JsonObject response = r.getAsJsonObject("response");
            Type listType = new TypeToken<List<SteamCommunityApp>>() {}.getType();
            return builder().fromJson(response.getAsJsonArray("apps"), listType);
        });
    }
}
