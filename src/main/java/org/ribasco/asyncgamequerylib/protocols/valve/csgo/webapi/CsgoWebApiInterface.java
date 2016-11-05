package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi;

import com.google.gson.JsonObject;
import org.ribasco.asyncgamequerylib.core.AbstractWebApiInterface;
import org.ribasco.asyncgamequerylib.core.client.AbstractRestClient;
import org.ribasco.asyncgamequerylib.core.exceptions.JsonElementNotFoundException;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

abstract public class CsgoWebApiInterface extends AbstractWebApiInterface<SteamWebApiClient, CsgoWebApiRequest, CsgoWebApiResponse> {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public CsgoWebApiInterface(SteamWebApiClient client) {
        super(client);
    }

    protected JsonObject getResult(JsonObject root) {
        if (root.has("result")) {
            return root.getAsJsonObject("result");
        }
        throw new JsonElementNotFoundException(root, "Did not find 'result' section from the response");
    }
}
