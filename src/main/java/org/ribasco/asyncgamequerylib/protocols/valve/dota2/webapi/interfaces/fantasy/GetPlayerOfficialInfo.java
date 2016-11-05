package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.fantasy;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2FantasyRequest;

public class GetPlayerOfficialInfo extends Dota2FantasyRequest {
    public GetPlayerOfficialInfo(int apiVersion, int accountId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETPLAYERINFO, apiVersion);
        urlParam("accountid", accountId);
    }
}
