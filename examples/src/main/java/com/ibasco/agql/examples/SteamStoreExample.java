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

package com.ibasco.agql.examples;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamStoreService;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.LocalizedNameTag;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.PopularTag;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.steamstore.pojos.SteamStoreAppResponse;

import java.util.Arrays;
import java.util.List;

public class SteamStoreExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("D76CC40EC8877CE3F63C48D3BC572236")) {
            SteamStoreService service = new SteamStoreService(client);
            SteamStoreAppResponse response = service.getAppList(10).join();
            response.getApps().forEach(app -> System.out.printf("\t[Page: 1] App: %s%n", app.getName()));
            response = service.getAppList(response.getLastAppid(), 10).join();
            response.getApps().forEach(app -> System.out.printf("\t[Page: 2] App: %s%n", app.getName()));

            List<LocalizedNameTag> localizedTags = service.getLocalizedNameForTags("russian", Arrays.asList(493, 113)).join();
            localizedTags.forEach(tag -> System.out.printf("\tEnglish Name: %s, Normalized Name: %s, Localized Name: %s%n", tag.getEnglishName(), tag.getNormalizedName(), tag.getName()));

            List<PopularTag> popularTags = service.getPopularTag("english").join();
            popularTags.forEach(tag -> System.out.printf("\tId: %d, Name: %s%n", tag.getTagId(), tag.getName()));
        }
    }
}
