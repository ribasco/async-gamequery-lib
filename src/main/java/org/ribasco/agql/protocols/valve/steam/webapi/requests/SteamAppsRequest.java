package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamAppsRequest extends SteamWebApiRequest {
    public SteamAppsRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_APPS, apiMethod, apiVersion);
    }
}
