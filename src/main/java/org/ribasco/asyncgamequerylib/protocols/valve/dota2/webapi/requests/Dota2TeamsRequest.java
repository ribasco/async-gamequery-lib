package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2WebApiRequest;

abstract public class Dota2TeamsRequest extends Dota2WebApiRequest {
    public Dota2TeamsRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_TEAMS, apiMethod, apiVersion, language);
    }
}
