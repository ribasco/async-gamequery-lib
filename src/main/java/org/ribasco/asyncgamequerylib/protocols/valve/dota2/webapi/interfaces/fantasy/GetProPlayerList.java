package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.fantasy;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2FantasyRequest;

public class GetProPlayerList extends Dota2FantasyRequest {
    public GetProPlayerList(int apiVersion) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETPROPLAYERLSIT, apiVersion);
    }
}
