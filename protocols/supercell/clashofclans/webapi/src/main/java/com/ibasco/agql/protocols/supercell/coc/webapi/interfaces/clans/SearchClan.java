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
import com.ibasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiRequest;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * <p>SearchClan class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class SearchClan extends CocWebApiRequest {

    /**
     * <p>Constructor for SearchClan.</p>
     *
     * @param apiVersion a int
     * @param criteria a {@link com.ibasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria} object
     */
    public SearchClan(int apiVersion, CocSearchCriteria criteria) {
        super(apiVersion, CocApiConstants.UF_COC_CLAN_SEARCH);
        for (Map.Entry<String, Object> searchCriteria : criteria.getCriteriaSet()) {
            urlParam(searchCriteria.getKey(), searchCriteria.getValue());
        }
    }
}
