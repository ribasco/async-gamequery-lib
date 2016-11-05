package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.econ;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2EconRequest;

public class GetItemIconPath extends Dota2EconRequest {
    public GetItemIconPath(int apiVersion, String iconName, int iconType) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETITEMICONPATH, apiVersion, null);
        urlParam("iconname", iconName);
        urlParam("icontype", iconType);
    }
}
