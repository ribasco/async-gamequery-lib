package org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi;

import com.google.gson.reflect.TypeToken;
import org.ribasco.asyncgamequerylib.core.KeyValuePair;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos.CocLeague;
import org.ribasco.asyncgamequerylib.protocols.supercell.coc.webapi.pojos.CocPlayerRankInfo;

import java.lang.reflect.Type;
import java.util.List;

public class CocTypes {
    public static final Type COC_LIST_LEAGUE = new TypeToken<List<CocLeague>>() {
    }.getType();
    public static final Type COC_LIST_KEYVALUE_STRING = new TypeToken<List<KeyValuePair<String, String>>>() {
    }.getType();
    public static final Type COC_LIST_PLAYER_RANK_INFO = new TypeToken<List<CocPlayerRankInfo>>() {
    }.getType();
}
