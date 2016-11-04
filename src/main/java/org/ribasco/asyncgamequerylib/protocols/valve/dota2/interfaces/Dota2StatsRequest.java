package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2WebApiRequest;

abstract public class Dota2StatsRequest extends Dota2WebApiRequest {
    public Dota2StatsRequest(String apiMethod, int apiVersion, String language) {
        super(Dota2ApiConstants.DOTA2_INTERFACE_MATCHSTATS, apiMethod, apiVersion, language);
    }
}
