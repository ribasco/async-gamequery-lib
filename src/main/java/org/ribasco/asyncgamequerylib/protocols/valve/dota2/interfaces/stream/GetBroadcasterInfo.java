package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.stream;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2StreamRequest;

public class GetBroadcasterInfo extends Dota2StreamRequest {
    public GetBroadcasterInfo(int apiVersion, long broadcasterSteamId, int leagueId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETBROADCASTERINFO, apiVersion, null);
        urlParam("broadcaster_steam_id", broadcasterSteamId);
        urlParam("league_id", leagueId);
    }
}
