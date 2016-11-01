package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.interfaces.storefront;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiRequest;

public class GetFeaturedGames extends SteamWebApiRequest {
    public GetFeaturedGames(String apiInterface, String apiMethod, int apiVersion) {
        super(apiInterface, apiMethod, apiVersion);
    }
}
