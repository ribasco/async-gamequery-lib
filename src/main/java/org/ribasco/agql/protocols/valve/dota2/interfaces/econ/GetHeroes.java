package org.ribasco.agql.protocols.valve.dota2.interfaces.econ;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.interfaces.Dota2EconRequest;

public class GetHeroes extends Dota2EconRequest {
    public GetHeroes(int apiVersion, boolean itemizedOnly, String language) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETHEROES, apiVersion, language);
        urlParam("itemizedonly", itemizedOnly);
    }
}
