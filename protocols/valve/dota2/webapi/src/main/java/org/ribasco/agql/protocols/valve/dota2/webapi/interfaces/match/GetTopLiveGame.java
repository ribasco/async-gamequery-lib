package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetTopLiveGame extends Dota2MatchRequest {
    public GetTopLiveGame(int apiVersion, int partner) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTOPLIVEGAME, apiVersion, null);
        urlParam("partner", partner);
    }
}
