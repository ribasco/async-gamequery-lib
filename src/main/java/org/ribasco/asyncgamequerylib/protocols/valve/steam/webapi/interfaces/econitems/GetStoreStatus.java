package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.requests.SteamEconItemsRequest;

public class GetStoreStatus extends SteamEconItemsRequest {
    public GetStoreStatus(int appId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETSTORESTATUS, apiVersion);
    }
}
