package org.ribasco.agql.protocols.valve.dota2.interfaces;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.Dota2WebApiRequest;

abstract public class Dota2MatchRequest extends Dota2WebApiRequest {
    public Dota2MatchRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_MATCH, apiMethod, apiVersion, language);
    }
}
