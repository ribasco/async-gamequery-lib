package org.ribasco.agql.protocols.supercell.coc.webapi.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.ribasco.agql.protocols.supercell.coc.webapi.pojos.CocLeagueSeason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class CocLeagueSeasonDeserializer implements JsonDeserializer<CocLeagueSeason> {
    private static final Logger log = LoggerFactory.getLogger(CocLeagueSeasonDeserializer.class);

    @Override
    public CocLeagueSeason deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CocLeagueSeason season = new CocLeagueSeason();
        season.setSeasonDate(json.getAsJsonObject().getAsJsonPrimitive("id").getAsString());
        return season;
    }
}
