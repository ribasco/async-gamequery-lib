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

import com.google.common.cache.CacheStats;
import com.ibasco.agql.core.exceptions.ReadTimeoutException;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class SourceServerQueryEx implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceServerQueryEx.class);
    private SourceRconClient sourceRconClient;
    private SourceQueryClient sourceQueryClient;
    private MasterServerQueryClient masterServerQueryClient;

    public SourceServerQueryEx() {
        sourceRconClient = new SourceRconClient();
        sourceQueryClient = new SourceQueryClient();
        masterServerQueryClient = new MasterServerQueryClient();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing");
        sourceRconClient.close();
        sourceQueryClient.close();
        masterServerQueryClient.close();
    }

    public static void main(String[] args) {
        try {
            SourceServerQueryEx app = new SourceServerQueryEx();
            app.queryAllServers();
            //app.listServers();
            app.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listServers() {
        MasterServerFilter filter = new MasterServerFilter()
                .appId(550)
                .dedicated(true)
                //.isEmpty(false)
                .isSecure(true);
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayServerFromMaster).join();
        log.info("DONE");
    }

    private void displayServerFromMaster(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        if (address != null)
            log.info("IP: {}", address);
        else
            log.error("Error : {}", error);
    }


    public void runSimpleTestCached() {
        MasterServerFilter filter = new MasterServerFilter()
                .appId(550)
                .dedicated(true)
                .isEmpty(false)
                .isSecure(true);
        for (int i = 0; i < 3; i++) {
            log.info("Running Iteration #{}", i);
            double start = System.currentTimeMillis();
            runTestCached(8, filter);
            double end = ((System.currentTimeMillis() - start) / 1000) / 60;
            log.info("Iteration #{} Completed in {} minutes", i, end);
        }
    }

    public void queryAllServers() {
        MasterServerFilter filter = MasterServerFilter.create()
                .appId(550)
                .dedicated(true)
                .isEmpty(false)
                .isSecure(true);
        double start = System.currentTimeMillis();
        queryAllServers(10, filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed  in {} minutes", end);
    }

    private Map<String, Double> runTestCached(int sleepTime, MasterServerFilter filter) {
        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        sourceQueryClient.setSleepTime(sleepTime);
        try {
            List<CompletableFuture<?>> requestList = new ArrayList<>();

            try {
                masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                    try {
                        if (masterServerError != null) {
                            log.debug("[MASTER : ERROR] :  From: {} = {}", masterServerSender, masterServerError.getMessage());
                            if (masterServerError instanceof ReadTimeoutException) {
                                masterTimeout.incrementAndGet();
                            } else
                                masterError.incrementAndGet();
                            return;
                        }
                        log.debug("[MASTER : INFO] : {}", serverAddress);
                        masterServerCtr.incrementAndGet();

                        CompletableFuture<SourceServer> infoFuture = sourceQueryClient.getServerInfo(serverAddress).whenComplete((sourceServer, serverInfoError) -> {
                            if (serverInfoError != null) {
                                log.debug("[SERVER : ERROR] : {}", serverInfoError.getMessage());
                                if (serverInfoError instanceof ReadTimeoutException) {
                                    serverInfoTimeout.incrementAndGet();
                                } else
                                    serverInfoErr.incrementAndGet();
                                return;
                            }
                            serverInfoCtr.incrementAndGet();
                            log.debug("[SERVER : INFO] : {}", sourceServer);
                        });
                        requestList.add(infoFuture);

                        //Get Challenge
                        CompletableFuture<Integer> challengeFuture = sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress);
                        requestList.add(challengeFuture);

                        CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayersCached(serverAddress);
                        playersFuture.whenComplete((players, playerError) -> {
                            if (playerError != null) {
                                log.debug("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
                                if (playerError instanceof ReadTimeoutException)
                                    playersTimeout.incrementAndGet();
                                else
                                    playersOtherErr.incrementAndGet();
                                return;
                            }
                            playersCtr.incrementAndGet();
                            log.debug("[PLAYERS : INFO] : PlayerData = {}", players);
                        });
                        requestList.add(playersFuture);

                        CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRulesCached(serverAddress);
                        rulesFuture.whenComplete((rules, rulesError) -> {
                            if (rulesError != null) {
                                log.debug("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
                                if (rulesError instanceof ReadTimeoutException)
                                    rulesTimeout.incrementAndGet();
                                else
                                    rulesOtherErr.incrementAndGet();
                                return;
                            }
                            rulesCtr.incrementAndGet();
                            log.debug("[RULES : INFO] Rules = {}", rules);
                        });

                        requestList.add(rulesFuture);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }).exceptionally(throwable -> new Vector<>()).join(); //masterServerList

                log.info("Waiting for ALL requests to complete...");
                CompletableFuture.allOf(requestList.toArray(new CompletableFuture[0])).whenComplete(new BiConsumer<Void, Throwable>() {
                    @Override
                    public void accept(Void aVoid, Throwable throwable) {
                        log.info("REQUESTS FINISHED PROCESSING");
                    }
                }).join();
            } catch (Exception e) {
                log.error("Error master server : {}", e.getMessage());
            }


            log.debug("   Total Master Server Retrieved: {}", masterServerCtr);
            log.debug("   Total Master Server Error (Others): {}", masterError);
            log.debug("   Total Master Server Error (Timeout): {}", masterTimeout);
            log.debug(" ");
            log.debug("   Total Server Info Retrieved: {}", serverInfoCtr);
            log.debug("   Total Server Info Error (Others): {}", serverInfoErr);
            log.debug("   Total Server Info Error (Timeout): {}", serverInfoTimeout);
            log.debug(" ");
            log.debug("   Total Challenge Numbers Received: {}", challengeCtr);
            log.debug("   Total Challenge Error (Others): {}", challengeErr);
            log.debug("   Total Challenge Error (Timeout): {}", challengeTimeout);
            log.debug(" ");
            log.debug("   Total Player Records Received: {}", playersCtr);
            log.debug("   Total Player Error (Others): {}", playersOtherErr);
            log.debug("   Total Player Error (Timeout): {}", playersTimeout);
            log.debug(" ");
            log.debug("   Total Rules Records Received: {}", rulesCtr);
            log.debug("   Total Rules Error (Others): {}", rulesOtherErr);
            log.debug("   Total Rules Error (Timeout): {}", rulesTimeout);
            log.debug(" ");
            log.debug("   Total Challenge Entries in Cache : {}", sourceQueryClient.getChallengeCache().size());
            CacheStats stats = sourceQueryClient.getChallengeCache().stats();
            log.debug("   Cache Stats: Average load penalty: {}, Load Count: {}, Load Exception Count: {}, Load Success Count: {}, Hit Count: {}, Hit Rate: {}", stats.averageLoadPenalty(), stats.loadCount(), stats.loadExceptionCount(), stats.loadSuccessCount(), stats.hitCount(), stats.hitRate());

            successRateInfo = Math.round((serverInfoCtr.doubleValue() / masterServerCtr.doubleValue()) * 100.0D);
            successRateChallenge = Math.round((challengeCtr.doubleValue() / masterServerCtr.doubleValue()) * 100.0D);
            successRatePlayer = Math.round((playersCtr.doubleValue() / challengeCtr.doubleValue()) * 100.0D);
            successRateRules = Math.round((rulesCtr.doubleValue() / challengeCtr.doubleValue()) * 100.0D);

            resultMap.put("masterTotal", masterServerCtr.doubleValue());
            resultMap.put("masterErrorOther", masterError.doubleValue());
            resultMap.put("masterErrorTimeout", masterTimeout.doubleValue());

            resultMap.put("infoTotal", serverInfoCtr.doubleValue());
            resultMap.put("infoErrorOther", serverInfoErr.doubleValue());
            resultMap.put("infoErrorTimeout", serverInfoTimeout.doubleValue());
            resultMap.put("infoRate", successRateInfo);

            resultMap.put("challengeTotal", challengeCtr.doubleValue());
            resultMap.put("challengeErrorOther", challengeErr.doubleValue());
            resultMap.put("challengeErrorTimeout", challengeTimeout.doubleValue());
            resultMap.put("challengeRate", successRateChallenge);

            resultMap.put("playerTotal", playersCtr.doubleValue());
            resultMap.put("playerErrorOther", playersOtherErr.doubleValue());
            resultMap.put("playerErrorTimeout", playersTimeout.doubleValue());
            resultMap.put("playerRate", successRatePlayer);

            resultMap.put("rulesTotal", rulesCtr.doubleValue());
            resultMap.put("rulesErrorOther", rulesOtherErr.doubleValue());
            resultMap.put("rulesErrorTimeout", rulesTimeout.doubleValue());
            resultMap.put("rulesRate", successRateRules);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Double> queryAllServers(int sleepTime, MasterServerFilter filter) {

        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        try {
            sourceQueryClient.setSleepTime(sleepTime);

            List<CompletableFuture<?>> requestList = new ArrayList<>();

            try {
                masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                    try {
                        if (masterServerError != null) {
                            log.debug("[MASTER : ERROR] :  From: {} = {}", masterServerSender, masterServerError.getMessage());
                            if (masterServerError instanceof ReadTimeoutException) {
                                masterTimeout.incrementAndGet();
                            } else
                                masterError.incrementAndGet();
                            return;
                        }

                        log.debug("[MASTER : INFO] : {}", serverAddress);
                        masterServerCtr.incrementAndGet();

                        CompletableFuture<SourceServer> infoFuture = sourceQueryClient.getServerInfo(serverAddress).whenComplete((sourceServer, serverInfoError) -> {
                            if (serverInfoError != null) {
                                log.debug("[SERVER : ERROR] : {}", serverInfoError.getMessage());
                                if (serverInfoError instanceof ReadTimeoutException) {
                                    serverInfoTimeout.incrementAndGet();
                                } else
                                    serverInfoErr.incrementAndGet();
                                return;
                            }
                            serverInfoCtr.incrementAndGet();
                            log.debug("[SERVER : INFO] : {}", sourceServer);
                        });

                        requestList.add(infoFuture);

                        //Get Challenge
                        CompletableFuture<Integer> challengeFuture = sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress)
                                .whenComplete((challenge, serverChallengeError) -> {
                                    if (serverChallengeError != null) {
                                        log.debug("[CHALLENGE : ERROR] Message: '{}')", serverChallengeError.getMessage());
                                        if (serverChallengeError instanceof ReadTimeoutException)
                                            challengeTimeout.incrementAndGet();
                                        else
                                            challengeErr.incrementAndGet();
                                        return;
                                    }
                                    log.debug("[CHALLENGE : INFO] Challenge '{}'", challenge);
                                    challengeCtr.incrementAndGet();

                                    CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayers(challenge, serverAddress);

                                    playersFuture.whenComplete((players, playerError) -> {
                                        if (playerError != null) {
                                            log.debug("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
                                            if (playerError instanceof ReadTimeoutException)
                                                playersTimeout.incrementAndGet();
                                            else
                                                playersOtherErr.incrementAndGet();
                                            return;
                                        }
                                        playersCtr.incrementAndGet();
                                        log.debug("[PLAYERS : INFO] : PlayerData = {}", players);
                                    });
                                    requestList.add(playersFuture);

                                    CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(challenge, serverAddress);
                                    rulesFuture.whenComplete((rules, rulesError) -> {
                                        if (rulesError != null) {
                                            log.debug("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
                                            if (rulesError instanceof ReadTimeoutException)
                                                rulesTimeout.incrementAndGet();
                                            else
                                                rulesOtherErr.incrementAndGet();
                                            return;
                                        }
                                        rulesCtr.incrementAndGet();
                                        log.debug("[RULES : INFO] Rules = {}", rules);
                                    });
                                    requestList.add(rulesFuture);
                                });
                        requestList.add(challengeFuture);
                    } catch (Exception e) {
                        log.error("Error occured inside the master list callback", e);
                    }
                }).get(); //masterServerList

                log.info("Waiting for requests to complete. There are a total of {} requests in the list", requestList.size());
                CompletableFuture.allOf(requestList.toArray(new CompletableFuture[0])).get();

            } catch (Exception e) {
                log.error("Error occured AFTER the master list join", e);
            } finally {
                log.debug("   Total Master Server Retrieved: {}", masterServerCtr);
                log.debug("   Total Master Server Error (Others): {}", masterError);
                log.debug("   Total Master Server Error (Timeout): {}", masterTimeout);
                log.debug(" ");
                log.debug("   Total Server Info Retrieved: {}", serverInfoCtr);
                log.debug("   Total Server Info Error (Others): {}", serverInfoErr);
                log.debug("   Total Server Info Error (Timeout): {}", serverInfoTimeout);
                log.debug(" ");
                log.debug("   Total Challenge Numbers Received: {}", challengeCtr);
                log.debug("   Total Challenge Error (Others): {}", challengeErr);
                log.debug("   Total Challenge Error (Timeout): {}", challengeTimeout);
                log.debug(" ");
                log.debug("   Total Player Records Received: {}", playersCtr);
                log.debug("   Total Player Error (Others): {}", playersOtherErr);
                log.debug("   Total Player Error (Timeout): {}", playersTimeout);
                log.debug(" ");
                log.debug("   Total Rules Records Received: {}", rulesCtr);
                log.debug("   Total Rules Error (Others): {}", rulesOtherErr);
                log.debug("   Total Rules Error (Timeout): {}", rulesTimeout);
            }

            successRateInfo = Math.round((serverInfoCtr.doubleValue() / masterServerCtr.doubleValue()) * 100.0D);
            successRateChallenge = Math.round((challengeCtr.doubleValue() / masterServerCtr.doubleValue()) * 100.0D);
            successRatePlayer = Math.round((playersCtr.doubleValue() / challengeCtr.doubleValue()) * 100.0D);
            successRateRules = Math.round((rulesCtr.doubleValue() / challengeCtr.doubleValue()) * 100.0D);

            resultMap.put("masterTotal", masterServerCtr.doubleValue());
            resultMap.put("masterErrorOther", masterError.doubleValue());
            resultMap.put("masterErrorTimeout", masterTimeout.doubleValue());

            resultMap.put("infoTotal", serverInfoCtr.doubleValue());
            resultMap.put("infoErrorOther", serverInfoErr.doubleValue());
            resultMap.put("infoErrorTimeout", serverInfoTimeout.doubleValue());
            resultMap.put("infoRate", successRateInfo);

            resultMap.put("challengeTotal", challengeCtr.doubleValue());
            resultMap.put("challengeErrorOther", challengeErr.doubleValue());
            resultMap.put("challengeErrorTimeout", challengeTimeout.doubleValue());
            resultMap.put("challengeRate", successRateChallenge);

            resultMap.put("playerTotal", playersCtr.doubleValue());
            resultMap.put("playerErrorOther", playersOtherErr.doubleValue());
            resultMap.put("playerErrorTimeout", playersTimeout.doubleValue());
            resultMap.put("playerRate", successRatePlayer);

            resultMap.put("rulesTotal", rulesCtr.doubleValue());
            resultMap.put("rulesErrorOther", rulesOtherErr.doubleValue());
            resultMap.put("rulesErrorTimeout", rulesTimeout.doubleValue());
            resultMap.put("rulesRate", successRateRules);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
