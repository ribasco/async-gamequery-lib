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

package com.ibasco.agql.protocols.valve.dota2.webapi;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;
import org.asynchttpclient.Response;

/**
 * <p>Dota2WebApiClient class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2WebApiClient extends SteamWebApiClient {

    /**
     * <p>Constructor for Dota2WebApiClient.</p>
     *
     * @param apiToken
     *         a {@link java.lang.String} object
     */
    public Dota2WebApiClient(String apiToken) {
        super(apiToken);
    }

    /** {@inheritDoc} */
    @Override
    protected SteamWebApiResponse createWebApiResponse(Response response) {
        return new Dota2WebApiResponse(response);
    }
}
