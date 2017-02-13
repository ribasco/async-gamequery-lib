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
import com.ibasco.agql.protocols.valve.dota2.webapi.Dota2WebApiClient;
import com.ibasco.agql.protocols.valve.dota2.webapi.enums.Dota2IconType;
import com.ibasco.agql.protocols.valve.dota2.webapi.interfaces.*;
import com.ibasco.agql.protocols.valve.dota2.webapi.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Dota2WebApiQueryEx extends BaseWebApiAuthExample {
    private static final Logger log = LoggerFactory.getLogger(Dota2WebApiQueryEx.class);

    private Dota2WebApiClient apiClient;

    public static void main(String[] args) throws Exception {
        Dota2WebApiQueryEx app = new Dota2WebApiQueryEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
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
            gameItems.forEach(Dota2WebApiQueryEx::displayResult);

            List<Dota2Heroes> heroes = econInterface.getGameHeroes(false, "en").get();
            heroes.forEach(Dota2WebApiQueryEx::displayResult);

            String iconPath = econInterface.getItemIconPath("faker_gold", Dota2IconType.NORMAL).get();
            log.info("Icon Path: {}", iconPath);

            List<Dota2Rarities> rarities = econInterface.getRarities("en").get();
            rarities.forEach(Dota2WebApiQueryEx::displayResult);

            Integer prizePool = econInterface.getTournamentPrizePool(4122).get();
            log.info("Prize Pool : {}", prizePool);

            Dota2EventStats stats = econInterface.getEventStatsForAccount(205557093, 4122).get();
            log.info("Event Stats: {}", stats);

            //Fantasy Interface
            Dota2FantasyPlayerInfo playerInfo = fantasyInterface.getPlayerOfficialInfo(23883296).get();
            log.info("Player Info: {}", playerInfo);

            List<Dota2FantasyProPlayerInfo> proPlayerInfos = fantasyInterface.getProPlayerList().get();
            proPlayerInfos.forEach(Dota2WebApiQueryEx::displayResult);

            //Match
            List<Dota2LiveLeagueGame> gameDetails = matchInterface.getLiveLeagueGames().get();
            gameDetails.forEach(Dota2WebApiQueryEx::displayResult);

            List<Dota2League> leagues = matchInterface.getLeagueListing().get();
            leagues.forEach(Dota2WebApiQueryEx::displayResult);

            Dota2MatchDetails matchDetails = matchInterface.getMatchDetails(2753811554L).get();
            log.info("Match Details: {}", matchDetails);
            matchDetails.getPlayers().forEach(Dota2WebApiQueryEx::displayResult);

            Dota2MatchHistory matchHistory = matchInterface.getMatchHistory().get();
            log.info("Match History : {}", matchHistory);

            List<Dota2MatchDetails> matchDetailsBySeq = matchInterface.getMatchHistoryBySequenceNum(1, 10).get();
            matchDetailsBySeq.forEach(Dota2WebApiQueryEx::displayResult);

            List<Dota2MatchTeamInfo> teams = matchInterface.getTeamInfoById(1, 10).get();
            teams.forEach(Dota2WebApiQueryEx::displayResult);

            List<Dota2TopLiveGame> topLiveGames = matchInterface.getTopLiveGame(1).get();
            topLiveGames.forEach(Dota2WebApiQueryEx::displayResult);

            //Stats
            Dota2RealtimeServerStats serverStats = statsInterface.getRealtimeStats(90105101693392898L).get();
            log.info("Server Stats : {}", serverStats);

            //Stream
            Dota2BroadcasterInfo bInfo = streamInterface.getBroadcasterInfo(292948090, -1).get();
            log.info("Broadcaster Info: {}", bInfo);

            //Teams
            List<Dota2TeamDetails> teamDetails = teamInterface.getTeamInfo(4, -1).get();
            teamDetails.forEach(Dota2WebApiQueryEx::displayResult);
        } finally {
            log.info("Closing Client");
            apiClient.close();
        }
    }

    private static void displayResult(Object result) {
        log.info("{} = {}", result.getClass().getSimpleName(), result.toString());
    }

    @Override
    public void close() throws IOException {
        apiClient.close();
    }
}
