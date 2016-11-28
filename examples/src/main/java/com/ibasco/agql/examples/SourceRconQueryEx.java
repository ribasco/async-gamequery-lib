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
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SourceRconQueryEx implements BaseExample {
    private static final Logger log = LoggerFactory.getLogger(SourceRconQueryEx.class);
    private SourceRconClient sourceRconClient;

    @Override
    public void run() throws Exception {
        sourceRconClient = new SourceRconClient();
        this.testRcon();
    }

    @Override
    public void close() {
        try {
            sourceRconClient.close();
        } catch (IOException ignored) {
        }
    }

    public void testRcon() throws InterruptedException {
        InetSocketAddress address1 = new InetSocketAddress("192.168.1.14", 27015);

        List<String> commands = Arrays.asList(
                "status",
                "cvarlist",
                "sm plugins list",
                "echo from command 4",
                "sm version",
                "sm exts list",
                "meta version",
                "meta list",
                "unknown_cmd");

        try {
            //Authenticate
            sourceRconClient.authenticate(address1, "password").whenComplete((success, throwable) -> {
                if (success != null) {
                    log.info("Successfully Authenticated for {}", address1);
                } else
                    log.error("Problem authenticating rcon with {}", address1);
            }).join();

            List<CompletableFuture> futures = new ArrayList<>();

            try {
                for (String command : commands) {
                    log.info("Executing command '{}'", command);
                    CompletableFuture<String> resultFuture = sourceRconClient.execute(address1, command).whenComplete(this::handleResponse);
                    futures.add(resultFuture);
                }
            } catch (RconNotYetAuthException e) {
                e.printStackTrace();
            }

            //Wait
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
