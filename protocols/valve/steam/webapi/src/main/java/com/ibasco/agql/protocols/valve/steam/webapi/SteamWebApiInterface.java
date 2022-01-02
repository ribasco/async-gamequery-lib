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

package com.ibasco.agql.protocols.valve.steam.webapi;

import com.google.gson.JsonObject;
import com.ibasco.agql.core.AbstractWebApiInterface;

/**
 * <p>Represents a Steam Web API Interface</p>
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Steam_Web_API#Interfaces_and_method">Steam Interfaces and
 * Methods</a>
 */
abstract public class SteamWebApiInterface
        extends AbstractWebApiInterface<SteamWebApiClient, SteamWebApiRequest, SteamWebApiResponse> {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public SteamWebApiInterface(SteamWebApiClient client) {
        super(client);
    }

    protected JsonObject getSteamResult(JsonObject root) {
        if (root.has("result")) {
            return root.getAsJsonObject("result");
        }
        return null;
    }
}
