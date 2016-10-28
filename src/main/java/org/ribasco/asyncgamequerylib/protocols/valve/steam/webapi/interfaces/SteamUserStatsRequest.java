package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamUserStatsRequest extends SteamWebApiRequest {
    public SteamUserStatsRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_USER_STATS, apiMethod, apiVersion);
    }
}
