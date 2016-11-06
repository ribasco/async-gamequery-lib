package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.interfaces.SteamEconItems;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamEconItemsStoreMeta;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamEconPlayerItem;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SteamEconItemsQuery {
    private static final Logger log = LoggerFactory.getLogger(SteamEconItemsQuery.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        String authToken = "903BC0B13739EF74242523BC3013F076";
        SteamWebApiClient apiClient = new SteamWebApiClient(authToken);
        try {
            SteamEconItems econItems = new SteamEconItems(apiClient);
            List<SteamEconPlayerItem> playerItems = econItems.getPlayerItems(730, 76561197960761020L, SteamEconItems.VERSION_1).get();
            log.info("Player Items");
            playerItems.forEach(SteamEconItemsQuery::displayResult);

            //Display CS-GO Schema
            SteamEconSchema schema = econItems.getSchema(730, SteamEconItems.VERSION_2).get();
            log.info("Schema: {}", schema);

            log.info(" - Items");
            schema.getItems().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Attributes:");
            schema.getAttributes().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Attribute Controlled Attached Particles:");
            schema.getAttributeControlledAttachedParticles().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Item Levels");
            schema.getItemLevels().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Item Sets");
            schema.getItemSets().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Kill Eater Score Types");
            schema.getKillEaterScoreTypes().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Kill Eater Ranks");
            schema.getKillEaterRanks().forEach(SteamEconItemsQuery::displayResult);
            log.info(" - Origin Names");
            schema.getOriginNames().forEach(SteamEconItemsQuery::displayResult);

            String schemaUrl = econItems.getSchemaUrl(440, SteamEconItems.VERSION_1).get();
            log.info("Schema URL = {}", schemaUrl);

            SteamEconItemsStoreMeta storeMetadata = econItems.getStoreMetadata(730, SteamEconItems.VERSION_1).get();
            log.info("Store Meta Data: {}", storeMetadata);

            Integer status = econItems.getStoreStatus(440, SteamEconItems.VERSION_1).get();
            log.info("Store status = {}", status);
        } finally {
            apiClient.close();
        }
    }

    public static void displayResult(Object item) {
        log.info("{} = {}", item.getClass().getSimpleName(), item.toString());
    }
}
