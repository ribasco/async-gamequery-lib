package org.ribasco.agql.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;

public class GetPackageDetails extends SteamStoreApiRequest {
    public GetPackageDetails(int apiVersion, int packageId, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_PACKAGE_DETAILS, countryCode, language);
        urlParam("packageids", packageId);
    }
}
