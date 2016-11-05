package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2EconRequest;

public class GetRarities extends Dota2EconRequest {
    public GetRarities(int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETRARITIES, apiVersion, language);
    }
}
