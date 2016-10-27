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

package com.ribasco.rglib;

import com.google.common.cache.CacheStats;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.exceptions.ReadTimeoutException;
import com.ribasco.rglib.core.exceptions.TimeoutException;
import com.ribasco.rglib.core.session.SessionId;
import com.ribasco.rglib.core.session.SessionValue;
import com.ribasco.rglib.core.utils.ConcurrentUtils;
import com.ribasco.rglib.protocols.valve.source.SourceMasterFilter;
import com.ribasco.rglib.protocols.valve.source.client.SourceQueryClient;
import com.ribasco.rglib.protocols.valve.source.client.SourceRconClient;
import com.ribasco.rglib.protocols.valve.source.enums.SourceChallengeType;
import com.ribasco.rglib.protocols.valve.source.enums.SourceMasterServerRegion;
import com.ribasco.rglib.protocols.valve.source.exceptions.RconNotYetAuthException;
import com.ribasco.rglib.protocols.valve.source.pojos.SourcePlayer;
import com.ribasco.rglib.protocols.valve.source.pojos.SourceServer;
import com.ribasco.rglib.protocols.valve.steam.master.MasterServerFilter;
import com.ribasco.rglib.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ribasco.rglib.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ribasco.rglib.protocols.valve.steam.master.enums.MasterServerType;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by raffy on 9/13/2016.
 */
public class DemoClient {
    private static final Logger log = LoggerFactory.getLogger(DemoClient.class);

    private Map<String, Double> resultMap = new HashMap<>();

    private SourceRconClient sourceRconClient;
    private SourceQueryClient sourceQueryClient;
    private MasterServerQueryClient masterServerQueryClient;

    public DemoClient() {
        sourceRconClient = new SourceRconClient();
        sourceQueryClient = new SourceQueryClient();
        masterServerQueryClient = new MasterServerQueryClient();
    }

    public void close() throws IOException {
        sourceRconClient.close();
        sourceQueryClient.close();
        masterServerQueryClient.close();
    }

    public static void main(String[] args) throws Exception {
        final DemoClient app = new DemoClient();
        try {
            //app.testQueue();
            //app.runSimpleAppTest();
            //app.runTestSuite();
            app.testRcon();
            //app.runSimpleTest();
            app.runSimpleTestCached();
            //app.runSimpleTestEx();
            //app.listAllServers();
            //app.testBatch();
            //
            //app.testRcon2();

            //app.testChallengeCache();
            //app.testDuplicateQueryRequests();

            //app.testChallengeCacheEx();
            // app.testMultimap();
            //app.testQueue();
            //app.extractThenProcess();
            //app.testSimpleQuery();

            //app.testMasterServerClient();
            //app.runNewSimpleTest();
        } finally {
            log.info("Closing App");
            app.close();
        }
    }

