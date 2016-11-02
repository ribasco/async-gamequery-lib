package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamStoreApiRequest;

public class GetAppDetails extends SteamStoreApiRequest {
    public GetAppDetails(int apiVersion, int appId, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_APPDETAILS, countryCode, language);
        param("appids", appId);
    }
}
