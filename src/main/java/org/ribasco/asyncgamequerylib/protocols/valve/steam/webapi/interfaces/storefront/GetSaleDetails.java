package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamStoreApiRequest;

public class GetSaleDetails extends SteamStoreApiRequest {
    public GetSaleDetails(int apiVersion, int saleId, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_SALE_DETAILS, countryCode, language);
        urlParam("id", saleId);
    }
}
