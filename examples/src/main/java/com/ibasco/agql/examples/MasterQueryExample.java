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

import com.ibasco.agql.core.util.ConcurrentUtil;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.TransportOptions;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MasterQueryExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MasterQueryExample.class);

    private MasterServerQueryClient client;

    private final ThreadPoolExecutor masterExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                                                               60L, TimeUnit.SECONDS,
                                                               new SynchronousQueue<>(),
                                                               new DefaultThreadFactory("agql-master"));

    private final Set<InetSocketAddress> addressSet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        new MasterQueryExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        Options masterOptions = OptionBuilder.newBuilder()
                                             /*.option(TransportOptions.READ_TIMEOUT, 3000)
                                             .option(MasterServerOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 13L)
                                             .option(MasterServerOptions.FAILSAFE_RATELIMIT_PERIOD, 60000L)
                                             .option(MasterServerOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                             .option(MasterServerOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 1)*/
                                             .option(TransportOptions.THREAD_EXECUTOR_SERVICE, masterExecutor)
                                             .build();
        client = new MasterServerQueryClient(masterOptions);
        this.listAllServers();
    }

    @Override
    public void close() throws IOException {
        client.close();
        ConcurrentUtil.shutdown(masterExecutor);
    }

    public void listAllServers() {
        int appId = Integer.parseInt(promptInput("Please enter an App ID (optional): ", false, "-1"));

        MasterServerFilter filter = MasterServerFilter.create().dedicated(true).isEmpty(false);

        if (appId > 0)
            filter.appId(appId);

        log.info("Displaying Non-Empty servers for App Id: {}", (appId > 0) ? appId : "N/A");
        Vector<InetSocketAddress> addresses = null;
        try {
            addressSet.clear();
            addresses = client.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIpStream).join();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (addresses != null)
                log.info("Done (Total: {})", addresses.size());
        }

    }

    public void displayIpStream(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        if (addressSet.add(address)) {
            log.info("Received Server Address : {}", address);
        } else {
            log.info("Received DUPLICATE Address: {}", address);
        }
    }
}
