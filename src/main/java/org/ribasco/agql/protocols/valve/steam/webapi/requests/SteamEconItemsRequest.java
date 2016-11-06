package org.ribasco.agql.protocols.valve.steam.webapi.requests;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

//TODO: Add language support
abstract public class SteamEconItemsRequest extends SteamWebApiRequest {
    public SteamEconItemsRequest(int appId, String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_ECON_ITEMS, apiMethod, apiVersion);
        property(SteamApiConstants.STEAM_PROP_APPID, appId);
    }
}
