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

abstract public class GameServersRequest extends SteamWebApiRequest {

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiMethod
     *         a {@link String} object
     * @param apiVersion
     *         a int
     */
    public GameServersRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_GAMESERVERS_SERVICE, apiMethod, apiVersion);
    }
}
