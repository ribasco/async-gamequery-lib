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

package com.ibasco.agql.protocols.valve.steam.webapi.adapters;

import com.google.gson.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppPcRequirements;

import java.lang.reflect.Type;

/**
 * Special handler for properties that can either be an object or array.
 *
 * @see <a href="https://github.com/ribasco/async-gamequery-lib/issues/12">Issue #12</a>
 */
public class StoreAppPcRequirementsDeserializer implements JsonDeserializer<StoreAppPcRequirements> {

    @Override
    public StoreAppPcRequirements deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StoreAppPcRequirements requirements = new StoreAppPcRequirements();

        //Fail fast and just return an empty value
        if (json.isJsonNull() || json.isJsonPrimitive() || (json.isJsonArray() && json.getAsJsonArray().size() == 0)) {
            return requirements;
        }

        JsonObject object = json.getAsJsonObject();
        if (object.has("minimum")) {
            requirements.setMinimum(object.get("minimum").getAsString());
        }
        if (object.has("recommended")) {
            requirements.setRecommended(object.get("recommended").getAsString());
        }
        return requirements;
    }
}
