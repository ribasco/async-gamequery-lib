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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamAssetDescription;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamAssetDescDeserializer implements JsonDeserializer<SteamAssetDescription> {

    private static final Logger log = LoggerFactory.getLogger(SteamAssetDescDeserializer.class);

    /** {@inheritDoc} */
    @Override
    public SteamAssetDescription deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SteamAssetDescription desc = new SteamAssetDescription();
        JsonObject jsonObj = json.getAsJsonObject();
        String type = jsonObj.getAsJsonPrimitive("type").getAsString();
        String value = jsonObj.getAsJsonPrimitive("value").getAsString();
        Map<String, String> mAppData = new HashMap<>();
        JsonElement appData = jsonObj.get("app_data");
        if (appData.isJsonObject()) {
            desc.setAppData(context.deserialize(appData, HashMap.class));
        } else {
            desc.setAppData(mAppData);
        }
        desc.setType(type);
        desc.setValue(value);
        return desc;
    }
}
