package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamNewsRequest extends SteamWebApiRequest {
    public SteamNewsRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_NEWS, apiMethod, apiVersion);
    }
}
