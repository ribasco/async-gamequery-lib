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

package com.ibasco.agql.protocols.supercell.coc.webapi;

import com.google.gson.reflect.TypeToken;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeague;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;
import org.jetbrains.annotations.ApiStatus;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>CocTypes class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocTypes {

    /** Constant <code>COC_LIST_LEAGUE</code> */
    public static final Type COC_LIST_LEAGUE = new TypeToken<List<CocLeague>>() {
    }.getType();

    /** Constant <code>COC_LIST_PLAYER_RANK_INFO</code> */
    public static final Type COC_LIST_PLAYER_RANK_INFO = new TypeToken<List<CocPlayerRankInfo>>() {
    }.getType();
}
