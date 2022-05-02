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
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameservers.GetServerList;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.GameServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameServersService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public GameServersService(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<GameServer>> getServerList(String filter) {
        return getServerList(filter, -1);
    }

    public CompletableFuture<List<GameServer>> getServerList(String filter, int limit) {
        CompletableFuture<JsonObject> json = sendRequest(new GetServerList(VERSION_1, filter, limit));
        return json.thenApply((JsonObject element) -> {
            JsonObject response = element.get("response").getAsJsonObject();
            if (response.has("servers")) {
                JsonArray array = response.getAsJsonArray("servers");
                Type listType = new TypeToken<List<GameServer>>() {}.getType();
                return builder().fromJson(array, listType);
            }
            return new ArrayList<>();
        });
    }
}
