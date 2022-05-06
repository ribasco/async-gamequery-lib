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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.players.GetPlayerInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerDetailedInfo;
import org.jetbrains.annotations.ApiStatus;
import java.util.concurrent.CompletableFuture;

/**
 * <p>A Web API Implementation of the Player interface. Contains methods for player related inquiries.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/players">Clash of Clans API - Players</a>
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocPlayers extends CocWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient} instance
     */
    public CocPlayers(CocWebApiClient client) {
        super(client);
    }

    /**
     * <p>Retrieve a detailed information about a Player</p>
     *
     * @param playerTag
     *         A unique player {@link java.lang.String} identifier followed by a hashtag
     *
     * @return A {@link java.util.concurrent.CompletableFuture} containing a future result of {@link com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerDetailedInfo}
     */
    public CompletableFuture<CocPlayerDetailedInfo> getPlayerInfo(String playerTag) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerInfo(VERSION_1, playerTag));
        return json.thenApply(root -> builder().fromJson(root, CocPlayerDetailedInfo.class));
    }
}
