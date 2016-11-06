package org.ribasco.agql.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.requests.SteamEconItemsRequest;

public class GetStoreMetadata extends SteamEconItemsRequest {
    public GetStoreMetadata(int appId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETSTOREMETA, apiVersion);
    }
}
