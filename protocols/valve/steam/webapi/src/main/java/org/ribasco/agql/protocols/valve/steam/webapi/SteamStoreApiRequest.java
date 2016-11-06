package org.ribasco.agql.protocols.valve.steam.webapi;

abstract public class SteamStoreApiRequest extends SteamWebApiRequest {
    public SteamStoreApiRequest(int apiVersion, String urlFormat) {
        this(apiVersion, urlFormat, "us", "english");
    }

    public SteamStoreApiRequest(int apiVersion, String urlFormat, String countryCode, String language) {
        super(null, null, apiVersion);
        //Override super constructor property
        baseUrlFormat(urlFormat);
        property(SteamApiConstants.SF_PROP_BASEURL, SteamApiConstants.SF_BASE_URL_FORMAT);
        urlParam("c", countryCode);
        urlParam("l", language);
    }
}
