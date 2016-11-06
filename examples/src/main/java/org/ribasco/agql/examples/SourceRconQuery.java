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
import java.util.function.BiConsumer;

public class SourceRconQuery {
    private static final Logger log = LoggerFactory.getLogger(SourceRconQuery.class);
    private SourceRconClient sourceRconClient;

    public SourceRconQuery() {
        sourceRconClient = new SourceRconClient();
    }

    public static void main(String[] args) {
        try {
            SourceRconQuery query = new SourceRconQuery();
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
                "meta list");

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
                    CompletableFuture<String> resultFuture = sourceRconClient.execute(address1, command)
                            .whenComplete(new BiConsumer<String, Throwable>() {
                                @Override
                                public void accept(String response, Throwable error) {
                                    if (error != null) {
                                        log.error("Error occured while executing command: {}", error.getMessage());
                                        return;
                                    }
                                    log.info("Received Reply for command '{}': \n{}", command, response);
                                }
                            });
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

        //sourceRconClient.waitForAll();
    }
}
