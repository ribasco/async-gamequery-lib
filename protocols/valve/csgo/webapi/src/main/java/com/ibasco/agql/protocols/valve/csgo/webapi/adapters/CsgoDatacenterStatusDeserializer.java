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

package com.ibasco.agql.protocols.valve.csgo.webapi.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ibasco.agql.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>CsgoDatacenterStatusDeserializer class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoDatacenterStatusDeserializer implements JsonDeserializer<List<CsgoDatacenterStatus>> {

    private static final Logger log = LoggerFactory.getLogger(CsgoDatacenterStatusDeserializer.class);

    /** {@inheritDoc} */
    @Override
    public List<CsgoDatacenterStatus> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        log.debug("{}", json);
        JsonObject dataCenters = json.getAsJsonObject();
        List<CsgoDatacenterStatus> datacenterStatusList = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : dataCenters.entrySet()) {
            String location = entry.getKey();
            CsgoDatacenterStatus status = context.deserialize(entry.getValue(), CsgoDatacenterStatus.class);
            status.setLocation(location);
            datacenterStatusList.add(status);
        }
        return datacenterStatusList;
    }
}
