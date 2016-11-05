package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.interfaces;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.ribasco.asyncgamequerylib.core.client.AbstractRestClient;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.CsgoWebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.adapters.CsgoDatacenterStatusDeserializer;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.interfaces.servers.GetGameServersStatus;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.pojos.CsgoGameServerStatus;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CsgoServers extends CsgoWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public CsgoServers(SteamWebApiClient client) {
        super(client);
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        builder.registerTypeAdapter(new TypeToken<List<CsgoDatacenterStatus>>() {
        }.getType(), new CsgoDatacenterStatusDeserializer());
    }

    public CompletableFuture<CsgoGameServerStatus> getGameServerStatus() {
        CompletableFuture<JsonObject> json = sendRequest(new GetGameServersStatus(VERSION_1));
        return json.thenApply(r -> fromJson(getResult(r), CsgoGameServerStatus.class));
    }
}
