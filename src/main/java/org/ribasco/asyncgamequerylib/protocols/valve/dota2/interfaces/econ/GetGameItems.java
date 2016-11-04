package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces.Dota2EconRequest;

public class GetGameItems extends Dota2EconRequest {
    public GetGameItems(int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETGAMEITEMS, apiVersion, language);
    }
}
