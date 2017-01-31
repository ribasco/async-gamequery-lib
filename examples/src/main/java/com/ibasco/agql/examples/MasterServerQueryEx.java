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
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MasterServerQueryEx extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MasterServerQueryEx.class);

    private MasterServerQueryClient masterServerQueryClient;

    public static void main(String[] args) throws Exception {
        MasterServerQueryEx app = new MasterServerQueryEx();
        app.run();
    }

    @Override
    public void run() throws Exception {
        masterServerQueryClient = new MasterServerQueryClient();
        this.listAllServers();
    }

    public void close() throws IOException {
        masterServerQueryClient.close();
    }

    public void listAllServers() {
        int appId = Integer.valueOf(promptInput("Please enter an App ID (optional): ", false, "-1"));

        MasterServerFilter filter = MasterServerFilter.create().dedicated(true).isEmpty(false);

        if (appId > 0)
            filter.appId(appId);

        log.info("Displaying Non-Empty servers for App Id: {}", (appId > 0) ? appId : "N/A");
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayIpStream).join();
        log.info("Done");
    }

    public void displayIpStream(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        log.info("Server : {}", address);
    }
}
