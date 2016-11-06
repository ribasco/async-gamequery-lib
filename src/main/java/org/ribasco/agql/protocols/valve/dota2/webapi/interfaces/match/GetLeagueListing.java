package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetLeagueListing extends Dota2MatchRequest {
    public GetLeagueListing(int apiVersion) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETLEAGUELIST, apiVersion, null);
    }
}
