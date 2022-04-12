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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.econ;

import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import com.ibasco.agql.protocols.valve.dota2.webapi.requests.Dota2EconRequest;

/**
 * <p>GetRarities class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetRarities extends Dota2EconRequest {
    /**
     * <p>Constructor for GetRarities.</p>
     *
     * @param apiVersion a int
     * @param language a {@link java.lang.String} object
     */
    public GetRarities(int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETRARITIES, apiVersion, language);
    }
}
