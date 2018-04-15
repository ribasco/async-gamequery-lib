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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.ibasco.agql.core.AbstractWebApiRequest;

import java.util.Optional;

abstract public class CocWebApiRequest extends AbstractWebApiRequest {

    public CocWebApiRequest(int apiVersion, String urlFormat) {
        this(apiVersion, urlFormat, Optional.empty(),Optional.empty(),Optional.empty());
    }

    public CocWebApiRequest(int apiVersion, String urlFormat, int limit) {
        this(apiVersion, urlFormat, Optional.of(limit), Optional.empty(),Optional.empty());
    }

    /**
     * <i>Do not use this method,because :</i>
     * <ul>
     *     <li>The type of parameter {@code before} and {@code after} should be {@code String}</li>
     *     <li>{@code limit},{@code before} and {@code after} are optional, Should not pass to server with a default value(if you don't need them)</li>
     * </ul>
     * @deprecated  Use {@code CocWebApiRequest(int, String, Optional, Optional, Optional)}  instead
     * @param apiVersion
     * @param urlFormat
     * @param limit
     * @param before
     * @param after
     * @see CocWebApiRequest#CocWebApiRequest(int, String, Optional, Optional, Optional)
     *
     */
    public CocWebApiRequest(int apiVersion, String urlFormat, int limit, int before, int after) {
        super(apiVersion);
        baseUrlFormat(urlFormat);
        property(CocApiConstants.UF_PROP_VERSION, apiVersion);
        property(CocApiConstants.UF_PROP_BASEURL, CocApiConstants.UF_COC_BASE);
        limit(limit);
        before(Integer.toString(before));
        after(Integer.toString(after));
    }

    /**
     * CocWebApiRequest Constructor
     *
     * @param apiVersion
     * @param urlFormat
     * @param limit Limit the number of items returned from server.
     * @param before Return only items that occur after this marker. After marker can be found from the response, inside the 'paging' property.
     * @param after Return only items that occur before this marker. Before marker can be found from the response, inside the 'paging' property.
     */
    public CocWebApiRequest(int apiVersion, String urlFormat, Optional<Integer> limit, Optional<String> before, Optional<String> after) {
        super(apiVersion);
        baseUrlFormat(urlFormat);
        property(CocApiConstants.UF_PROP_VERSION, apiVersion);
        property(CocApiConstants.UF_PROP_BASEURL, CocApiConstants.UF_COC_BASE);
        limit.ifPresent( o -> limit(o));
        before.ifPresent( o -> before(o));
        after.ifPresent( o -> after(o));
    }
    public void limit(int limit) {
        urlParam("limit", limit);
    }

    public void before(String before) {
        urlParam("before", before);
    }

    public void after(String after) {
        urlParam("after", after);
    }
}
