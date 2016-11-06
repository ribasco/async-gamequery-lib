package org.ribasco.agql.protocols.valve.dota2.interfaces.fantasy;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.interfaces.Dota2FantasyRequest;

public class GetProPlayerList extends Dota2FantasyRequest {
    public GetProPlayerList(int apiVersion) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETPROPLAYERLSIT, apiVersion);
    }
}
