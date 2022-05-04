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

package com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore;

import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.requests.SteamStoreServiceRequest;

public class GetAppList extends SteamStoreServiceRequest {

    public GetAppList(int apiVersion) {
        super("GetAppList", apiVersion);
    }

    public GetAppList(int apiVersion, int maxResults) {
        super("GetAppList", apiVersion);
        urlParam("max_results", maxResults);
    }

    public GetAppList(int apiVersion, int lastAppId, int maxResults) {
        super("GetAppList", apiVersion);
        urlParam("last_appid", lastAppId);
        urlParam("max_results", maxResults);
    }

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     */
    public GetAppList(int apiVersion, int ifModifiedSince, String haveDescriptionLanguage, boolean includeGames, boolean includeDlc, boolean includeSoftware, boolean includeVideos, boolean includeHardware, int lastAppId, int maxResults) {
        super("GetAppList", apiVersion);
        urlParam("if_modified_since", ifModifiedSince);
        urlParam("have_description_language", haveDescriptionLanguage);
        urlParam("include_games", includeGames);
        urlParam("include_dlc", includeDlc);
        urlParam("include_software", includeSoftware);
        urlParam("include_videos", includeVideos);
        urlParam("include_hardware", includeHardware);
        urlParam("last_appid", lastAppId);
        urlParam("max_results", maxResults);
    }
}
