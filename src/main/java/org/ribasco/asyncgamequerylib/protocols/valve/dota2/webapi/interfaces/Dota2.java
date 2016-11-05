package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

public final class Dota2 {
    public static Dota2Econ createEcon(SteamWebApiClient client) {
        return new Dota2Econ(client);
    }

    public static Dota2Fantasy createFantasy(SteamWebApiClient client) {
        return new Dota2Fantasy(client);
    }

    public static Dota2Match createMatch(SteamWebApiClient client) {
        return new Dota2Match(client);
    }
}
