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
| GameServersService | Contains methods relating to game servers                       |
| SteamStoreService  | Contains methods relating to the steam store service            |

### Steam Apps

**Get a list of available Steam Apps**

~~~java
steamApps.getAppList().exceptionally(throwable->{
        log.error("Error Occured",throwable);
        return new ArrayList<>();
        }).thenAccept(this::displayResult).join();
~~~

**Get a list of servers based on the address specified**

~~~java
steamApps.getServersAtAddress(InetAddress.getByName("103.28.55.237"))
        .exceptionally(throwable->{
        log.error("Error occured",throwable);
        return new ArrayList<>();
        })
        .thenAccept(steamServers->{steamServers.stream().forEachOrdered(steamServer->log.info("Server: {}",steamServer.getAddr()));})
        .join();
~~~

**Get a server's update status**

~~~java
steamApps.getServerUpdateStatus(2146,550).thenAccept(new Consumer<ServerUpdateStatus>(){
@Override
public void accept(ServerUpdateStatus serverUpdateStatus){
        log.info("Success: {}, Message: {}, Required Version: {}",serverUpdateStatus.isSuccess(),serverUpdateStatus.getMessage(),serverUpdateStatus.getRequiredVersion());
        }
        }).join();
~~~

### Steam Econ Items

**Get Player Items**

~~~java
List<SteamEconPlayerItem> playerItems=steamEconItems.getPlayerItems(730,76561197960761020L,SteamEconItems.VERSION_1).get();
        log.info("Player Items: {}",playerItems);
~~~

**Get Schema**

~~~java
SteamEconSchema schema=steamEconItems.getSchema(440).join();
        log.info("Schema: {}",schema);
~~~

**Get Schema URL**

~~~java
String schemaUrl=steamEconItems.getSchemaUrl(440).join();
        log.info("Schema URL : {}",schemaUrl);
~~~

**Get Store Metadata**

~~~java
SteamEconItemsStoreMeta storeMedaData=steamEconItems.getStoreMetadata(440).join();
        log.info("Store Meta Data: {}",storeMedaData);
~~~

**Get Store Status**

~~~java
Integer storeStatus=steamEconItems.getStoreStatus(440).join();
        log.info("Store Status: {}",storeStatus);
~~~

### Steam Economy

**Get Asset Prices**

~~~java
steamEconomy.getAssetPrices(730).thenAccept(new Consumer<List<SteamAssetPriceInfo>>(){
@Override
public void accept(List<SteamAssetPriceInfo> steamAssetPriceInfos){
        log.info("Retrieved Steam Asset Price Info for CSGO");
        steamAssetPriceInfos.forEach(new Consumer<SteamAssetPriceInfo>(){
@Override
public void accept(SteamAssetPriceInfo steamAssetPriceInfo){
        log.info(" {}",steamAssetPriceInfo);
        }
        });
        }
        }).join();
~~~

**Get Asset Class Info**

~~~java
steamEconomy.getAssetClassInfo(730,"en",186150629L,506856209L,506856210L,903185406L,
        613589849L,613589850L,613589851L,613589852L,613589853L,613589854L,613589855L).thenAccept(new Consumer<Map<String, SteamAssetClassInfo>>(){
@Override
public void accept(Map<String, SteamAssetClassInfo> stringSteamAssetClassInfoMap){
        stringSteamAssetClassInfoMap.forEach((classId,classInfo)->{
        log.info("ID: {}, Info: {}",classId,classInfo);
        });
        }
        }).join();
~~~

### Steam News

**Get News for App**

~~~java
steamNews.getNewsForApp(550).thenAccept(new Consumer<List<SteamNewsItem>>(){
@Override
public void accept(List<SteamNewsItem> steamNewsItems){
        steamNewsItems.forEach(new Consumer<SteamNewsItem>(){
@Override
public void accept(SteamNewsItem steamNewsItem){
        log.info("News Item: {}",steamNewsItem.getTitle());
        }
        });
        }
        }).join();
~~~

### Steam Player Service

**Get Recently Played Games**

