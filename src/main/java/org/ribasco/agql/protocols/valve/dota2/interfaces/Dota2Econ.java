package org.ribasco.agql.protocols.valve.dota2.interfaces;

import com.google.gson.JsonObject;
import org.ribasco.agql.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.agql.protocols.valve.dota2.enums.Dota2IconType;
import org.ribasco.agql.protocols.valve.dota2.interfaces.econ.*;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2EventStats;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2GameItem;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2Heroes;
import org.ribasco.agql.protocols.valve.dota2.pojos.Dota2Rarities;
import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants.DEFAULT_LANG;

public class Dota2Econ extends Dota2WebApiInterface {

    private static final Logger log = LoggerFactory.getLogger(Dota2Econ.class);

    private static final String LIST_NAME_ITEMS = "items";
    private static final String LIST_NAME_HEROES = "heroes";
    private static final String LIST_NAME_RARITIES = "rarities";

    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link SteamWebApiClient} instance
     */
    public Dota2Econ(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<Dota2GameItem>> getGameItems() {
        return getGameItems(DEFAULT_LANG);
    }

    public CompletableFuture<List<Dota2GameItem>> getGameItems(String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetGameItems(VERSION_1, language));
        return json.thenApply(r -> asListOf(Dota2GameItem.class, LIST_NAME_ITEMS, r));
    }

    public CompletableFuture<List<Dota2Heroes>> getGameHeroes(boolean itemizedOnly, String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetHeroes(VERSION_1, itemizedOnly, language));
        return json.thenApply(r -> asListOf(Dota2Heroes.class, LIST_NAME_HEROES, r));
    }

    public CompletableFuture<String> getItemIconPath(String iconName, Dota2IconType iconType) {
        CompletableFuture<JsonObject> json = sendRequest(new GetItemIconPath(VERSION_1, iconName, iconType.getType()));
        return json.thenApply(r -> {
            JsonObject result = r.getAsJsonObject("result");
            if (result.has("path"))
                return result.getAsJsonPrimitive("path").getAsString();
            return null;
        });
    }

    public CompletableFuture<List<Dota2Rarities>> getRarities(String language) {
        CompletableFuture<JsonObject> json = sendRequest(new GetRarities(VERSION_1, language));
        return json.thenApply(r -> asListOf(Dota2Rarities.class, LIST_NAME_RARITIES, r));
    }

    public CompletableFuture<Integer> getTournamentPrizePool(int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetTournamentPrizePool(VERSION_1, leagueId));
        return json.thenApply(r -> getValidResult(r).getAsJsonPrimitive("prize_pool").getAsInt());
    }

    public CompletableFuture<Dota2EventStats> getEventStatsForAccount(int accountId, int leagueId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetEventStatsForAccount(VERSION_1, accountId, leagueId, null));
        return json.thenApply(r -> fromJson(getValidResult(r), Dota2EventStats.class));
    }
}
