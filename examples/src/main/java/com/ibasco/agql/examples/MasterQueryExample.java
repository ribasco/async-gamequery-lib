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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        Integer appId = promptInputInt("Please enter an App ID (optional | -1 default): ", true, "-1", "masterAppId");
        final MasterServerFilter filter = buildServerFilter(appId);
        if (appId > 0)
            filter.appId(appId);

        log.info("Displaying Non-Empty servers for App Id: {}", (appId > 0) ? appId : "N/A");
        Set<InetSocketAddress> addresses = null;
        try {
            addressSet.clear();
            addresses = client.getServers(MasterServerType.GOLDSRC, MasterServerRegion.REGION_ALL, filter, this::displayIpStream).join();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (addresses != null)
                System.out.printf("\033[0;32mDONE\033[0m \033[0;36m(Total: %d)\033[0m", addressSet.size());
        }
    }

    private void displayIpStream(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        if (addressSet.add(address)) {
            System.out.printf("\033[0;34mReceived Server Address :\033[0m \033[0;33m%s\033[0m\n", address);
        } else {
            System.out.printf("\033[0;31mReceived Server Address (DUPLICATE) : %s\033[0m\n", address);
        }
    }

    /**
     * Create our server filter using {@link MasterServerFilter} builder
     *
     * @return The {@link MasterServerFilter} created from the user input
     */
    private MasterServerFilter buildServerFilter(Integer appId) {
        System.out.println("Note: Type '\u001B[32mskip\u001B[0m' to exclude filter");

        //Integer appId = promptInputInt("List servers only from this app id (int)", false, null, "srcMasterAppId");
        Boolean nonEmptyServers = promptInputBool("List only non-empty servers? (y/n)", false, null, "srcMasterEmptySvrs");
        Boolean passwordProtected = promptInputBool("List password protected servers? (y/n)", false, null, "srcMasterPassProtect");
        Boolean dedicatedServers = promptInputBool("List dedicated servers (y/n)", false, "y", "srcMasterDedicated");
        String serverTags = promptInput("Specify public server tags (separated with comma)", false, null, "srcMasterServerTagsPublic");
        String serverTagsHidden = promptInput("Specify hidden server tags (separated with comma)", false, null, "srcMasterServerTagsHidden");
        Boolean whiteListedOnly = promptInputBool("Display only whitelisted servers? (y/n)", false, null, "srcMasterWhitelisted");

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
}
