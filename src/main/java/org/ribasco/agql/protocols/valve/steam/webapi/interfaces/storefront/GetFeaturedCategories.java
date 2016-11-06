package org.ribasco.agql.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;

public class GetFeaturedCategories extends SteamStoreApiRequest {
    public GetFeaturedCategories(int apiVersion, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_FEATURED_CATEGORIES, countryCode, language);
    }
}
