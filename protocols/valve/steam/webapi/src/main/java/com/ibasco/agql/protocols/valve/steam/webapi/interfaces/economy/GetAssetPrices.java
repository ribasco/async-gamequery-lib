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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.economy;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamEconomyRequest;

/**
 * <p>GetAssetPrices class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetAssetPrices extends SteamEconomyRequest {

    /**
     * <p>Constructor for GetAssetPrices.</p>
     *
     * @param apiVersion
     *         a int
     * @param appId
     *         a int
     * @param currency
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     */
    public GetAssetPrices(int apiVersion, int appId, String currency, String language) {
        super("GetAssetPrices", apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_APPID, appId);
        urlParam(SteamApiConstants.STEAM_URLPARAM_CURRENCY, currency);
        urlParam(SteamApiConstants.STEAM_URLPARAM_LANGUAGE, language);
    }
}
