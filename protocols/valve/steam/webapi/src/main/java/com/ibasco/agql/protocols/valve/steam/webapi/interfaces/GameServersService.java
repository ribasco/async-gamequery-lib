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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.core.exceptions.WebException;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiInterface;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameservers.*;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.gameservers.pojos.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class GameServersService extends SteamWebApiInterface {

    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link SteamWebApiClient} instance
     */
    public GameServersService(SteamWebApiClient client) {
        super(client);
    }

    public CompletableFuture<NewGameServerAccount> createAccount(int appId, String memo) {
        CompletableFuture<JsonObject> json = sendRequest(new CreateAccount(VERSION_1, appId, memo));
        return json.thenApply(this::getResponse).thenApply(response -> {
            Type listType = new TypeToken<NewGameServerAccount>() {}.getType();
            return builder().fromJson(response, listType);
        });
    }

    public CompletableFuture<Void> deleteAccount(long steamId) {
        return sendRequest(new DeleteAccount(VERSION_1, steamId));
    }

    public CompletableFuture<GameServerAccount> getAccountList() {
        CompletableFuture<JsonObject> json = sendRequest(new GetAccountList(VERSION_1));
        return json.thenApply(this::getResponse).thenApply(response -> builder().fromJson(response, GameServerAccount.class));
    }

    public CompletableFuture<GameServerAccountPublicInfo> getAccountPublicInfo(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetAccountPublicInfo(VERSION_1, steamId));
        return json.thenApply(this::getResponse).thenApply(response -> builder().fromJson(response, GameServerAccountPublicInfo.class));
    }

    public CompletableFuture<List<GameServer>> getServerList(String filter) {
        return getServerList(filter, -1);
    }

    public CompletableFuture<List<GameServer>> getServerList(String filter, int limit) {
        CompletableFuture<JsonObject> json = sendRequest(new GetServerList(VERSION_1, filter, limit));
        return json.thenApply(this::getResponse).thenApply(new Function<JsonObject, List<GameServer>>() {
            @Override
            public List<GameServer> apply(JsonObject response) {
                List<GameServer> responseList;
                if (!response.has("servers")) {
                    responseList = new ArrayList<>();
                    return responseList;
                } else {
                    JsonArray array = response.getAsJsonArray("servers");
                    Type listType = new TypeToken<List<GameServer>>() {}.getType();
                    return builder().fromJson(array, listType);
                }
            }
        });
    }

    public CompletableFuture<String> resetLoginToken(long steamId) {
        CompletableFuture<JsonObject> json = sendRequest(new ResetLoginToken(VERSION_1, steamId));
        return json.thenApply(this::getResponse).thenApply(response -> {
            if (!response.has("login_token"))
                return null;
            return response.get("login_token").getAsString();
        });
    }

    public CompletableFuture<Void> setMemo(long steamId, String memo) {
        CompletableFuture<JsonObject> json = sendRequest(new SetMemo(VERSION_1, steamId, memo));
        return json.thenAccept(this::getResponse);
    }

    public CompletableFuture<LoginTokenStatus> queryLoginTokenStatus(String loginToken) {
        CompletableFuture<JsonObject> json = sendRequest(new QueryLoginToken(VERSION_1, loginToken));
        return json.thenApply(this::getResponse).thenApply(response -> builder().fromJson(response, LoginTokenStatus.class));
    }

    private JsonObject getResponse(JsonObject element) {
        if (!element.has("response"))
            throw new WebException("Missing 'response' object");
        return element.get("response").getAsJsonObject();
    }
}
