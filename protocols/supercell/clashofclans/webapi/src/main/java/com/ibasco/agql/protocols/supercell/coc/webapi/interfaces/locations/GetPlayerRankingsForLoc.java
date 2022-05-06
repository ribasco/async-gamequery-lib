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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.locations;

import com.ibasco.agql.protocols.supercell.coc.webapi.CocApiConstants;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiRequest;
import org.jetbrains.annotations.ApiStatus;

/**
 * <p>GetPlayerRankingsForLoc class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class GetPlayerRankingsForLoc extends CocWebApiRequest {

    /**
     * <p>Constructor for GetPlayerRankingsForLoc.</p>
     *
     * @param apiVersion
     *         a int
     * @param locationId
     *         a int
     */
    public GetPlayerRankingsForLoc(int apiVersion, int locationId) {
        this(apiVersion, locationId, -1, -1, -1);
    }

    /**
     * <p>Constructor for GetPlayerRankingsForLoc.</p>
     *
     * @param apiVersion
     *         a int
     * @param locationId
     *         a int
     * @param limit
     *         a int
     * @param before
     *         a int
     * @param after
     *         a int
     */
    public GetPlayerRankingsForLoc(int apiVersion, int locationId, int limit, int before, int after) {
        super(apiVersion, CocApiConstants.UF_COC_LOCATION_PLAYER_RANK, limit, before, after);
        property(CocApiConstants.UF_PROP_LOCATION_ID, locationId);
    }

    /**
     * <p>Constructor for GetPlayerRankingsForLoc.</p>
     *
     * @param apiVersion
     *         a int
     * @param locationId
     *         a int
     * @param limit
     *         a int
     */
    public GetPlayerRankingsForLoc(int apiVersion, int locationId, int limit) {
        this(apiVersion, locationId, limit, -1, -1);
    }
}
