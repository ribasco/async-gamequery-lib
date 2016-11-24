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

public class Dota2Fantasy extends Dota2WebApiInterface {

    public Dota2Fantasy(SteamWebApiClient client) {
        super(client);
    }

    /*public CompletableFuture<Object> getFantasyPlayerStats() {
        //TODO: need to obtain a valid fantasy league id to test this properly
        return null;
    }*/

    public CompletableFuture<Dota2FantasyPlayerInfo> getPlayerOfficialInfo(int accountId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerOfficialInfo(VERSION_1, accountId));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2FantasyPlayerInfo.class));
    }

    public CompletableFuture<List<Dota2FantasyProPlayerInfo>> getProPlayerList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetProPlayerList(VERSION_1));
        return json.thenApply(r -> asCollectionOf(Dota2FantasyProPlayerInfo.class, "player_infos", r));
    }

}
