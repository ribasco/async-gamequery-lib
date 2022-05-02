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

import com.ibasco.agql.core.util.Console;
import com.ibasco.agql.examples.base.BaseWebApiAuthExample;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import com.ibasco.agql.protocols.valve.steam.webapi.enums.VanityUrlType;
import com.ibasco.agql.protocols.valve.steam.webapi.interfaces.*;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * <p>SteamWebApiExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("all")
public class SteamWebApiExample extends BaseWebApiAuthExample {

    private static final Logger log = LoggerFactory.getLogger(SteamWebApiExample.class);

    private SteamWebApiClient client;

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        new SteamWebApiExample().run(args);
    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        String authToken = getToken("steam");
        client = new SteamWebApiClient(authToken);

        SteamApps steamApps = new SteamApps(client);
        SteamNews steamNews = new SteamNews(client);
        SteamUserStats steamUserStats = new SteamUserStats(client);
        SteamPlayerService playerService = new SteamPlayerService(client);
        SteamUser steamUser = new SteamUser(client);
        SteamEconomy steamEconomy = new SteamEconomy(client);
        SteamStorefront storeFront = new SteamStorefront(client);
        SteamEconItems steamEconItems = new SteamEconItems(client);
        GameServersService gameServersService = new GameServersService(client);

        steamApps.getAppList().exceptionally(throwable -> {
            log.error("Error Occured", throwable);
            return new ArrayList<>();
        }).thenAccept(SteamWebApiExample::displayResult).join();

        steamApps.getServersAtAddress(InetAddress.getByName("103.28.55.237"))
                 .exceptionally(throwable -> {
                     log.error("Error occured", throwable);
                     return new ArrayList<>();
                 })
                 .thenAccept(steamServers -> {
                     steamServers.parallelStream().forEachOrdered(steamServer -> Console.println("Server: %s", steamServer.getAddr()));
                 }).join();

        steamApps.getServerUpdateStatus(2146, 550).thenAccept(new Consumer<ServerUpdateStatus>() {
            @Override
            public void accept(ServerUpdateStatus serverUpdateStatus) {
                Console.println("Success: %s, Message: %s, Required Version: %d", serverUpdateStatus.isSuccess(), serverUpdateStatus.getMessage(), serverUpdateStatus.getRequiredVersion());
            }
        }).join();

        //Steam News
        steamNews.getNewsForApp(550).thenAccept(new Consumer<List<SteamNewsItem>>() {
            @Override
            public void accept(List<SteamNewsItem> steamNewsItems) {
                steamNewsItems.forEach(new Consumer<SteamNewsItem>() {
                    @Override
                    public void accept(SteamNewsItem steamNewsItem) {
                        log.info("News Item: {}", steamNewsItem.getTitle());
                    }
                });
            }
        }).join();

