package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2WebApiRequest;

abstract public class Dota2FantasyRequest extends Dota2WebApiRequest {
    public Dota2FantasyRequest(String apiMethod, int apiVersion) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_FANTASY, apiMethod, apiVersion, null);
    }
}
