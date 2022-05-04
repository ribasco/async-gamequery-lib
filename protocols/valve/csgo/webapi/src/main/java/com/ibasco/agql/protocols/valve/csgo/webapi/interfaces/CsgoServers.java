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

package com.ibasco.agql.protocols.valve.csgo.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.valve.csgo.webapi.CsgoWebApiInterface;
import com.ibasco.agql.protocols.valve.csgo.webapi.adapters.CsgoDatacenterStatusDeserializer;
import com.ibasco.agql.protocols.valve.csgo.webapi.interfaces.servers.GetGameMapsPlaytime;
import com.ibasco.agql.protocols.valve.csgo.webapi.interfaces.servers.GetGameServersStatus;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameMapPlaytimeInfo;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameServerStatus;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>CsgoServers class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoServers extends CsgoWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.core.AbstractRestClient} instance
     */
    public CsgoServers(SteamWebApiClient client) {
        super(client);
    }

    /** {@inheritDoc} */
    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(new TypeToken<List<CsgoDatacenterStatus>>() {
        }.getType(), new CsgoDatacenterStatusDeserializer());
    }

    /**
     * <p>getGameServerStatus.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<CsgoGameServerStatus> getGameServerStatus() {
        CompletableFuture<JsonObject> json = sendRequest(new GetGameServersStatus(VERSION_1));
        return json.thenApply(r -> fromJson(getResult(r), CsgoGameServerStatus.class));
    }

    /**
     * Get game maps playtime information
     *
     * @param interval
     *         What recent interval is requested, possible values: day, week, month
     * @param gameMode
     *         What game mode is requested, possible values: competitive, casual
     * @param mapGroup
     *         What maps are requested, possible values: operation
     *
     * @return A {@link CompletableFuture} object.
     */
    public CompletableFuture<CsgoGameMapPlaytimeInfo> getMapPlaytimeInfo(String interval, String gameMode, String mapGroup) {
        CompletableFuture<JsonObject> json = sendRequest(new GetGameMapsPlaytime(VERSION_1, interval, gameMode, mapGroup));
        return json.thenApply(res -> res.get("result").getAsJsonObject()).thenApply(jsonObject -> builder().fromJson(jsonObject, CsgoGameMapPlaytimeInfo.class));
    }
}
