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

import com.ibasco.agql.examples.base.BaseWebApiAuthExample;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria;
import com.ibasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import com.ibasco.agql.protocols.supercell.coc.webapi.enums.CocWarFrequency;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocClans;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocLeagues;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocLocations;
import com.ibasco.agql.protocols.supercell.coc.webapi.interfaces.CocPlayers;
import org.jetbrains.annotations.ApiStatus;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>CocWebApiExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class CocWebApiExample extends BaseWebApiAuthExample {

    private static final Logger log = LoggerFactory.getLogger(CocWebApiExample.class);

    private CocWebApiClient client;

    /**
     * <p>main.</p>
     *
     * @param args
     *         an array of {@link java.lang.String} objects
     */
    public static void main(String[] args) {
        new CocWebApiExample().run(args);

    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) {
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
        locations.getPlayerRankingsFromLocation(32000185).thenAccept(CocWebApiExample::displayListResults).join();

        log.info("Displaying Leagues");
        leagues.getLeagueList().thenAccept(CocWebApiExample::displayListResults).join();
        log.info("Displaying League Seasons");
        leagues.getLeagueSeasons(29000022).thenAccept(CocWebApiExample::displayListResults).join();
        log.info("Displaying League Season Player Rankings");
        leagues.getLeagueSeasonsPlayerRankings(29000022, "2016-06", 10).thenAccept(CocWebApiExample::displayListResults).join();

        log.info("Retrieving Detailed Player Information");
        players.getPlayerInfo("#J0PYGCG").thenAccept(p -> log.info("Player Info: {}", p)).join();
    }

    /**
     * <p>displayListResults.</p>
     *
     * @param list
     *         a {@link java.util.List} object
     * @param <T>
     *         a T class
     */
    public static <T> void displayListResults(List<T> list) {
        list.forEach(o -> log.info("{}", o.toString()));
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (client != null)
            client.close();
    }
}
