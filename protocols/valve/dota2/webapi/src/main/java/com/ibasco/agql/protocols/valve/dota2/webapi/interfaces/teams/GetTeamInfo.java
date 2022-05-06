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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.teams;

import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import com.ibasco.agql.protocols.valve.dota2.webapi.requests.Dota2TeamsRequest;

/**
 * <p>GetTeamInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetTeamInfo extends Dota2TeamsRequest {

    /**
     * <p>Constructor for GetTeamInfo.</p>
     *
     * @param apiVersion
     *         a int
     * @param teamId
     *         a int
     * @param leagueId
     *         a int
     */
    public GetTeamInfo(int apiVersion, int teamId, int leagueId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTEAMINFO, apiVersion, null);
        urlParam("team_id", teamId);
        urlParam("league_id", leagueId);
    }
}
