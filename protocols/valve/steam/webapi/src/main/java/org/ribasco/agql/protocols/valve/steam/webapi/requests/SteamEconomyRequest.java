package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class SteamEconomyRequest extends SteamWebApiRequest {
    public SteamEconomyRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_ECONOMY, apiMethod, apiVersion);
    }
}
