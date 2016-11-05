package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2WebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.stream.GetBroadcasterInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.pojos.Dota2BroadcasterInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.concurrent.CompletableFuture;

public class Dota2Stream extends Dota2WebApiInterface {
    /**
     * {@inheritDoc}
     *
     * @param client
     */
    public Dota2Stream(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<Dota2BroadcasterInfo> getBroadcasterInfo(long broadcasterSteamId, int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetBroadcasterInfo(VERSION_1, broadcasterSteamId, leagueId));
        return json.thenApply(r -> fromJson(r, Dota2BroadcasterInfo.class));
    }
}
