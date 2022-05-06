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

import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.cheatreport.ReportCheatData;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData;
import java.util.concurrent.CompletableFuture;

/**
 * <p>SteamCheatReportingService class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamCheatReportingService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} instance
     */
    public SteamCheatReportingService(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>Reports cheat data to Valve</p>
     *
     * @param cheatData
     *         {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData} containing all the required parameters
     *
     * @return A {@link java.util.concurrent.CompletableFuture} returning a {@link java.lang.Void} result
     */
    public CompletableFuture<Boolean> reportCheatData(CheatData cheatData) {
        CompletableFuture<JsonObject> json = sendRequest(new ReportCheatData(cheatData, VERSION_1));
        return json.thenApply(r -> {
            JsonObject result = r.getAsJsonObject("result");
            return result != null;
        }).exceptionally(err -> false);
    }
}
