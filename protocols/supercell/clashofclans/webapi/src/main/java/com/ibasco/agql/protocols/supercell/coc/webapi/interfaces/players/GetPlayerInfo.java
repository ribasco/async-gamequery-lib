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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.players;

import com.ibasco.agql.protocols.supercell.coc.webapi.CocApiConstants;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiRequest;
import org.jetbrains.annotations.ApiStatus;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class GetPlayerInfo extends CocWebApiRequest {

    /**
     * <p>Constructor for GetPlayerInfo.</p>
     *
     * @param apiVersion
     *         a int
     * @param playerTag
     *         a {@link java.lang.String} object
     */
    public GetPlayerInfo(int apiVersion, String playerTag) {
        super(apiVersion, CocApiConstants.UF_COC_PLAYER_INFO);
        property(CocApiConstants.UF_PROP_PLAYERTAG, encode(playerTag));
    }
}
