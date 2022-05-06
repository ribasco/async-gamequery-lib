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

import com.google.gson.JsonElement;
import com.ibasco.agql.core.AbstractWebApiResponse;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>CocWebApiResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocWebApiResponse extends AbstractWebApiResponse<JsonElement> {

    /**
     * <p>Constructor for CocWebApiResponse.</p>
     *
     * @param response
     *         a {@link org.asynchttpclient.Response} object
     */
    public CocWebApiResponse(Response response) {
        super(response);
    }
}
