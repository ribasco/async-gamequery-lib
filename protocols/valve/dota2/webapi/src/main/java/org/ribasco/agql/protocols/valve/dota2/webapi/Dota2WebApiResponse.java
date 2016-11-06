package org.ribasco.agql.protocols.valve.dota2.webapi;

import org.asynchttpclient.Response;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;

public class Dota2WebApiResponse extends SteamWebApiResponse {
    public Dota2WebApiResponse(Response response) {
        super(response);
    }
}
