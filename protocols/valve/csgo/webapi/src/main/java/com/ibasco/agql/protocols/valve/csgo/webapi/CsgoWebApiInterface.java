/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.csgo.webapi;

import com.google.gson.JsonObject;
import com.ibasco.agql.core.AbstractWebApiInterface;
import com.ibasco.agql.core.client.AbstractRestClient;
import com.ibasco.agql.core.exceptions.JsonElementNotFoundException;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;

abstract public class CsgoWebApiInterface extends AbstractWebApiInterface<SteamWebApiClient, CsgoWebApiRequest, CsgoWebApiResponse> {
    /**
     * <p>Default Constructor</p>
     *
     * @param client
     *         A {@link AbstractRestClient} instance
     */
    public CsgoWebApiInterface(SteamWebApiClient client) {
        super(client);
    }

    protected JsonObject getResult(JsonObject root) {
        if (root.has("result")) {
            return root.getAsJsonObject("result");
        }
        throw new JsonElementNotFoundException(root, "Did not find 'result' section from the response");
    }
}
