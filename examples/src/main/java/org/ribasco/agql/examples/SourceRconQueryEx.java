package org.ribasco.agql.examples;

import org.ribasco.agql.protocols.valve.source.query.client.SourceRconClient;
import org.ribasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SourceRconQueryEx {
    private static final Logger log = LoggerFactory.getLogger(SourceRconQueryEx.class);
    private SourceRconClient sourceRconClient;

    public SourceRconQueryEx() {
        sourceRconClient = new SourceRconClient();
    }

    public static void main(String[] args) {
        try {
            SourceRconQueryEx query = new SourceRconQueryEx();
            query.testRcon();
            query.close();
        } catch (InterruptedException e) {
            log.error("Error", e);
        }
    }

    private void close() {
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
            sourceRconClient.authenticate(address1, "***REMOVED***").whenComplete((success, throwable) -> {
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
