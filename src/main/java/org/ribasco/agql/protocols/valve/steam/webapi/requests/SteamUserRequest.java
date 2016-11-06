package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamUserRequest extends SteamWebApiRequest {
    public SteamUserRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_USER, apiMethod, apiVersion);
    }
}
