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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamNewsRequest;

public class GetNewsForApp extends SteamNewsRequest {

    public GetNewsForApp(int apiVersion, int appId) {
        super("GetNewsForApp", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
    }

    public GetNewsForApp maxLength(int maxLength) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_MAXLENGTH, maxLength);
        return this;
    }

    public GetNewsForApp endDate(int endDate) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_ENDDATE, endDate);
        return this;
    }

    public GetNewsForApp count(int count) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_COUNT, count);
        return this;
    }

    public GetNewsForApp feeds(String feeds) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_FEEDS, feeds);
        return this;
    }
}