~~~java
playerService.getRecentlyPlayedGames(76561198010872093L,500).thenAccept(new Consumer<List<SteamPlayerRecentPlayed>>(){
@Override
public void accept(List<SteamPlayerRecentPlayed> steamPlayerRecentPlayeds){
        steamPlayerRecentPlayeds.forEach(new Consumer<SteamPlayerRecentPlayed>(){
@Override
public void accept(SteamPlayerRecentPlayed steamPlayerRecentPlayed){
        log.info("Recently Played: {} = {}",steamPlayerRecentPlayed.getName(),steamPlayerRecentPlayed.getTotalPlaytime());
        }
        });
        }
        }).join();
~~~

**Get Badges**

~~~java
playerService.getBadges(76561198010872093L).thenAccept(new Consumer<SteamPlayerBadgeInfo>(){
@Override
public void accept(SteamPlayerBadgeInfo steamPlayerBadgeInfo){
        log.info("Total Player Badges: {}",steamPlayerBadgeInfo.getPlayerBadges().size());
        steamPlayerBadgeInfo.getPlayerBadges().forEach(new Consumer<SteamPlayerBadge>(){
@Override
public void accept(SteamPlayerBadge steamPlayerBadge){
        log.info("- {}",steamPlayerBadge.getBadgeId());
        }
        });
        }
        }).join();
~~~

**Get Community Badge Progress**

~~~java
playerService.getCommunityBadgeProgress(76561198010872093L,1).thenAccept(new Consumer<List<SteamQuestStatus>>(){
@Override
public void accept(List<SteamQuestStatus> steamQuestStatuses){
        log.info("Total Steam Quests: {}",steamQuestStatuses.size());
        steamQuestStatuses.forEach(new Consumer<SteamQuestStatus>(){
@Override
public void accept(SteamQuestStatus steamQuestStatus){
        log.info("Quest = {}, Status = {}",steamQuestStatus.getQuestId(),steamQuestStatus.isCompleted());
        }
        });
        }
        }).join();
~~~

**Get Steam Level**

~~~java
playerService.getSteamLevel(76561198010872093L).thenAccept(new Consumer<Integer>(){
@Override
public void accept(Integer integer){
        log.info("Steam Level : {}",integer);
        }
        }).join();
~~~

**Get Steam Game Lender ID**

~~~java
playerService.getSteamGameLenderId(76561198010872093L,550).thenAccept(new Consumer<String>(){
@Override
public void accept(String s){
        log.info("Lender Id: {}",s);
        }
        }).join();
~~~

### Steam Storefront

**Get Store App Details**

~~~java
StoreAppDetails storeAppDetails=storeFront.getAppDetails(550).join();
        log.info("Storefront App Details: {}",storeAppDetails);
~~~

**Get Featured Apps**

~~~java
StoreFeaturedApps featuredApps=storeFront.getFeaturedApps().join();
        log.info("Featured Apps: {}",featuredApps);
~~~

**Get Featured Categories**

~~~java
StoreFeaturedCategories featuredCategories=storeFront.getFeaturedCategories().join();
        log.info("Featured Categories: {}",featuredCategories);
~~~

**Get Package Details**

~~~java
StorePackageDetails packageDetails=storeFront.getPackageDetails(54029).join();
        log.info("Package Details: {}",packageDetails);
~~~

**Get Sale Details**

~~~java
StoreSaleDetails storeSaleDetails=storeFront.getSaleDetails(0).join();
        log.info("Sale Details: {}",storeSaleDetails);
~~~

### Steam User

**Get Friend List**

~~~java
steamUser.getFriendList(76561198010872093L,"friend").thenAccept(new Consumer<List<SteamFriend>>(){
@Override
public void accept(List<SteamFriend> steamFriends){
        steamFriends.forEach(new Consumer<SteamFriend>(){
@Override
public void accept(SteamFriend steamFriend){
        log.info("Friend: {}",steamFriend.getSteamId());
        }
        });
        }
        }).join();
~~~

**Get Player Bans**

~~~java
steamUser.getPlayerBans(76561198010872093L).thenAccept(new Consumer<List<SteamBanStatus>>(){
@Override
public void accept(List<SteamBanStatus> steamBanStatuses){
        steamBanStatuses.forEach(new Consumer<SteamBanStatus>(){
@Override
public void accept(SteamBanStatus steamBanStatus){
        log.info("Ban status for {} = {}, Economy Ban: {}",steamBanStatus.getSteamIdAsLong(),steamBanStatus.isVacBanned(),steamBanStatus.getEconomyBan());
        }
        });
        }
        }).join();
~~~

**Get Player Profiles**