    public void runNewSimpleTest() {
        MasterServerFilter filter = MasterServerFilter.create()
                .appId(550)
                .dedicated(true)
                .isEmpty(false)
                .isSecure(true);
        double start = System.currentTimeMillis();
        runNewSimpleTest(10, filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed in {} minutes", end);
    }

    private Map<String, Double> runNewSimpleTest(int sleepTime, MasterServerFilter filter) {

        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        try {
            sourceQueryClient.setSleepTime(sleepTime);

            masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                try {
                    if (masterServerError != null) {
                        log.debug("[MASTER : ERROR] : From: {} = {}", masterServerSender, masterServerError.getMessage());
                        if (masterServerError instanceof ReadTimeoutException) {
                            masterTimeout.incrementAndGet();
                        } else
                            masterError.incrementAndGet();
                        return;
                    }

                    log.debug("[MASTER : INFO] : {}", serverAddress);
                    masterServerCtr.incrementAndGet();

                    sourceQueryClient.getServerInfo(serverAddress, (serverInfo, infoSender, serverInfoError) -> {
                        if (serverInfoError != null) {
                            log.debug("[SERVER : ERROR] : From: {} = {}", infoSender, serverInfoError.getMessage());
                            if (serverInfoError instanceof ReadTimeoutException) {
                                serverInfoTimeout.incrementAndGet();
                            } else
                                serverInfoErr.incrementAndGet();
                            return;
                        }

                        serverInfoCtr.incrementAndGet();
                        log.debug("[SERVER : INFO] : From: {} (Data: {})", infoSender, serverInfo);
                    });

                    //Get Challenge
                    sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress, (challengeNum, serverChallengeSender, serverChallengeError) -> {
                        try {
                            if (serverChallengeError != null) {
                                log.debug("[CHALLENGE : ERROR] For: '{}', Message: '{}')", serverChallengeSender, serverChallengeError.getMessage());
                                if (serverChallengeError instanceof ReadTimeoutException)
                                    challengeTimeout.incrementAndGet();
                                else
                                    challengeErr.incrementAndGet();
                                return;
                            }
                            log.debug("[CHALLENGE : INFO] Challenge '{}' from {}", challengeNum, serverChallengeSender);
                            challengeCtr.incrementAndGet();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).thenAccept(challenge -> {
                        CompletableFuture<List<SourcePlayer>> playersFuture = sourceQueryClient.getPlayers(challenge, serverAddress, new Callback<List<SourcePlayer>>() {
                            @Override
                            public void onComplete(List<SourcePlayer> players, InetSocketAddress playerSender, Throwable playerError) {
                                if (playerError != null) {
                                    log.debug("[PLAYERS : ERROR] For: '{}', Message: '{}')", playerSender, playerError.getMessage());
                                    if (playerError instanceof ReadTimeoutException)
                                        playersTimeout.incrementAndGet();
                                    else
                                        playersOtherErr.incrementAndGet();
                                    return;
                                }
                                playersCtr.incrementAndGet();
                                log.debug("[PLAYERS : INFO] : From {}, PlayerData = {}", playerSender, players);
                            }
                        });
                        playersFuture.whenComplete(new BiConsumer<List<SourcePlayer>, Throwable>() {
                            @Override
                            public void accept(List<SourcePlayer> sourcePlayers, Throwable throwable) {
                                CompletableFuture<Map<String, String>> rulesFuture = sourceQueryClient.getServerRules(challenge, serverAddress, (rules, rulesSender, rulesError) -> {
                                    if (rulesError != null) {
                                        log.debug("[RULES : ERROR] For: '{}', Message: '{}')", rulesSender, rulesError.getMessage());
                                        if (rulesError instanceof ReadTimeoutException)
                                            rulesTimeout.incrementAndGet();
                                        else
                                            rulesOtherErr.incrementAndGet();
                                        return;
                                    }
                                    rulesCtr.incrementAndGet();
                                    log.debug("[RULES : INFO] From {}, Rules = {}", rulesSender, rules);
                                });
                            }
                        });
                    });
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }).exceptionally(new Function<Throwable, Vector<InetSocketAddress>>() {
                @Override
                public Vector<InetSocketAddress> apply(Throwable throwable) {
                    return new Vector<InetSocketAddress>();
                }
            }).get(); //masterServerList

            //log.info("Waiting for requests to complete...");
            sourceQueryClient.waitForAll();

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

    public void testMasterServerClient() {
        MasterServerFilter filter = new MasterServerFilter().dedicated(true).appId(730);//.isEmpty(false);
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, new Callback<InetSocketAddress>() {
            @Override
            public void onComplete(InetSocketAddress response, InetSocketAddress sender, Throwable error) {
                if (error != null) {
                    log.error("Error from master : {}", error);
                    return;
                }
                log.info("Got from master {}", response);
            }
        });
    }

    public void testSimpleQuery() {
        try {
            InetSocketAddress address = new InetSocketAddress("192.168.1.14", 27015);
            sourceQueryClient.getServerInfo(address, new Callback<SourceServer>() {
                @Override
                public void onComplete(SourceServer response, InetSocketAddress sender, Throwable error) {
                    if (error != null) {
                        log.error("Got error {}", error.getMessage());
                        return;
                    }
                    log.info("Got Response: \n{}", response);
                }
            }).get();

            CompletableFuture<String> res = sourceRconClient.authenticate(address, "***REMOVED***", null).thenCompose(success -> {
                if (success) {
                    try {
                        return sourceRconClient.execute(address, "status", null);
                    } catch (RconNotYetAuthException e) {
                        e.printStackTrace();
                    }
                }
                return CompletableFuture.completedFuture("");
            });

            log.info("Result : {}", res.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void extractThenProcess() {
        SourceMasterFilter filter = new SourceMasterFilter()
                .appId(550)
                .dedicated(true)
                //.isEmpty(false)
                .isSecure(true);

        try {
            log.info("Retrieving master logger list...");
            final Vector<InetSocketAddress> serverList = sourceQueryClient.getMasterServerList(SourceMasterServerRegion.REGION_ALL, filter, null).get();

            AtomicInteger playerError = new AtomicInteger();
            AtomicInteger playerCount = new AtomicInteger();
            AtomicInteger rulesError = new AtomicInteger();
            AtomicInteger rulesCount = new AtomicInteger();

            log.info("Retrieval Done. Total Servers {}", serverList.size());
            for (int i = 0; i < 1; i++) {
                log.info("START: Iteration #{}. Total Servers: {}", i, serverList.size());
                for (InetSocketAddress address : serverList) {
                    //log.info("Processing Address : {}. Current Queue Size: {}, Cache Size: {}", address, sourceQueryClient.getMessenger().getRequestQueue().size(), sourceQueryClient.getChallengeCache().size());
                    sourceQueryClient.getPlayers(address, (response, sender, error) -> {
                        if (error != null) {
                            //log.error("[PLAYERS : ERROR] {}", error.getMessage());
                            playerError.incrementAndGet();
                            return;
                        }
                        playerCount.incrementAndGet();
                        //log.info("[PLAYERS] for {} = {}", address, response);
                    });
                    ConcurrentUtils.sleepUninterrupted(10);
                    sourceQueryClient.getServerRules(address, (response, sender, error) -> {
                        if (error != null) {
                            //log.error("[RULES : ERROR] {}", error.getMessage());
                            rulesError.incrementAndGet();
                            return;
                        }
                        rulesCount.incrementAndGet();
                        //log.info("[RULES] for {} = {}", address, response);
                    });
                    ConcurrentUtils.sleepUninterrupted(10);
                }
                sourceQueryClient.waitForAll();
                log.info("FINISHED: Iteration #{}. Total Player Records : {}, Total Player Error: {}, (Check Sum: {}), Total Rules Records: {}, Total Rules Error: {} (Check Sum: {}), Cache Size: {}", i, playerCount.get(), playerError.get(), playerCount.get() + playerError.get(), rulesCount.get(), rulesError.get(), rulesCount.get() + rulesError.get(), sourceQueryClient.getChallengeCache().size());
                playerCount.set(0);
                playerError.set(0);
                rulesCount.set(0);
                rulesError.set(0);
                ConcurrentUtils.sleepUninterrupted(10000);
            }

            ConcurrentUtils.sleepUninterrupted(10000);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    enum ProductQuality {
        High, Medium, Low

    }

    class Product implements Comparable<Product> {

        String name;

        ProductQuality priority;

        Product(String str, ProductQuality pri) {
            name = str;
            priority = pri;
        }

        public int compareTo(Product msg2) {
            return priority.compareTo(msg2.priority);
        }

        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", priority=" + priority +
                    '}';
        }

    }

    class MessageComparator implements Comparator<Product> {
        public int compare(Product msg1, Product msg2) {
            return new CompareToBuilder()
                    .append(msg1.priority, msg2.priority)
                    .toComparison();
        }

    }

    private class SessionIdComparator implements Comparator<SessionId> {
        @Override
        public int compare(SessionId o1, SessionId o2) {
            return new CompareToBuilder().append(o1.getId(), o2.getId()).toComparison();
        }

    }

    private class SessionValueComparator implements Comparator<SessionValue> {
        @Override
        public int compare(SessionValue o1, SessionValue o2) {
            return new CompareToBuilder()
                    .append(o1.getIndex(), o2.getIndex())
                    .toComparison();
        }

    }

    public void testMultimap() {
        Multimap<String, String> map = Multimaps.synchronizedSortedSetMultimap(TreeMultimap.create());
        map.put("request-type-1", "e");
        map.put("request-type-1", "c");
        map.put("request-type-1", "a");
        map.put("request-type-1", "d");
        map.put("request-type-1", "b");
        map.put("request-type-2", "status");
        map.put("request-type-2", "cvarlist");
        map.put("request-type-2", "pekpek");
        map.entries().stream().forEachOrdered(new Consumer<Map.Entry<String, String>>() {
            @Override
            public void accept(Map.Entry<String, String> stringStringEntry) {
                log.info("Key = {}, Value = {}", stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        });
    }

    public void testDuplicateQueryRequests() throws InterruptedException {
        InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.14", 27015);

        for (int i = 0; i < 500; i++) {
            int reqNum = i + 1;
            //log.info("Sending Request {}", reqNum);
            sourceQueryClient.getServerInfo(serverAddress, (serverInfo, infoSender, serverInfoError) -> {
                if (serverInfoError != null) {
                    log.debug("[REQUEST {}] : From: {} = {} (Session: {})", reqNum, infoSender, serverInfoError.getMessage(), sourceQueryClient.getMessenger().getSessionManager().getSessionEntries());
                    return;
                }
                log.debug("[REQUEST {}] : From: {} (Data: {}) (Session: {})", reqNum, infoSender, serverInfo, sourceQueryClient.getMessenger().getSessionManager().getSessionEntries());
            });
            Thread.sleep(200);
        }
        Thread.sleep(10000);
        sourceQueryClient.waitForAll();
    }

    public void testChallengeCache() throws InterruptedException, ExecutionException {
        InetSocketAddress address1 = new InetSocketAddress("192.168.1.14", 27015);

        for (int i = 0; i < 500; i++) {
            log.info("{}) Retrieving Player List", i);
            sourceQueryClient.getPlayersCached(address1, (response, sender, error) -> {
                if (error != null) {
                    log.error(error.getMessage());
                    return;
                }
                log.info("Got response from command 1: {}", response);
            });

            log.info("{}) Retrieving Server Rules", i);
            sourceQueryClient.getServerRulesCached(address1, (response, sender, error) -> {
                if (error != null) {
                    log.error(error.getMessage());
                    return;
                }
                log.info("Got response from command 2: {}", response);
            });

            ConcurrentUtils.sleepUninterrupted(500);
        }
        sourceQueryClient.waitForAll();
    }

    public void testRcon2() {
        InetSocketAddress address1 = new InetSocketAddress("192.168.1.14", 27015);
        InetSocketAddress address2 = new InetSocketAddress("192.168.1.14", 27016);

        InetSocketAddress currentIp = address1;

        //Authenticate
        try {
            sourceRconClient.authenticate(currentIp, "***REMOVED***", (requestId, sender, error) -> {
                if (requestId != null) {
                    log.info("Successfully Authenticated: {}", requestId);
                } else
                    log.error("Problem authenticating rcon with {}", sender);
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sourceRconClient.waitForAll();

    }

    public void testRcon() throws InterruptedException {
        InetSocketAddress address1 = new InetSocketAddress("192.168.1.14", 27015);
        InetSocketAddress address2 = new InetSocketAddress("192.168.1.14", 27016);

        List<String> commands = Arrays.asList(
                "status",
                "cvarlist",
                "sm plugins list",
                "echo from command 4",
                "sm version",
                "sm exts list",
                "meta version",
                "meta list");

        try {
            //Authenticate
            sourceRconClient.authenticate(address1, "***REMOVED***", (requestId, sender, error) -> {
                if (requestId != null) {
                    log.info("Successfully Authenticated: {} for {}", requestId, sender);
                } else
                    log.error("Problem authenticating rcon with {}", sender);
            }).get();

            List<CompletableFuture> futures = new ArrayList<>();

            try {
                for (String command : commands) {
                    log.info("Executing command '{}'", command);
                    futures.add(sourceRconClient.execute(address1, command, (response, sender, error) -> {
                        if (error != null) {
                            log.error("Error occured while executing command: {}", error.getMessage());
                            return;
                        }
                        log.info("Received Reply for command '{}': \n{}", command, response);
                    }));
                }
            } catch (RconNotYetAuthException e) {
                e.printStackTrace();
            }

            //Wait
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sourceRconClient.waitForAll();
    }

    public void listAllServers() {
        SourceMasterFilter filter = new SourceMasterFilter()
                .appId(550)
                .dedicated(true)
                //.isEmpty(false)
                .isSecure(true);


        //client.setSleepTime(15);
        sourceQueryClient.getMasterServerList(SourceMasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
            log.info("{}", serverAddress);
        });

    }

    public void runSimpleAppTest() {
        SourceMasterFilter filter = new SourceMasterFilter()
                .appId(550)
                .dedicated(true)
                .isEmpty(false)
                .isSecure(true);
        double start = System.currentTimeMillis();
        runTest(8, filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed in {} minutes", Math.round(end));
    }

    public void runSimpleTestEx() {
        SourceMasterFilter filter = new SourceMasterFilter()
                .appId(550)
                .dedicated(true)
                //.isEmpty(false)
                .isSecure(true);
        double start = System.currentTimeMillis();
        runTestEx(8, filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed in {} minutes", end);
    }

    public void runSimpleTest() {
        SourceMasterFilter filter = new SourceMasterFilter()
                .appId(550)
                .dedicated(true)
                //.isEmpty(false)
                .isSecure(true);
        double start = System.currentTimeMillis();
        runTest(8, filter);
        double end = ((System.currentTimeMillis() - start) / 1000) / 60;
        log.info("Test Completed in {} minutes", end);
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

    public void runTestSuite() {
        final ArrayList<Map<String, Double>> iterations = new ArrayList<>();
        double start, stop, total;

        SourceMasterFilter filter = new SourceMasterFilter().appId(550).dedicated(true).isEmpty(false).isSecure(true);

        //Repeat 5 times
        for (int iteration = 0; iteration < 5; iteration++) {
            log.info("===================================================================================================================");
            log.info("ITERATION-START: #{}", iteration);
            log.info("===================================================================================================================");
            int ctr = 1;
            //Sleep Time
            for (int sleepInterval = 5; sleepInterval < 16; sleepInterval++) {
                try {
                    log.info("=> Test #{} START: Running with Sleep Interval of {}", ctr, sleepInterval);

                    start = System.currentTimeMillis();
                    Map<String, Double> results = runTest(sleepInterval, filter);
                    stop = System.currentTimeMillis();
                    total = (stop - start) / 1000;

                    //Display the results
                    displayResults(iteration, sleepInterval, total, results);

                    log.info("=> Test #{} Completed in {} seconds. Resting for 1 minute", ctr++, total);
                    //Wait 1 minute before starting the next test
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("===================================================================================================================");
            log.info("ITERATION-END: #{}", iteration);
            log.info("===================================================================================================================");
        }
    }

    public void displayResults(int iterationNum, int testNum, double completionTime, Map<String, Double> results) {

        if (results != null) {
            StringBuilder header = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Double> entry : results.entrySet()) {
                header.append(entry.getKey()).append(",");
                sb.append(entry.getValue()).append(",");
            }

            log.info("{},{},{},{}", iterationNum, testNum, completionTime, sb);
        }
    }

    private Map<String, Double> runTestEx(int sleepTime, SourceMasterFilter filter) {
        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        try {
            sourceQueryClient.setSleepTime(sleepTime);

            sourceQueryClient.getMasterServerList(SourceMasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                try {
                    if (masterServerError != null) {
                        log.debug("[MASTER : ERROR] : From: {} = {}", masterServerSender, masterServerError.getMessage());
                        if (masterServerError instanceof ReadTimeoutException) {
                            masterTimeout.incrementAndGet();
                        } else
                            masterError.incrementAndGet();
                        return;
                    }

                    log.debug("[MASTER : INFO] : {}", serverAddress);
                    masterServerCtr.incrementAndGet();

                    sourceQueryClient.getServerInfo(serverAddress, (serverInfo, infoSender, serverInfoError) -> {
                        if (serverInfoError != null) {
                            log.debug("[SERVER : ERROR] : From: {} = {}", infoSender, serverInfoError.getMessage());
                            if (serverInfoError instanceof ReadTimeoutException) {
                                serverInfoTimeout.incrementAndGet();
                            } else
                                serverInfoErr.incrementAndGet();
                            return;
                        }

                        serverInfoCtr.incrementAndGet();
                        log.debug("[SERVER : INFO] : From: {} (Data: {})", infoSender, serverInfo);
                    });

                    try {
                        sourceQueryClient.getPlayers(serverAddress, (players, playerSender, playerError) -> {
                            if (playerError != null) {
                                log.debug("[PLAYERS : ERROR] For: '{}', Message: '{}')", playerSender, playerError.getMessage());
                                if (playerError instanceof ReadTimeoutException)
                                    playersTimeout.incrementAndGet();
                                else
                                    playersOtherErr.incrementAndGet();
                                return;
                            }
                            playersCtr.incrementAndGet();
                            log.debug("[PLAYERS : INFO] : From {}, PlayerData = {}", playerSender, players);
                        });

                        sourceQueryClient.getServerRules(serverAddress, (rules, rulesSender, rulesError) -> {
                            if (rulesError != null) {
                                log.debug("[RULES : ERROR] For: '{}', Message: '{}')", rulesSender, rulesError.getMessage());
                                if (rulesError instanceof ReadTimeoutException)
                                    rulesTimeout.incrementAndGet();
                                else
                                    rulesOtherErr.incrementAndGet();
                                return;
                            }
                            rulesCtr.incrementAndGet();
                            log.debug("[RULES : INFO] From {}, Rules = {}", rulesSender, rules);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }).get(); //masterServerList

            //log.info("Waiting for requests to complete...");
            sourceQueryClient.waitForAll();

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

    private Map<String, Double> runTestCached(int sleepTime, MasterServerFilter filter) {
        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        try {
            sourceQueryClient.setSleepTime(sleepTime);

            try {
                masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                    try {
                        if (masterServerError != null) {
                            log.debug("[MASTER : ERROR] : From: {} = {}", masterServerSender, masterServerError.getMessage());
                            if (masterServerError instanceof TimeoutException) {
                                masterTimeout.incrementAndGet();
                            } else
                                masterError.incrementAndGet();
                            return;
                        }

                        log.debug("[MASTER] : {}", serverAddress);
                        masterServerCtr.incrementAndGet();

                        sourceQueryClient.getServerInfo(serverAddress, (serverInfo, infoSender, serverInfoError) -> {
                            if (serverInfoError != null) {
                                log.debug("[SERVER : ERROR] : From: {} = {}", infoSender, serverInfoError.getMessage());
                                if (serverInfoError instanceof TimeoutException) {
                                    serverInfoTimeout.incrementAndGet();
                                } else
                                    serverInfoErr.incrementAndGet();
                                return;
                            }
                            serverInfoCtr.incrementAndGet();
                            log.debug("[SERVER] : From: {} (Data: {})", infoSender, serverInfo);
                        });

                        sourceQueryClient.getPlayersCached(serverAddress, (players, playerSender, playerError) -> {
                            if (playerError != null) {
                                log.debug("[PLAYERS : ERROR] For: '{}', Message: '{}')", playerSender, playerError.getMessage());
                                if (playerError instanceof TimeoutException)
                                    playersTimeout.incrementAndGet();
                                else
                                    playersOtherErr.incrementAndGet();
                                return;
                            }
                            playersCtr.incrementAndGet();
                            log.debug("[PLAYERS] : From {}, PlayerData = {}", playerSender, players);
                        });

                        sourceQueryClient.getServerRulesCached(serverAddress, (rules, rulesSender, rulesError) -> {
                            if (rulesError != null) {
                                log.debug("[RULES : ERROR] For: '{}', Message: '{}')", rulesSender, rulesError.getMessage());
                                if (rulesError instanceof TimeoutException)
                                    rulesTimeout.incrementAndGet();
                                else
                                    rulesOtherErr.incrementAndGet();
                                return;
                            }
                            rulesCtr.incrementAndGet();
                            log.debug("[RULES] From {}, Rules = {}", rulesSender, rules);
                        });
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }).get(); //masterServerList
            } catch (Exception e) {
                log.error("Error occured in master logger list {}", e.getMessage());
            }

            //log.info("Waiting for requests to complete...");
            sourceQueryClient.waitForAll();

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

    private Map<String, Double> runTest(int sleepTime, SourceMasterFilter filter) {

        final Map<String, Double> resultMap = new HashMap<>();

        double successRateInfo, successRateChallenge, successRatePlayer, successRateRules;
        final AtomicInteger masterServerCtr = new AtomicInteger(), masterError = new AtomicInteger(), masterTimeout = new AtomicInteger();
        final AtomicInteger serverInfoCtr = new AtomicInteger(), serverInfoTimeout = new AtomicInteger(), serverInfoErr = new AtomicInteger();
        final AtomicInteger challengeCtr = new AtomicInteger(), challengeTimeout = new AtomicInteger(), challengeErr = new AtomicInteger();
        final AtomicInteger playersCtr = new AtomicInteger(), playersTimeout = new AtomicInteger(), playersOtherErr = new AtomicInteger();
        final AtomicInteger rulesCtr = new AtomicInteger(), rulesTimeout = new AtomicInteger(), rulesOtherErr = new AtomicInteger();

        try {
            sourceQueryClient.setSleepTime(sleepTime);

            sourceQueryClient.getMasterServerList(SourceMasterServerRegion.REGION_ALL, filter, (serverAddress, masterServerSender, masterServerError) -> {
                try {
                    if (masterServerError != null) {
                        log.debug("[MASTER : ERROR] : From: {} = {}", masterServerSender, masterServerError.getMessage());
                        if (masterServerError instanceof ReadTimeoutException) {
                            masterTimeout.incrementAndGet();
                        } else
                            masterError.incrementAndGet();
                        return;
                    }

                    log.debug("[MASTER : INFO] : {}", serverAddress);
                    masterServerCtr.incrementAndGet();

                    sourceQueryClient.getServerInfo(serverAddress, (serverInfo, infoSender, serverInfoError) -> {
                        if (serverInfoError != null) {
                            log.debug("[SERVER : ERROR] : From: {} = {}", infoSender, serverInfoError.getMessage());
                            if (serverInfoError instanceof ReadTimeoutException) {
                                serverInfoTimeout.incrementAndGet();
                            } else
                                serverInfoErr.incrementAndGet();
                            return;
                        }

                        serverInfoCtr.incrementAndGet();
                        log.debug("[SERVER : INFO] : From: {} (Data: {})", infoSender, serverInfo);
                    });

                    //Get Challenge
                    sourceQueryClient.getServerChallenge(SourceChallengeType.PLAYER, serverAddress, (challengeNum, serverChallengeSender, serverChallengeError) -> {
                        try {
                            if (serverChallengeError != null) {
                                log.debug("[CHALLENGE : ERROR] For: '{}', Message: '{}')", serverChallengeSender, serverChallengeError.getMessage());
                                if (serverChallengeError instanceof ReadTimeoutException)
                                    challengeTimeout.incrementAndGet();
                                else
                                    challengeErr.incrementAndGet();
                                return;
                            }
                            log.debug("[CHALLENGE : INFO] Challenge '{}' from {}", challengeNum, serverChallengeSender);
                            challengeCtr.incrementAndGet();

                            sourceQueryClient.getPlayers(challengeNum, serverAddress, (players, playerSender, playerError) -> {
                                if (playerError != null) {
                                    log.debug("[PLAYERS : ERROR] For: '{}', Message: '{}')", playerSender, playerError.getMessage());
                                    if (playerError instanceof ReadTimeoutException)
                                        playersTimeout.incrementAndGet();
                                    else
                                        playersOtherErr.incrementAndGet();
                                    return;
                                }
                                playersCtr.incrementAndGet();
                                log.debug("[PLAYERS : INFO] : From {}, PlayerData = {}", playerSender, players);
                            });

                            sourceQueryClient.getServerRules(challengeNum, serverAddress, (rules, rulesSender, rulesError) -> {
                                if (rulesError != null) {
                                    log.debug("[RULES : ERROR] For: '{}', Message: '{}')", rulesSender, rulesError.getMessage());
                                    if (rulesError instanceof ReadTimeoutException)
                                        rulesTimeout.incrementAndGet();
                                    else
                                        rulesOtherErr.incrementAndGet();
                                    return;
                                }
                                rulesCtr.incrementAndGet();
                                log.debug("[RULES : INFO] From {}, Rules = {}", rulesSender, rules);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }).get(); //masterServerList

            //log.info("Waiting for requests to complete...");
            sourceQueryClient.waitForAll();

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
