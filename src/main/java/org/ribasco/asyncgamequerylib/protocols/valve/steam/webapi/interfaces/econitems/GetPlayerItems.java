package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.SteamEconItemsRequest;

public class GetPlayerItems extends SteamEconItemsRequest {
    public GetPlayerItems(int appId, long steamId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETPLAYERITEMS, apiVersion);
        urlParam(SteamApiConstants.STEAM_URLPARAM_STEAMID, steamId);
    }
}
