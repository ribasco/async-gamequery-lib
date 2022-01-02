/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetClanRankingsForLoc;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetLocationInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetLocations;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.locations.GetPlayerRankingsForLoc;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocClanRankInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLocation;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the Location interface. Contains methods for clan/player inquiries based on
 * location.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/locations">Clash of Clans API - Locations</a>
 */
public class CocLocations extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link CocWebApiClient} instance
     */
    public CocLocations(CocWebApiClient client) {
        super(client);
    }

    /**
     * <p>List all available locations</p>
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLocation}
     */
    public CompletableFuture<List<CocLocation>> getLocations() {
        return getLocations(-1, -1, -1);
    }

    /**
     * <p>List all available locations</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLocation}
     */
    public CompletableFuture<List<CocLocation>> getLocations(int limit) {
        return getLocations(limit, -1, -1);
    }

    /**
     * <p>List all available locations</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLocation}
     */
    public CompletableFuture<List<CocLocation>> getLocations(int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocations(VERSION_1, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocLocation>>() {
            @Override
            public List<CocLocation> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocLocation>>() {
                }.getType());
            }
        });
    }

    /**
     * <p>Get information about specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     *
     * @return A {@link CompletableFuture} containing a future result of {@link CocLocation}
     *
     * @see CocLocations#getLocations()
     */
    public CompletableFuture<CocLocation> getLocationInfo(int locationId) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocationInfo(VERSION_1, locationId));
        return json.thenApply(root -> builder().fromJson(root, CocLocation.class));
    }

    /**
     * <p>Get clan rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocClanRankInfo}
     */
    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId) {
        return getClanRankingsFromLocation(locationId, -1);
    }

    /**
     * <p>Get clan rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocClanRankInfo}
     */
    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId, int limit) {
        return getClanRankingsFromLocation(locationId, limit, -1, -1);
    }

    /**
     * <p>Get clan rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocClanRankInfo}
     */
    public CompletableFuture<List<CocClanRankInfo>> getClanRankingsFromLocation(int locationId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocClanRankInfo>>() {
            @Override
            public List<CocClanRankInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocClanRankInfo>>() {
                }.getType());
            }
        });
    }

    /**
     * <p>Get player rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocPlayerRankInfo}
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId) {
        return getPlayerRankingsFromLocation(locationId, -1);
    }

    /**
     * <p>Get player rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocPlayerRankInfo}
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId, int limit) {
        return getPlayerRankingsFromLocation(locationId, limit, -1, -1);
    }

    /**
     * <p>Get player rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link Integer} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     * @param after
     *         (optional) An {@link Integer} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both. Otherwise use -1 to disregard.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocPlayerRankInfo}
     */
    public CompletableFuture<List<CocPlayerRankInfo>> getPlayerRankingsFromLocation(int locationId, int limit, int before, int after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, List<CocPlayerRankInfo>>() {
            @Override
            public List<CocPlayerRankInfo> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                return builder().fromJson(items, new TypeToken<List<CocPlayerRankInfo>>() {
                }.getType());
            }
        });
    }

}
