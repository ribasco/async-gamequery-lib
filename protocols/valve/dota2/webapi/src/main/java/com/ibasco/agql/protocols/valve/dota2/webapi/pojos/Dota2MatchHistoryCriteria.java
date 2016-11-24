/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
