package org.ribasco.agql.protocols.valve.dota2;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

abstract public class Dota2WebApiRequest extends SteamWebApiRequest {
    public Dota2WebApiRequest(String apiInterface, String apiMethod, int apiVersion, String language) {
        super(apiInterface, apiMethod, apiVersion);
        urlParam(Dota2ApiConstants.DOTA2_URLPARAM_LANG, language);
    }
}
