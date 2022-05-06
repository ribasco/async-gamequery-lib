/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.ibasco.agql.core.AbstractWebApiRequest;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>Abstract CocWebApiRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
abstract public class CocWebApiRequest extends AbstractWebApiRequest {

    /**
     * <p>Constructor for CocWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     * @param urlFormat
     *         a {@link java.lang.String} object
     */
    public CocWebApiRequest(int apiVersion, String urlFormat) {
        this(apiVersion, urlFormat, -1);
    }

    /**
     * <p>Constructor for CocWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     * @param urlFormat
     *         a {@link java.lang.String} object
     * @param limit
     *         a int
     */
    public CocWebApiRequest(int apiVersion, String urlFormat, int limit) {
        this(apiVersion, urlFormat, limit, -1, -1);
    }

    /**
     * <p>Constructor for CocWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     * @param urlFormat
     *         a {@link java.lang.String} object
     * @param limit
     *         a int
     * @param before
     *         a int
     * @param after
     *         a int
     */
    public CocWebApiRequest(int apiVersion, String urlFormat, int limit, int before, int after) {
        super(apiVersion);
        baseUrlFormat(urlFormat);
        property(CocApiConstants.UF_PROP_VERSION, apiVersion);
        property(CocApiConstants.UF_PROP_BASEURL, CocApiConstants.UF_COC_BASE);
        limit(limit);
        before(before);
        after(after);
    }

    /**
     * <p>limit.</p>
     *
     * @param limit
     *         a int
     */
    public void limit(int limit) {
        urlParam("limit", limit);
    }

    /**
     * <p>before.</p>
     *
     * @param before
     *         a int
     */
    public void before(int before) {
        urlParam("before", before);
    }

    /**
     * <p>after.</p>
     *
     * @param after
     *         a int
     */
    public void after(int after) {
        urlParam("after", after);
    }
}
