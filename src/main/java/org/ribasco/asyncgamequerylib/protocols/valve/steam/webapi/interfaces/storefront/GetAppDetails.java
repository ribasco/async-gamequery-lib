package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

public class GetAppDetails extends SteamWebApiRequest {
    public GetAppDetails(String apiInterface, String apiMethod, int apiVersion) {
        super(apiInterface, apiMethod, apiVersion);
    }
}