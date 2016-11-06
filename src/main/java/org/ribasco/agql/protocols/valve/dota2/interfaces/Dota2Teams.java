package org.ribasco.agql.protocols.valve.dota2.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.agql.protocols.valve.dota2.interfaces.teams.GetTeamInfo;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2TeamDetails;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Dota2Teams extends Dota2WebApiInterface {
    /**
     * {@inheritDoc}
     *
     * @param client
     */
    public Dota2Teams(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<Dota2TeamDetails>> getTeamInfo(int teamId, int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTeamInfo(VERSION_1, teamId, leagueId));
        return json.thenApply(r -> asListOf(Dota2TeamDetails.class, "teams", r));
    }
}
