package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2WebApiRequest;

abstract public class Dota2FantasyRequest extends Dota2WebApiRequest {
    public Dota2FantasyRequest(String apiMethod, int apiVersion) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_FANTASY, apiMethod, apiVersion, null);
    }
}
