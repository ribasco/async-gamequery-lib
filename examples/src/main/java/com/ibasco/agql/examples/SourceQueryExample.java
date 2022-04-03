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
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryOptions;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.pojos.SourceServer;
import com.ibasco.agql.protocols.valve.source.query.protocols.info.SourceQueryInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.protocols.rules.SourceQueryRulesResponse;
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

    private Boolean skipServersInError;

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
            printConsoleBanner();
            System.out.println();

            //For every 1 minute, we have 3,465 addresses available to query

            //query client configuration
            // - Enabled rate limiting so we don't send too fast
            // - set rate limit type to SMOOTH so requests are sent evenly at a steady rate
            // - set retry max attempts and backoff parameters
            // - Provide a custom executor for query client. We are responsible for shutting down this executor, not the library.
            // - Set channel pooling strategy to FIXED (POOL_TYPE) with using a fixed number of pooled connections of 50 (POOL_MAX_CONNECTIONS)
            // - Set read timeout to 1000ms (1 second)
            Options queryOptions = OptionBuilder.newBuilder()
                                                //override default value, enable rate limiting (default is: false)
                                                .option(SourceQueryOptions.FAILSAFE_RATELIMIT_ENABLED, true)
                                                .option(SourceQueryOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                .option(SourceQueryOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 15)
                                                .option(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, true)
                                                .option(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 50L)
                                                .option(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L)
                                                .option(SourceQueryOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d)
                                                .option(TransportOptions.THREAD_EXECUTOR_SERVICE, queryExecutor)
                                                .option(TransportOptions.POOL_TYPE, ChannelPoolType.FIXED)
                                                .option(TransportOptions.POOL_MAX_CONNECTIONS, 50)
                                                .option(TransportOptions.READ_TIMEOUT, 1000)
                                                .build();
            queryClient = new SourceQueryClient(queryOptions);

            //master client configuration
            // - Configuring the Rate limit type to SMOOTH
            // - Provide a custom executor for master client. We are responsible for shutting down this executor, not the library.
            Options masterOptions = OptionBuilder.newBuilder()
                                                 .option(MasterServerOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                 .option(TransportOptions.THREAD_EXECUTOR_SERVICE, masterExecutor)
                                                 .build();
            masterClient = new MasterServerQueryClient(masterOptions);
            runInteractiveExample();
        } catch (Exception e) {
            log.error("Failed to run query: {}", e.getMessage());
            throw e;
        }
    }

    public void runInteractiveExample() throws Exception {
        Boolean queryAllServers = promptInputBool("Query all available servers? (y/n)", true, "y", "queryAllServers");
        long start, end;

        final SourceQueryAggregateProcessor processor = new SourceQueryAggregateProcessor();
        final Phaser phaser = new Phaser();

        int total = 0;
        start = System.currentTimeMillis();
        phaser.register();
        if (queryAllServers) {
            System.out.println("Note: Type 'skip' to exclude filter");
            final MasterServerFilter filter = buildServerFilter();
            printLine();
            System.out.printf("\033[1;36mFetching server list using filter \033[1;33m'%s'\033[0m\n", filter);
            printLine();
            total = fetchServersAndQuery(filter, phaser, processor);
        } else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, null, "queryAddress");
            int iterations = Integer.parseInt(promptInput("How many times should we repeat our queries? (int)", false, "1", "queryIterations"));

            InetSocketAddress address = NetUtil.parseAddress(addressString, 27015);
            System.out.println("Waiting for the queries to complete");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++)
                queryServer(address, phaser).whenComplete(processor).join();
            total = 1;
        }
        phaser.arriveAndAwaitAdvance();
        end = System.currentTimeMillis() - start;

        printLine();
        if (end < 1) {
            System.out.printf("Test Completed  in %03d seconds\n", Duration.ofMillis(end).getSeconds());
        } else {
            System.out.printf("Test Completed  in %02f minutes\n", Duration.ofMillis(end).getSeconds() / 60.0D);
        }
        System.out.flush();

        printLine();
        System.out.printf("Test Results (Total address processed: %d)\n", total);
        printLine();
        System.out.flush();

        for (Map.Entry<SourceQueryType, SourceQueryStatCounter> entry : processor.getStats().entrySet()) {
            String name = entry.getKey().name();
            SourceQueryStatCounter stat = entry.getValue();
            System.out.printf("\033[0;33m%-10s\033[0m (\033[1;32mSuccess\033[0m: %05d, \033[1;31mFailure\033[0m: %05d, Total: %05d)\n", name, stat.getSuccessCount(), stat.getFailureCount(), stat.getTotalCount());
        }
        System.out.flush();

        for (Map.Entry<AbstractClient.ClientStatistics.Stat, Integer> e : queryClient.getClientStatistics().getValues().entrySet()) {
            AbstractClient.ClientStatistics.Stat stat = e.getKey();
            Integer count = e.getValue();
            System.out.printf("\033[0;33m%s\033[0m = %05d\n", stat.name(), count);
        }
        printLine();
    }

    private static void printLine() {
        System.out.println("\033[0;36m=================================================================================================================================\033[0m");
        System.out.flush();
    }

    private void printConsoleBanner() {
        System.out.println("\033[0;36m███████╗ ██████╗ ██╗   ██╗██████╗  ██████╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ ██╗   ██╗\033[0m");
        System.out.println("\033[0;36m██╔════╝██╔═══██╗██║   ██║██╔══██╗██╔════╝██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗╚██╗ ██╔╝\033[0m");
        System.out.println("\033[0;36m███████╗██║   ██║██║   ██║██████╔╝██║     █████╗      ██║   ██║██║   ██║█████╗  ██████╔╝ ╚████╔╝ \033[0m");
        System.out.println("\033[0;36m╚════██║██║   ██║██║   ██║██╔══██╗██║     ██╔══╝      ██║▄▄ ██║██║   ██║██╔══╝  ██╔══██╗  ╚██╔╝  \033[0m");
        System.out.println("\033[0;36m███████║╚██████╔╝╚██████╔╝██║  ██║╚██████╗███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║   ██║   \033[0m");
        System.out.println("\033[0;36m╚══════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚══════╝     ╚══▀▀═╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝   ╚═╝   \033[0m");
        System.out.println("\033[0;36m                                             \033[0;33mPowered by Asynchronous Game Query Library\033[0m");
    }

    /**
     * Create our server filter using {@link MasterServerFilter} builder
     *
     * @return The {@link MasterServerFilter} created from the user input
     */
    private MasterServerFilter buildServerFilter() {
        Integer appId = promptInputInt("List servers only from this app id (int)", false, null, "srcQryAppId");
        Boolean nonEmptyServers = promptInputBool("List only non-empty servers? (y/n)", false, null, "srcQryEmptySvrs");
        skipServersInError = promptInputBool("Skip servers with error? (y/n)", false, null, "srcQrySkipServers");
        Boolean passwordProtected = promptInputBool("List passw0rd protected servers? (y/n)", false, null, "srcQryPassProtect");
        Boolean dedicatedServers = promptInputBool("List dedicated servers (y/n)", false, "y", "srcQryDedicated");
        String serverTags = promptInput("Specify public server tags (separated with comma)", false, null, "srcQryServerTagsPublic");
        String serverTagsHidden = promptInput("Specify hidden server tags (separated with comma)", false, null, "srcQryServerTagsHidden");
        Boolean whiteListedOnly = promptInputBool("Display only whitelisted servers? (y/n)", false, null, "srcQryWhitelisted");

        MasterServerFilter filter = MasterServerFilter.create();

        if (whiteListedOnly != null)
            filter.isWhitelisted(whiteListedOnly);
        if (dedicatedServers != null)
            filter.dedicated(dedicatedServers);
        if (nonEmptyServers != null && nonEmptyServers)
            filter.isEmpty(true);
        if (passwordProtected != null)
            filter.isPasswordProtected(passwordProtected);
        if (serverTags != null) {
            String[] serverTagsArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(serverTags, ",");
            if (Arrays.stream(serverTagsArray).noneMatch(v -> "skip".trim().equalsIgnoreCase(v))) {
                filter.gametypes(serverTagsArray);
                for (String type : serverTagsArray) {
                    System.out.printf(" * Added server tag filter: '%s'\n", type);
                }
            }
        }
        if (serverTagsHidden != null) {
            String[] serverTagsHiddenArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(serverTagsHidden, ",");
            if (Arrays.stream(serverTagsHiddenArray).noneMatch(v -> "skip".trim().equalsIgnoreCase(v))) {
                filter.gamedata(serverTagsHiddenArray);
                for (String type : serverTagsHiddenArray) {
                    System.out.printf(" * Added hidden server tag filter: %s\n", type);
                }
            }
        }
        if (appId != null && appId > 0)
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
        final AtomicInteger addressCtr = new AtomicInteger();
        //fetch server list and block the main thread until it completes
        masterClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (address, sender, error) -> {
            if (error != null)
                throw new CompletionException(ConcurrentUtil.unwrap(error));
            queryServer(address, phaser).whenComplete(processor);
            addressCtr.incrementAndGet();
        }).join();
        System.out.println("\033[0;32mDONE\033[0m");
        return addressCtr.get();
    }

    /**
     * Performs all types of queries for the specified address. This is just one example on how you can combine all operations in one single asynchronous call.
     *
     * @param address
     *         The {@link InetSocketAddress} of the remote server to query
     * @param phaser
     *         The synchronization barrier for managing synchronization between requests/responses.
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
                                .thenCombine(queryClient.getInfo(address).thenApply(SourceQueryInfoResponse::getResult).handle(result.ofType(SourceQueryType.INFO)), Functions::selectFirst)
                                .thenCombine(queryClient.getPlayers(address).handle(result.ofType(SourceQueryType.PLAYERS)), Functions::selectFirst)
                                .thenCombine(queryClient.getRules(address).thenApply(SourceQueryRulesResponse::getResult).handle(result.ofType(SourceQueryType.RULES)), Functions::selectFirst);
    }

    @Override
    public void close() throws IOException {
        if (queryClient != null) {
            queryClient.close();
            System.out.println("(CLOSE) \033[0;35mClosed query client\033[0m");
        }
        if (masterClient != null) {
            masterClient.close();
            System.out.println("(CLOSE) \033[0;35mClosed master client\033[0m");
        }
        if (ConcurrentUtil.shutdown(queryExecutor))
            System.out.println("(CLOSE) \033[0;35mQuery executor gracefully shutdown\033[0m");
        if (ConcurrentUtil.shutdown(masterExecutor))
            System.out.println("(CLOSE) \033[0;35mMaster query executor gracefully shutdown\033[0m");
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
         * A factory method which returns a callback for processing the specified {@link SourceQueryType}. Once a response has been received, the callback generated by this menthod
         * will be notified and data (response or error) will be stored in this instance.
         *
         * @param responseType
         *         The {@link SourceQueryType} identifying the type of response (INFO, PLAYER or RULES) the generated callback will process.
         * @param <R>
         *         Type of the response
         *
         * @return A {@link BiFunction} callback that is notified once a response/error is has been received
         */
        @SuppressWarnings("unchecked")
        private <R> BiFunction<R, Throwable, SourceQueryAggregate> ofType(final SourceQueryType responseType) {
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

        //private final SetMultimap<InetSocketAddress, Throwable> errorMap = MultimapBuilder.SetMultimapBuilder;

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

        public void recordFailure(InetSocketAddress address, Throwable error) {
            this.failureCount.incrementAndGet();
        }
    }

    /**
     * A class for processing {@link SourceQueryAggregate} instances (For Collecting statistics)
     */
    private class SourceQueryAggregateProcessor implements BiConsumer<SourceQueryAggregate, Throwable> {

        private final AtomicInteger counter = new AtomicInteger();

        private final Map<SourceQueryType, SourceQueryStatCounter> stats = new ConcurrentHashMap<>();

        @Override
        public synchronized void accept(SourceQueryAggregate result, Throwable error) {
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
                infoCounter.recordFailure(result.getAddress(), infoError);
            } else {
                infoCounter.recordSuccess();
            }

            //process players
            if (playerError != null) {
                playerCounter.recordFailure(result.getAddress(), playerError);
            } else {
                playerCounter.recordSuccess();
            }

            //process rules
            if (rulesError != null) {
                rulesCounter.recordFailure(result.getAddress(), rulesError);
            } else {
                rulesCounter.recordSuccess();
            }

            if (skipServersInError != null && skipServersInError && result.hasError())
                return;

            System.out.printf("\033[1;35m%05d)\033[0m \u001B[33m%-15s:%05d\u001B[0m => \u001B[34m[PLAYERS]\u001B[0m: %s \u001B[34m[RULES]\u001B[0m: %s \u001B[32m[INFO]\u001B[0m: %-64s\n",
                              counter.incrementAndGet(),
                              result.getAddress().getHostString(),
                              result.getAddress().getPort(),
                              formatResult(result, SourceQueryType.PLAYERS),
                              formatResult(result, SourceQueryType.RULES),
                              formatResult(result, SourceQueryType.INFO)
            );
            System.out.flush();
        }

        public Map<SourceQueryType, SourceQueryStatCounter> getStats() {
            return stats;
        }

        private String formatResult(SourceQueryAggregate result, SourceQueryType type) {
            final int padSize = 20;
            Throwable error = result.getError(type);
            String data;
            if (error != null) {
                if (error instanceof NullPointerException) {
                    error.printStackTrace(System.err);
                }
                return "\u001B[31m" + StringUtils.rightPad(errorName(error), padSize, " ") + "\u001B[0m";
            } else {
                switch (type) {
                    case INFO: {
                        data = result.getInfo() != null ? result.getInfo().getName() : "N/A";
                        break;
                    }
                    case PLAYERS: {
                        data = result.getPlayers() != null ? String.valueOf(result.getPlayers().size()) : "N/A";
                        break;
                    }
                    case RULES: {
                        data = result.getRules() != null ? String.valueOf(result.getRules().size()) : "N/A";
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

        private String errorName(Throwable error) {
            if (error instanceof TimeoutException) {
                return "TIMED OUT";
            } else {
                return error.getClass().getSimpleName();
            }
        }
    }
}