~~~java
steamUser.getPlayerProfiles(76561198010872093L).thenAccept(new Consumer<List<SteamPlayerProfile>>(){
@Override
public void accept(List<SteamPlayerProfile> steamPlayerProfiles){
        steamPlayerProfiles.forEach(p->log.info("{}",p));
        }
        }).join();
~~~

**Get User Group List**

~~~java
steamUser.getUserGroupList(76561198010872093L).thenAccept(steamGroupIds->{
        steamGroupIds.forEach(new Consumer<SteamGroupId>(){
@Override
public void accept(SteamGroupId steamGroupId){
        log.info("Steam Group Id: {}",steamGroupId.getGroupId());
        }
        });
        }).join();
~~~

**Get Steam Id From Vanity URL**

~~~java
steamUser.getSteamIdFromVanityUrl("zenmast3r",VanityUrlType.DEFAULT).thenAccept(new Consumer<Long>(){
@Override
public void accept(Long aLong){
        log.info("Got Steam Id From Vanity url: {}",aLong);
        }
        }).join();
~~~

### Steam User Stats

**Get Global Achievement Percentage for App**

~~~java
steamUserStats.getGlobalAchievementPercentagesForApp(550).thenAccept(new Consumer<List<SteamGameAchievement>>(){
@Override
public void accept(List<SteamGameAchievement> steamGameAchievements){
        steamGameAchievements.forEach(new Consumer<SteamGameAchievement>(){
@Override
public void accept(SteamGameAchievement steamGameAchievement){
        log.info("Achievement: {}, {}",steamGameAchievement.getName(),steamGameAchievement.getPercentage());
        }
        });
        }
        }).join();
~~~

**Get Schema For Game**

~~~java
steamUserStats.getSchemaForGame(550).thenAccept(steamGameStatsSchemaInfo->{
        log.info("Achievement Schemas : {}",steamGameStatsSchemaInfo.getAchievementSchemaList().size());
        log.info("Stats Schemas : {}",steamGameStatsSchemaInfo.getStatsSchemaList().size());
        steamGameStatsSchemaInfo.getAchievementSchemaList().forEach(new Consumer<SteamGameAchievementSchema>(){
@Override
public void accept(SteamGameAchievementSchema steamGameAchievementSchema){
        log.info("ach - {}",steamGameAchievementSchema.getName());
        }
        });
        steamGameStatsSchemaInfo.getStatsSchemaList().forEach(new Consumer<SteamGameStatsSchema>(){
@Override
public void accept(SteamGameStatsSchema steamGameStatsSchema){
        log.info("stats - {}",steamGameStatsSchema.getName());
        }
        });
        }).join();
~~~

**Get Total Number of Current Players for Game**

~~~java
steamUserStats.getNumberOfCurrentPlayers(550).thenAccept(new Consumer<Integer>(){
@Override
public void accept(Integer integer){
        log.info("Total Players : {}",integer);
        }
        }).join();
~~~

**Get Player Achievements**

~~~java
steamUserStats.getPlayerAchievements(76561198010872093L,550).thenAccept(new Consumer<List<SteamPlayerAchievement>>(){
@Override
public void accept(List<SteamPlayerAchievement> steamPlayerAchievements){
        log.info("Player Achievements: {}",steamPlayerAchievements.size());
        steamPlayerAchievements.forEach(new Consumer<SteamPlayerAchievement>(){
@Override
public void accept(SteamPlayerAchievement steamPlayerAchievement){
        log.info("- {} = {}",steamPlayerAchievement.getName(),steamPlayerAchievement.getAchieved());
        }
        });
        }
        }).join();
~~~

**Get User Stats for Game**

~~~java
steamUserStats.getUserStatsForGame(76561198010872093L,550).thenAccept(new Consumer<SteamPlayerStats>(){
@Override
public void accept(SteamPlayerStats playerStats){
        log.info("Player Stats:");
        log.info("- Achievements: {}",playerStats.getAchievements().size());
        log.info("- Stats: {}",playerStats.getStats().size());
        }
        }).join();
~~~

### GameServersService

