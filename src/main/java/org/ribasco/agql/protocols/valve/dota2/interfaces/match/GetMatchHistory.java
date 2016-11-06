package org.ribasco.agql.protocols.valve.dota2.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.interfaces.Dota2MatchRequest;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2MatchHistoryCriteria;

public class GetMatchHistory extends Dota2MatchRequest {
    public GetMatchHistory(int apiVersion, Dota2MatchHistoryCriteria criteria) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHHISTORY, apiVersion, null);
        if (criteria != null)
            urlParam(criteria);
    }
}
