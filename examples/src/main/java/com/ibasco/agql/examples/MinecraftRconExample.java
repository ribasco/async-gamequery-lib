/*
 * Copyright (c) 2018-2022 Asynchronous Game Query Library
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
import com.ibasco.agql.protocols.valve.source.query.SourceRconAuthStatus;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinecraftRconExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MinecraftRconExample.class);

    private SourceRconClient mcRconClient;

    /**
     * For internal testing purposes
     */
    public static void main(String[] args) throws Exception {
        new MinecraftRconExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        //remember to set the terminator flag to false
        mcRconClient = new SourceRconClient(false);
        this.testRcon();
    }

    @Override
    public void close() {
        try {
            mcRconClient.close();
        } catch (IOException ignored) {
        }
    }

    public void testRcon() throws InterruptedException {
        String address = promptInput("Please enter the minecraft server address", true, "", "mcRconIp");
        int port = Integer.valueOf(promptInput("Please enter the server port", false, "25575", "mcRconPort"));

        boolean authenticated = false;

        InetSocketAddress serverAddress = new InetSocketAddress(address, port);

        while (!authenticated) {
            String password = promptInput("Please enter the rcon password", true, "", "msRconPass");
            log.info("Connecting to server {}:{}, with password = {}", address, port, StringUtils.replaceAll(password, ".", "*"));
            SourceRconAuthStatus authStatus = mcRconClient.authenticate(serverAddress, password).join();
            if (!authStatus.isAuthenticated()) {
                log.error("ERROR: Could not authenticate from server (Reason: {})", authStatus.getReason());
            } else
                authenticated = true;
        }

        log.info("Successfully authenticated from server : {}", address);
        while (true) {
            String command = promptInput("\nEnter rcon command: ", true);
            try {
                mcRconClient.execute(serverAddress, command).whenComplete(this::handleResponse).join();
            } catch (RconNotYetAuthException e) {
                throw new RuntimeException(e);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    private void handleResponse(String response, Throwable error) {
        if (error != null) {
            log.error("Error occured while executing command: {}", error.getMessage());
            return;
        }
        log.info("Received Reply: \n{}", response);
    }
}
