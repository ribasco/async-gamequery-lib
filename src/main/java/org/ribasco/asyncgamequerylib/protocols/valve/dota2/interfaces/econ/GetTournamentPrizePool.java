package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2EconRequest;

public class GetTournamentPrizePool extends Dota2EconRequest {
    public GetTournamentPrizePool(int apiVersion, int leagueId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTOURNPRIZEPOOL, apiVersion, null);
        urlParam("leagueid", leagueId);
    }
}
