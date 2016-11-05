package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ribasco.asyncgamequerylib.core.AbstractWebApiInterface;
import org.ribasco.asyncgamequerylib.core.reflect.CollectionParameterizedType;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.exceptions.Dota2WebException;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.ArrayList;
import java.util.List;

abstract public class Dota2WebApiInterface extends AbstractWebApiInterface<SteamWebApiClient, Dota2WebApiRequest, Dota2WebApiResponse> {
    /**
     * {@inheritDoc}
     */
    public Dota2WebApiInterface(SteamWebApiClient client) {
        super(client);
    }

    protected JsonObject getValidResult(JsonObject json) {
        JsonObject result = json.getAsJsonObject("result");
        if (result != null)
            return result;
        throw new Dota2WebException(String.format("The response either did not contain the desired result or the server responded in error. (JSON = %s)", json));
    }

    protected <T> List<T> asCollectionOf(Class type, String listName, JsonObject root) {
        return asListOf(type, listName, root, false);
    }

    @SuppressWarnings("all")
    protected <T> List<T> asListOf(Class type, String listName, JsonObject root, boolean strict) {
        //Process json
        JsonArray items = null;
        try {
            items = getValidResult(root).getAsJsonArray(listName);
        } catch (Dota2WebException e) {
            //if we didn't find a result container, check the top level
            if (root.has(listName) && root.get(listName).isJsonArray())
                items = root.getAsJsonArray(listName);
        }
        if (items == null) {
            if (strict)
                throw new Dota2WebException(String.format("Unable to find '%s' from the response", listName));
            else
                return new ArrayList<>();
        }
        //Parse then return the Parameterized List
        return fromJson(items, new CollectionParameterizedType(type, ArrayList.class));
    }
}
