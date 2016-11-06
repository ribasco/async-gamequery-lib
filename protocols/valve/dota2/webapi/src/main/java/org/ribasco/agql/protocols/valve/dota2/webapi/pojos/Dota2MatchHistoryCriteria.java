package org.ribasco.agql.protocols.valve.dota2.webapi.pojos;

import org.ribasco.agql.core.CriteriaBuilder;

public class Dota2MatchHistoryCriteria extends CriteriaBuilder<Dota2MatchHistoryCriteria> {

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
