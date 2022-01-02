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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamPlayerServiceRequest;

/**
 * Return a list of games owned by the player
 */
public class GetOwnedGames extends SteamPlayerServiceRequest {
    public GetOwnedGames(int apiVersion, long steamId, boolean includeAppInfo, boolean includePlayedFreeGames) {
        super("GetOwnedGames", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STEAMID, steamId);
        urlParam("include_appinfo", (includeAppInfo) ? 1 : 0);
        urlParam("include_played_free_games", (includePlayedFreeGames) ? 1 : 0);
    }
}
