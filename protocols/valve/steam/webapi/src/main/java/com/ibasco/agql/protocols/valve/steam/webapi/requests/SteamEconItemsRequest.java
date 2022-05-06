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

package com.ibasco.agql.protocols.valve.steam.webapi.requests;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

//TODO: Add language support

/**
 * <p>Abstract SteamEconItemsRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SteamEconItemsRequest extends SteamWebApiRequest {

    /**
     * <p>Constructor for SteamEconItemsRequest.</p>
     *
     * @param appId
     *         a int
     * @param apiMethod
     *         a {@link java.lang.String} object
     * @param apiVersion
     *         a int
     */
    public SteamEconItemsRequest(int appId, String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_ECON_ITEMS, apiMethod, apiVersion);
        property(SteamApiConstants.STEAM_PROP_APPID, appId);
    }
}
