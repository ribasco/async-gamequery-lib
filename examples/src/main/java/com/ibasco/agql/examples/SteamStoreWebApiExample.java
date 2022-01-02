/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.examples;

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamStorefront;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SteamStoreWebApiExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SteamStoreWebApiExample.class);

    private SteamWebApiClient apiClient;

    public static void main(String[] args) throws Exception {
        new SteamStoreWebApiExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        apiClient = new SteamWebApiClient();
        //Create the steam store interface
        SteamStorefront steamStore = new SteamStorefront(apiClient);
        StoreAppDetails appDetails = steamStore.getAppDetails(550).get();
        log.info("App Details : {}", appDetails);
        StoreFeaturedApps featuredApps = steamStore.getFeaturedApps().get();
        log.info("Featured Apps: {}", featuredApps);
        StoreFeaturedCategories featuredCategories = steamStore.getFeaturedCategories().get();
        log.info("Featured Categories: {}", featuredCategories);
        StorePackageDetails packageDetails = steamStore.getPackageDetails(54029).get();
        log.info("Store Package Details: {}", packageDetails);
        StoreSaleDetails saleDetails = steamStore.getSaleDetails(34241).get();
        log.info("Sale Details: {}", saleDetails);
    }

    @Override
    public void close() throws IOException {
        if (apiClient != null)
            apiClient.close();
    }
}
