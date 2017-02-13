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

import com.ibasco.agql.examples.base.BaseWebApiAuthExample;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.SteamEconItems;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsStoreMeta;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconPlayerItem;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SteamEconItemsQueryEx extends BaseWebApiAuthExample {
    private static final Logger log = LoggerFactory.getLogger(SteamEconItemsQueryEx.class);

    private SteamWebApiClient apiClient;

    public static void main(String[] args) throws Exception {
        SteamEconItemsQueryEx app = new SteamEconItemsQueryEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
        String authToken = getToken("steam");
        apiClient = new SteamWebApiClient(authToken);

        SteamEconItems econItems = new SteamEconItems(apiClient);
        List<SteamEconPlayerItem> playerItems = econItems.getPlayerItems(730, 76561197960761020L, SteamEconItems.VERSION_1).get();
        log.info("Player Items");
        playerItems.forEach(SteamEconItemsQueryEx::displayResult);

        //Display CS-GO Schema
        SteamEconSchema schema = econItems.getSchema(730, SteamEconItems.VERSION_2).get();
        log.info("Schema: {}", schema);

        log.info(" - Items");
        schema.getItems().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Attributes:");
        schema.getAttributes().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Attribute Controlled Attached Particles:");
        schema.getAttributeControlledAttachedParticles().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Item Levels");
        schema.getItemLevels().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Item Sets");
        schema.getItemSets().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Kill Eater Score Types");
        schema.getKillEaterScoreTypes().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Kill Eater Ranks");
        schema.getKillEaterRanks().forEach(SteamEconItemsQueryEx::displayResult);
        log.info(" - Origin Names");
        schema.getOriginNames().forEach(SteamEconItemsQueryEx::displayResult);

        String schemaUrl = econItems.getSchemaUrl(440, SteamEconItems.VERSION_1).get();
        log.info("Schema URL = {}", schemaUrl);

        SteamEconItemsStoreMeta storeMetadata = econItems.getStoreMetadata(730, SteamEconItems.VERSION_1).get();
        log.info("Store Meta Data: {}", storeMetadata);

        Integer status = econItems.getStoreStatus(440, SteamEconItems.VERSION_1).get();
        log.info("Store status = {}", status);
    }

    public static void displayResult(Object item) {
        log.info("{} = {}", item.getClass().getSimpleName(), item.toString());
    }

    @Override
    public void close() throws IOException {
        if (apiClient != null) {
            apiClient.close();
        }
    }
}
