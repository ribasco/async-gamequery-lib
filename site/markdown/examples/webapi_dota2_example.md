Dota 2 Web API Examples
=======================

Usage examples of Dota 2 Web API Interfaces

### Available Interfaces

| **Interface Name** | **Description**                                                                 |
|--------------------|---------------------------------------------------------------------------------|
| Dota2Econ          | Contains methods relating to Dota 2 Economy                                     |
| Dota2Fantasy       | Contains methods relating to Dota 2 fantasy team system                         |
| Dota2Match         | Contains methods relating to real time match details                            |
| Dota2Stats         | Contains methods for retrieving real time match statistics from a dota 2 server |
| Dota2Stream        | Contains methods relating to Dota 2 streaming system                            |
| Dota2Teams         | Contains methods relating to Dota 2 teams                                       |

### Econ Interface

**Get Game Items**

~~~
List<Dota2GameItem> gameItems = econInterface.getGameItems().get();
log.info("All of the game items..");
gameItems.forEach(Dota2WebApiQueryEx::displayResult);
~~~

**Get Game Heroes**

~~~
List<Dota2Heroes> heroes = econInterface.getGameHeroes(false, "en").get();
heroes.forEach(Dota2WebApiQueryEx::displayResult);
~~~

**Get Item Icon Path**

~~~
String iconPath = econInterface.getItemIconPath("faker_gold", Dota2IconType.NORMAL).get();
log.info("Icon Path: {}", iconPath);
~~~

**Get Rarities**

~~~
List<Dota2Rarities> rarities = econInterface.getRarities("en").get();
rarities.forEach(Dota2WebApiQueryEx::displayResult);
~~~

**Get tournament prize pool**

~~~
Integer prizePool = econInterface.getTournamentPrizePool(4122).get();
log.info("Prize Pool : {}", prizePool);
~~~

**Get event stats from a specific account**

~~~
Dota2EventStats stats = econInterface.getEventStatsForAccount(205557093, 4122).get();
log.info("Event Stats: {}", stats); 
~~~

### Fantasy Interface

**Get a fantasy player's info**

~~~
Dota2FantasyPlayerInfo playerInfo = fantasyInterface.getPlayerOfficialInfo(23883296).get();
log.info("Player Info: {}", playerInfo);
~~~

**Get a list of Pro Dota 2 Players**

~~~
List<Dota2FantasyProPlayerInfo> proPlayerInfos = fantasyInterface.getProPlayerList().get();
proPlayerInfos.forEach(Dota2WebApiQueryEx::displayResult);
~~~

### Match Interface

**Get a List of Live League Games**

~~~
List<Dota2LiveLeagueGame> gameDetails = matchInterface.getLiveLeagueGames().get();
gameDetails.forEach(this::displayResult);
~~~

**Get current league listings**

~~~
List<Dota2League> leagues = matchInterface.getLeagueListing().get();
leagues.forEach(this::displayResult);
~~~

**Get Match Details based on League**

~~~
Dota2MatchDetails matchDetails = matchInterface.getMatchDetails(2753811554L).get();
log.info("Match Details: {}", matchDetails);
matchDetails.getPlayers().forEach(this::displayResult);
~~~

**Get latest match history**

~~~
Dota2MatchHistory matchHistory = matchInterface.getMatchHistory().get();
log.info("Match History : {}", matchHistory);
~~~

**Get match history by sequence number**

~~~
List<Dota2MatchDetails> matchDetailsBySeq = matchInterface.getMatchHistoryBySequenceNum(1, 10).get();
matchDetailsBySeq.forEach(this::displayResult);
~~~

**Get Team Information by ID**

~~~
List<Dota2MatchTeamInfo> teams = matchInterface.getTeamInfoById(1, 10).get();
teams.forEach(this::displayResult);
~~~

**Get a list of Top Live Games**

~~~
List<Dota2TopLiveGame> topLiveGames = matchInterface.getTopLiveGame(1).get();
topLiveGames.forEach(this::displayResult);
~~~

### Stats Interface

**Get realtime server stats**

~~~
Dota2RealtimeServerStats serverStats = statsInterface.getRealtimeStats(90105101693392898L).get();
log.info("Server Stats : {}", serverStats);
~~~

### Stream Interface

**Get Broadcaster Info**

~~~
Dota2BroadcasterInfo bInfo = streamInterface.getBroadcasterInfo(292948090, -1).get();
log.info("Broadcaster Info: {}", bInfo);
~~~

### Teams Interface

**Get Dota 2 Team Information**

~~~
List<Dota2TeamDetails> teamDetails = teamInterface.getTeamInfo(4, -1).get();
teamDetails.forEach(this::displayResult);
~~~
