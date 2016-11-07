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

package org.ribasco.agql.protocols.valve.csgo.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.agql.core.client.AbstractRestClient;
import org.ribasco.agql.protocols.valve.csgo.webapi.CsgoWebApiInterface;
import org.ribasco.agql.protocols.valve.csgo.webapi.adapters.CsgoDatacenterStatusDeserializer;
import org.ribasco.agql.protocols.valve.csgo.webapi.interfaces.servers.GetGameServersStatus;
import org.ribasco.agql.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
import org.ribasco.agql.protocols.valve.csgo.webapi.pojos.CsgoGameServerStatus;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CsgoServers extends CsgoWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public CsgoServers(SteamWebApiClient client) {
        super(client);
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(new TypeToken<List<CsgoDatacenterStatus>>() {
        }.getType(), new CsgoDatacenterStatusDeserializer());
    }

    public CompletableFuture<CsgoGameServerStatus> getGameServerStatus() {
        CompletableFuture<JsonObject> json = sendRequest(new GetGameServersStatus(VERSION_1));
        return json.thenApply(r -> fromJson(getResult(r), CsgoGameServerStatus.class));
    }
}
