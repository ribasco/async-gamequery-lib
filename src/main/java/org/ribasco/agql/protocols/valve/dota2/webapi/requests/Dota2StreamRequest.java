package org.ribasco.agql.protocols.valve.dota2.webapi.requests;

import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.webapi.Dota2WebApiRequest;

abstract public class Dota2StreamRequest extends Dota2WebApiRequest {
    public Dota2StreamRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_STREAMSYS, apiMethod, apiVersion, language);
    }
}
