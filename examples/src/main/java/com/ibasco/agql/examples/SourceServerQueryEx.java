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

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceServerQueryEx extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceServerQueryEx.class);
    private SourceQueryClient sourceQueryClient;
    private MasterServerQueryClient masterServerQueryClient;

    public static void main(String[] args) throws Exception {
        SourceServerQueryEx q = new SourceServerQueryEx();
        q.run();
    }

    public void queryAllServers() {
        int appId = Integer.valueOf(promptInput("List servers only from this app id (int) [none]: ", false, "-1"));
        Boolean emptyServers = promptInputBool("List only empty servers? (y/n) [none]: ", false, null);
        Boolean passwordProtected = promptInputBool("List only password protected servers? (y/n) [none]: ", false, null);
        Boolean dedicatedServers = promptInputBool("List only dedicated servers (y/n) [y]: ", false, "y");

        MasterServerFilter filter = MasterServerFilter.create()
                .dedicated(dedicatedServers)
                .isPasswordProtected(passwordProtected)
                .allServers()
                .isEmpty(emptyServers);

        if (appId > 0)
            filter.appId(appId);

        log.info("Using filter: {}", filter.toString());

        double start = System.currentTimeMillis();
        queryAllServers(filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed  in {} minutes", end);
    }

    private Map<String, Double> queryAllServers(MasterServerFilter filter) {

        final Map<String, Double> resultMap = new HashMap<>();

        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesErr = new AtomicInteger();

        try {
            List<CompletableFuture<?>> requestList = new ArrayList<>();

            try {
                masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                    try {
                        if (masterServerError != null) {
                            log.error("[MASTER : ERROR] :  From: {} = {}", masterServerSender, masterServerError.getMessage());
                            masterError.incrementAndGet();
                            return;
                        }

                        log.info("[MASTER : INFO] : {}", serverAddress);
                        masterServerCtr.incrementAndGet();

                        CompletableFuture<SourceServer> infoFuture = sourceQueryClient.getServerInfo(serverAddress).whenComplete((sourceServer, serverInfoError) -> {
                            if (serverInfoError != null) {
                                log.error("[SERVER : ERROR] : {}", serverInfoError.getMessage());
                                serverInfoErr.incrementAndGet();
                                return;
                            }
                            serverInfoCtr.incrementAndGet();
                            log.info("[SERVER : INFO] : {}", sourceServer);
                        });

                        requestList.add(infoFuture);

                        //Get Challenge
                        CompletableFuture<Integer> challengeFuture = sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress)
                                .whenComplete((challenge, serverChallengeError) -> {
                                    if (serverChallengeError != null) {
                                        log.error("[CHALLENGE : ERROR] Message: '{}')", serverChallengeError.getMessage());
                                        challengeErr.incrementAndGet();
                                        return;
                                    }
                                    log.info("[CHALLENGE : INFO] Challenge '{}'", challenge);
                                    challengeCtr.incrementAndGet();

                                    CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayers(challenge, serverAddress);

                                    playersFuture.whenComplete((players, playerError) -> {
                                        if (playerError != null) {
                                            log.error("[PLAYERS : ERROR] Message: '{}')", playerError.getMessage());
                                            playersErr.incrementAndGet();
                                            return;
                                        }
                                        playersCtr.incrementAndGet();
                                        log.info("[PLAYERS : INFO] : Total Players for Server '{}' = {}", serverAddress, players.size());
                                        players.forEach(player -> log.info("    Index: {}, Name: {}, Duration: {}, Score: {}", player.getIndex(), player.getName(), player.getDuration(), player.getScore()));
                                    });
                                    requestList.add(playersFuture);

                                    CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(challenge, serverAddress);
                                    rulesFuture.whenComplete((rules, rulesError) -> {
                                        if (rulesError != null) {
                                            log.error("[RULES : ERROR] Message: '{}')", rulesError.getMessage());
                                            rulesErr.incrementAndGet();
                                            return;
                                        }
                                        rulesCtr.incrementAndGet();
                                        log.info("[RULES : INFO] Total # of Rules for Server '{}' = {}", serverAddress, rules.size());
                                        rules.forEach((key, value) -> log.info("    {} = {}", key, value));
                                    });
                                    requestList.add(rulesFuture);
                                });
                        requestList.add(challengeFuture);
                    } catch (Exception e) {
                        log.error("Error occured inside the master list callback", e);
                    }
                }).get(); //masterServerList

                log.info("Waiting for {} requests to complete", requestList.size());
                CompletableFuture[] futures = requestList.toArray(new CompletableFuture[0]);
                CompletableFuture.allOf(futures).get();
            } catch (Exception e) {
                log.error("Error occured during processing", e);
            } finally {
                log.info("   Total Master Server Retrieved: {}", masterServerCtr);
                log.info("   Total Master Server Error: {}", masterError);
                log.info(" ");
                log.info("   Total Server Info Retrieved: {}", serverInfoCtr);
                log.info("   Total Server Info Error: {}", serverInfoErr);
                log.info(" ");
                log.info("   Total Challenge Numbers Received: {}", challengeCtr);
                log.info("   Total Challenge Error: {}", challengeErr);
                log.info(" ");
                log.info("   Total Player Records Received: {}", playersCtr);
                log.info("   Total Player Error: {}", playersErr);
                log.info(" ");
                log.info("   Total Rules Records Received: {}", rulesCtr);
                log.info("   Total Rules Error: {}", rulesErr);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() throws Exception {
        //Inititalize client
        sourceQueryClient = new SourceQueryClient();
        masterServerQueryClient = new MasterServerQueryClient();

        this.queryAllServers();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing");
        sourceQueryClient.close();
        masterServerQueryClient.close();
    }
}
