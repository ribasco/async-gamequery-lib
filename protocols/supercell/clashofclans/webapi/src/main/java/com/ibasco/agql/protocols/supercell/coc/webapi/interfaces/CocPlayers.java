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

import com.google.gson.JsonObject;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiInterface;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.players.GetPlayerInfo;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerDetailedInfo;

import java.util.concurrent.CompletableFuture;

/**
 * <p>A Web API Implementation of the Player interface. Contains methods for player related inquiries.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.clashofclans.com/api-docs/index.html#!/players">Clash of Clans API - Players</a>
 */
public class CocPlayers extends CocWebApiInterface {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link CocWebApiClient} instance
     */
    public CocPlayers(CocWebApiClient client) {
        super(client);
    }

    /**
     * <p>Retrieve a detailed information about a Player</p>
     *
     * @param playerTag
     *         A unique player {@link String} identifier followed by a hashtag
     *
     * @return A {@link CompletableFuture} containing a future result of {@link CocPlayerDetailedInfo}
     */
    public CompletableFuture<CocPlayerDetailedInfo> getPlayerInfo(String playerTag) {
        CompletableFuture<JsonObject> json = sendRequest(new GetPlayerInfo(VERSION_1, playerTag));
        return json.thenApply(root -> builder().fromJson(root, CocPlayerDetailedInfo.class));
    }
}
