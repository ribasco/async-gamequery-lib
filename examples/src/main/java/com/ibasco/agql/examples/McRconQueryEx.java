/*
 *   MIT License
 *
 *   Copyright (c) 2016 Asynchronous Game Query Library
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
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

public class McRconQueryEx extends BaseExample {
    private static final Logger log = LoggerFactory.getLogger(McRconQueryEx.class);
    private SourceRconClient mcRconClient;

    /**
     * For internal testing purposes
     */
    public static void main(String[] args) throws Exception {
        McRconQueryEx c = new McRconQueryEx();
        c.run();
    }

    @Override
    public void run() throws Exception {
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
