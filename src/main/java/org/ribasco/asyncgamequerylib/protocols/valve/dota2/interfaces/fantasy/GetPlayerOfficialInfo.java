package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.fantasy;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2FantasyRequest;

public class GetPlayerOfficialInfo extends Dota2FantasyRequest {
    public GetPlayerOfficialInfo(int apiVersion, int accountId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETPLAYERINFO, apiVersion);
        urlParam("accountid", accountId);
    }
}
