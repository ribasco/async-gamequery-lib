/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ribasco.agql.protocols.valve.csgo.webapi.adapters;

import com.google.gson.*;
import org.ribasco.agql.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsgoDatacenterStatusDeserializer implements JsonDeserializer<List<CsgoDatacenterStatus>> {

    private static final Logger log = LoggerFactory.getLogger(CsgoDatacenterStatusDeserializer.class);

    @Override
    public List<CsgoDatacenterStatus> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        log.info("{}", json);
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
