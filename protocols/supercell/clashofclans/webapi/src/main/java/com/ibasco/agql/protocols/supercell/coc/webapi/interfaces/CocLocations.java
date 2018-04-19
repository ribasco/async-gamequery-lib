/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces;

import com.google.gson.GsonBuilder;
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
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.paging.Paging;

import java.util.List;
import java.util.Optional;
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
    public CompletableFuture<Paging<List<CocLocation>>> getLocations() {
        return getLocations(Optional.empty(), Optional.empty(),Optional.empty());
    }

    /**
     * <p>List all available locations</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLocation}
     */
    public CompletableFuture<Paging<List<CocLocation>>> getLocations(int limit) {
        return getLocations(Optional.of(limit), Optional.empty(),Optional.empty());
    }

    /**
     * <p>List all available locations</p>
     *
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link String} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both.
     * @param after
     *         (optional) An {@link String} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *
     * @return A {@link CompletableFuture} containing a future result for a {@link List} of {@link CocLocation}
     */
    public CompletableFuture<Paging<List<CocLocation>>> getLocations(Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetLocations(VERSION_1, limit, before, after));
        return json.thenApply(new Function<JsonObject, Paging<List<CocLocation>>>() {
            @Override
            public Paging<List<CocLocation>> apply(JsonObject root) {
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
    public CompletableFuture<Paging<List<CocClanRankInfo>>> getClanRankingsFromLocation(int locationId) {
        return getClanRankingsFromLocation(locationId, Optional.empty(), Optional.empty(),Optional.empty());
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
    public CompletableFuture<Paging<List<CocClanRankInfo>>> getClanRankingsFromLocation(int locationId, int limit) {
        return getClanRankingsFromLocation(locationId, Optional.of(limit), Optional.empty(),Optional.empty());
    }

    /**
     * <p>Get clan rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link String} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both.
     * @param after
     *         (optional) An {@link String} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocClanRankInfo}
     */
    public CompletableFuture<Paging<List<CocClanRankInfo>>> getClanRankingsFromLocation(int locationId, Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, Paging<List<CocClanRankInfo>>>() {
            @Override
            public Paging<List<CocClanRankInfo>> apply(JsonObject root) {
                return builder().fromJson(root, new TypeToken<Paging<List<CocClanRankInfo>>>() {
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
    public CompletableFuture<Paging<List<CocPlayerRankInfo>>> getPlayerRankingsFromLocation(int locationId) {
        return getPlayerRankingsFromLocation(locationId, Optional.empty(), Optional.empty(),Optional.empty());
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
    public CompletableFuture<Paging<List<CocPlayerRankInfo>>> getPlayerRankingsFromLocation(int locationId, int limit) {
        return getPlayerRankingsFromLocation(locationId, Optional.of(limit), Optional.empty(),Optional.empty());
    }

    /**
     * <p>Get player rankings for a specific location</p>
     *
     * @param locationId
     *         An {@link Integer} representing the identifier of the location to retrieve.
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param before
     *         (optional) An {@link String} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note         that only after
     *         or before can be specified for a request, not both.
     * @param after
     *         (optional) An {@link String} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *
     * @return A {@link CompletableFuture} containing a future result of a {@link List} of {@link CocPlayerRankInfo}
     */
    public CompletableFuture<Paging<List<CocPlayerRankInfo>>> getPlayerRankingsFromLocation(int locationId, Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerRankingsForLoc(VERSION_1, locationId, limit, before, after));
        return json.thenApply(new Function<JsonObject, Paging<List<CocPlayerRankInfo>>>() {
            @Override
            public Paging<List<CocPlayerRankInfo>> apply(JsonObject root) {
                return builder().fromJson(root, new TypeToken<Paging<List<CocPlayerRankInfo>>>() {
                }.getType());
            }
        });
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        super.configureBuilder(builder);
        builder.serializeNulls();
    }
}
