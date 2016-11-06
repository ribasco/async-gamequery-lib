package org.ribasco.agql.protocols.valve.csgo.webapi;

import org.asynchttpclient.Response;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;

public class CsgoWebApiResponse extends SteamWebApiResponse {
    public CsgoWebApiResponse(Response response) {
        super(response);
    }
}
