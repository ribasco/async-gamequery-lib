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

package com.ribasco.rglib.protocols.valve.steam;

import com.ribasco.rglib.core.client.AbstractRestClient;
import com.ribasco.rglib.protocols.valve.steam.webapi.*;
import com.ribasco.rglib.protocols.valve.steam.webapi.interfaces.user.ResolveVanityURL;
import com.ribasco.rglib.protocols.valve.steam.webapi.pojos.*;
import org.asynchttpclient.BoundRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by raffy on 9/14/2016.
 */
public class SteamWebApiClient extends AbstractRestClient<SteamWebApiRequest, SteamWebApiResponse> {
    private static final Logger log = LoggerFactory.getLogger(SteamWebApiClient.class);

    private String apiToken;

    /**
     * Default Constructor
     *
     * @param apiToken The steam web webapi token
     */
    public SteamWebApiClient(String apiToken) {
        this.apiToken = apiToken;
    }

    /**
     * Include the API Authentication Token
     *
     * @param builder
     */
    @Override
    protected void prepareRequest(BoundRequestBuilder builder) {
        super.prepareRequest(builder);
        builder.addQueryParam("key", apiToken);
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public static void main(String[] args) {
        String authToken = "903BC0B13739EF74242523BC3013F076";
        SteamWebApiClient apiClient = new SteamWebApiClient(authToken);

        try {
            SteamApps steamApps = new SteamApps(apiClient);
            SteamNews steamNews = new SteamNews(apiClient);
            SteamUserStats steamUserStats = new SteamUserStats(apiClient);
            SteamPlayerService playerService = new SteamPlayerService(apiClient);
            SteamUser steamUser = new SteamUser(apiClient);
            SteamEconomy steamEconomy = new SteamEconomy(apiClient);

            steamApps.getAppList().exceptionally(throwable -> {
                log.error("Error Occured", throwable);
                return new ArrayList<>();
            }).thenAccept(apiClient::displayResult).join();

            steamApps.getServersAtAddress(InetAddress.getByName("103.28.55.237"))
                    .exceptionally(throwable -> {
                        log.error("Error occured", throwable);
                        return new ArrayList<>();
                    })
                    .thenAccept(steamServers -> {
                        steamServers.parallelStream().forEachOrdered(steamServer -> log.info("Server: {}", steamServer.getAddr()));
                    }).join();
            steamApps.getServerUpdateStatus(2146, 550).thenAccept(new Consumer<ServerUpdateStatus>() {
                @Override
                public void accept(ServerUpdateStatus serverUpdateStatus) {
                    log.info("Success: {}, Message: {}, Required Version: {}", serverUpdateStatus.isSuccess(), serverUpdateStatus.getMessage(), serverUpdateStatus.getRequiredVersion());
                }
            }).join();
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
            steamUserStats.getNumberOfCurrentPlayers(550).thenAccept(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    log.info("Total Players : {}", integer);
                }
            }).join();
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
            steamUserStats.getUserStatsForGame(76561198010872093L, 550).thenAccept(new Consumer<SteamPlayerStats>() {
                @Override
                public void accept(SteamPlayerStats playerStats) {
                    log.info("Player Stats:");
                    log.info("- Achievements: {}", playerStats.getAchievements().size());
                    log.info("- Stats: {}", playerStats.getStats().size());
                }
            }).join();

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

            playerService.getSteamLevel(76561198010872093L).thenAccept(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                    log.info("Steam Level : {}", integer);
                }
            }).join();

            playerService.getSteamGameLenderId(76561198010872093L, 550).thenAccept(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    log.info("Lender Id: {}", s);
                }
            }).join();

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

            steamUser.getPlayerProfiles(76561198010872093L).thenAccept(new Consumer<List<SteamPlayerProfile>>() {
                @Override
                public void accept(List<SteamPlayerProfile> steamPlayerProfiles) {
                    steamPlayerProfiles.forEach(p -> log.info("{}", p));
                }
            }).join();

            steamUser.getUserGroupList(76561198010872093L).thenAccept(steamGroupIds -> {
                steamGroupIds.forEach(new Consumer<SteamGroupId>() {
                    @Override
                    public void accept(SteamGroupId steamGroupId) {
                        log.info("Steam Group Id: {}", steamGroupId.getGroupId());
                    }
                });
            }).join();

            steamUser.getSteamIdFromVanityUrl("zenmast3r", ResolveVanityURL.VanityUrlType.DEFAULT).thenAccept(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) {
                    log.info("Got Steam Id From Vanity url: {}", aLong);
                }
            }).join();

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

            steamEconomy.getAssetClassInfo(730, "en", 186150629L, 506856209L, 506856210L, 903185406L,
                    613589849L, 613589850L, 613589851L, 613589852L, 613589853L, 613589854L, 613589855L).thenAccept(new Consumer<Map<String, SteamAssetClassInfo>>() {
                @Override
                public void accept(Map<String, SteamAssetClassInfo> stringSteamAssetClassInfoMap) {
                    stringSteamAssetClassInfoMap.forEach((classId, classInfo) -> {
                        log.info("ID: {}, Info: {}", classId, classInfo);
                    });
                }
            }).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                apiClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayResult(Object result) {
        log.info("Result {}", result);
    }
}
