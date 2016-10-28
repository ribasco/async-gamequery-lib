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

package com.ribasco.rglib.protocols.supercell.clashofclans.webapi.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.CocSearchCriteria;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.CocWebApiClient;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.CocWebApiInterface;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.interfaces.clans.GetClanInfo;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.interfaces.clans.GetClanMembers;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.interfaces.clans.GetClanWarLog;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.interfaces.clans.SearchClan;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.pojos.CocClanDetailedInfo;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.pojos.CocPlayer;
import com.ribasco.rglib.protocols.supercell.clashofclans.webapi.pojos.CocWarLogEntry;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A CoC Interface for issuing clan specific queries
 */
public class CocClans extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link CocWebApiClient} instance
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
     * @param criteria A {@link CocSearchCriteria} to help your life much easier
     *
     * @return A {@link CompletableFuture} containing a {@link List} of clans matching the criteria. Empty if no match found.
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
     * @param clanTag A {@link String} representing the clan tag
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayer}
     */
    public CompletableFuture<List<CocPlayer>> getClanMembers(String clanTag) {
        return getClanMembers(clanTag, -1, -1, -1);
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag A {@link String} representing the clan tag
     * @param limit   An {@link Integer} limiting the number of records returned
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayer}
     */
    public CompletableFuture<List<CocPlayer>> getClanMembers(String clanTag, int limit) {
        return getClanMembers(clanTag, limit, -1, -1);
    }

    /**
     * <p>List clan members</p>
     *
     * @param clanTag A {@link String} representing the clan tag
     * @param limit   An {@link Integer} limiting the number of records returned
     * @param after
     * @param before
     *
     * @return A {@link CompletableFuture} returning an instance of {@link List} of type {@link CocPlayer}
     */
    public CompletableFuture<List<CocPlayer>> getClanMembers(String clanTag, int limit, int after, int before) {
        CompletableFuture<JsonObject> json = sendRequest(new GetClanMembers(VERSION_1, clanTag, limit, after, before));
        return json.thenApply(new Function<JsonObject, List<CocPlayer>>() {
            @Override
            public List<CocPlayer> apply(JsonObject root) {
                JsonArray items = root.getAsJsonArray("items");
                Type type = new TypeToken<List<CocPlayer>>() {
                }.getType();
                return builder().fromJson(items, type);
            }
        });
    }

    public CompletableFuture<List<CocWarLogEntry>> getClanWarLog(String clanTag) {
        return getClanWarLog(clanTag, -1, -1, -1);
    }

    /**
     * <p>Retrieve clan's clan war log</p>
     *
     * @param clanTag
     * @param limit
     * @param after
     * @param before
     *
     * @return
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
