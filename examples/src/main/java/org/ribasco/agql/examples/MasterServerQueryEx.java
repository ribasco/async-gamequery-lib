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

package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.steam.master.MasterServerFilter;
import org.ribasco.agql.protocols.valve.steam.master.client.MasterServerQueryClient;
import org.ribasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import org.ribasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MasterServerQueryEx {

    private static final Logger log = LoggerFactory.getLogger(MasterServerQueryEx.class);

    private MasterServerQueryClient masterServerQueryClient;

    public MasterServerQueryEx() {
        masterServerQueryClient = new MasterServerQueryClient();
    }

    public static void main(String[] args) throws IOException {
        MasterServerQueryEx masterQuery = new MasterServerQueryEx();
        try {
            masterQuery.listAllServers();
        } finally {
            masterQuery.close();
        }
    }

    public void close() throws IOException {
        masterServerQueryClient.close();
    }

    public void listAllServers() {
        MasterServerFilter filter = MasterServerFilter.create().dedicated(true);
        masterServerQueryClient.setSleepTime(8);
        masterServerQueryClient.getServerList(MasterServerType.SOURCE, MasterServerRegion.REGION_ALL, filter, this::displayResult).join();
    }

    public void displayResult(InetSocketAddress address, InetSocketAddress sender, Throwable error) {
        log.info("Server : {}", address);
    }

}
