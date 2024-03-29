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

package com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.match;

import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistoryCriteria;
import com.ibasco.agql.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

/**
 * <p>GetMatchHistory class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class GetMatchHistory extends Dota2MatchRequest {

    /**
     * <p>Constructor for GetMatchHistory.</p>
     *
     * @param apiVersion
     *         a int
     * @param criteria
     *         a {@link com.ibasco.agql.protocols.valve.dota2.webapi.pojos.Dota2MatchHistoryCriteria} object
     */
    public GetMatchHistory(int apiVersion, Dota2MatchHistoryCriteria criteria) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHHISTORY, apiVersion, null);
        if (criteria != null)
            urlParam(criteria);
    }
}
