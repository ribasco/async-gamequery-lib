package org.ribasco.agql.protocols.valve.dota2.webapi.requests;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2WebApiRequest;

abstract public class Dota2EconRequest extends Dota2WebApiRequest {
    public Dota2EconRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_ECON, apiMethod, apiVersion, language);
    }
}