**Get server addresses from Master**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            MasterServerFilter filter = MasterServerFilter.create().appId(730).dedicated(true);
            CompletableFuture<List<GameServer>> serverListFuture = gameServersService.getServerList(filter.toString(), 10);
            int ctr = 1;
            for (GameServer gameServer : serverListFuture.join()) {
                System.out.printf("%03d) name = %s, ip = %s%n", ctr++, gameServer.getName(), gameServer.getAddr());
            }
        }
    }
}
```

**Create Account**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            NewGameServerAccount newAccount = gameServersService.createAccount(550, "Test").join();
            System.out.println(newAccount);
        }
    }
}
```

**Delete Account**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            long steamId = 0L;
            gameServersService.deleteAccount(steamId).join();
            System.out.printf("Deleted account: %d%n", steamId);
        }
    }
}
```

**Get Account Info**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            GameServerAccount accountInfo = gameServersService.getAccountList().join();
            System.out.println(accountInfo);
            for (GameServerAccountDetail detail : accountInfo.getServers()) {
                System.out.println(detail);
            }
        }
    }
}
```

**Reset Login Token**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            long steamId = 0L;
            String newLoginToken = gameServersService.resetLoginToken(steamId).join();
            System.out.printf("New login token for steam id '%s': '%s'%n", steamId, newLoginToken);
        }
    }
}
```

**Query Login Token Status**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            long steamId = 0L;
            LoginTokenStatus tokenStatus = gameServersService.queryLoginTokenStatus(steamId).join();
            System.out.printf("Token status: %s%n", tokenStatus);
        }
    }
}
```

**Get Account List**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            GameServerAccount accountInfo = gameServersService.getAccountList().join();
            System.out.println(accountInfo);
            for (GameServerAccountDetail detail : accountInfo.getServers()) {
                System.out.println(detail);
            }
        }
    }
}
```

**Get Account Public Info**

```java
class WebApiExample {

    public static void main(String[] args) throws Exception {
        try (GameServersService gameServersService = new GameServersService(client)) {
            long steamId = 0L;
            GameServerAccountPublicInfo publicInfo = gameServersService.getAccountPublicInfo(steamId).join();
            System.out.println(publicInfo);
        }
    }
}
```

### SteamStoreService

**Get App List**

```java
public class SteamStoreExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("<api token>")) {
            SteamStoreService service = new SteamStoreService(client);
            SteamStoreAppResponse response = service.getAppList(10).join();
            response.getApps().forEach(app -> System.out.printf("\t[Page: 1] App: %s%n", app.getName()));
            response = service.getAppList(response.getLastAppid(), 10).join();
            response.getApps().forEach(app -> System.out.printf("\t[Page: 2] App: %s%n", app.getName()));
        }
    }
}
```

**Get Localized Name for Tags**

```java
public class SteamStoreExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("<api token>")) {
            SteamStoreService service = new SteamStoreService(client);
            List<LocalizedNameTag> localizedTags = service.getLocalizedNameForTags("russian", Arrays.asList(493, 113)).join();
            localizedTags.forEach(tag -> System.out.printf("\tEnglish Name: %s, Normalized Name: %s, Localized Name: %s%n", tag.getEnglishName(), tag.getNormalizedName(), tag.getName()));
        }
    }
}
```

**Get Popular Tags**

```java
public class SteamStoreExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("<api token>")) {
            SteamStoreService service = new SteamStoreService(client);
            List<PopularTag> popularTags = service.getPopularTag("english").join();
            popularTags.forEach(tag -> System.out.printf("\tId: %d, Name: %s%n", tag.getTagId(), tag.getName()));
        }
    }
}
```

### Web API Util

**Get Supported API List**

```java
public class WebApiUtilExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("<api token>")) {
            SteamWebAPIUtil service = new SteamWebAPIUtil(client);
            List<ApiInterface> apiInterfaceList = service.getSupportedApiList().join();
            for (ApiInterface apiInterface : apiInterfaceList) {
                System.out.printf("Interface: %s (Methods: %d)%n", apiInterface.getName(), apiInterface.getMethods().size());
            }
        }
    }
}
```

**Get Server Info**

```java
public class WebApiUtilExample {

    public static void main(String[] args) throws Exception {
        try (SteamWebApiClient client = new SteamWebApiClient("<api token>")) {
            SteamWebAPIUtil service = new SteamWebAPIUtil(client);
            ServerInfo info = service.getServerInfo().join();
            System.out.printf("Time: %d, Time String: %s%n", info.getServertime(), info.getServerTimeString());
        }
    }
}
```