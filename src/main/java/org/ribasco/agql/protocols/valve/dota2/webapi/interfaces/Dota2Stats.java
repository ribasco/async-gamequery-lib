package org.ribasco.agql.protocols.valve.dota2.webapi.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2WebApiInterface;
import org.ribasco.agql.protocols.valve.dota2.webapi.interfaces.stats.GetRealtimeStats;
import org.ribasco.agql.protocols.valve.dota2.webapi.pojos.Dota2RealtimeServerStats;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.concurrent.CompletableFuture;

public class Dota2Stats extends Dota2WebApiInterface {
    /**
     * {@inheritDoc}
     *
     * @param client
     */
    public Dota2Stats(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<Dota2RealtimeServerStats> getRealtimeStats(long serverSteamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRealtimeStats(VERSION_1, serverSteamId));
        return json.thenApply(r -> fromJson(r, Dota2RealtimeServerStats.class));
    }
}
