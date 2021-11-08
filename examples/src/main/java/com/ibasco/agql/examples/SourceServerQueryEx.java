/*
 * Copyright 2021 Rafael Luis L. Ibasco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.examples;

import com.ibasco.agql.core.utils.ConcurrentUtils;
import com.ibasco.agql.core.utils.NetUtils;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceChallengeType;
import com.ibasco.agql.protocols.valve.source.query.exceptions.SourceChallengeException;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class SourceServerQueryEx extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceServerQueryEx.class);

    private SourceQueryClient sourceQueryClient;

    private MasterServerQueryClient masterServerQueryClient;

    public static void main(String[] args) throws Exception {
        new SourceServerQueryEx().run();
    }

    public void performQuery() throws Exception {
        Boolean queryAllServers = promptInputBool("Query all available servers? (y/n)", true, "n");

        double start = 0.0d, end = 0.0d;

        if (queryAllServers) {
            int appId = Integer.parseInt(promptInput("List servers only from this app id (int)", false, null, "srcQryAppId"));
            Boolean emptyServers = promptInputBool("List only empty servers? (y/n)", false, null, "srcQryEmptySvrs");
            Boolean passwordProtected = promptInputBool("List only passwd protected servers? (y/n)", false, null, "srcQryPassProtect");
            Boolean dedicatedServers = promptInputBool("List only dedicated servers (y/n)", false, "y", "srcQryDedicated");
            MasterServerFilter filter = MasterServerFilter.create()
                                                          .dedicated(dedicatedServers)
                                                          .isPasswordProtected(passwordProtected)
                                                          .allServers()
                                                          .isEmpty(emptyServers);

            if (appId > 0)
                filter.appId(appId);

            log.info("Using filter: {}", filter.toString());

            start = System.currentTimeMillis();
            performQuery(filter);
            end = ((System.currentTimeMillis() - start) / 1000) / 60;
        } else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, "192.168.1.34:27015"); //127.0.0.1:27015
            /*InetSocketAddress address = NetUtils.parseAddress(addressString, 27015);

            log.info("START: Info query on address: {}:{}", address.getHostString(), address.getPort());
            start = System.currentTimeMillis();
            log.info("Obtaining challenge number using INFO request packet");
            AtomicInteger challengeNumber = new AtomicInteger();
            challengeNumber.set(getServerChallenge(address, SourceChallengeType.INFO));
            log.info("Received info challenge number from server: {}", challengeNumber.get());
            log.info("Querying server info using challenge number '{}'", challengeNumber.get());*/

            AtomicInteger challengeNumber = new AtomicInteger();
            final InetSocketAddress[] addressList = {
                    new InetSocketAddress("116.251.214.166", 13371),
                    new InetSocketAddress("103.62.48.1", 27015),
                    new InetSocketAddress("103.10.124.53", 27230),
                    new InetSocketAddress("116.251.214.166", 13374),
                    new InetSocketAddress("103.10.124.51", 27109),
                    new InetSocketAddress("103.10.124.53", 27187),
                    new InetSocketAddress("103.10.124.57", 27119),
                    new InetSocketAddress("103.10.124.59", 27195),
                    new InetSocketAddress("139.99.39.9", 27015),
                    new InetSocketAddress("139.99.5.139", 27015),
                    new InetSocketAddress("139.99.22.193", 27015),
                    new InetSocketAddress("103.10.124.60", 27122),
                    new InetSocketAddress("159.65.129.148", 27018),
                    new InetSocketAddress("103.10.124.57", 27163),
                    new InetSocketAddress("103.10.124.55", 27190),
                    new InetSocketAddress("121.121.202.124", 27019),
                    new InetSocketAddress("121.121.202.124", 27018),
                    new InetSocketAddress("121.121.202.124", 27016),
                    new InetSocketAddress("155.133.232.24", 27138),
                    new InetSocketAddress("155.133.232.23", 27149),
                    new InetSocketAddress("155.133.232.23", 27153),
                    new InetSocketAddress("155.133.249.25", 27204),
                    new InetSocketAddress("155.133.249.21", 27123),
                    new InetSocketAddress("190.217.33.89", 27136),
                    new InetSocketAddress("155.133.249.20", 27122),
                    new InetSocketAddress("190.217.33.86", 27142),
                    new InetSocketAddress("190.217.33.89", 27212),
                    new InetSocketAddress("155.133.249.20", 27165),
                    new InetSocketAddress("190.217.33.88", 27113),
                    new InetSocketAddress("155.133.249.26", 27171),
                    new InetSocketAddress("190.217.33.86", 27191),
                    new InetSocketAddress("190.217.33.89", 27161),
                    new InetSocketAddress("155.133.249.20", 27116),
                    new InetSocketAddress("190.217.33.86", 27175),
                    new InetSocketAddress("190.217.33.87", 27160),
                    new InetSocketAddress("190.217.33.89", 27138),
                    new InetSocketAddress("190.217.33.86", 27100),
                    new InetSocketAddress("190.217.33.86", 27127),
                    new InetSocketAddress("190.217.33.86", 27231),
                    new InetSocketAddress("190.217.33.87", 27174),
                    new InetSocketAddress("155.133.249.26", 27097),
                    new InetSocketAddress("155.133.249.20", 27157),
                    new InetSocketAddress("190.217.33.87", 27134),
                    new InetSocketAddress("190.217.33.89", 27226),
                    new InetSocketAddress("155.133.249.26", 27127),
                    new InetSocketAddress("190.217.33.88", 27214),
                    new InetSocketAddress("155.133.249.20", 27155),
                    new InetSocketAddress("190.217.33.87", 27124),
                    new InetSocketAddress("155.133.249.20", 27105),
                    new InetSocketAddress("190.217.33.86", 27141),
                    new InetSocketAddress("155.133.249.26", 27140),
                    new InetSocketAddress("155.133.249.25", 27223),
                    new InetSocketAddress("190.217.33.86", 27168),
                    new InetSocketAddress("190.217.33.86", 27216),
                    new InetSocketAddress("190.217.33.89", 27187),
                    new InetSocketAddress("155.133.249.25", 27187),
                    new InetSocketAddress("190.217.33.88", 27127),
                    new InetSocketAddress("131.221.33.162", 27015),
                    new InetSocketAddress("155.133.249.21", 27149),
                    new InetSocketAddress("190.217.33.86", 27137),
                    new InetSocketAddress("155.133.249.21", 27216),
                    new InetSocketAddress("155.133.249.21", 27183),
                    new InetSocketAddress("190.217.33.86", 27102),
                    new InetSocketAddress("155.133.249.26", 27124),
                    new InetSocketAddress("190.217.33.86", 27204),
                    new InetSocketAddress("190.217.33.87", 27149),
                    new InetSocketAddress("155.133.249.26", 27165),
                    new InetSocketAddress("190.217.33.88", 27107),
                    new InetSocketAddress("190.217.33.86", 27116),
                    new InetSocketAddress("155.133.249.26", 27220),
                    new InetSocketAddress("155.133.249.26", 27096),
                    new InetSocketAddress("155.133.249.26", 27222),
                    new InetSocketAddress("190.217.33.86", 27218),
                    new InetSocketAddress("190.217.33.89", 27106),
                    new InetSocketAddress("155.133.249.26", 27144),
                    new InetSocketAddress("155.133.249.25", 27175),
                    new InetSocketAddress("155.133.249.20", 27133),
                    new InetSocketAddress("155.133.249.25", 27157),
                    new InetSocketAddress("190.171.168.35", 27020),
                    new InetSocketAddress("155.133.249.21", 27152),
                    new InetSocketAddress("190.217.33.89", 27095),
                    new InetSocketAddress("190.217.33.89", 27195),
                    new InetSocketAddress("155.133.249.21", 27107),
                    new InetSocketAddress("190.217.33.88", 27179),
                    new InetSocketAddress("190.217.33.87", 27233),
                    new InetSocketAddress("190.217.33.89", 27146),
                    new InetSocketAddress("190.217.33.89", 27163),
                    new InetSocketAddress("190.217.33.88", 27111),
                    new InetSocketAddress("155.133.249.20", 27132),
                    new InetSocketAddress("155.133.249.21", 27198),
                    new InetSocketAddress("155.133.249.20", 27191),
                    new InetSocketAddress("190.217.33.89", 27182),
                    new InetSocketAddress("190.217.33.86", 27207),
                    new InetSocketAddress("190.217.33.89", 27096),
                    new InetSocketAddress("155.133.249.26", 27135),
                    new InetSocketAddress("155.133.249.20", 27197),
                    new InetSocketAddress("155.133.249.26", 27164),
                    new InetSocketAddress("190.217.33.89", 27178),
                    new InetSocketAddress("190.217.33.87", 27196),
                    new InetSocketAddress("155.133.249.25", 27119),
                    new InetSocketAddress("190.217.33.86", 27125),
                    new InetSocketAddress("155.133.249.20", 27113),
                    new InetSocketAddress("190.217.33.89", 27166),
                    new InetSocketAddress("190.217.33.87", 27147),
                    new InetSocketAddress("190.217.33.87", 27171),
                    new InetSocketAddress("155.133.249.20", 27102),
                    new InetSocketAddress("155.133.249.20", 27195),
                    new InetSocketAddress("190.217.33.86", 27138)
            };

            CountDownLatch latch = new CountDownLatch(addressList.length);

            log.debug("Querying a total of {} addresses", addressList.length);
            for (InetSocketAddress address : addressList) {
                log.debug("Querying Server: {}", address);
                sourceQueryClient.getServerChallenge(SourceChallengeType.INFO, address).whenComplete(new BiConsumer<Integer, Throwable>() {
                    @Override
                    public void accept(Integer challenge, Throwable throwable) {
                        if (throwable != null) {
                            latch.countDown();
                            throw new CompletionException(String.format("Failed to retrieve challenge number from server '%s'", address), throwable);
                        }
                        log.debug("Got challenge number: {} from address {}", challenge, address);
                        /*sourceQueryClient.getServerInfo(challenge, address).whenComplete((sourceServer, exception) -> {
                            latch.countDown();
                            if (exception != null) {
                                if (exception instanceof CompletionException && exception.getCause() instanceof SourceChallengeException) {
                                    SourceChallengeException ex = (SourceChallengeException) exception.getCause();
                                    throw new CompletionException("Server threw a challenge number", ex);
                                    //log.info("Challenge number updated to: {}", challengeNumber);
                                }
                                throw new CompletionException(String.format("Failed to query server info from address: %s", address), exception);
                            }
                            log.info("({}) SERVER INFO: {}", challenge, sourceServer);
                        });*/
                    }
                });

                Thread.sleep(10);
            }
            log.info("Waiting for the queries to complete");
            latch.await();

            /*log.info("END: Info query on address: {}:{}", address.getHostString(), address.getPort());

            int challengePlayers = getServerChallenge(address, SourceChallengeType.PLAYER);
            log.info("Server Players (Challenge: {})", challengePlayers);
            for (SourcePlayer player : getPlayers(address, challengePlayers)) {
                log.info("Player: {}", player.getName());
            }
            int challengeRules = getServerChallenge(address, SourceChallengeType.RULES);

            Map<String, String> serverRules = getServerRules(address, challengeRules);
            log.info("Server Rules (Challenge: {})", challengeRules);
            for (Map.Entry<String, String> entry : serverRules.entrySet()) {
                log.info("{} = {}", entry.getKey(), entry.getValue());
            }*/

            end = ((System.currentTimeMillis() - start) / 1000) / 60;
        }

        log.info("Test Completed  in {} minutes", end);
    }

    private SourceServer getServerInfo(InetSocketAddress address) throws ExecutionException, InterruptedException {
        log.info("(override) Querying server: {}", address);
        return sourceQueryClient.getServerInfo(address, true).get();
    }

    private SourceServer getServerInfo(InetSocketAddress address, int challenge) throws ExecutionException, InterruptedException {
        log.info("Querying server: {}", address);
        return sourceQueryClient.getServerInfo(challenge, address).get();
    }

    private int getServerChallenge(InetSocketAddress address, SourceChallengeType type) throws ExecutionException, InterruptedException {
        return sourceQueryClient.getServerChallenge(type, address).get();
    }

    private List<SourcePlayer> getPlayers(InetSocketAddress address, int challenge) throws ExecutionException, InterruptedException {
        return sourceQueryClient.getPlayers(challenge, address).get();
    }

    private Map<String, String> getServerRules(InetSocketAddress address, int challenge) throws ExecutionException, InterruptedException {
        return sourceQueryClient.getServerRules(challenge, address).get();
    }

    private void performQuery(MasterServerFilter filter) {
        AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger();
        AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        AtomicInteger challengeCtr = new AtomicInteger(), challengeErr = new AtomicInteger();
        AtomicInteger playersCtr = new AtomicInteger(), playersErr = new AtomicInteger();
        AtomicInteger rulesCtr = new AtomicInteger(), rulesErr = new AtomicInteger();

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
                CompletableFuture<?>[] futures = requestList.toArray(new CompletableFuture[0]);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() throws Exception {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            //Inititalize client
            sourceQueryClient = new SourceQueryClient();
            masterServerQueryClient = new MasterServerQueryClient();
            log.info("NOTE: Depending on your selected criteria, the application may time some time to complete. You can review the log file(s) once the program exits.");
            ConcurrentUtils.sleepUninterrupted(500);
            this.performQuery();
        } catch (Exception e) {
            log.error("Failed to run query: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        log.info("Closing source query client");
        sourceQueryClient.close();
        log.info("Closing Master query client");
        masterServerQueryClient.close();
    }
}
