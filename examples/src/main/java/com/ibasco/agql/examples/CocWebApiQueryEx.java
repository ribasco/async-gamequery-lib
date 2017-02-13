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
import com.ibasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.enums.CocWarFrequency;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocClans;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocLeagues;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocLocations;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocPlayers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CocWebApiQueryEx extends BaseWebApiAuthExample {
    private static final Logger log = LoggerFactory.getLogger(CocWebApiQueryEx.class);

    private CocWebApiClient client;

    public static <T> void displayListResults(List<T> list) {
        list.forEach(o -> log.info("{}", o.toString()));
    }

    public static void main(String[] args) {
        CocWebApiQueryEx app = new CocWebApiQueryEx();
        app.run();
    }

    @Override
    public void run() {
        String token = getToken("supercell");
        client = new CocWebApiClient(token);

        //Instantiate api interfaces
        CocClans clans = new CocClans(client);
        CocLocations locations = new CocLocations(client);
        CocLeagues leagues = new CocLeagues(client);
        CocPlayers players = new CocPlayers(client);

        log.info("Search Clans");
        clans.searchClans(CocSearchCriteria.create().warFrequency(CocWarFrequency.ALWAYS).limit(10)).thenAccept(clanInfos -> log.info("Size: {}, Data: {}", clanInfos.size(), clanInfos)).join();
        log.info("Get Clan Info");
        clans.getClanInfo("#PUYJGC2U").thenAccept(clanInfo -> log.info("Clan Info: {}", clanInfo)).join();
        log.info("Get Clan Members");
        clans.getClanMembers("#PUYJGC2U").thenAccept(cocPlayers -> cocPlayers.forEach(cocPlayer -> log.info("{}", cocPlayer))).join();
        log.info("Get Clan Warlog");
        clans.getClanWarLog("#PUYJGC2U").thenAccept(cocWarLogEntries -> cocWarLogEntries.forEach(cocWarLogEntry -> log.info("War Log Entry: {}", cocWarLogEntry))).join();

        log.info("Get Locations");
        locations.getLocations().thenAccept(cocClanLocations -> cocClanLocations.forEach(cocClanLocation -> log.info("Location: {}", cocClanLocation))).join();
        locations.getLocationInfo(32000000).thenAccept(cocLocation -> log.info("Single Location: {}", cocLocation)).join();
        locations.getClanRankingsFromLocation(32000185).thenAccept(cocClanRankInfos -> cocClanRankInfos.forEach(cocClanRankInfo -> log.info("Ranking: {}", cocClanRankInfo))).join();
        log.info("Displaying Player Rankings by Location");
        locations.getPlayerRankingsFromLocation(32000185).thenAccept(CocWebApiQueryEx::displayListResults).join();

        log.info("Displaying Leagues");
        leagues.getLeagueList().thenAccept(CocWebApiQueryEx::displayListResults).join();
        log.info("Displaying League Seasons");
        leagues.getLeagueSeasons(29000022).thenAccept(CocWebApiQueryEx::displayListResults).join();
        log.info("Displaying League Season Player Rankings");
        leagues.getLeagueSeasonsPlayerRankings(29000022, "2016-06", 10).thenAccept(CocWebApiQueryEx::displayListResults).join();

        log.info("Retrieving Detailed Player Information");
        players.getPlayerInfo("#J0PYGCG").thenAccept(p -> log.info("Player Info: {}", p)).join();
    }

    @Override
    public void close() throws IOException {
        if (client != null)
            client.close();
    }
}
