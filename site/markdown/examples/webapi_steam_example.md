Steam Web API Example
=====================

Usage examples for Steam Web API Interfaces 

### Available Interfaces

| **Interface Name** | **Description**                                                 |
|--------------------|-----------------------------------------------------------------|
| SteamApps          | Contains methods relating to Steam Apps in general.             |
| SteamEconItems     | Contains methods relating to in-game items for supported games. |
| SteamEconomy       | Contains methods relating to games' store's assets.             |
| SteamNews          | Contains methods relating to steam news                         |
| SteamPlayerService | Contains methods relating to a steam user’s games.              |
| SteamStorefront    | Contains methods relating to the steam storefront               |
| SteamUser          | Contains methods provide information about Steam users.         |
| SteamUserStats     | Contains methods to fetch global stat information by game.      |

### Steam Apps

**Get a list of available Steam Apps**

~~~
steamApps.getAppList().exceptionally(throwable -> {
    log.error("Error Occured", throwable);
    return new ArrayList<>();
}).thenAccept(this::displayResult).join();
~~~

**Get a list of servers based on the address specified**

~~~
steamApps.getServersAtAddress(InetAddress.getByName("103.28.55.237"))
.exceptionally(throwable -> {
    log.error("Error occured", throwable);
    return new ArrayList<>();
})
.thenAccept(steamServers -> { steamServers.stream().forEachOrdered(steamServer -> log.info("Server: {}", steamServer.getAddr()));})
.join();
~~~

**Get a server's update status**

~~~
steamApps.getServerUpdateStatus(2146, 550).thenAccept(new Consumer<ServerUpdateStatus>() {
    @Override
    public void accept(ServerUpdateStatus serverUpdateStatus) {
        log.info("Success: {}, Message: {}, Required Version: {}", serverUpdateStatus.isSuccess(), serverUpdateStatus.getMessage(), serverUpdateStatus.getRequiredVersion());
    }
}).join();
~~~

### Steam Econ Items

**Get Player Items**

~~~
List<SteamEconPlayerItem> playerItems = steamEconItems.getPlayerItems(730, 76561197960761020L, SteamEconItems.VERSION_1).get();
log.info("Player Items: {}", playerItems);
~~~

**Get Schema**

~~~
SteamEconSchema schema = steamEconItems.getSchema(440).join();
log.info("Schema: {}", schema);
~~~

**Get Schema URL**

~~~
String schemaUrl = steamEconItems.getSchemaUrl(440).join();
log.info("Schema URL : {}", schemaUrl);
~~~

**Get Store Metadata**

~~~
SteamEconItemsStoreMeta storeMedaData = steamEconItems.getStoreMetadata(440).join();
log.info("Store Meta Data: {}", storeMedaData);
~~~

**Get Store Status**

~~~
Integer storeStatus = steamEconItems.getStoreStatus(440).join();
log.info("Store Status: {}", storeStatus);
~~~

### Steam Economy

**Get Asset Prices**

~~~
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
~~~

**Get Asset Class Info**

~~~
steamEconomy.getAssetClassInfo(730, "en", 186150629L, 506856209L, 506856210L, 903185406L,
        613589849L, 613589850L, 613589851L, 613589852L, 613589853L, 613589854L, 613589855L).thenAccept(new Consumer<Map<String, SteamAssetClassInfo>>() {
    @Override
    public void accept(Map<String, SteamAssetClassInfo> stringSteamAssetClassInfoMap) {
        stringSteamAssetClassInfoMap.forEach((classId, classInfo) -> {
            log.info("ID: {}, Info: {}", classId, classInfo);
        });
    }
}).join();
~~~

### Steam News

**Get News for App**

~~~
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
~~~

### Steam Player Service

**Get Recently Played Games**

~~~
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
~~~

**Get Badges**

~~~
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
~~~

**Get Community Badge Progress**

~~~
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
~~~

**Get Steam Level**

~~~
playerService.getSteamLevel(76561198010872093L).thenAccept(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) {
        log.info("Steam Level : {}", integer);
    }
}).join();
~~~

**Get Steam Game Lender ID**

~~~
playerService.getSteamGameLenderId(76561198010872093L, 550).thenAccept(new Consumer<String>() {
    @Override
    public void accept(String s) {
        log.info("Lender Id: {}", s);
    }
}).join();
~~~

### Steam Storefront

**Get Store App Details**

~~~
StoreAppDetails storeAppDetails = storeFront.getAppDetails(550).join();
log.info("Storefront App Details: {}", storeAppDetails);
~~~

**Get Featured Apps**

~~~
StoreFeaturedApps featuredApps = storeFront.getFeaturedApps().join();
log.info("Featured Apps: {}", featuredApps);
~~~

**Get Featured Categories**

~~~
StoreFeaturedCategories featuredCategories = storeFront.getFeaturedCategories().join();
log.info("Featured Categories: {}", featuredCategories);
~~~

**Get Package Details**

~~~
StorePackageDetails packageDetails = storeFront.getPackageDetails(54029).join();
log.info("Package Details: {}", packageDetails);
~~~

**Get Sale Details**

~~~
StoreSaleDetails storeSaleDetails = storeFront.getSaleDetails(0).join();
log.info("Sale Details: {}", storeSaleDetails);
~~~

### Steam User

**Get Friend List**

~~~
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
~~~

**Get Player Bans**

~~~
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
~~~

**Get Player Profiles**

~~~
steamUser.getPlayerProfiles(76561198010872093L).thenAccept(new Consumer<List<SteamPlayerProfile>>() {
    @Override
    public void accept(List<SteamPlayerProfile> steamPlayerProfiles) {
        steamPlayerProfiles.forEach(p -> log.info("{}", p));
    }
}).join();
~~~

**Get User Group List**

~~~
steamUser.getUserGroupList(76561198010872093L).thenAccept(steamGroupIds -> {
    steamGroupIds.forEach(new Consumer<SteamGroupId>() {
        @Override
        public void accept(SteamGroupId steamGroupId) {
            log.info("Steam Group Id: {}", steamGroupId.getGroupId());
        }
    });
}).join();
~~~

**Get Steam Id From Vanity URL**

~~~
steamUser.getSteamIdFromVanityUrl("zenmast3r", VanityUrlType.DEFAULT).thenAccept(new Consumer<Long>() {
    @Override
    public void accept(Long aLong) {
        log.info("Got Steam Id From Vanity url: {}", aLong);
    }
}).join();
~~~

### Steam User Stats

**Get Global Achievement Percentage for App**

~~~
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
~~~

**Get Schema For Game**

~~~
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
~~~

**Get Total Number of Current Players for Game**

~~~
steamUserStats.getNumberOfCurrentPlayers(550).thenAccept(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) {
        log.info("Total Players : {}", integer);
    }
}).join();
~~~

**Get Player Achievements**

~~~
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
~~~

**Get User Stats for Game**

~~~
steamUserStats.getUserStatsForGame(76561198010872093L, 550).thenAccept(new Consumer<SteamPlayerStats>() {
    @Override
    public void accept(SteamPlayerStats playerStats) {
        log.info("Player Stats:");
        log.info("- Achievements: {}", playerStats.getAchievements().size());
        log.info("- Stats: {}", playerStats.getStats().size());
    }
}).join();
~~~