        //Steam User Stats
        steamUserStats.getGlobalAchievementPercentagesForApp(550).thenAccept(new Consumer<List<SteamGameAchievement>>() {
            @Override
            public void accept(List<SteamGameAchievement> steamGameAchievements) {
                steamGameAchievements.forEach(new Consumer<SteamGameAchievement>() {
                    @Override
                    public void accept(SteamGameAchievement steamGameAchievement) {
                        log.info("Achievement: {}, {}", steamGameAchievement.getName(), steamGameAchievement.getPercentage());
                    }
                });
            }
        }).join();
        steamUserStats.getSchemaForGame(550).thenAccept(steamGameStatsSchemaInfo -> {
            log.info("Achievement Schemas : {}", steamGameStatsSchemaInfo.getAchievementSchemaList().size());
            log.info("Stats Schemas : {}", steamGameStatsSchemaInfo.getStatsSchemaList().size());
            steamGameStatsSchemaInfo.getAchievementSchemaList().forEach(new Consumer<SteamGameAchievementSchema>() {
                @Override
                public void accept(SteamGameAchievementSchema steamGameAchievementSchema) {
                    log.info("ach - {}", steamGameAchievementSchema.getName());
                }
            });
            steamGameStatsSchemaInfo.getStatsSchemaList().forEach(new Consumer<SteamGameStatsSchema>() {
                @Override
                public void accept(SteamGameStatsSchema steamGameStatsSchema) {
                    log.info("stats - {}", steamGameStatsSchema.getName());
                }
            });
        }).join();
        steamUserStats.getNumberOfCurrentPlayers(550).thenAccept(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                log.info("Total Players : {}", integer);
            }
        }).join();
        steamUserStats.getPlayerAchievements(76561198010872093L, 550).thenAccept(new Consumer<List<SteamPlayerAchievement>>() {
            @Override
            public void accept(List<SteamPlayerAchievement> steamPlayerAchievements) {
                log.info("Player Achievements: {}", steamPlayerAchievements.size());
                steamPlayerAchievements.forEach(new Consumer<SteamPlayerAchievement>() {
                    @Override
                    public void accept(SteamPlayerAchievement steamPlayerAchievement) {
                        log.info("- {} = {}", steamPlayerAchievement.getName(), steamPlayerAchievement.getAchieved());
                    }
                });
            }
        }).join();
        //steamUserStats.getGlobalStatsForGame(730, 1, "'global.map.emp_isle'")
        steamUserStats.getUserStatsForGame(76561198010872093L, 550).thenAccept(new Consumer<SteamPlayerStats>() {
            @Override
            public void accept(SteamPlayerStats playerStats) {
                log.info("Player Stats:");
                log.info("- Achievements: {}", playerStats.getAchievements().size());
                log.info("- Stats: {}", playerStats.getStats().size());
            }
        }).join();

        //Player Service
        playerService.getRecentlyPlayedGames(76561198010872093L, 500).thenAccept(new Consumer<List<SteamPlayerRecentPlayed>>() {
            @Override
            public void accept(List<SteamPlayerRecentPlayed> steamPlayerRecentPlayeds) {
                steamPlayerRecentPlayeds.forEach(new Consumer<SteamPlayerRecentPlayed>() {
                    @Override
                    public void accept(SteamPlayerRecentPlayed steamPlayerRecentPlayed) {
                        log.info("Recently Played: {} = {}", steamPlayerRecentPlayed.getName(), steamPlayerRecentPlayed.getTotalPlaytime());
                    }
                });
            }
        }).join();

        playerService.getBadges(76561198010872093L).thenAccept(new Consumer<SteamPlayerBadgeInfo>() {
            @Override
            public void accept(SteamPlayerBadgeInfo steamPlayerBadgeInfo) {
                log.info("Total Player Badges: {}", steamPlayerBadgeInfo.getPlayerBadges().size());
                steamPlayerBadgeInfo.getPlayerBadges().forEach(new Consumer<SteamPlayerBadge>() {
                    @Override
                    public void accept(SteamPlayerBadge steamPlayerBadge) {
                        log.info("- {}", steamPlayerBadge.getBadgeId());
                    }
                });
            }
        }).join();

        playerService.getCommunityBadgeProgress(76561198010872093L, 1).thenAccept(new Consumer<List<SteamQuestStatus>>() {
            @Override
            public void accept(List<SteamQuestStatus> steamQuestStatuses) {
                log.info("Total Steam Quests: {}", steamQuestStatuses.size());
                steamQuestStatuses.forEach(new Consumer<SteamQuestStatus>() {
                    @Override
                    public void accept(SteamQuestStatus steamQuestStatus) {
                        log.info("Quest = {}, Status = {}", steamQuestStatus.getQuestId(), steamQuestStatus.isCompleted());
                    }
                });
            }
        }).join();

        playerService.getSteamLevel(76561198010872093L).thenAccept(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                log.info("Steam Level : {}", integer);
            }
        }).join();

        playerService.getSteamGameLenderId(76561198010872093L, 550).thenAccept(new Consumer<String>() {
            @Override
            public void accept(String s) {
                log.info("Lender Id: {}", s);
            }
        }).join();

        //Steam User
        steamUser.getFriendList(76561198010872093L, "friend").thenAccept(new Consumer<List<SteamFriend>>() {
            @Override
            public void accept(List<SteamFriend> steamFriends) {
                steamFriends.forEach(new Consumer<SteamFriend>() {
                    @Override
                    public void accept(SteamFriend steamFriend) {
                        log.info("Friend: {}", steamFriend.getSteamId());
                    }
                });
            }
        }).join();

        steamUser.getPlayerBans(76561198010872093L).thenAccept(new Consumer<List<SteamBanStatus>>() {
            @Override
            public void accept(List<SteamBanStatus> steamBanStatuses) {
                steamBanStatuses.forEach(new Consumer<SteamBanStatus>() {
                    @Override
                    public void accept(SteamBanStatus steamBanStatus) {
                        log.info("Ban status for {} = {}, Economy Ban: {}", steamBanStatus.getSteamIdAsLong(), steamBanStatus.isVacBanned(), steamBanStatus.getEconomyBan());
                    }
                });
            }
        }).join();

        steamUser.getPlayerProfiles(76561198010872093L).thenAccept(new Consumer<List<SteamPlayerProfile>>() {
            @Override
            public void accept(List<SteamPlayerProfile> steamPlayerProfiles) {
                steamPlayerProfiles.forEach(p -> log.info("{}", p));
            }
        }).join();

        steamUser.getUserGroupList(76561198010872093L).thenAccept(steamGroupIds -> {
            steamGroupIds.forEach(new Consumer<SteamGroupId>() {
                @Override
                public void accept(SteamGroupId steamGroupId) {
                    log.info("Steam Group Id: {}", steamGroupId.getGroupId());
                }
            });
        }).join();

        steamUser.getSteamIdFromVanityUrl("zenmast3r", VanityUrlType.DEFAULT).thenAccept(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                log.info("Got Steam Id From Vanity url: {}", aLong);
            }
        }).join();

        //Steam Economy
        steamEconomy.getAssetPrices(730).thenAccept(new Consumer<List<SteamAssetPriceInfo>>() {
            @Override
            public void accept(List<SteamAssetPriceInfo> steamAssetPriceInfos) {
                log.info("Retrieved Steam Asset Price Info for CSGO");
                steamAssetPriceInfos.forEach(new Consumer<SteamAssetPriceInfo>() {
                    @Override
                    public void accept(SteamAssetPriceInfo steamAssetPriceInfo) {
                        log.info(" {}", steamAssetPriceInfo);
                    }
                });
            }
        }).join();

        steamEconomy.getAssetClassInfo(730, "en", 186150629L, 506856209L, 506856210L, 903185406L,
                                       613589849L, 613589850L, 613589851L, 613589852L, 613589853L, 613589854L, 613589855L).thenAccept(new Consumer<Map<String, SteamAssetClassInfo>>() {
            @Override
            public void accept(Map<String, SteamAssetClassInfo> stringSteamAssetClassInfoMap) {
                stringSteamAssetClassInfoMap.forEach((classId, classInfo) -> {
                    log.info("ID: {}, Info: {}", classId, classInfo);
                });
            }
        }).join();

        //Econ Items
        List<SteamEconPlayerItem> playerItems = steamEconItems.getPlayerItems(730, 76561197960761020L, SteamEconItems.VERSION_1).get();
        log.info("Player Items: {}", playerItems);

        SteamEconSchema schema = steamEconItems.getSchema(440).join();
        log.info("Schema: {}", schema);

        String schemaUrl = steamEconItems.getSchemaUrl(440).join();
        log.info("Schema URL : {}", schemaUrl);

        SteamEconItemsStoreMeta storeMedaData = steamEconItems.getStoreMetadata(440).join();
        log.info("Store Meta Data: {}", storeMedaData);

        Integer storeStatus = steamEconItems.getStoreStatus(440).join();
        log.info("Store Status: {}", storeStatus);

        //Storefront
        StoreAppDetails storeAppDetails = storeFront.getAppDetails(550).join();
        log.info("Storefront App Details: {}", storeAppDetails);

        StoreFeaturedApps featuredApps = storeFront.getFeaturedApps().join();
        log.info("Featured Apps: {}", featuredApps);

        StoreFeaturedCategories featuredCategories = storeFront.getFeaturedCategories().join();
        log.info("Featured Categories: {}", featuredCategories);

        StorePackageDetails packageDetails = storeFront.getPackageDetails(54029).join();
        log.info("Package Details: {}", packageDetails);

        StoreSaleDetails storeSaleDetails = storeFront.getSaleDetails(0).join();
        log.info("Sale Details: {}", storeSaleDetails);

        MasterServerFilter filter = MasterServerFilter.create().appId(730).dedicated(true);
        CompletableFuture<List<Server>> serverListFuture = gameServersService.getServerList(filter.toString(), 10);
        int ctr = 1;
        for (Server server : serverListFuture.join()) {
            System.out.printf("%03d) name = %s, ip = %s%n", ctr++, server.getName(), server.getAddr());
        }
    }

    private static void displayResult(Object result) {
        Console.println("%s", result);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (client != null)
            client.close();
    }
}
