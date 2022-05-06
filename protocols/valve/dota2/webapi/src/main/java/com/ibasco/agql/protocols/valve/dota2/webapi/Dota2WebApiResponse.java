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

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;
import org.asynchttpclient.Response;

/**
 * <p>Dota2WebApiResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2WebApiResponse extends SteamWebApiResponse {

    /**
     * <p>Constructor for Dota2WebApiResponse.</p>
     *
     * @param response
     *         a {@link org.asynchttpclient.Response} object
     */
    public Dota2WebApiResponse(Response response) {
        super(response);
    }
}
