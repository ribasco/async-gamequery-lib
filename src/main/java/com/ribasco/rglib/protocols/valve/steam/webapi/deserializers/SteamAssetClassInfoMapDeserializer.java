/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package com.ribasco.rglib.protocols.valve.steam.webapi.deserializers;

import com.google.gson.*;
import com.ribasco.rglib.protocols.valve.steam.webapi.pojos.SteamAssetClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamAssetClassInfoMapDeserializer implements JsonDeserializer<Map<String, SteamAssetClassInfo>> {
    private static final Logger log = LoggerFactory.getLogger(SteamAssetClassInfoMapDeserializer.class);

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
        log.info("Finished populating map. Total Map Size: {}", map.size());
        return map;
    }
}
