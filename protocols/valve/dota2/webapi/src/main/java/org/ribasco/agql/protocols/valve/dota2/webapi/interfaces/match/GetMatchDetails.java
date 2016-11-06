package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetMatchDetails extends Dota2MatchRequest {
    public GetMatchDetails(int apiVersion, long matchId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHDETAIL, apiVersion, null);
        urlParam("match_id", matchId);
    }
}
