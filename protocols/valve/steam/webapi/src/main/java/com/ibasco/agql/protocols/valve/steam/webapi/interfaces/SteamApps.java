/*
 * Copyright (c) 2022 Asynchronous Game Query Library
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
import com.ibasco.agql.core.exceptions.AgqlRuntimeException;
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
 *
 * @author Rafael Luis Ibasco
 */
public class SteamApps extends SteamWebApiInterface {

    private static final Logger log = LoggerFactory.getLogger(SteamApps.class);

    /**
     * <p>Constructor for SteamApps.</p>
     *
     * @param client a {@link com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient} object
     */
    public SteamApps(SteamWebApiClient client) {
        super(client);
    }

    /**
     * <p>getAppList.</p>
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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

    /**
     * <p>getServersAtAddress.</p>
     *
     * @param address a {@link java.lang.String} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     * @throws java.net.UnknownHostException if any.
     */
    public CompletableFuture<List<SteamGameServer>> getServersAtAddress(String address) throws UnknownHostException {
        return getServersAtAddress(InetAddress.getByName(address));
    }

    /**
     * <p>getServersAtAddress.</p>
     *
     * @param address a {@link java.net.InetAddress} object
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
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
            throw new AgqlRuntimeException("Server returned an invalid response");
        });
    }

    /**
     * <p>getServerUpdateStatus.</p>
     *
     * @param version a int
     * @param appId a int
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public CompletableFuture<ServerUpdateStatus> getServerUpdateStatus(int version, int appId) {
        CompletableFuture<JsonObject> updateStatusFuture = sendRequest(new UpToDateCheck(1, version, appId));
        return updateStatusFuture.thenApply(root -> builder().fromJson(root.getAsJsonObject("response"), ServerUpdateStatus.class));
    }
}
