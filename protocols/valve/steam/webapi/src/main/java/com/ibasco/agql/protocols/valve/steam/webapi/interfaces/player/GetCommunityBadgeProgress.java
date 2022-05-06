/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.player;

import com.ibasco.agql.protocols.valve.steam.webapi.requests.SteamPlayerServiceRequest;

/**
 * Gets all the quests needed to get the specified badge, and which are completed
 *
 * @author Rafael Luis Ibasco
 */
public class GetCommunityBadgeProgress extends SteamPlayerServiceRequest {

    /**
     * <p>Constructor for GetCommunityBadgeProgress.</p>
     *
     * @param apiVersion
     *         a int
     * @param steamId
     *         a long
     * @param badgeId
     *         a int
     */
    public GetCommunityBadgeProgress(int apiVersion, long steamId, int badgeId) {
        super("GetCommunityBadgeProgress", apiVersion);
        urlParam("steamid", steamId);
        urlParam("badgeid", badgeId);
    }
}
