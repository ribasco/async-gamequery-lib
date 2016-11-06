package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.econ;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2EconRequest;

public class GetTournamentPrizePool extends Dota2EconRequest {
    public GetTournamentPrizePool(int apiVersion, int leagueId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTOURNPRIZEPOOL, apiVersion, null);
        urlParam("leagueid", leagueId);
    }
}
