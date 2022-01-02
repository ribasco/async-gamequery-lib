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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import com.ibasco.agql.core.AbstractCriteriaBuilder;

public class Dota2MatchHistoryCriteria extends AbstractCriteriaBuilder<Dota2MatchHistoryCriteria> {

    public Dota2MatchHistoryCriteria heroId(int heroId) {
        return put("hero_id", heroId);
    }

    public Dota2MatchHistoryCriteria gameMode(int gameMode) {
        return put("game_mode", gameMode);
    }

    public Dota2MatchHistoryCriteria skill(int skill) {
        return put("skill", skill);
    }

    public Dota2MatchHistoryCriteria minPlayers(int minPlayers) {
        return put("min_players", minPlayers);
    }

    public Dota2MatchHistoryCriteria accountId(long accountId) {
        return put("account_id", accountId);
    }

    public Dota2MatchHistoryCriteria leagueId(long leagueId) {
        return put("league_id", leagueId);
    }

    public Dota2MatchHistoryCriteria startAtMatchId(long matchId) {
        return put("start_at_match_id", matchId);
    }

    public Dota2MatchHistoryCriteria matchesRequested(int totalMatchRequested) {
        return put("matches_requested", totalMatchRequested);
    }

    public Dota2MatchHistoryCriteria tournamentGamesOnly(boolean value) {
        return put("tournament_games_only", (value) ? 1 : 0);
    }
}
