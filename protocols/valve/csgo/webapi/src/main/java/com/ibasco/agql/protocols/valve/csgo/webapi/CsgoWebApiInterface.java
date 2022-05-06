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

package com.ibasco.agql.protocols.valve.csgo.webapi;

import com.google.gson.JsonObject;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.core.exceptions.JsonElementNotFoundException;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

/**
 * <p>Abstract CsgoWebApiInterface class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class CsgoWebApiInterface extends AbstractWebApiInterface<SteamWebApiClient, CsgoWebApiRequest, CsgoWebApiResponse> {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link com.ibasco.agql.core.AbstractRestClient} instance
     */
    public CsgoWebApiInterface(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getResult.</p>
     *
     * @param root
     *         a {@link com.google.gson.JsonObject} object
     *
     * @return a {@link com.google.gson.JsonObject} object
     */
    protected JsonObject getResult(JsonObject root) {
        if (root.has("result")) {
            return root.getAsJsonObject("result");
        }
        throw new JsonElementNotFoundException(root, "Did not find 'result' section from the response");
    }
}
