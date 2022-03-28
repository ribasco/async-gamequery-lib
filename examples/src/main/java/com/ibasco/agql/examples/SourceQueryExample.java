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
import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.NetUtil;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.examples.query.PlayersHandler;
import com.ibasco.agql.examples.query.ResponseHandler;
import com.ibasco.agql.examples.query.RulesHandler;
import com.ibasco.agql.examples.query.ServerInfoHandler;
import com.ibasco.agql.protocols.valve.source.query.client.SourceQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

public class SourceQueryExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceQueryExample.class);

    private SourceQueryClient queryClient;

    private MasterServerQueryClient masterClient;

    private final NumberFormat nf = new DecimalFormat("00000");

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final ConcurrentLinkedDeque<DelayedQueryTask> requestQueue = new ConcurrentLinkedDeque<>();

    public SourceQueryExample() {}

    public static void main(String[] args) throws Exception {
        SourceQueryExample example = new SourceQueryExample();
        example.run(args);
        example.close();
    }

    private ScheduledFuture<?> requester;

    @Override
    public void run(String[] args) throws Exception {
        try {
            queryClient = new SourceQueryClient(); //queryOptions
            masterClient = new MasterServerQueryClient();
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
        //noinspection rawtypes
        final Map<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> queries = new HashMap<>();
        queries.put(queryClient::getServerInfo, new ServerInfoHandler(phaser));
        queries.put(queryClient::getPlayers, new PlayersHandler(phaser));
        queries.put(queryClient::getServerRules, new RulesHandler(phaser));

        int requestDelay = Integer.parseInt(promptInput("Delay in request (ms)", true, "200", "requestDelay"));
        startProcessing(requestDelay);

        int total = 0;
        start = System.currentTimeMillis();
        phaser.register();
        if (queryAllServers) {
            final MasterServerFilter filter = buildServerFilter();
            log.info("Fetching server list using filter '{}'", filter);
            total = fetchServersAndQuery(filter, phaser, queries);
        } else {
            String addressString = promptInput("Enter the address of the server you want to query (<ip>:<port>)", true, null, "queryAddress");
            int requestCount = Integer.parseInt(promptInput("How many requests should we run?", false, "1", "requestCount"));
            InetSocketAddress address = NetUtil.parseAddress(addressString, 27015);
            log.info("Waiting for the queries to complete");
            start = System.currentTimeMillis();
            //phaser.register();
            queryServer(address, phaser, queries, requestCount);
            total = 1;
        }
        phaser.arriveAndAwaitAdvance();
        end = System.currentTimeMillis() - start;

        if (end < 1) {
            log.info("Test Completed  in {} seconds", Duration.ofMillis(end).getSeconds());
        } else {
            log.info("Test Completed  in {} minutes", Duration.ofMillis(end).getSeconds() / 60.0D);
        }

        log.info("=================================================================================================================================");
        log.info("Test Results (Total address fetched: {})", total);
        log.info("=================================================================================================================================");
        //noinspection rawtypes
        for (Map.Entry<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> entry : queries.entrySet()) {
            ResponseHandler<?> handler = entry.getValue();
            //assert handler.getTotalCount() == total;
            handler.printStats();
            handler.reset();
        }
        for (Map.Entry<AbstractClient.ClientStatistics.Stat, Integer> e : queryClient.getClientStatistics().getValues().entrySet()) {
            AbstractClient.ClientStatistics.Stat stat = e.getKey();
            Integer count = e.getValue();
            log.info("{} = {}", stat.name(), count);

        }
        log.info("=================================================================================================================================");
    }

    private void startProcessing(int delay) {
        if (this.requester != null && !this.requester.isDone())
            return;
        this.requester = scheduler.scheduleAtFixedRate(() -> {
            DelayedQueryTask task = requestQueue.pollFirst();
            if (task == null)
                return;
            task.run();
        }, 0, delay, TimeUnit.MILLISECONDS);
    }

    private void stopProcessing() {
        if (this.requester == null)
            return;
        if (!this.requester.isDone()) {
            if (this.requester.cancel(true)) {
                this.requester = null;
            }
        }
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

    private static class DelayedQueryTask implements Runnable {

        private final InetSocketAddress address;

        private final Function<InetSocketAddress, CompletableFuture> query;

        private final ResponseHandler<?> handler;

        private DelayedQueryTask(InetSocketAddress address, Function<InetSocketAddress, CompletableFuture> query, ResponseHandler<?> handler) {
            this.address = address;
            this.query = query;
            this.handler = handler;
        }

        @Override
        public void run() {
            //noinspection unchecked
            query.apply(address).whenComplete(handler);
        }
    }

    /**
     * Fetch a new list from the master server and query
     *
     * @param filter
     *         {@link MasterServerFilter}
     */
    private int fetchServersAndQuery(MasterServerFilter filter, Phaser phaser, Map<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> queries) {
        List<InetSocketAddress> addressList = masterClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, (address, sender, error) -> {
            if (error != null)
                throw new CompletionException(ConcurrentUtil.unwrap(error));
            queryServer(address, phaser, queries);
        }).join();
        return addressList.size();
    }

    private void queryServer(InetSocketAddress address, Phaser phaser, Map<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> queries) {
        queryServer(address, phaser, queries, null);
    }

    private void queryServer(InetSocketAddress address, Phaser phaser, Map<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> queries, Integer count) {
        try {
            int total = count == null ? 1 : count;
            for (int i = 0; i < total; i++) {
                for (Map.Entry<Function<InetSocketAddress, CompletableFuture>, ResponseHandler> entry : queries.entrySet()) {
                    phaser.register();
                    log.debug("QUERY => Sending request (Address: {}, {})", address, entry.getKey());
                    requestQueue.addFirst(new DelayedQueryTask(address, entry.getKey(), entry.getValue()));
                }
            }
        } catch (Exception e) {
            log.error("Error occured during processing", e);
        }
    }

    @Override
    public void close() throws IOException {
        log.info("Closing source query client");
        queryClient.close();
        log.info("Closing master query client");
        masterClient.close();
        if (ConcurrentUtil.shutdown(scheduler)) {
            log.info("Successfully shutdown scheduler");
        }
    }
}
