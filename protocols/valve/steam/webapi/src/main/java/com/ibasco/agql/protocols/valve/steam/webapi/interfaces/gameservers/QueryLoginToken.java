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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameservers;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.GameServersRequest;

public class QueryLoginToken extends GameServersRequest {

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     */
    public QueryLoginToken(int apiVersion, String loginToken) {
        super("QueryLoginToken", apiVersion);
        urlParam("login_token", loginToken);
    }
}
