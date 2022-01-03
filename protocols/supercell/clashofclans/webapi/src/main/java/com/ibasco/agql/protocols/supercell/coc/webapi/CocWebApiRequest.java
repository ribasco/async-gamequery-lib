/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.ibasco.agql.core.AbstractWebApiRequest;
import org.jetbrains.annotations.ApiStatus;

@Deprecated
@ApiStatus.ScheduledForRemoval
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
        property(CocApiConstants.UF_PROP_VERSION, apiVersion);
        property(CocApiConstants.UF_PROP_BASEURL, CocApiConstants.UF_COC_BASE);
        limit(limit);
        before(before);
        after(after);
    }

    public void limit(int limit) {
        urlParam("limit", limit);
    }

    public void before(int before) {
        urlParam("before", before);
    }

    public void after(int after) {
        urlParam("after", after);
    }
}
