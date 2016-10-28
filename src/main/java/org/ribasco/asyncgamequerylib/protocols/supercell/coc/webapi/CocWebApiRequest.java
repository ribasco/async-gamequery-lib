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

package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi;

import org.ribasco.asyncgamequerylib.core.AbstractWebApiRequest;

abstract public class CocWebApiRequest extends AbstractWebApiRequest {

    public CocWebApiRequest(int apiVersion, String urlFormat) {
        this(apiVersion, urlFormat, -1);
    }

    public CocWebApiRequest(int apiVersion, String urlFormat, int limit) {
        this(apiVersion, urlFormat, limit, -1, -1);
    }

    public CocWebApiRequest(int apiVersion, String urlFormat, int limit, int before, int after) {
        super(apiVersion);
        baseUrlFormat(urlFormat);
        baseUrlProperty(CocApiConstants.UF_PROP_VERSION, apiVersion);
        baseUrlProperty(CocApiConstants.UF_PROP_BASEURL, CocApiConstants.UF_COC_BASE);
        limit(limit);
        before(before);
        after(after);
    }

    public void limit(int limit) {
        param("limit", limit);
    }

    public void before(int before) {
        param("before", before);
    }

    public void after(int after) {
        param("after", after);
    }
}
