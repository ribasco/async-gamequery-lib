package com.ibasco.agql.protocols.valve.steam.webapi.requests;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamApiConstants;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiRequest;

public class SteamCommunityServiceRequest extends SteamWebApiRequest {

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiMethod
     *         a {@link String} object
     * @param apiVersion
     *         a int
     */
    public SteamCommunityServiceRequest(String apiMethod, int apiVersion) {
        super(SteamApiConstants.STEAM_COMMUNITY_SERVICE, apiMethod, apiVersion);
    }
}
