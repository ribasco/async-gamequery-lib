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

import com.ibasco.agql.core.AbstractClient;
import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryOptions;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerOptions;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SourceQueryExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryExample.class);

    private SourceQueryClient queryClient;

    private MasterServerQueryClient masterClient;

    private static final NumberFormat nf = new DecimalFormat("00000");

    public SourceQueryExample() {}

    public static void main(String[] args) throws Exception {
        SourceQueryExample example = new SourceQueryExample();
        example.run(args);
    }

    private final ThreadPoolExecutor masterExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                                                                             60L, TimeUnit.SECONDS,
                                                                             new SynchronousQueue<>(),
                                                                             new DefaultThreadFactory("agql-master"));

    private final ThreadPoolExecutor queryExecutor = new ThreadPoolExecutor(9, Integer.MAX_VALUE,
                                                                            60L, TimeUnit.SECONDS,
                                                                            new LinkedBlockingQueue<>(),
                                                                            new DefaultThreadFactory("agql-query"));

    @Override
    public void run(String[] args) throws Exception {
        try {
            //query client
            // - Enabled rate limiting so we don't send too fast
            // - Change rate limiting type to BURST
            // - Used a custom executor for query client. We are responsible for shutting down this executor, not the library.
            Options queryOptions = OptionBuilder.newBuilder()
                                                .option(SourceQueryOptions.FAILSAFE_ENABLED, true)
                                                .option(SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED, false)
                                                .option(SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.BURST)
                                                .option(TransportOptions.THREAD_EXECUTOR_SERVICE, queryExecutor)
                                                .build();
            queryClient = new SourceQueryClient(queryOptions);

            //master client initialization
            // - Configuring the Rate limit type to SMOOTH
            // - Used a custom executor for master client. We are responsible for shutting down this executor, not the library.
            Options masterOptions = OptionBuilder.newBuilder()
                                                 .option(MasterServerOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                 .option(TransportOptions.THREAD_EXECUTOR_SERVICE, masterExecutor)
                                                 .build();
            masterClient = new MasterServerQueryClient(masterOptions);
            log.info("NOTE: Depending on your selected criteria, the application may time some time to complete. You can review the log file(s) once the program exits.");
            runInteractiveExample();
        } catch (Exception e) {
            log.error("Failed to run query: {}", e.getMessage());
            throw e;
        }
    }

    public void runInteractiveExample() throws Exception {
        Boolean queryAllServers = promptInputBool("Query all available servers? (y/n)", true, "y", "queryAllServers");
        long start, end;

        final Phaser phaser = new Phaser();
        final SourceQueryAggregateProcessor processor = new SourceQueryAggregateProcessor();

        int total = 0;
        start = System.currentTimeMillis();
        phaser.register();
        if (queryAllServers) {
            final MasterServerFilter filter = buildServerFilter();
            log.info("Fetching server list using filter '{}'", filter);
            total = fetchServersAndQuery(filter, phaser, processor);
        } else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, null, "queryAddress");
            InetSocketAddress address = NetUtil.parseAddress(addressString, 27015);
            log.info("Waiting for the queries to complete");
            start = System.currentTimeMillis();
            queryServer(address, phaser);
            total = 1;
        }
        phaser.arriveAndAwaitAdvance();
        end = System.currentTimeMillis() - start;

        log.info("=================================================================================================================================");

        if (end < 1) {
            log.info("Test Completed  in {} seconds", Duration.ofMillis(end).getSeconds());
        } else {
            log.info("Test Completed  in {} minutes", Duration.ofMillis(end).getSeconds() / 60.0D);
        }

        log.info("=================================================================================================================================");
        log.info("Test Results (Total address fetched: {})", total);
        log.info("=================================================================================================================================");

        for (Map.Entry<SourceQueryType, SourceQueryStatCounter> entry : processor.getStats().entrySet()) {
            String name = entry.getKey().name();
            SourceQueryStatCounter stat = entry.getValue();
            log.info("{} (Success: {}, Failure: {}, Total: {})", name, nf.format(stat.getSuccessCount()), nf.format(stat.getFailureCount()), nf.format(stat.getTotalCount()));
        }

        for (Map.Entry<AbstractClient.ClientStatistics.Stat, Integer> e : queryClient.getClientStatistics().getValues().entrySet()) {
            AbstractClient.ClientStatistics.Stat stat = e.getKey();
            Integer count = e.getValue();
            log.info("{} = {}", stat.name(), nf.format(count));

        }
        log.info("=================================================================================================================================");
    }

    private MasterServerFilter buildServerFilter() {
        int appId = Integer.parseInt(promptInput("List servers only from this app id (int)", false, null, "srcQryAppId"));
        Boolean emptyServers = promptInputBool("List only empty servers? (y/n)", false, null, "srcQryEmptySvrs");
        Boolean passwordProtected = promptInputBool("List only passwd protected servers? (y/n)", false, null, "srcQryPassProtect");
        Boolean dedicatedServers = promptInputBool("List only dedicated servers (y/n)", false, "y", "srcQryDedicated");
        MasterServerFilter filter = MasterServerFilter.create().dedicated(dedicatedServers).isPasswordProtected(passwordProtected).allServers().isEmpty(emptyServers);
        if (appId > 0)
            filter.appId(appId);
        return filter;
    }

    /**
     * Fetch a new list from the master server and query
     *
     * @param filter
     *         {@link MasterServerFilter}
     */
    private int fetchServersAndQuery(final MasterServerFilter filter, final Phaser phaser, SourceQueryAggregateProcessor processor) {
        List<InetSocketAddress> addressList = masterClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (address, sender, error) -> {
            if (error != null)
                throw new CompletionException(ConcurrentUtil.unwrap(error));
            queryServer(address, phaser).whenComplete(processor);
        }).join();
        return addressList.size();
    }

    /**
     * Performs all types of queries for the specified address. This is just one example on how you can combine all operations in one single asynchronous call.
     *
     * @param address
     *         The {@link InetSocketAddress} of the remote server to query
     * @param phaser
     *         The synchronization barrier that is notified once a result has been received
     *
     * @return A {@link CompletableFuture} that is notified once all the responses from each type of query have been received.
     */
    private CompletableFuture<SourceQueryAggregate> queryServer(final InetSocketAddress address, final Phaser phaser) {
        SourceQueryAggregate result = new SourceQueryAggregate(address, phaser);
        if (MasterServer.isTerminatingAddress(address))
            return ConcurrentUtil.failedFuture(new IllegalArgumentException("Invalid address: " + address));
        //we register three parties for the info, player and rules requests
        phaser.bulkRegister(3);
        return CompletableFuture.completedFuture(result)
                                .thenCombine(queryClient.getServerInfo(address).handle(result.ofType(SourceQueryType.INFO)), Functions::selectFirst)
                                .thenCombine(queryClient.getPlayers(address).handle(result.ofType(SourceQueryType.PLAYERS)), Functions::selectFirst)
                                .thenCombine(queryClient.getServerRules(address).handle(result.ofType(SourceQueryType.RULES)), Functions::selectFirst);
    }

    @Override
    public void close() throws IOException {
        if (queryClient != null) {
            log.info("Closing source query client");
            queryClient.close();
        }
        if (masterClient != null) {
            log.info("Closing master query client");
            masterClient.close();
        }
        if (ConcurrentUtil.shutdown(queryExecutor))
            log.info("Successfully shutdown query executor");
        if (ConcurrentUtil.shutdown(masterExecutor))
            log.info("Successfully shutdown master query executor");
    }

    /**
     * Enumeration for identifying the type of source query
     */
    private enum SourceQueryType {
        INFO,
        PLAYERS,
        RULES
    }

    /**
     * An aggregate for INFO, PLAYER and RULES queries for a specific server {@link InetSocketAddress}
     */
    private static class SourceQueryAggregate {

        private final InetSocketAddress address;

        private SourceServer info;

        private Collection<SourcePlayer> players;

        private Map<String, String> rules;

        private final Map<SourceQueryType, Throwable> status = new HashMap<>();

        private final Phaser phaser;

        private SourceQueryAggregate(final InetSocketAddress address, final Phaser phaser) {
            this.address = address;
            this.phaser = phaser;
        }

        private SourceServer getInfo() {
            return info;
        }

        private Collection<SourcePlayer> getPlayers() {
            return players;
        }

        private Map<String, String> getRules() {
            return rules;
        }

        private InetSocketAddress getAddress() {
            return address;
        }

        private Throwable getError(SourceQueryType type) {
            return ConcurrentUtil.unwrap(status.get(type));
        }

        private boolean hasError() {
            return status.entrySet().stream().anyMatch(e -> e.getValue() != null);
        }

        private boolean hasError(SourceQueryType type) {
            return status.get(type) != null;
        }

        /**
         * A factory method which returns a callback for processing the specified {@link SourceQueryType}
         *
         * @param responseType
         *         {@link SourceQueryType} enumeration identifying the type of response (INFO, PLAYER or RULES)
         * @param <R>
         *         Type of the response
         *
         * @return A {@link BiFunction} callback that is called by the client
         */
        @SuppressWarnings("unchecked")
        private <R> BiFunction<R, Throwable, SourceQueryAggregate> ofType(SourceQueryType responseType) {
            return (response, error) -> {
                try {
                    boolean hasError = error != null;
                    if (hasError)
                        status.put(responseType, error);
                    switch (responseType) {
                        case INFO: {
                            if (hasError) {
                                SourceQueryAggregate.this.info = null;
                            } else {
                                SourceQueryAggregate.this.info = (SourceServer) response;
                            }
                            break;
                        }
                        case PLAYERS: {
                            if (hasError) {
                                SourceQueryAggregate.this.players = new ArrayList<>();
                            } else {
                                SourceQueryAggregate.this.players = (List<SourcePlayer>) response;
                            }
                            break;
                        }
                        case RULES: {
                            if (hasError) {
                                SourceQueryAggregate.this.rules = new HashMap<>();
                            } else {
                                SourceQueryAggregate.this.rules = (Map<String, String>) response;
                            }
                            break;
                        }
                        default:
                            throw new IllegalStateException("Invalid result recieved from server: " + response);
                    }
                    return SourceQueryAggregate.this;
                } finally {
                    phaser.arriveAndDeregister();
                }
            };
        }
    }

    /**
     * A stat counter for a specific {@link SourceQueryType}
     */
    private static class SourceQueryStatCounter {

        private final SourceQueryType type;

        private final AtomicInteger successCount = new AtomicInteger();

        private final AtomicInteger failureCount = new AtomicInteger();

        private SourceQueryStatCounter(SourceQueryType type) {
            this.type = type;
        }

        public SourceQueryType getType() {
            return type;
        }

        public int getSuccessCount() {
            return successCount.get();
        }

        public int getFailureCount() {
            return failureCount.get();
        }

        public int getTotalCount() {
            return getSuccessCount() + getFailureCount();
        }

        public void recordSuccess() {
            this.successCount.incrementAndGet();
        }

        public void recordFailure(Throwable error) {
            this.failureCount.incrementAndGet();
        }
    }

    /**
     * A class for processing {@link SourceQueryAggregate} instances (For Collecting statistics)
     */
    private static class SourceQueryAggregateProcessor implements BiConsumer<SourceQueryAggregate, Throwable> {

        private final AtomicInteger counter = new AtomicInteger();

        private final Map<SourceQueryType, SourceQueryStatCounter> stats = new ConcurrentHashMap<>();

        @Override
        public void accept(SourceQueryAggregate result, Throwable error) {
            if (error != null)
                throw new CompletionException(ConcurrentUtil.unwrap(error));

            SourceQueryStatCounter infoCounter = stats.computeIfAbsent(SourceQueryType.INFO, SourceQueryStatCounter::new);
            SourceQueryStatCounter playerCounter = stats.computeIfAbsent(SourceQueryType.PLAYERS, SourceQueryStatCounter::new);
            SourceQueryStatCounter rulesCounter = stats.computeIfAbsent(SourceQueryType.RULES, SourceQueryStatCounter::new);

            Throwable infoError = result.getError(SourceQueryType.INFO);
            Throwable playerError = result.getError(SourceQueryType.PLAYERS);
            Throwable rulesError = result.getError(SourceQueryType.RULES);

            //process info
            if (infoError != null) {
                infoCounter.recordFailure(infoError);
            } else {
                infoCounter.recordSuccess();
            }

            //process players
            if (playerError != null) {
                playerCounter.recordFailure(playerError);
            } else {
                playerCounter.recordSuccess();
            }

            //process rules
            if (rulesError != null) {
                rulesCounter.recordFailure(rulesError);
            } else {
                rulesCounter.recordSuccess();
            }

            System.out.printf("%05d) \u001B[33m%-15s:%05d\u001B[0m => \u001B[34m[PLAYERS]\u001B[0m: %s \u001B[34m[RULES]\u001B[0m: %s \u001B[32m[INFO]\u001B[0m: %-64s\n",
                              counter.incrementAndGet(),
                              result.getAddress().getHostString(),
                              result.getAddress().getPort(),
                              formatResult(result, SourceQueryType.PLAYERS),
                              formatResult(result, SourceQueryType.RULES),
                              formatResult(result, SourceQueryType.INFO)
            );
        }

        public Map<SourceQueryType, SourceQueryStatCounter> getStats() {
            return stats;
        }

        private static String formatResult(SourceQueryAggregate result, SourceQueryType type) {
            final int padSize = 30;
            Throwable error = result.getError(type);
            String data;
            if (error != null) {
                return "\u001B[31m" + StringUtils.rightPad(errorName(error), padSize, " ") + "\u001B[0m";
            } else {
                switch (type) {
                    case INFO: {
                        data = result.getInfo().getName();
                        break;
                    }
                    case PLAYERS: {
                        data = String.valueOf(result.getPlayers().size());
                        break;
                    }
                    case RULES: {
                        data = String.valueOf(result.getRules().size());
                        break;
                    }
                    default: {
                        data = "ERR";
                        break;
                    }
                }
            }
            return StringUtils.rightPad(data, padSize, " ");
        }

        private static String errorName(Throwable error) {
            if (error instanceof TimeoutException) {
                return "TIMED OUT";
            } else {
                return error.getClass().getSimpleName();
            }
        }
    }
}
