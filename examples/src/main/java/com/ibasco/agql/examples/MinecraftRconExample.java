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

import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;
import org.apache.commons.lang3.RegExUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

//TODO: Merge with SourceRconExample
/**
 * <p>MinecraftRconExample class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MinecraftRconExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(MinecraftRconExample.class);

    private SourceRconClient mcRconClient;

    /**
     * For internal testing purposes
     *
     * @param args an array of {@link java.lang.String} objects
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {
        new MinecraftRconExample().run(args);
    }

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        //remember to set the terminator flag to false
        Options options = OptionBuilder.newBuilder().option(SourceRconOptions.USE_TERMINATOR_PACKET, false).build();
        mcRconClient = new SourceRconClient(options);
        this.testRcon();
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        try {
            mcRconClient.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * <p>testRcon.</p>
     *
     * @throws java.lang.InterruptedException if any.
     */
    public void testRcon() throws InterruptedException {
        String address = promptInput("Please enter the minecraft server address", true, "", "mcRconIp");
        int port = promptInputInt("Please enter the server port", false, "25575", "mcRconPort");

        boolean authenticated = false;

        InetSocketAddress serverAddress = new InetSocketAddress(address, port);

        while (!authenticated) {
            String password = promptInput("Please enter the rcon password", true, "", "msRconPass");
            log.info("Connecting to server {}:{}, with password = {}", address, port, RegExUtils.replaceAll(password, ".", "*"));
            SourceRconAuthResponse authResponse = mcRconClient.authenticate(serverAddress, password.getBytes(StandardCharsets.US_ASCII)).join();
            if (!authResponse.isAuthenticated()) {
                log.error("ERROR: Could not authenticate from server (Reason: {})", authResponse.getReason());
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

    private void handleResponse(SourceRconCmdResponse response, Throwable error) {
        if (error != null) {
            log.error("Error occured while executing command: {}", error.getMessage());
            return;
        }
        log.info("Received Reply: \n{}", response.getResult());
    }
}
