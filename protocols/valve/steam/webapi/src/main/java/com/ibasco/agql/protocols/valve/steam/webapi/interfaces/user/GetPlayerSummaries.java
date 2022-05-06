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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.user;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamUserRequest;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

/**
 * <p>GetPlayerSummaries class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetPlayerSummaries extends SteamUserRequest {

    /**
     * <p>Constructor for GetPlayerSummaries.</p>
     *
     * @param apiVersion
     *         a int
     * @param steamIds
     *         a {@link java.util.List} object
     */
    public GetPlayerSummaries(int apiVersion, List<Long> steamIds) {
        this(apiVersion, steamIds.toArray(new Long[0]));
    }

    /**
     * <p>Constructor for GetPlayerSummaries.</p>
     *
     * @param apiVersion
     *         a int
     * @param steamIds
     *         a {@link java.lang.Long} object
     */
    public GetPlayerSummaries(int apiVersion, Long... steamIds) {
        super("GetPlayerSummaries", apiVersion);
        urlParam("steamids", StringUtils.join(steamIds, ","));
    }
}
