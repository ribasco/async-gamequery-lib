package org.ribasco.agql.protocols.valve.dota2.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.agql.protocols.valve.dota2.interfaces.stats.GetRealtimeStats;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2ServerStats;
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

    public CompletableFuture<Dota2ServerStats> getRealtimeStats(long serverSteamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRealtimeStats(VERSION_1, serverSteamId));
        return json.thenApply(r -> fromJson(r, Dota2ServerStats.class));
    }
}
