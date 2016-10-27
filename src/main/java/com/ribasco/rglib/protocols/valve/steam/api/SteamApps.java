/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ribasco.rglib.core.exceptions.RglibUncheckedException;
import com.ribasco.rglib.protocols.valve.steam.SteamWebApiClient;
import com.ribasco.rglib.protocols.valve.steam.api.apps.GetAppList;
import com.ribasco.rglib.protocols.valve.steam.api.apps.GetServersAtAddress;
import com.ribasco.rglib.protocols.valve.steam.api.apps.UpToDateCheck;
import com.ribasco.rglib.protocols.valve.steam.pojos.ServerUpdateStatus;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamApp;
import com.ribasco.rglib.protocols.valve.steam.pojos.SteamGameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Contains the methods for the ISteamApps interface
 */
public class SteamApps extends SteamInterface {

    private static final Logger log = LoggerFactory.getLogger(SteamApps.class);

    public SteamApps(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamApp>> getAppList() {
        final CompletableFuture<JsonObject> json = client.sendRequest(new GetAppList(2));
        return json.thenApply((JsonObject element) -> {
            JsonElement jsonApps = element.get("applist")
                    .getAsJsonObject()
                    .getAsJsonArray("apps");
            Type appListType = new TypeToken<List<SteamApp>>() {
            }.getType();
            return client.getJsonBuilder().fromJson(jsonApps, appListType);
        });
    }

    public CompletableFuture<List<SteamGameServer>> getServersAtAddress(String address) throws UnknownHostException {
        return getServersAtAddress(InetAddress.getByName(address));
    }

    public CompletableFuture<List<SteamGameServer>> getServersAtAddress(InetAddress address) {
        final CompletableFuture<JsonObject> json = client.sendRequest(new GetServersAtAddress(1, address.getHostAddress()));
        return json.thenApply(new Function<JsonObject, List<SteamGameServer>>() {
            @Override
            public List<SteamGameServer> apply(JsonObject root) {
                JsonObject response = root.getAsJsonObject("response");
                Boolean success = response.getAsJsonPrimitive("success").getAsBoolean();
                JsonArray serverList = response.getAsJsonArray("servers");
                if (success) {
                    Type serverListType = new TypeToken<List<SteamGameServer>>() {
                    }.getType();
                    return client.getJsonBuilder().fromJson(serverList, serverListType);
                }
                throw new RglibUncheckedException("Server returned an invalid response");
            }
        });
    }

    public CompletableFuture<ServerUpdateStatus> getServerUpdateStatus(int version, int appId) {
        CompletableFuture<JsonObject> updateStatusFuture = client.sendRequest(new UpToDateCheck(1, version, appId));
        return updateStatusFuture.thenApply(root -> client.getJsonBuilder().fromJson(root.getAsJsonObject("response"), ServerUpdateStatus.class));
    }
}
