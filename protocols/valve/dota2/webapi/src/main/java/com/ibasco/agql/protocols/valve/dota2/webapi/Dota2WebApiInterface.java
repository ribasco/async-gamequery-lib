/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.dota2.webapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.core.reflect.types.CollectionParameterizedType;
import com.ibasco.agql.protocols.valve.dota2.webapi.exceptions.Dota2WebException;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

import java.util.ArrayList;
import java.util.List;

abstract public class Dota2WebApiInterface extends AbstractWebApiInterface<SteamWebApiClient, Dota2WebApiRequest, Dota2WebApiResponse> {

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
