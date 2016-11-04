package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.match;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2MatchRequest;

public class GetTopLiveGame extends Dota2MatchRequest {
    public GetTopLiveGame(int apiVersion, int partner) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETTOPLIVEGAME, apiVersion, null);
        urlParam("partner", partner);
    }
}
