/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.cheatreport.ReportCheatData;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.CheatData;

import java.util.concurrent.CompletableFuture;

public class SteamCheatReportingService extends SteamWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamCheatReportingService(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>Reports cheat data to Valve</p>
     *
     * @param cheatData
     *         {@link CheatData} containing all the required parameters
     *
     * @return A {@link CompletableFuture} returning a {@link Void} result
     */
    public CompletableFuture<Boolean> reportCheatData(CheatData cheatData) {
        CompletableFuture<JsonObject> json = sendRequest(new ReportCheatData(cheatData, VERSION_1));
        return json.thenApply(r -> {
            JsonObject result = r.getAsJsonObject("result");
            return result != null;
        }).exceptionally(err -> false);
    }
}
