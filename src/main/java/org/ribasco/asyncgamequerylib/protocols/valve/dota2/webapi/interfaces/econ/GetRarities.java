package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2EconRequest;

public class GetRarities extends Dota2EconRequest {
    public GetRarities(int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETRARITIES, apiVersion, language);
    }
}
