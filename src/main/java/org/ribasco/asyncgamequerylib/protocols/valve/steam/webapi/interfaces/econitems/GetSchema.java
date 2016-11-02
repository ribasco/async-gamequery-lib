package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.SteamEconItemsRequest;

public class GetSchema extends SteamEconItemsRequest {
    public GetSchema(int appId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETSCHEMA, apiVersion);
    }
}
