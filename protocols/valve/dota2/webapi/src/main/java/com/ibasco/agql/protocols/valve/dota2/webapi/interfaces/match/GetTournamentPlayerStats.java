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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import com.ibasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

/**
 * <p>GetTournamentPlayerStats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetTournamentPlayerStats extends Dota2MatchRequest {
    /**
     * <p>Constructor for GetTournamentPlayerStats.</p>
     *
     * @param apiVersion a int
     * @param accountId a long
     * @param leagueId a int
     * @param heroId a int
     * @param timeFrame a {@link java.lang.String} object
     * @param matchId a long
     * @param phaseId a int
     */
    public GetTournamentPlayerStats(int apiVersion, long accountId, int leagueId, int heroId, String timeFrame, long matchId, int phaseId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTOURNPLAYERSTATS, apiVersion, null);
        urlParam("account_id", accountId);
        urlParam("league_id", leagueId);
        urlParam("hero_id", heroId);
        urlParam("time_frame", timeFrame);
        urlParam("match_id", matchId);
        urlParam("phase_id", phaseId);
    }
}
