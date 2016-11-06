package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.ribasco.agql.protocols.valve.steam.webapi.interfaces.SteamStorefront;
import org.ribasco.agql.protocols.valve.steam.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SteamStoreWebApiQuery {
    private static final Logger log = LoggerFactory.getLogger(SteamStoreWebApiQuery.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        SteamWebApiClient apiClient = new SteamWebApiClient();
        try {
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
        } finally {
            apiClient.close();
        }
    }
}
