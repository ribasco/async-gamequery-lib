package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamNewsRequest extends SteamWebApiRequest {
    public SteamNewsRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_NEWS, apiMethod, apiVersion);
    }
}
