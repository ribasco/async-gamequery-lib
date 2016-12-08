# Clash of Clans

### Available Interfaces

| **Interface Name** | **Description**             |
|--------------------|-----------------------------|
| CocClans           | Clan Information            |
| CocLocations       | Location Information        |
| CocLeagues         | League Information          |
| CocPlayers         | Player Specific Information |


### Clans

**Search clans**

~~~
CocSearchCriteria searchCriteria = CocSearchCriteria.create().warFrequency(CocWarFrequency.ALWAYS).limit(10);
clans.searchClans(searchCriteria).thenAccept(clanInfos -> log.info("Size: {}, Data: {}", clanInfos.size(), clanInfos).join();
~~~

**Get clan Information**

~~~
String clanTag = "#ABCDEFGH";
clans.getClanInfo(clanTag).thenAccept(clanInfo -> log.info("Clan Info: {}", clanInfo)).join();
~~~

**Get list of clan members**

~~~
String clanTag = "#ABCDEFGH";
clans.getClanMembers(clanTag).thenAccept(cocPlayers -> cocPlayers.forEach(cocPlayer -> log.info("{}", cocPlayer))).join();
~~~

**Get clan war log details**

~~~
String clanTag = "#ABCDEFGH";
clans.getClanWarLog(clanTag).thenAccept(cocWarLogEntries -> cocWarLogEntries.forEach(cocWarLogEntry -> log.info("War Log Entry: {}", cocWarLogEntry))).join();
~~~

### Locations

**Get Locations**

~~~
locations.getLocations().thenAccept(cocClanLocations -> cocClanLocations.forEach(cocClanLocation -> log.info("Location: {}", cocClanLocation))).join();
~~~

**Get Location Info**

~~~
locations.getLocationInfo(32000000).thenAccept(cocLocation -> log.info("Single Location: {}", cocLocation)).join();
~~~

**Get Clan Rankings by Location**

~~~
locations.getClanRankingsFromLocation(32000185).thenAccept(cocClanRankInfos -> cocClanRankInfos.forEach(cocClanRankInfo -> log.info("Ranking: {}", cocClanRankInfo))).join();
~~~

**Get Player Rankings by Location**

~~~
locations.getPlayerRankingsFromLocation(32000185).thenAccept(this::displayListResults).join();

<T> void displayListResults(List<T> list) {
    list.forEach(o -> log.info("{}", o.toString()));
}
~~~

### Leagues

**Get League List**

~~~
leagues.getLeagueList().thenAccept(this::displayListResults).join();
~~~

**Display League Seasons**

~~~
leagues.getLeagueSeasons(29000022).thenAccept(CocWebApiQueryEx::displayListResults).join();
~~~

**Display League Season Player Rankings**

~~~
leagues.getLeagueSeasonsPlayerRankings(29000022, "2016-06", 10).thenAccept(CocWebApiQueryEx::displayListResults).join();
~~~

### Player

**Get Player Info**

~~~
players.getPlayerInfo("#J0PYGCG").thenAccept(p -> log.info("Player Info: {}", p)).join();
~~~