package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2WebApiRequest;

abstract public class Dota2EconRequest extends Dota2WebApiRequest {
    public Dota2EconRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_ECON, apiMethod, apiVersion, language);
    }
}
