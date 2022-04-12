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

package com.ibasco.agql.protocols.valve.steam.webapi.adapters;

import com.google.gson.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamAssetClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamAssetClassInfoMapDeserializer implements JsonDeserializer<Map<String, SteamAssetClassInfo>> {
    private static final Logger log = LoggerFactory.getLogger(SteamAssetClassInfoMapDeserializer.class);

    /** {@inheritDoc} */
    @Override
    public Map<String, SteamAssetClassInfo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, SteamAssetClassInfo> map = new HashMap<>();
        JsonObject jObject = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            //Only process entries that are of type object
            if (value.isJsonObject()) {
                map.put(key, context.deserialize(value, SteamAssetClassInfo.class));
            }
        }
        log.debug("Finished populating map. Total Map Size: {}", map.size());
        return map;
    }
}
