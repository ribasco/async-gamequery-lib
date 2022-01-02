/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.teams.GetTeamInfo;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2TeamDetails;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Dota2Teams extends Dota2WebApiInterface {

    public Dota2Teams(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<Dota2TeamDetails>> getTeamInfo(int teamId) {
        return getTeamInfo(teamId, -1);
    }

    public CompletableFuture<List<Dota2TeamDetails>> getTeamInfo(int teamId, int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTeamInfo(VERSION_1, teamId, leagueId));
        return json.thenApply(r -> asCollectionOf(Dota2TeamDetails.class, "teams", r));
    }
}
