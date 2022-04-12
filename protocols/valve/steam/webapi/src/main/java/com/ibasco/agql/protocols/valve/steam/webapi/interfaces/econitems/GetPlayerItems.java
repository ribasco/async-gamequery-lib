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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.econitems;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamEconItemsRequest;

/**
 * <p>GetPlayerItems class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetPlayerItems extends SteamEconItemsRequest {
    /**
     * <p>Constructor for GetPlayerItems.</p>
     *
     * @param appId a int
     * @param steamId a long
     * @param apiVersion a int
     */
    public GetPlayerItems(int appId, long steamId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETPLAYERITEMS, apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STEAMID, steamId);
    }
}
