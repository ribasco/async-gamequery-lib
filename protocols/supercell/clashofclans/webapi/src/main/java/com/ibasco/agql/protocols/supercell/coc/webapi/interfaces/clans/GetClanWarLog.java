/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

public class GetClanWarLog extends CocWebApiRequest {
    public GetClanWarLog(int apiVersion, String clanTag) {
        this(apiVersion, clanTag, -1, -1, -1);
    }

    public GetClanWarLog(int apiVersion, String clanTag, int limit, int after, int before) {
        super(apiVersion, CocApiConstants.UF_COC_CLAN_WARLOG, limit, after, before);
        property(CocApiConstants.UF_PROP_CLANTAG, encode(clanTag));
    }
}
