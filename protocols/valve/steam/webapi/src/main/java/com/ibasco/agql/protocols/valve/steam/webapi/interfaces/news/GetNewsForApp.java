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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamNewsRequest;

/**
 * <p>GetNewsForApp class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetNewsForApp extends SteamNewsRequest {

    /**
     * <p>Constructor for GetNewsForApp.</p>
     *
     * @param apiVersion a int
     * @param appId a int
     */
    public GetNewsForApp(int apiVersion, int appId) {
        super("GetNewsForApp", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
    }

    /**
     * <p>maxLength.</p>
     *
     * @param maxLength a int
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news.GetNewsForApp} object
     */
    public GetNewsForApp maxLength(int maxLength) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_MAXLENGTH, maxLength);
        return this;
    }

    /**
     * <p>endDate.</p>
     *
     * @param endDate a int
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news.GetNewsForApp} object
     */
    public GetNewsForApp endDate(int endDate) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_ENDDATE, endDate);
        return this;
    }

    /**
     * <p>count.</p>
     *
     * @param count a int
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news.GetNewsForApp} object
     */
    public GetNewsForApp count(int count) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_COUNT, count);
        return this;
    }

    /**
     * <p>feeds.</p>
     *
     * @param feeds a {@link java.lang.String} object
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.interfaces.news.GetNewsForApp} object
     */
    public GetNewsForApp feeds(String feeds) {
        urlParam(SteamApiConstants.STEAM_URLPARAM_FEEDS, feeds);
        return this;
    }
}
