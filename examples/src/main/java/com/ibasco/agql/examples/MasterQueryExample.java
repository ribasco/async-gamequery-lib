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

import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MasterQueryExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MasterQueryExample.class);

    private MasterServerQueryClient client;

    private final Set<InetSocketAddress> addressSet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        new MasterQueryExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        client = new MasterServerQueryClient();
        this.listAllServers();
        System.exit(0);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    public void listAllServers() {
        int appId = Integer.parseInt(promptInput("Please enter an App ID (optional | -1 default): ", true, "-1", "masterAppId"));

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
                System.out.printf("\033[0;32mDONE\033[0m \033[0;36m(Total: %d)\033[0m", addressSet.size());
        }
    }

    public void displayIpStream(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        if (addressSet.add(address)) {
            System.out.printf("\033[0;34mReceived Server Address :\033[0m \033[0;33m%s\033[0m\n", address);
        } else {
            System.out.printf("\033[0;31mReceived Server Address (DUPLICATE) : %s\033[0m\n", address);
        }
    }
}
