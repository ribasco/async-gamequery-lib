package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamPlayerServiceRequest extends SteamWebApiRequest {
    public SteamPlayerServiceRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_PLAYER_SERVICE, apiMethod, apiVersion);
    }
}
