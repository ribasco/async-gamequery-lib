package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistoryCriteria;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetMatchHistory extends Dota2MatchRequest {
    public GetMatchHistory(int apiVersion, Dota2MatchHistoryCriteria criteria) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHHISTORY, apiVersion, null);
        if (criteria != null)
            urlParam(criteria);
    }
}
