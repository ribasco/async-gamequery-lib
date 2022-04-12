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

package com.ibasco.agql.protocols.valve.dota2.webapi.requests;

import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2WebApiRequest;

/**
 * <p>Abstract Dota2MatchRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class Dota2MatchRequest extends Dota2WebApiRequest {
    /**
     * <p>Constructor for Dota2MatchRequest.</p>
     *
     * @param apiMethod a {@link java.lang.String} object
     * @param apiVersion a int
     * @param language a {@link java.lang.String} object
     */
    public Dota2MatchRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_MATCH, apiMethod, apiVersion, language);
    }
}
