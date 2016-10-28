/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi;

import org.asynchttpclient.BoundRequestBuilder;
import org.ribasco.asyncgamequerylib.core.client.AbstractRestClient;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.enums.CocWarFrequency;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.interfaces.CocClans;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.interfaces.CocLocations;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.pojos.CocClanDetailedInfo;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.pojos.CocClanRankInfo;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.pojos.CocPlayerRankInfo;
import org.ribasco.asyncgamequerylib.protocols.supercell.clashofclans.webapi.pojos.CocWarLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by raffy on 10/27/2016.
 */
public class CocWebApiClient extends AbstractRestClient<CocWebApiRequest, CocWebApiResponse> {

    private static final Logger log = LoggerFactory.getLogger(CocWebApiClient.class);

    private String apiToken;

    public CocWebApiClient(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    protected void prepareRequest(BoundRequestBuilder builder) {
        builder.addHeader("authorization", String.format("Bearer %s", this.apiToken));
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2" +
                "NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImVmZjE4NGVhLTJkNzMtND" +
                "E0NC04NjY5LTg3NmU2NmYxZGFiNiIsImlhdCI6MTQ3NzIwNDIwOSwic3ViIjoiZGV2ZWxvcGVyLzM3NTAwMGFiLWVkMmUtOTU" +
                "yNC1hNzhlLTMyMDJlNTc0MGFiMCIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2ls" +
                "dmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjQ5LjE0Ny4xNS4xIl0sInR5cGUiOiJjbGllbnQifV19.GxzA" +
                "PxGSCchWxATXaJBUOtJviRvOlK1U7FnxnR5tYKPrQsZ2MSMsPJC0PCAZ4Tfi0abCTBzNwn5xy3bmHc4aQQ";

        try (CocWebApiClient client = new CocWebApiClient(token)) {
            CocClans clans = new CocClans(client);
            CocLocations locations = new CocLocations(client);

            clans.searchClans(CocSearchCriteria.create().warFrequency(CocWarFrequency.ALWAYS).limit(10)).thenAccept(new Consumer<List<CocClanDetailedInfo>>() {
                @Override
                public void accept(List<CocClanDetailedInfo> clanInfos) {
                    log.info("Size: {}, Data: {}", clanInfos.size(), clanInfos);
                }
            }).join();
            clans.getClanInfo("#PUYJGC2U").thenAccept(new Consumer<CocClanDetailedInfo>() {
                @Override
                public void accept(CocClanDetailedInfo clanInfo) {
                    log.info("Clan Info: {}", clanInfo);
                }
            }).join();

            clans.getClanMembers("#PUYJGC2U")
                    .thenAccept(cocPlayers -> cocPlayers.forEach(cocPlayer -> log.info("{}", cocPlayer)))
                    .join();

            clans.getClanWarLog("#PUYJGC2U").thenAccept(new Consumer<List<CocWarLogEntry>>() {
                @Override
                public void accept(List<CocWarLogEntry> cocWarLogEntries) {
                    cocWarLogEntries.forEach(new Consumer<CocWarLogEntry>() {
                        @Override
                        public void accept(CocWarLogEntry cocWarLogEntry) {
                            log.info("War Log Entry: {}", cocWarLogEntry);
                        }
                    });
                }
            }).join();

            locations.getLocations().thenAccept(cocClanLocations -> cocClanLocations.forEach(cocClanLocation -> log.info("Location: {}", cocClanLocation))).join();

            locations.getLocationInfo(32000000).thenAccept(cocLocation -> log.info("Single Location: {}", cocLocation)).join();

            locations.getClanRankingsFromLocation(32000185).thenAccept(new Consumer<List<CocClanRankInfo>>() {
                @Override
                public void accept(List<CocClanRankInfo> cocClanRankInfos) {
                    cocClanRankInfos.forEach(new Consumer<CocClanRankInfo>() {
                        @Override
                        public void accept(CocClanRankInfo cocClanRankInfo) {
                            log.info("Ranking: {}", cocClanRankInfo);
                        }
                    });
                }
            }).join();

            locations.getPlayerRankingsFromLocation(32000185).thenAccept(new Consumer<List<CocPlayerRankInfo>>() {
                @Override
                public void accept(List<CocPlayerRankInfo> cocPlayerRankInfos) {
                    cocPlayerRankInfos.forEach(new Consumer<CocPlayerRankInfo>() {
                        @Override
                        public void accept(CocPlayerRankInfo cocPlayerRankInfo) {
                            log.info("Player Rankings: {}", cocPlayerRankInfo);
                        }
                    });
                }
            }).join();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
