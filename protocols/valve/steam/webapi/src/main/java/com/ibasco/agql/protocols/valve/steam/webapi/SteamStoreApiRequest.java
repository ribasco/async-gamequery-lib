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

package com.ibasco.agql.protocols.valve.steam.webapi;

/**
 * <p>Abstract SteamStoreApiRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SteamStoreApiRequest extends SteamWebApiRequest {

    /**
     * <p>Constructor for SteamStoreApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     * @param urlFormat
     *         a {@link java.lang.String} object
     */
    public SteamStoreApiRequest(int apiVersion, String urlFormat) {
        this(apiVersion, urlFormat, "us", "english");
    }

    /**
     * <p>Constructor for SteamStoreApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     * @param urlFormat
     *         a {@link java.lang.String} object
     * @param countryCode
     *         a {@link java.lang.String} object
     * @param language
     *         a {@link java.lang.String} object
     */
    public SteamStoreApiRequest(int apiVersion, String urlFormat, String countryCode, String language) {
        super(null, null, apiVersion);
        //Override super constructor property
        baseUrlFormat(urlFormat);
        property(SteamApiConstants.SF_PROP_BASEURL, SteamApiConstants.SF_BASE_URL_FORMAT);
        urlParam("c", countryCode);
        urlParam("l", language);
    }
}
