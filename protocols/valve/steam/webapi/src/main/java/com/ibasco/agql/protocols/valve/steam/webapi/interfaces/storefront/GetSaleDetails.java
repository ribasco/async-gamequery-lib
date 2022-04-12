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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.storefront;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamStoreApiRequest;

/**
 * <p>GetSaleDetails class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetSaleDetails extends SteamStoreApiRequest {
    /**
     * <p>Constructor for GetSaleDetails.</p>
     *
     * @param apiVersion a int
     * @param saleId a int
     * @param countryCode a {@link java.lang.String} object
     * @param language a {@link java.lang.String} object
     */
    public GetSaleDetails(int apiVersion, int saleId, String countryCode, String language) {
        super(apiVersion, SteamApiConstants.SF_METHOD_SALE_DETAILS, countryCode, language);
        urlParam("id", saleId);
    }
}
