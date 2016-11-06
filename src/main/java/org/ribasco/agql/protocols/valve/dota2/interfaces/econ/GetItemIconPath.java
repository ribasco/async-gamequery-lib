package org.ribasco.agql.protocols.valve.dota2.interfaces.econ;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.interfaces.Dota2EconRequest;

public class GetItemIconPath extends Dota2EconRequest {
    public GetItemIconPath(int apiVersion, String iconName, int iconType) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETITEMICONPATH, apiVersion, null);
        urlParam("iconname", iconName);
        urlParam("icontype", iconType);
    }
}
