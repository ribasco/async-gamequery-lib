package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamPlayerServiceRequest extends SteamWebApiRequest {
    public SteamPlayerServiceRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_PLAYER_SERVICE, apiMethod, apiVersion);
    }
}
