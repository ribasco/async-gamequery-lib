package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.adapters;

import com.google.gson.*;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.pojos.CsgoDatacenterStatus;
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
