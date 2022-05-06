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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamwebapiutil.GetServerInfo;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamwebapiutil.GetSupportedAPIList;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamwebapiutil.pojos.ApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamwebapiutil.pojos.ServerInfo;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SteamWebAPIUtil extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamWebAPIUtil(SteamWebApiClient client) {
        super(client);
    }

    /**
     * Get a list of supported API interfaces.
     *
     * @return A {@link CompletableFuture} with return type of {@link ApiInterface}
     */
    public CompletableFuture<List<ApiInterface>> getSupportedApiList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetSupportedAPIList(VERSION_1));
        return json.thenApply(res -> res.get("apilist").getAsJsonObject().get("interfaces"))
                   .thenApply(response -> {
                       Type type = new TypeToken<List<ApiInterface>>() {}.getType();
                       return builder().fromJson(response, type);
                   });
    }

    /**
     * Retrieve information about the API server
     *
     * @return A {@link CompletableFuture} with return type of {@link ServerInfo}
     */
    public CompletableFuture<ServerInfo> getServerInfo() {
        CompletableFuture<JsonObject> json = sendRequest(new GetServerInfo(VERSION_1));
        return json.thenApply(JsonElement::getAsJsonObject).thenApply(response -> builder().fromJson(response, ServerInfo.class));
    }
}
