package org.ribasco.agql.protocols.valve.dota2.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.agql.protocols.valve.dota2.interfaces.stream.GetBroadcasterInfo;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2BroadcasterInfo;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

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
