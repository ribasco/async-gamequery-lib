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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces;

import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2WebApiInterface;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.stats.GetRealtimeStats;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2RealtimeServerStats;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Dota2Stats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2Stats extends Dota2WebApiInterface {

    /**
     * <p>Constructor for Dota2Stats.</p>
     *
     * @param client
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public Dota2Stats(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getRealtimeStats.</p>
     *
     * @param serverSteamId
     *         a long
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Dota2RealtimeServerStats> getRealtimeStats(long serverSteamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRealtimeStats(VERSION_1, serverSteamId));
        return json.thenApply(r -> fromJson(r, Dota2RealtimeServerStats.class));
    }
}
