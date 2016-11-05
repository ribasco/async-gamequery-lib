package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2WebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.teams.GetTeamInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.pojos.Dota2TeamDetails;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

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

    public CompletableFuture<List<Dota2TeamDetails>> getTeamInfo(int teamId) {
        return getTeamInfo(teamId, -1);
    }

    public CompletableFuture<List<Dota2TeamDetails>> getTeamInfo(int teamId, int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTeamInfo(VERSION_1, teamId, leagueId));
        return json.thenApply(r -> asCollectionOf(Dota2TeamDetails.class, "teams", r));
    }
}
