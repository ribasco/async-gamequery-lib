/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.steam.webapi;

import com.ibasco.agql.core.AbstractWebApiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Steam API Request
 */
abstract public class SteamWebApiRequest extends AbstractWebApiRequest {

    private static final Logger log = LoggerFactory.getLogger(SteamWebApiRequest.class);

    private String steamApiInterface;

    private String steamApiMethod;

    public SteamWebApiRequest(String apiInterface, String apiMethod, int apiVersion) {
        super(apiVersion);

        this.steamApiInterface = resolveProperties(apiInterface);
        this.steamApiMethod = apiMethod;
        baseUrlFormat(SteamApiConstants.STEAM_BASE_URL_FORMAT);
        property(SteamApiConstants.STEAM_PROP_INTERFACE, this.steamApiInterface);
        property(SteamApiConstants.STEAM_PROP_METHOD, this.steamApiMethod);
        property(SteamApiConstants.STEAM_PROP_VERSION, apiVersion);
    }

    public String getSteamApiInterface() {
        return steamApiInterface;
    }

    public void setSteamApiInterface(String steamApiInterface) {
        this.steamApiInterface = steamApiInterface;
    }

    public String getSteamApiMethod() {
        return steamApiMethod;
    }

    public void setSteamApiMethod(String steamApiMethod) {
        this.steamApiMethod = steamApiMethod;
    }
}
