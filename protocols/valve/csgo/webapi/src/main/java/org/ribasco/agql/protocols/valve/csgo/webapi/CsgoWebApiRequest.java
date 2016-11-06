package org.ribasco.agql.protocols.valve.csgo.webapi;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

public class CsgoWebApiRequest extends SteamWebApiRequest {
    public CsgoWebApiRequest(String apiInterface, String apiMethod, int apiVersion) {
        super(apiInterface, apiMethod, apiVersion);
    }
}
