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
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.fantasy.GetPlayerOfficialInfo;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.fantasy.GetProPlayerList;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2FantasyPlayerInfo;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2FantasyProPlayerInfo;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Dota2Fantasy class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2Fantasy extends Dota2WebApiInterface {

    /**
     * <p>Constructor for Dota2Fantasy.</p>
     *
     * @param client a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public Dota2Fantasy(SteamWebApiClient client) {
        super(client);
    }

    /*public CompletableFuture<Object> getFantasyPlayerStats() {
        //TODO: need to obtain a valid fantasy league id to test this properly
        return null;
    }*/

    /**
     * <p>getPlayerOfficialInfo.</p>
     *
     * @param accountId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<Dota2FantasyPlayerInfo> getPlayerOfficialInfo(int accountId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerOfficialInfo(VERSION_1, accountId));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2FantasyPlayerInfo.class));
    }

    /**
     * <p>getProPlayerList.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<List<Dota2FantasyProPlayerInfo>> getProPlayerList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetProPlayerList(VERSION_1));
        return json.thenApply(r -> asCollectionOf(Dota2FantasyProPlayerInfo.class, "player_infos", r));
    }

}
