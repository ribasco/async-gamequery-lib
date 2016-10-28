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

package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.interfaces.clans;

import org.asynchttpclient.RequestBuilder;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.CocApiConstants;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.CocWebApiRequest;

/**
 * Created by raffy on 10/27/2016.
 */
public class GetClanMembers extends CocWebApiRequest {

    private int limit;
    private int after;
    private int before;

    public GetClanMembers(int apiVersion, String clanTag, int limit, int after, int before) {
        super(CocApiConstants.COC_CLANS, String.format("/%s/members", encodeString(clanTag)), apiVersion);
    }

    @Override
    protected void buildRequest(RequestBuilder requestBuilder) {
        addParam("limit", this.limit);
        addParam("after", this.after);
        addParam("before", this.before);
    }
}
