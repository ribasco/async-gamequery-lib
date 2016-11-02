package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.econitems;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.SteamEconItemsRequest;

public class GetSchemaUrl extends SteamEconItemsRequest {
    public GetSchemaUrl(int appId, int apiVersion) {
        super(appId, SteamApiConstants.STEAM_METHOD_ECONITEMS_GETSCHEMAURL, apiVersion);
    }
}
