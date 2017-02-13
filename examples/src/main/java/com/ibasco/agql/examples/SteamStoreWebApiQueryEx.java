/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.examples;

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamStorefront;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SteamStoreWebApiQueryEx extends BaseExample {
    private static final Logger log = LoggerFactory.getLogger(SteamStoreWebApiQueryEx.class);

    private SteamWebApiClient apiClient;

    public static void main(String[] args) throws Exception {
        SteamStoreWebApiQueryEx app = new SteamStoreWebApiQueryEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
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
