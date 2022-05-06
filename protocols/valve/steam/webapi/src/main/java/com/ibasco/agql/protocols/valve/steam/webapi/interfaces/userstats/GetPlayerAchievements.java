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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.userstats;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamUserStatsRequest;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class GetPlayerAchievements extends SteamUserStatsRequest {

    /**
     * <p>Constructor for GetPlayerAchievements.</p>
     *
     * @param apiVersion
     *         a int
     * @param steamId
     *         a long
     * @param appId
     *         a int
     */
    public GetPlayerAchievements(int apiVersion, long steamId, int appId) {
        this(apiVersion, steamId, appId, null);
    }

    /**
     * <p>Constructor for GetPlayerAchievements.</p>
     *
     * @param apiVersion
     *         a int
     * @param steamId
     *         a long
     * @param appId
     *         a int
     * @param language
     *         a {@link java.lang.String} object
     */
    public GetPlayerAchievements(int apiVersion, long steamId, int appId, String language) {
        super("GetPlayerAchievements", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STEAMID, steamId);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
        urlParam("l", defaultIfNull(language, "en"));
    }
}
