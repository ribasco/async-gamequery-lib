package org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.interfaces.servers;

import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.CsgoApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.csgo.webapi.interfaces.CsgoServersRequest;

public class GetGameServersStatus extends CsgoServersRequest {
    public GetGameServersStatus(int apiVersion) {
        super(CsgoApiConstants.CSGO_METHOD_GETGAMESERVERSTATUS, apiVersion);
    }
}
