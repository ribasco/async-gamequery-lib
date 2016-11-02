package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamStoreApiRequest;

public class GetFeaturedGames extends SteamStoreApiRequest {
    public GetFeaturedGames(int apiVersion, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_FEATURED, countryCode, language);
    }
}
