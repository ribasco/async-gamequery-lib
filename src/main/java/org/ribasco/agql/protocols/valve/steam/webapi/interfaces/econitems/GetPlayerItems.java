package org.ribasco.agql.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.requests.SteamEconItemsRequest;

public class GetPlayerItems extends SteamEconItemsRequest {
    public GetPlayerItems(int appId, long steamId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETPLAYERITEMS, apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STEAMID, steamId);
    }
}
