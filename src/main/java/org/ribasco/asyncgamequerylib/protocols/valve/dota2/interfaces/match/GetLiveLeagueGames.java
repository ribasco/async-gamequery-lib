package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.match;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2MatchRequest;

public class GetLiveLeagueGames extends Dota2MatchRequest {
    public GetLiveLeagueGames(int apiVersion, int leagueId, int matchId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETLIVELEAGUEGAMES, apiVersion, null);
        urlParam("league_id", leagueId);
        urlParam("match_id", matchId);
    }
}
