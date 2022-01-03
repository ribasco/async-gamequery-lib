/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.supercell.coc.webapi.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ibasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocLeagueSeasonDeserializer implements JsonDeserializer<CocLeagueSeason> {

    private static final Logger log = LoggerFactory.getLogger(CocLeagueSeasonDeserializer.class);

    @Override
    public CocLeagueSeason deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CocLeagueSeason season = new CocLeagueSeason();
        season.setSeasonDate(json.getAsJsonObject().getAsJsonPrimitive("id").getAsString());
        return season;
    }
}
