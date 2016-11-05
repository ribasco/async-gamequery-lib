package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2EconRequest;

public class GetEventStatsForAccount extends Dota2EconRequest {
    public GetEventStatsForAccount(int apiVersion, int accountId, int leagueId, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETACCNTEVENTSTATS, apiVersion, language);
        urlParam("accountid", accountId);
        urlParam("eventid", leagueId);
    }
}
