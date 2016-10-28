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

package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.deserializers;

import com.google.gson.*;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos.SteamAssetDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamAssetDescDeserializer implements JsonDeserializer<SteamAssetDescription> {
    private static final Logger log = LoggerFactory.getLogger(SteamAssetDescDeserializer.class);

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
