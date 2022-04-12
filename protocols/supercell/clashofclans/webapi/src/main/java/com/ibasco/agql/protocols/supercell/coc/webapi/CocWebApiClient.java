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

import com.ibasco.agql.core.AbstractRestClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An API Client for Clash of Clans
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocWebApiClient extends AbstractRestClient {
    private static final Logger log = LoggerFactory.getLogger(CocWebApiClient.class);

    /**
     * <p>Constructor for CocWebApiClient.</p>
     *
     * @param apiToken a {@link java.lang.String} object
     */
    public CocWebApiClient(String apiToken) {
        super(apiToken);
    }

    /** {@inheritDoc} */
    @Override
    protected CocWebApiResponse createWebApiResponse(Response response) {
        log.debug("Creating Response for : {}", response);
        return new CocWebApiResponse(response);
    }

    /** {@inheritDoc} */
    @Override
    protected void applyAuthenticationScheme(RequestBuilder requestBuilder, String authToken) {
        requestBuilder.addHeader("authorization", String.format("Bearer %s", authToken));
    }
}
