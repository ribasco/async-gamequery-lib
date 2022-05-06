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

package com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.clans;

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
public class GetClanMembers extends CocWebApiRequest {

    /**
     * <p>Constructor for GetClanMembers.</p>
     *
     * @param apiVersion
     *         a int
     * @param clanTag
     *         a {@link java.lang.String} object
     * @param limit
     *         a int
     * @param after
     *         a int
     * @param before
     *         a int
     */
    public GetClanMembers(int apiVersion, String clanTag, int limit, int after, int before) {
        super(apiVersion, CocApiConstants.UF_COC_CLAN_MEMBERS);
        property(CocApiConstants.UF_PROP_CLANTAG, encode(clanTag));
        limit(limit);
        before(before);
        after(after);
    }
}
