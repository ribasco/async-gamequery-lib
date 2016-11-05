package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi;

import org.asynchttpclient.Response;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiResponse;

public class CsgoWebApiResponse extends SteamWebApiResponse {
    public CsgoWebApiResponse(Response response) {
        super(response);
    }
}
