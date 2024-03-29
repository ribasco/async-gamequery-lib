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
import java.util.Collection;

public class GetLocalizedNameForTags extends SteamStoreServiceRequest {

    /**
     * <p>Constructor for SteamWebApiRequest.</p>
     *
     * @param apiVersion
     *         a int
     */
    public GetLocalizedNameForTags(int apiVersion, String language, Collection<Integer> tagIds) {
        super("GetLocalizedNameForTags", apiVersion);
        urlParam("language", language);
        if (tagIds != null && !tagIds.isEmpty())
            urlParam("tagids", tagIds);
    }
}
