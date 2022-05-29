package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.communityservice;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamCommunityServiceRequest;
import java.util.Collection;

public class GetApps extends SteamCommunityServiceRequest {

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     */
    public GetApps(int apiVersion, Collection<Integer> appIds) {
        super("GetApps", apiVersion);
        urlParam("appids", appIds);
    }
}
