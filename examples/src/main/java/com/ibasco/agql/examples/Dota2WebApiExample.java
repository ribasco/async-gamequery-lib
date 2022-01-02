/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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
import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2WebApiClient;
import com.ibasco.agql.protocols.valve.dota2.webapi.enums.Dota2IconType;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.*;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Dota2WebApiExample extends BaseWebApiAuthExample {

    private static final Logger log = LoggerFactory.getLogger(Dota2WebApiExample.class);

    private long serverSteamId = -1;

    private Dota2WebApiClient apiClient;

    public static void main(String[] args) throws Exception {
        new Dota2WebApiExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        apiClient = new Dota2WebApiClient(getToken("steam"));
        try {
            Dota2Econ econInterface = Dota2.createEcon(apiClient);
            Dota2Fantasy fantasyInterface = Dota2.createFantasy(apiClient);
            Dota2Match matchInterface = new Dota2Match(apiClient);
            Dota2Stats statsInterface = new Dota2Stats(apiClient);
            Dota2Stream streamInterface = new Dota2Stream(apiClient);
            Dota2Teams teamInterface = new Dota2Teams(apiClient);

            //Econ Interface
            List<Dota2GameItem> gameItems = econInterface.getGameItems().get();
            log.info("All of the game items..");
            gameItems.forEach(Dota2WebApiExample::displayResult);

            List<Dota2Heroes> heroes = econInterface.getGameHeroes(false, "en").get();
            heroes.forEach(Dota2WebApiExample::displayResult);

            String iconPath = econInterface.getItemIconPath("faker_gold", Dota2IconType.NORMAL).get();
            log.info("Icon Path: {}", iconPath);

            List<Dota2Rarities> rarities = econInterface.getRarities("en").get();
            rarities.forEach(Dota2WebApiExample::displayResult);

            Integer prizePool = econInterface.getTournamentPrizePool(4122).get();
            log.info("Prize Pool : {}", prizePool);

            Dota2EventStats stats = econInterface.getEventStatsForAccount(205557093, 4122).get();
            log.info("Event Stats: {}", stats);

            //Fantasy Interface
            Dota2FantasyPlayerInfo playerInfo = fantasyInterface.getPlayerOfficialInfo(23883296).get();
            log.info("Player Info: {}", playerInfo);

            List<Dota2FantasyProPlayerInfo> proPlayerInfos = fantasyInterface.getProPlayerList().get();
            proPlayerInfos.forEach(Dota2WebApiExample::displayResult);

            //Match
            List<Dota2LiveLeagueGame> gameDetails = matchInterface.getLiveLeagueGames().get();
            gameDetails.forEach(Dota2WebApiExample::displayResult);

            List<Dota2League> leagues = matchInterface.getLeagueListing().get();
            leagues.forEach(Dota2WebApiExample::displayResult);

            Dota2MatchHistory matchHistory = matchInterface.getMatchHistory().get();

            log.info("Match History Details: {}", matchHistory);
            for (Dota2MatchHistoryInfo historyInfo : matchHistory.getMatches()) {
                CompletableFuture<Dota2MatchDetails> matchHistoryDetails = matchInterface.getMatchDetails(historyInfo.getMatchId());
                matchHistoryDetails.thenAccept(dota2MatchDetails -> log.debug("\t\tMatch: {}", dota2MatchDetails));
            }

            List<Dota2MatchDetails> matchDetailsBySeq = matchInterface.getMatchHistoryBySequenceNum(1, 10).get();
            matchDetailsBySeq.forEach(Dota2WebApiExample::displayResult);

            List<Dota2MatchTeamInfo> teams = matchInterface.getTeamInfoById(1, 10).get();
            teams.forEach(Dota2WebApiExample::displayResult);

            List<Dota2TopLiveGame> topLiveGames = matchInterface.getTopLiveGame(1).get();
            topLiveGames.forEach(dota2TopLiveGame -> {
                if (serverSteamId == -1)
                    serverSteamId = dota2TopLiveGame.getServerSteamId();
                log.info("{} = {}", dota2TopLiveGame.getClass().getSimpleName(), dota2TopLiveGame);
            });

            //Stats
            Dota2RealtimeServerStats serverStats = statsInterface.getRealtimeStats(serverSteamId).get();
            log.info("Server Stats : {}", serverStats);

            //Stream
            Dota2BroadcasterInfo bInfo = streamInterface.getBroadcasterInfo(292948090, -1).get();
            log.info("Broadcaster Info: {}", bInfo);

            //Teams
            List<Dota2TeamDetails> teamDetails = teamInterface.getTeamInfo(4, -1).get();
            teamDetails.forEach(Dota2WebApiExample::displayResult);
        } finally {
            log.info("Closing Client");
            apiClient.close();
        }
    }

    private static void displayResult(Object result) {
        log.info("{} = {}", result.getClass().getSimpleName(), result);
    }

    @Override
    public void close() throws IOException {
        apiClient.close();
    }
}
