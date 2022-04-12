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

/**
 * <p>SteamEconItemsExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsExample extends BaseWebApiAuthExample {

    private static final Logger log = LoggerFactory.getLogger(SteamEconItemsExample.class);

    private SteamWebApiClient client;

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        new SteamEconItemsExample().run(args);
    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        String authToken = getToken("steam");
        client = new SteamWebApiClient(authToken);

        SteamEconItems econItems = new SteamEconItems(client);
        List<SteamEconPlayerItem> playerItems = econItems.getPlayerItems(730, 76561197960761020L, SteamEconItems.VERSION_1).get();
        log.info("Player Items");
        playerItems.forEach(SteamEconItemsExample::displayResult);

        //Display CS-GO Schema
        SteamEconSchema schema = econItems.getSchema(730, SteamEconItems.VERSION_2).get();
        log.info("Schema: {}", schema);

        log.info(" - Items");
        schema.getItems().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Attributes:");
        schema.getAttributes().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Attribute Controlled Attached Particles:");
        schema.getAttributeControlledAttachedParticles().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Item Levels");
        schema.getItemLevels().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Item Sets");
        schema.getItemSets().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Kill Eater Score Types");
        schema.getKillEaterScoreTypes().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Kill Eater Ranks");
        schema.getKillEaterRanks().forEach(SteamEconItemsExample::displayResult);
        log.info(" - Origin Names");
        schema.getOriginNames().forEach(SteamEconItemsExample::displayResult);

        String schemaUrl = econItems.getSchemaUrl(440, SteamEconItems.VERSION_1).get();
        log.info("Schema URL = {}", schemaUrl);

        SteamEconItemsStoreMeta storeMetadata = econItems.getStoreMetadata(730, SteamEconItems.VERSION_1).get();
        log.info("Store Meta Data: {}", storeMetadata);

        Integer status = econItems.getStoreStatus(440, SteamEconItems.VERSION_1).get();
        log.info("Store status = {}", status);
    }

    /**
     * <p>displayResult.</p>
     *
     * @param item a {@link java.lang.Object} object
     */
    public static void displayResult(Object item) {
        log.info("{} = {}", item.getClass().getSimpleName(), item);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
