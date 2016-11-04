package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.fantasy.GetPlayerOfficialInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.fantasy.GetProPlayerList;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos.Dota2FantasyPlayerInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos.Dota2FantasyProPlayerInfo;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Dota2Fantasy extends Dota2WebApiInterface {
    /**
     * {@inheritDoc}
     *
     * @param client
     */
    public Dota2Fantasy(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<Object> getFantasyPlayerStats() {
        //TODO: need to obtain a valid fantasy league id to test this properly
        return null;
    }

    public CompletableFuture<Dota2FantasyPlayerInfo> getPlayerOfficialInfo(int accountId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerOfficialInfo(VERSION_1, accountId));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2FantasyPlayerInfo.class));
    }

    public CompletableFuture<List<Dota2FantasyProPlayerInfo>> getProPlayerList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetProPlayerList(VERSION_1));
        return json.thenApply(r -> asListOf(Dota2FantasyProPlayerInfo.class, "player_infos", r));
    }

}
