/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.clans.GetClanInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.clans.GetClanMembers;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.clans.GetClanWarLog;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.clans.SearchClan;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanDetailedInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerBasicInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocWarLogEntry;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the Clan interface. Contains methods for clan-related inquiries.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/clans">Clash of Clans API - Clans</a>
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocClans extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link CocWebApiClient} instance
     */
    public CocClans(CocWebApiClient client) {
        super(client);
    }

    /**
     * <p>
     * Search all clans by name and/or filtering the results using various criteria. At least one filtering criteria
     * must be defined and if name is used as part of search, it is required to be at least three characters long.
     * It is not possible to specify ordering for results so clients should not rely on any specific
     * ordering as that may change in the future releases of the API.
     * </p>
     *
     * @param criteria
     *         A {@link CocSearchCriteria} to help your life much easier
     *
     * @return A {@link CompletableFuture} containing a {@link List} of clans matching the criteria. Empty if no match
     * found.
     */
    public CompletableFuture<List<CocClanDetailedInfo>> searchClans(CocSearchCriteria criteria) {
        CompletableFuture<JsonObject> json = sendRequest(new SearchClan(VERSION_1, criteria));
        return json.thenApply(new Function<JsonObject, List<CocClanDetailedInfo>>() {
            @Override
            public List<CocClanDetailedInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                Type type = new TypeToken<List<CocClanDetailedInfo>>() {
                }.getType();
                return builder().fromJson(items, type);
            }
        });
    }

    /**
     * <p>
     * Get information about a single clan by clan tag. Clan tags can be found using clan search operation.
     * Note that clan tags start with hash character '#' and that needs to be URL-encoded properly to work in URL,
     * so for example clan tag '#2ABC' would become '%232ABC' in the URL.
     * </p>
     *
     * @param clanTag
     *         A {@link String} preceded by a hash tag '#' character
     *
     * @return A {@link CompletableFuture} returning an instance of {@link CocClanDetailedInfo}
     */
    public CompletableFuture<CocClanDetailedInfo> getClanInfo(String clanTag) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanInfo(VERSION_1, clanTag));
        return json.thenApply(root -> builder().fromJson(root, CocClanDetailedInfo.class));
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag
     *         A {@link String} representing the clan tag
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayerBasicInfo}
     */
    public CompletableFuture<List<CocPlayerBasicInfo>> getClanMembers(String clanTag) {
        return getClanMembers(clanTag, -1, -1, -1);
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag
     *         A {@link String} representing the clan tag
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayerBasicInfo}
     */
    public CompletableFuture<List<CocPlayerBasicInfo>> getClanMembers(String clanTag, int limit) {
        return getClanMembers(clanTag, limit, -1, -1);
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag
     *         A {@link String} representing the clan tag
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After
     *         marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response,
     *         inside the 'paging' property. Note that only after or before can be specified for a request, not
     *         both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayerBasicInfo}
     */
    public CompletableFuture<List<CocPlayerBasicInfo>> getClanMembers(String clanTag, int limit, int after, int before) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanMembers(VERSION_1, clanTag, limit, after, before));
        return json.thenApply(new Function<JsonObject, List<CocPlayerBasicInfo>>() {
            @Override
            public List<CocPlayerBasicInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                Type type = new TypeToken<List<CocPlayerBasicInfo>>() {
                }.getType();
                return builder().fromJson(items, type);
            }
        });
    }

    /**
     * <p>Retrieve clan's clan war log</p>
     *
     * @param clanTag
     *         A {@link String} preceded by a hash tag '#' character
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocWarLogEntry}
     */
    public CompletableFuture<List<CocWarLogEntry>> getClanWarLog(String clanTag) {
        return getClanWarLog(clanTag, -1, -1, -1);
    }

    /**
     * <p>Retrieve clan's clan war log</p>
     *
     * @param clanTag
     *         A {@link String} preceded by a hash tag '#' character
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both. Otherwise use
     *         -1 to disregard.
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *         Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocWarLogEntry}
     */
    public CompletableFuture<List<CocWarLogEntry>> getClanWarLog(String clanTag, int limit, int after, int before) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanWarLog(VERSION_1, clanTag, limit, after, before));
        return json.thenApply(new Function<JsonObject, List<CocWarLogEntry>>() {
            @Override
            public List<CocWarLogEntry> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                Type type = new TypeToken<List<CocWarLogEntry>>() {
                }.getType();
                return builder().fromJson(items, type);
            }
        });
    }
}
