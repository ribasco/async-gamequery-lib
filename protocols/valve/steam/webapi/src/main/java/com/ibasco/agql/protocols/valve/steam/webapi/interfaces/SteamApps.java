/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.apps.GetAppList;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.apps.GetServersAtAddress;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.apps.UpToDateCheck;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.ServerUpdateStatus;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamApp;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamGameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Contains the methods for the ISteamApps interface
 */
public class SteamApps extends SteamWebApiInterface {

    private static final Logger log = LoggerFactory.getLogger(SteamApps.class);

    public SteamApps(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<List<SteamApp>> getAppList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetAppList(VERSION_2));
        return json.thenApply((JsonObject element) -> {
            JsonElement jsonApps = element.get("applist")
                    .getAsJsonObject()
                    .getAsJsonArray("apps");
            Type appListType = new TypeToken<List<SteamApp>>() {
            }.getType();
            return builder().fromJson(jsonApps, appListType);
        });
    }

    public CompletableFuture<List<SteamGameServer>> getServersAtAddress(String address) throws UnknownHostException {
        return getServersAtAddress(InetAddress.getByName(address));
    }

    public CompletableFuture<List<SteamGameServer>> getServersAtAddress(InetAddress address) {
        CompletableFuture<JsonObject> json = sendRequest(new GetServersAtAddress(1, address.getHostAddress()));
        return json.thenApply(root -> {
            JsonObject response = root.getAsJsonObject("response");
            boolean success = response.getAsJsonPrimitive("success").getAsBoolean();
            JsonArray serverList = response.getAsJsonArray("servers");
            if (success) {
                Type serverListType = new TypeToken<List<SteamGameServer>>() {
                }.getType();
                return builder().fromJson(serverList, serverListType);
            }
            throw new AsyncGameLibUncheckedException("Server returned an invalid response");
        });
    }

    public CompletableFuture<ServerUpdateStatus> getServerUpdateStatus(int version, int appId) {
        CompletableFuture<JsonObject> updateStatusFuture = sendRequest(new UpToDateCheck(1, version, appId));
        return updateStatusFuture.thenApply(root -> builder().fromJson(root.getAsJsonObject("response"), ServerUpdateStatus.class));
    }
}
