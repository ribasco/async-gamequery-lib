package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.stats;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2StatsRequest;

public class GetRealtimeStats extends Dota2StatsRequest {
    public GetRealtimeStats(int apiVersion, long serverSteamId) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETREALTIMESTATS, apiVersion, null);
        urlParam("server_steam_id", serverSteamId);
    }
}
