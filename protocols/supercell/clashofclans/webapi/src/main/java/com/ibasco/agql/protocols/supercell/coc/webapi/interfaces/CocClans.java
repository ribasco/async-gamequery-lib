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
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.paging.Paging;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * <p>A Web API Implementation of the Clan interface. Contains methods for clan-related inquiries.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/clans">Clash of Clans API - Clans</a>
 */
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
    public CompletableFuture<Paging<List<CocClanDetailedInfo>>> searchClans(CocSearchCriteria criteria) {
        CompletableFuture<JsonObject> json = sendRequest(new SearchClan(VERSION_1, criteria));
        return json.thenApply(new Function<JsonObject, Paging<List<CocClanDetailedInfo>>>() {
            @Override
            public Paging<List<CocClanDetailedInfo>> apply(JsonObject root) {
                Type type = new TypeToken<Paging<List<CocClanDetailedInfo>>>() {
                }.getType();
                return builder().fromJson(root, type);
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
    public CompletableFuture<Paging<List<CocPlayerBasicInfo>>> getClanMembers(String clanTag) {
        return getClanMembers(clanTag, Optional.empty(),Optional.empty(),Optional.empty());
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
    public CompletableFuture<Paging<List<CocPlayerBasicInfo>>> getClanMembers(String clanTag, int limit) {
        return getClanMembers(clanTag, Optional.of(limit), Optional.empty(),Optional.empty());
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag
     *         A {@link String} representing the clan tag
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param after
     *         (optional) An {@link String} that indicates to return only items that occur after this marker.
     *         After
     *         marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     * @param before
     *         (optional) An {@link String} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response,
     *         inside the 'paging' property. Note that only after or before can be specified for a request, not
     *         both.
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayerBasicInfo}
     */
    public CompletableFuture<Paging<List<CocPlayerBasicInfo>>> getClanMembers(String clanTag, Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanMembers(VERSION_1, clanTag, limit  , after, before));
        return json.thenApply(new Function<JsonObject, Paging<List<CocPlayerBasicInfo>>>() {
            @Override
            public Paging<List<CocPlayerBasicInfo>> apply(JsonObject root) {
                Type type = new TypeToken<Paging<List<CocPlayerBasicInfo>>>() {
                }.getType();
                return builder().fromJson(root, type);
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
    public CompletableFuture<Paging<List<CocWarLogEntry>>> getClanWarLog(String clanTag) {
        return getClanWarLog(clanTag,Optional.empty(), Optional.empty(),Optional.empty());
    }

    /**
     * <p>Retrieve clan's clan war log</p>
     *
     * @param clanTag
     *         A {@link String} preceded by a hash tag '#' character
     * @param limit
     *         An {@link Integer} limiting the number of records returned
     * @param after
     *         (optional) An {@link String} that indicates to return only items that occur after this marker.
     *         After marker can be found from the response, inside the 'paging' property. Note
     *         that only after or before can be specified for a request, not both.
     *
     * @param before
     *         (optional) An {@link String} that indicates to return only items that occur before this marker.
     *         Before marker can be found from the response, inside the 'paging' property. Note that only after
     *         or before can be specified for a request, not both.
     *
     * @return A {@link CompletableFuture} which contains a future result for a {@link List} of {@link CocWarLogEntry}
     */
    public CompletableFuture<Paging<List<CocWarLogEntry>>> getClanWarLog(String clanTag, Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanWarLog(VERSION_1, clanTag, limit, after, before));
        return json.thenApply(new Function<JsonObject, Paging<List<CocWarLogEntry>>>() {
            @Override
            public Paging<List<CocWarLogEntry>> apply(JsonObject root) {
                Type type = new TypeToken<Paging<List<CocWarLogEntry>>>() {
                }.getType();
                return builder().fromJson(root, type);
            }
        });
    }

    @Override
    protected void configureBuilder(GsonBuilder builder) {
        super.configureBuilder(builder);
        builder.serializeNulls();
    }
}
