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

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.exceptions.MaxAttemptsReachedException;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.util.Concurrency;
import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.FailsafeOptions;
import com.ibasco.agql.core.util.Net;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.common.message.SourceQueryResponse;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoRequest;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.info.SourceServer;
import com.ibasco.agql.protocols.valve.source.query.players.SourcePlayer;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayerRequest;
import com.ibasco.agql.protocols.valve.source.query.players.SourceQueryPlayerResponse;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesRequest;
import com.ibasco.agql.protocols.valve.source.query.rules.SourceQueryRulesResponse;
import com.ibasco.agql.protocols.valve.steam.master.MasterServer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerOptions;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Advanced example for executing batch asynchronous queries
 *
 * @author Rafael Luis Ibasco
 */
public class SourceQueryExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryExample.class);

    private final ThreadPoolExecutor masterExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                                                                             60L, TimeUnit.SECONDS,
                                                                             new SynchronousQueue<>(),
                                                                             new DefaultThreadFactory("master"));

    private final ThreadPoolExecutor queryExecutor = new ThreadPoolExecutor(9, Integer.MAX_VALUE,
                                                                            60L, TimeUnit.SECONDS,
                                                                            new LinkedBlockingQueue<>(),
                                                                            new DefaultThreadFactory("query"));

    private SourceQueryClient queryClient;

    private MasterServerQueryClient masterClient;

    private Boolean skipServersInError;

    /**
     * <p>Constructor for SourceQueryExample.</p>
     */
    public SourceQueryExample() {}

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        try {
            printConsoleBanner();
            System.out.println();

            //query client configuration
            // - Enabled rate limiting so we don't send too fast
            // - set rate limit type to SMOOTH so requests are sent evenly at a steady rate
            // - set retry max attempts and backoff parameters
            // - Provide a custom executor for query client. We are responsible for shutting down this executor, not the library.
            // - Set channel pooling strategy to FIXED (POOL_TYPE) with using a fixed number of pooled connections of 50 (POOL_MAX_CONNECTIONS)
            // - Set read timeout to 1000ms (1 second)
            /*SourceQueryOptions queryOptions = SourceQueryOptions.builder()
                                                                //override default value, enable rate limiting (default is: false)
                                                                .option(GeneralOptions.CONNECTION_POOLING, false)
                                                                .option(GeneralOptions.POOL_TYPE, ChannelPoolType.ADAPTIVE)
                                                                .option(GeneralOptions.POOL_MAX_CONNECTIONS, 50)
                                                                .option(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED, true)
                                                                .option(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                                .option(FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 5)
                                                                .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, true)
                                                                .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 50L)
                                                                .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L)
                                                                .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d)
                                                                //.option(GeneralOptions.THREAD_EXECUTOR_SERVICE, queryExecutor) //un-comment to use the provided custom executor
                                                                .option(GeneralOptions.READ_TIMEOUT, 5000)
                                                                .build();
            queryClient = new SourceQueryClient(queryOptions);*/
            queryClient = new SourceQueryClient();

            //master client configuration
            // - Configuring the Rate limit type to SMOOTH
            // - Provide a custom executor for master client. We are responsible for shutting down this executor, not the library.
            MasterServerOptions masterOptions = MasterServerOptions.builder()
                                                                   .option(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                                   //.option(GeneralOptions.THREAD_EXECUTOR_SERVICE, masterExecutor) //un-comment to use the provided custom executor
                                                                   .build();
            masterClient = new MasterServerQueryClient(masterOptions);
            runQueries();
        } catch (Exception e) {
            log.error("Failed to run query: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * <p>This will execute all three server queries at once and display the output to the user.</p>
     *
     * @throws java.lang.Exception
     *         if any.
     */
    public void runQueries() throws Exception {
        Boolean queryAllServers = promptInputBool("Query all available servers? (y/n)", true, "y", "queryAllServers");
        long start, end;

        final QueryAggregateProcessor processor = new QueryAggregateProcessor();

        //We use Phaser as our synchronization barrier
        // since at this point, we do not yet know how many servers we will query.
        final Phaser phaser = new Phaser();

        AtomicInteger total = new AtomicInteger();
        start = System.currentTimeMillis();
        phaser.register();

        //Retrieve all game servers from the master server and execute all three queries (INFO, PLAYER, RULES)
        if (queryAllServers) {
            System.out.println("Note: Type '\u001B[32mskip\u001B[0m' to exclude filter");
            final MasterServerFilter filter = buildServerFilter();
            printLine();
            System.out.printf("\033[1;36mFetching server list using filter \033[1;33m'%s'\033[0m\n", filter);
            printLine();
            fetchServersAndQuery(filter, phaser, processor, total); //this will block until we have received all addresses
        }
        //Only query a single server instance
        else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, null, "queryAddress");
            int iterations = promptInputInt("How many times should we repeat our queries? (int)", false, "1", "queryIterations");
            InetSocketAddress address = Net.parseAddress(addressString, 27015);
            System.out.println("Waiting for the queries to complete");
            start = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++)
                queryServer(address, phaser).whenComplete(processor).join();
            total.set(1);
        }
        //tell phaser to block, until all the registered parties (info, players and rules) have completed.
        phaser.arriveAndAwaitAdvance();

        //measure the duration
        end = System.currentTimeMillis() - start;

        printLine();
        if (end < 1) {
            System.out.printf("\033[0;33mTEST COMPLETED  in \033[0;36m%03d\033[0;33m seconds\033[0m\n", Duration.ofMillis(end).getSeconds());
        } else {
            System.out.printf("\033[0;33mTEST COMPLETED  in \033[0;36m%02f\033[0;33m minutes\033[0m\n", Duration.ofMillis(end).getSeconds() / 60.0D);
        }
        System.out.flush();

        printLine();
        System.out.printf("\033[1;33mTEST RESULTS (Total address processed: \033[0;36m%d\033[1;33m)\033[0m\n", total.get());
        printLine();
        System.out.flush();

        //Display the collected query statistics
        for (Map.Entry<QueryType, QueryStatsCounter> entry : processor.getStats().entrySet()) {
            String name = entry.getKey().name();
            QueryStatsCounter stat = entry.getValue();
            System.out.printf("\033[0;33m%-10s\033[0m (\033[1;32mSuccess\033[0m: %05d, \033[1;31mFailure\033[0m: %05d, Total: %05d)\n", name, stat.getSuccessCount(), stat.getFailureCount(), stat.getTotalCount());
        }
        System.out.flush();
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
        Boolean passwordProtected = promptInputBool("List password protected servers? (y/n)", false, null, "srcQryPassProtect");
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
     * Fetch a new list from the master server then attempt to obtains the INFO, PLAYERS and RULES of each address.
     *
     * @param filter
     *         {@link MasterServerFilter}
     */
    private void fetchServersAndQuery(final MasterServerFilter filter, final Phaser phaser, QueryAggregateProcessor processor, AtomicInteger counter) {
        phaser.register();
        //fetch server list and block the main thread until it completes
        masterClient.getServers(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (address, sender, error) -> {
                        if (error != null)
                            throw new CompletionException(Errors.unwrap(error));
                        queryServer(address, phaser).whenComplete(processor);
                        counter.incrementAndGet();
                    })
                    .thenAccept(response -> System.out.printf("\033[0;33m[MASTER QUERY]: Completed fetching a total of \033[0;36m'%d'\033[0;33m addresses\033[0m\n", counter.get()))
                    .thenRun(phaser::arriveAndDeregister);
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
    private CompletableFuture<QueryAggregate> queryServer(final InetSocketAddress address, final Phaser phaser) {
        QueryAggregate result = new QueryAggregate(address, phaser);
        if (MasterServer.isTerminatingAddress(address))
            return Concurrency.failedFuture(new IllegalArgumentException("Invalid address: " + address));
        //register three parties for the info, player and rules queries
        phaser.bulkRegister(3);
        //combining all three queries in one call
        return CompletableFuture.completedFuture(result)
                                .thenCombine(queryClient.getInfo(address).handle(QueryResponse.ofInfoType()), result::aggregate)
                                .thenCombine(queryClient.getPlayers(address).handle(QueryResponse.ofPlayerType()), result::aggregate)
                                .thenCombine(queryClient.getRules(address).handle(QueryResponse.ofRulesType()), result::aggregate);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        close(queryClient, "Query");
        close(masterClient, "Master");
        close(queryExecutor, "Query");
        close(masterExecutor, "Master");
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

    //<editor-fold desc="Private class/enum">

    /**
     * Enumeration for identifying the type of source query
     */
    private enum QueryType {
        INFO(SourceQueryInfoRequest.class, SourceQueryInfoResponse.class),
        PLAYERS(SourceQueryPlayerRequest.class, SourceQueryPlayerResponse.class),
        RULES(SourceQueryRulesRequest.class, SourceQueryRulesResponse.class);

        private final Class<? extends AbstractRequest> requestClass;

        private final Class<? extends AbstractResponse> responseClass;

        QueryType(Class<? extends AbstractRequest> requestClass, Class<? extends AbstractResponse> responseClass) {
            this.requestClass = requestClass;
            this.responseClass = responseClass;
        }

        public Class<? extends AbstractRequest> getRequestClass() {
            return requestClass;
        }

        public Class<? extends AbstractResponse> getResponseClass() {
            return responseClass;
        }
    }

    /**
     * A response container for a specific {@link QueryType} which stores both {@link SourceQueryResponse} and {@link Throwable} values (whichever is returned).
     */
    private static class QueryResponse<T> {

        private final QueryType type;

        private SourceQueryResponse<T> response;

        private Throwable error;

        private QueryResponse(final QueryType type) {
            this.type = type;
        }

        public static BiFunction<SourceQueryInfoResponse, Throwable, QueryResponse<SourceServer>> ofInfoType() {
            return ofType(QueryType.INFO);
        }

        public static <V, Q extends SourceQueryResponse<V>> BiFunction<Q, Throwable, QueryResponse<V>> ofType(QueryType type) {
            return new QueryResponse<V>(type)::save;
        }

        private QueryResponse save(SourceQueryResponse<T> response, Throwable error) {
            this.response = response;
            this.error = error;
            return this;
        }

        public static BiFunction<SourceQueryPlayerResponse, Throwable, QueryResponse<List<SourcePlayer>>> ofPlayerType() {
            return ofType(QueryType.PLAYERS);
        }

        public static BiFunction<SourceQueryRulesResponse, Throwable, QueryResponse<Map<String, String>>> ofRulesType() {
            return ofType(QueryType.RULES);
        }

        public QueryType getQueryType() {
            return type;
        }

        public SourceQueryResponse<T> getResponse() {
            return response;
        }

        public Throwable getError() {
            return error;
        }

        public boolean hasResponse() {
            return response != null;
        }

        public boolean hasError() {
            return error != null;
        }
    }

    /**
     * An aggregate for INFO, PLAYER and RULES queries for a specific server {@link InetSocketAddress}.
     */
    private static class QueryAggregate {

        private final InetSocketAddress address;

        private final Phaser phaser;

        private QueryResponse<SourceServer> infoQuery;

        private QueryResponse<Collection<SourcePlayer>> playersQuery;

        private QueryResponse<Map<String, String>> rulesQuery;

        private QueryAggregate(final InetSocketAddress address, final Phaser phaser) {
            this.address = address;
            this.phaser = phaser;
        }

        private QueryResponse<SourceServer> infoQuery() {
            return infoQuery;
        }

        private QueryResponse<Collection<SourcePlayer>> playersQuery() {
            return playersQuery;
        }

        private QueryResponse<Map<String, String>> rulesQuery() {
            return rulesQuery;
        }

        private InetSocketAddress getAddress() {
            return address;
        }

        private Throwable getError(QueryType type) {
            switch (type) {
                case INFO: {
                    if (infoQuery == null)
                        return null;
                    return infoQuery.getError();
                }
                case PLAYERS: {
                    if (playersQuery == null)
                        return null;
                    return playersQuery.getError();
                }
                case RULES: {
                    if (rulesQuery == null)
                        return null;
                    return rulesQuery.getError();
                }
                default: {
                    throw new IllegalStateException("Invalid query type");
                }
            }
        }

        private boolean hasError() {
            return infoQuery.hasError() || playersQuery.hasError() || rulesQuery.hasError();
        }

        private boolean hasError(QueryType type) {
            switch (type) {
                case INFO: {
                    return infoQuery != null && infoQuery.hasError();
                }
                case PLAYERS: {
                    return playersQuery != null && playersQuery.hasError();
                }
                case RULES: {
                    return rulesQuery != null && rulesQuery.hasError();
                }
                default: {
                    throw new IllegalStateException("Invalid query type");
                }
            }
        }

        /**
         * A function which collects the recieved {@link QueryResponse} for the specific {@link QueryType}
         *
         * @param aggregate
         *         The {@link QueryAggregate} which holds all resulting queries for an {@link InetSocketAddress}
         * @param queryResponse
         *         The {@link QueryResponse} to be collected and stored
         *
         * @return A {@link QueryAggregate} instance
         */
        @SuppressWarnings("unchecked")
        private <X> QueryAggregate aggregate(QueryAggregate aggregate, QueryResponse<X> queryResponse) {
            try {
                switch (queryResponse.getQueryType()) {
                    case INFO: {
                        assert queryResponse.getResponse() instanceof SourceQueryInfoResponse;
                        aggregate.infoQuery((QueryResponse<SourceServer>) queryResponse);
                        break;
                    }
                    case PLAYERS: {
                        assert queryResponse.getResponse() instanceof SourceQueryPlayerResponse;
                        aggregate.playersQuery((QueryResponse<Collection<SourcePlayer>>) queryResponse);
                        break;
                    }
                    case RULES: {
                        assert queryResponse.getResponse() instanceof SourceQueryRulesResponse;
                        aggregate.rulesQuery((QueryResponse<Map<String, String>>) queryResponse);
                        break;
                    }
                }
                return this;
            } finally {
                phaser.arriveAndDeregister();
            }
        }

        private void infoQuery(QueryResponse<SourceServer> infoQuery) {
            this.infoQuery = infoQuery;
        }

        private void playersQuery(QueryResponse<Collection<SourcePlayer>> playersQuery) {
            this.playersQuery = playersQuery;
        }

        private void rulesQuery(QueryResponse<Map<String, String>> rulesQuery) {
            this.rulesQuery = rulesQuery;
        }
    }

    /**
     * A stat counter for a specific {@link QueryType}
     */
    private static class QueryStatsCounter {

        private final QueryType type;

        private final AtomicInteger successCount = new AtomicInteger();

        private final AtomicInteger failureCount = new AtomicInteger();

        private QueryStatsCounter(QueryType type) {
            this.type = type;
        }

        public QueryType getType() {
            return type;
        }

        public int getTotalCount() {
            return getSuccessCount() + getFailureCount();
        }

        public int getSuccessCount() {
            return successCount.get();
        }

        public int getFailureCount() {
            return failureCount.get();
        }

        public void recordSuccess() {
            this.successCount.incrementAndGet();
        }

        public void recordFailure(InetSocketAddress address, Throwable error) {
            this.failureCount.incrementAndGet();
        }
    }

    /**
     * A class for processing {@link QueryAggregate} instances (For Collecting statistics)
     */
    private class QueryAggregateProcessor implements BiConsumer<QueryAggregate, Throwable> {

        private final AtomicInteger counter = new AtomicInteger();

        private final Map<QueryType, QueryStatsCounter> stats = new ConcurrentHashMap<>();

        @Override
        public void accept(QueryAggregate result, Throwable error) {
            if (error != null)
                throw new CompletionException(Errors.unwrap(error));

            QueryStatsCounter infoCounter = stats.computeIfAbsent(QueryType.INFO, QueryStatsCounter::new);
            QueryStatsCounter playerCounter = stats.computeIfAbsent(QueryType.PLAYERS, QueryStatsCounter::new);
            QueryStatsCounter rulesCounter = stats.computeIfAbsent(QueryType.RULES, QueryStatsCounter::new);

            Throwable infoError = result.getError(QueryType.INFO);
            Throwable playerError = result.getError(QueryType.PLAYERS);
            Throwable rulesError = result.getError(QueryType.RULES);

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
                              formatResult(result, QueryType.PLAYERS),
                              formatResult(result, QueryType.RULES),
                              formatResult(result, QueryType.INFO)
            );
            System.out.flush();
        }

        private String formatResult(QueryAggregate aggregate, QueryType type) {
            final int padSize = 20;
            Throwable error = Errors.unwrap(aggregate.getError(type));
            String data;
            if (error != null) {
                if (error instanceof NullPointerException) {
                    error.printStackTrace(System.err);
                }
                return "\u001B[31m" + StringUtils.rightPad(errorName(error), padSize, " ") + "\u001B[0m";
            } else {
                switch (type) {
                    case INFO: {
                        data = aggregate.infoQuery().hasResponse() ? aggregate.infoQuery().getResponse().getResult().getName() : "N/A";
                        break;
                    }
                    case PLAYERS: {
                        data = aggregate.playersQuery().hasResponse() ? String.valueOf(aggregate.playersQuery().getResponse().getResult().size()) : "N/A";
                        break;
                    }
                    case RULES: {
                        SourceQueryRulesResponse rulesResponse = (SourceQueryRulesResponse) aggregate.rulesQuery().getResponse();
                        data = aggregate.rulesQuery().hasResponse() ? String.valueOf(rulesResponse.getResult().size()) : "N/A";
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
            } else if (error instanceof MaxAttemptsReachedException) {
                return "RETRY_EXCEEDED (" + ((MaxAttemptsReachedException) error).getAttemptCount() + ")";
            } else if (error instanceof RejectedExecutionException) {
                return "CANCELLED";
            } else {
                return error.getClass().getSimpleName();
            }
        }

        public Map<QueryType, QueryStatsCounter> getStats() {
            return stats;
        }
    }
    //</editor-fold>
}
