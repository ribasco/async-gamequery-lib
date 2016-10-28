package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamUserRequest extends SteamWebApiRequest {
    public SteamUserRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_USER, apiMethod, apiVersion);
    }
}
