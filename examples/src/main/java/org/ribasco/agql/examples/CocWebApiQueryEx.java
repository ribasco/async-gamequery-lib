package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.supercell.coc.webapi.CocSearchCriteria;
import org.ribasco.agql.protocols.supercell.coc.webapi.CocWebApiClient;
import org.ribasco.agql.protocols.supercell.coc.webapi.enums.CocWarFrequency;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.CocClans;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.CocLeagues;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.CocLocations;
import org.ribasco.agql.protocols.supercell.coc.webapi.interfaces.CocPlayers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CocWebApiQueryEx {
    private static final Logger log = LoggerFactory.getLogger(CocWebApiQueryEx.class);

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2" +
                "NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImVmZjE4NGVhLTJkNzMtND" +
                "E0NC04NjY5LTg3NmU2NmYxZGFiNiIsImlhdCI6MTQ3NzIwNDIwOSwic3ViIjoiZGV2ZWxvcGVyLzM3NTAwMGFiLWVkMmUtOTU" +
                "yNC1hNzhlLTMyMDJlNTc0MGFiMCIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2ls" +
                "dmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjQ5LjE0Ny4xNS4xIl0sInR5cGUiOiJjbGllbnQifV19.GxzA" +
                "PxGSCchWxATXaJBUOtJviRvOlK1U7FnxnR5tYKPrQsZ2MSMsPJC0PCAZ4Tfi0abCTBzNwn5xy3bmHc4aQQ";

        try (CocWebApiClient client = new CocWebApiClient(token)) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void displayListResults(List<T> list) {
        list.forEach(o -> log.info("{}", o.toString()));
    }
}
