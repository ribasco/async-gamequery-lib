package org.ribasco.agql.protocols.valve.dota2.webapi;

import org.asynchttpclient.Response;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiResponse;

public class Dota2WebApiClient extends SteamWebApiClient {

    public Dota2WebApiClient(String apiToken) {
        super(apiToken);
    }

    @Override
    protected SteamWebApiResponse createWebApiResponse(Response response) {
        return new Dota2WebApiResponse(response);
    }
}
