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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.userstats;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamUserStatsRequest;

public class GetGlobalStatsForGame extends SteamUserStatsRequest {
    public GetGlobalStatsForGame(int apiVersion, int appId, int count, String name) {
        this(apiVersion, appId, count, name, -1, -1);
    }

    public GetGlobalStatsForGame(int apiVersion, int appId, int count, String name, int startDate, int endDate) {
        super("GetGlobalStatsForGame", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
        urlParam(SteamApiConstants.STEAM_URLPARAM_COUNT, count);
        urlParam(SteamApiConstants.STEAM_URLPARAM_NAME, name);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STARTDATE, startDate);
        urlParam(SteamApiConstants.STEAM_URLPARAM_ENDDATE, endDate);
    }
}
