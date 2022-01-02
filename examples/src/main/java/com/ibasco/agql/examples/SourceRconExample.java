/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.examples;

import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.examples.query.ResponseHandler;
import com.ibasco.agql.protocols.valve.source.query.SourceRconAuthStatus;
import com.ibasco.agql.protocols.valve.source.query.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconInvalidCredentialsException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.message.SourceRconCmdResponse;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SourceRconExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceRconExample.class);

    private static final String line = StringUtils.repeat("=", 158);

    private SourceRconClient rconClient;

    private InetSocketAddress serverAddress;

    private static boolean interactive;

    private static final String[] COMMANDS = new String[] {"status", "sm plugins list", "cvarlist", "maps *", "meta list", "sm exts list", "sm version", "find sv", "help sv_cheats"};

    private final AtomicBoolean authenticated = new AtomicBoolean();

    /**
     * For internal testing purposes
     */
    public static void main(String[] args) throws Exception {
        new SourceRconExample().run(args);
    }

    @Override
    public void run(String[] args) throws Exception {
        if (args != null && args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            if ("i".equalsIgnoreCase(args[0])) {
                interactive = true;
            } else {
                interactive = false;
            }
        }

        final OptionMap rconOptions = OptionBuilder.newBuilder()
                                                   .option(TransportOptions.POOL_MAX_CONNECTIONS, 8)
                                                   .option(SourceRconOptions.USE_TERMINATOR_PACKET, true)
                                                   .option(SourceRconOptions.STRICT_MODE, false)
                                                   .option(TransportOptions.POOL_ACQUIRE_TIMEOUT, Integer.MAX_VALUE)
                                                   .option(TransportOptions.READ_TIMEOUT, 5000)
                                                   .option(TransportOptions.CONNECTION_POOLING, true)
                                                   .option(TransportOptions.POOL_TYPE, ChannelPoolType.FIXED)
                                                   .build();

        try {
            rconClient = new SourceRconClient(rconOptions);
            printConsoleBanner();
            if (interactive)
                this.runRconConsole();
            else
                this.runBenchmark();
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        try {
            log.debug("Closing client");
            rconClient.close();
        } catch (IOException ignored) {
        }
    }

    private void authenticate(Map<InetSocketAddress, String> auth) throws RconNotYetAuthException {
        final Phaser phaser = new Phaser();
        //authenticate
        phaser.register();
        Map<InetSocketAddress, Boolean> result = new HashMap<>();
        for (Map.Entry<InetSocketAddress, String> server : auth.entrySet()) {
            log.info("Authenticating address '{}' with password '{}'", server.getKey(), server.getValue());
            phaser.register();
            rconClient.authenticate(server.getKey(), server.getValue().getBytes()).whenComplete((status, error) -> {
                try {
                    if (error != null) {
                        log.error("AUTH ERROR", error);
                        result.put(server.getKey(), false);
                    } else {
                        if (status.isAuthenticated()) {
                            log.info("({}) Successfully authenticated", status.getAddress());
                            result.put(server.getKey(), true);
                        } else {
                            log.info("({}) Authentication Failed: {}", status.getAddress(), status.getReason());
                            result.put(server.getKey(), false);
                        }
                    }
                } finally {
                    phaser.arriveAndDeregister();
                }
            });
        }
        phaser.arriveAndAwaitAdvance();

        if (result.containsValue(false)) {
            result.entrySet().stream().filter(p -> !p.getValue()).forEach(e -> log.error("Server {} did not authenticate successfully", e.getKey()));
            throw new RconNotYetAuthException("A server did not authenticate successfully");
        }
    }

    private void runBenchmark() throws Exception {
        final Map<InetSocketAddress, String> servers = new HashMap<>();
        servers.put(new InetSocketAddress("192.168.1.34", 27016), "G8oGC24io5zUt6ErS5ShD");
        servers.put(new InetSocketAddress("192.168.1.34", 27017), "twdeyprtkfs6TsH5SMqiR");
        //servers.put(new InetSocketAddress("192.168.1.34", 27018), "twdeyprtkfs6TsH5SMqiR");
        authenticate(servers);

        int size = 10000;
        final AtomicInteger success = new AtomicInteger();
        final AtomicInteger fail = new AtomicInteger();

        log.info("Running benchmark test ({} commands)", size);
        long start = System.nanoTime();

        final Phaser phaser = new Phaser();
        final BiConsumer<SourceRconCmdResponse, Throwable> handler = new BiConsumer<SourceRconCmdResponse, Throwable>() {

            @Override
            public synchronized void accept(SourceRconCmdResponse response, Throwable error) {
                try {
                    if (error != null) {
                        log.error("COMMAND [ERROR]", error);
                        fail.incrementAndGet();
                    } else {
                        if (!response.isSuccess()) {
                            log.error("COMMAND [ERROR]", response.getError());
                            fail.incrementAndGet();
                        } else {
                            success.incrementAndGet();
                        }
                    }
                } finally {
                    phaser.arriveAndDeregister();
                    int successCount = success.get();
                    int failCount = fail.get();
                    int totalCount = successCount + failCount;
                    int remaining = phaser.getUnarrivedParties();
                    if ((totalCount % 1000) == 0) {
                        log.info("Processed a total of {} queries in {} secs (Remaining: {}, Success: {}, Failed: {})", totalCount, Duration.ofNanos(System.nanoTime() - start).getSeconds(), remaining, successCount, failCount);
                    }
                }
            }
        };
        sendCommandBatch(size, servers.keySet(), handler, phaser);
        Duration duration = Duration.ofNanos(System.nanoTime() - start);
        log.info("Done. (Total Commands: {}, Duration: {} ms, Count: {}, Success: {}, Fail: {})", size, duration.toMillis(), phaser.getArrivedParties(), success.get(), fail.get());
        rconClient.printStatistics();
    }

    private void printConsoleBanner() {
        System.out.println("██████╗  ██████╗ ██████╗ ███╗   ██╗     ██████╗ ██████╗ ███╗   ██╗███████╗ ██████╗ ██╗     ███████╗");
        System.out.println("██╔══██╗██╔════╝██╔═══██╗████╗  ██║    ██╔════╝██╔═══██╗████╗  ██║██╔════╝██╔═══██╗██║     ██╔════╝");
        System.out.println("██████╔╝██║     ██║   ██║██╔██╗ ██║    ██║     ██║   ██║██╔██╗ ██║███████╗██║   ██║██║     █████╗  ");
        System.out.println("██╔══██╗██║     ██║   ██║██║╚██╗██║    ██║     ██║   ██║██║╚██╗██║╚════██║██║   ██║██║     ██╔══╝  ");
        System.out.println("██║  ██║╚██████╗╚██████╔╝██║ ╚████║    ╚██████╗╚██████╔╝██║ ╚████║███████║╚██████╔╝███████╗███████╗");
        System.out.println("╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝     ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚══════╝╚══════╝");
        System.out.println("                                                         Powered by Asynchronous Game Query Library");
    }

    public void runRconConsole() {
        String address = promptInput("Enter server address", true, "", "sourceRconIp");
        int port = Integer.parseInt(promptInput("Enter server port", false, "27015", "sourceRconPort"));

        //twdeyprtkfs6TsH5SMqiR
        serverAddress = new InetSocketAddress(address, port);
        AtomicBoolean stop = new AtomicBoolean();
        final BiConsumer<SourceRconCmdResponse, Throwable> handler = (res, error) -> {
            if (error != null) {
                Throwable cause = error instanceof CompletionException || error instanceof ExecutionException ? error.getCause() : error;
                if (cause instanceof CancellationException) {
                    System.out.println("Shutting down example program");
                    stop.compareAndSet(false, true);
                } else if (cause instanceof RconInvalidCredentialsException) {
                    System.err.println(cause.getMessage());
                    authenticated.set(false);
                } else if (cause instanceof ParseException) {
                    System.err.println(cause.getMessage());
                } else {
                    System.err.println("Error during execution of command: ");
                    cause.printStackTrace(System.err);
                }
                return;
            }
            if (res != null) {
                System.out.printf("\n%s\n", res.getResult());
                log.debug(res.getResult());
            }
        };

        while (!stop.get()) {
            if (!authenticated.get()) {
                String password = promptInput("Password", true, "", "sourceRconPass");
                System.out.printf("Connecting to server %s:%d, with password = %s\n", address, port, StringUtils.replaceAll(password, ".", "*"));
                try {
                    SourceRconAuthStatus status = rconClient.authenticate(serverAddress, password).join();
                    authenticated.set(status.isAuthenticated());
                    if (!authenticated.get())
                        System.err.printf("Error authenticating with server: '%s'\n", status.getReason());
                } catch (CompletionException e) {
                    if (e.getCause() instanceof ClosedChannelException) {
                        log.error("Connection dropped by remote server");
                        continue;
                    }
                    throw e;
                }
                continue;
            }
            String promptText = String.format("[%s:%d]: ", serverAddress.getAddress().getHostAddress(), serverAddress.getPort());
            String cmd = promptInput(promptText, true);
            parseCommand(cmd).whenComplete(handler).thenAccept(sourceRconCmdResponse -> System.out.print(promptText));
        }
    }

    private String promptRconInput() {
        return promptInput(String.format("\n[%s:%d]: ", serverAddress.getAddress().getHostAddress(), serverAddress.getPort()), true);
    }

    private CompletableFuture<SourceRconCmdResponse> parseCommand(String command) {
        if (command == null || command.trim().isEmpty())
            return ConcurrentUtil.failedFuture(new IllegalArgumentException("Command must not be empty"));
        command = command.trim();

        if (command.startsWith("/")) {
            String[] args = StringUtils.splitByWholeSeparatorPreserveAllTokens(command, StringUtils.SPACE, 3);
            if (command.startsWith("/help") || command.startsWith("/?") || command.startsWith("/h")) {
                commandUsage();
            } else if (command.startsWith("/stats")) {
                rconClient.printStatistics(System.out::println);
            } else if (command.startsWith("/batch")) {
                return commandBatch(args);
            } else if (command.startsWith("/quit")) {
                return ConcurrentUtil.failedFuture(new CancellationException());
            } else if (command.startsWith("/invalidate")) {
                return commandInvalidate(args);
            } else if (command.startsWith("/authenticate")) {
                return commandAuthenticate(args);
            } else if (command.startsWith("/cleanup")) {
                return commandCleanup(args);
            }
            else {
                return error(String.format("Unknown command '%s' (type /help for the commands available)", command));
            }
            return CompletableFuture.completedFuture(null);
        }

        //System.out.printf("Command '%s' sent\n", command);
        return rconClient.exec(serverAddress, command);
    }

    private CompletableFuture<SourceRconCmdResponse> commandCleanup(String[] args) {
        rconClient.cleanup();
        return success("done", args[0]);
    }

    private CompletableFuture<SourceRconCmdResponse> commandAuthenticate(String[] args) {
        authenticated.set(false);
        return success("done", args[0]);
    }

    private CompletableFuture<SourceRconCmdResponse> commandInvalidate(String[] args) {
        if (args.length < 1)
            return error("Arguments must be at least 1");
        rconClient.invalidate();
        return success("done", args[0]);
    }

    private CompletableFuture<SourceRconCmdResponse> commandBatch(String[] args) {
        if (args == null || args.length < 2)
            return ConcurrentUtil.failedFuture(new ParseException("Usage: /batch <amount> <command1>[;<command2>;<command3>]", 0));
        if (!"/batch".equalsIgnoreCase(args[0]))
            return error("Invalid first argument");
        if (!StringUtils.isNumeric(args[1]))
            return error("Second argument must be numeric");
        int count = NumberUtils.toInt(args[1], 1);
        String cmd = args.length >= 3 ? args[2] : null;
        if (cmd == null)
            System.out.println("Executing random commands");
        return executeBatch(count, cmd);
    }

    private void commandUsage() {
        clearConsole();
        System.out.println(line);
        System.out.println("RCON Console Commands");
        System.out.println(line);
        System.out.println("Rcon Command: <command>");
        System.out.println("Rcon Batch Command: /batch <amount> [command]");
        System.out.println("Help: /help, /h, /?");
        System.out.println("Invalidate connections: /invalidate");
        System.out.println("Statistics: /stats");
        System.out.println("Re-authenticate: /authenticate");
        System.out.println("Cleanup unsused channels: /cleanup");

    }

    private SourceRconCmdResponse successResponse(String msg, Object... args) {
        return new SourceRconCmdResponse(0, "", String.format(msg, args), true);
    }

    private void sendCommand(int count, InetSocketAddress address, BiConsumer<SourceRconCmdResponse, Throwable> handler, Phaser phaser) {
        for (int i = 0; i < count; i++) {
            phaser.register();
            String command = COMMANDS[RandomUtils.nextInt(0, COMMANDS.length)];
            rconClient.exec(address, command).whenComplete(handler);
        }
    }

    private void sendCommandBatch(int count, Set<InetSocketAddress> addressSet, BiConsumer<SourceRconCmdResponse, Throwable> handler, Phaser phaser) throws Exception {
        phaser.register();
        for (InetSocketAddress address : addressSet)
            sendCommand(count, address, handler, phaser);
        phaser.arriveAndAwaitAdvance();
    }

    private CompletableFuture<SourceRconCmdResponse> executeBatch(int count, String command) {
        return CompletableFuture.runAsync(() -> {

            System.out.println(line);
            System.out.printf("Executing %d '%s' command(s)\n", count, command == null ? "random" : command);
            System.out.println(line);

            final CountDownLatch latch = new CountDownLatch(count);
            final RconCommandHandler handleResponse = new RconCommandHandler(count, latch);
            try {
                log.info("Waiting for all futures to complete");
                for (int i = 0; i < count; i++) {
                    String cmd1 = command;
                    if (cmd1 == null)
                        cmd1 = COMMANDS[RandomUtils.nextInt(0, COMMANDS.length)];
                    rconClient.exec(serverAddress, cmd1).whenComplete(handleResponse);
                }
                latch.await();
            } catch (InterruptedException e) {
                throw new CompletionException(e);
            } finally {
                System.out.println(line);
                handleResponse.printStats();
                System.out.println(line);
            }
        }).thenApply(unsusd -> successResponse("DONE: Executed a total of %d command(s) to %s", count, serverAddress));
    }

    private CompletableFuture<SourceRconCmdResponse> success(String msg, String command) {
        return CompletableFuture.completedFuture(new SourceRconCmdResponse(0, command, msg, true));
    }

    private <V> CompletableFuture<V> error(String msg) {
        return ConcurrentUtil.failedFuture(new ParseException(msg, 0));
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static class RconCommandHandler extends ResponseHandler<SourceRconCmdResponse> {

        private final AtomicLong byteCounter = new AtomicLong();

        private final AtomicInteger iteration = new AtomicInteger();

        private final ConcurrentHashMap<String, CommandStats> commandCount = new ConcurrentHashMap<>();

        private final int increment;

        private final int count;

        private long startTime;

        private static final BiFunction<String, CommandStats, CommandStats> successCounter = (comamnd, stats) -> {
            if (stats == null)
                stats = new CommandStats();
            stats.success.incrementAndGet();
            return stats;
        };

        private static final BiFunction<String, CommandStats, CommandStats> failCounter = (command, stats) -> {
            if (stats == null)
                stats = new CommandStats();
            stats.fail.incrementAndGet();
            return stats;
        };

        private static class CommandStats {

            private final AtomicInteger success = new AtomicInteger();

            private final AtomicInteger fail = new AtomicInteger();
        }

        protected RconCommandHandler(int count, CountDownLatch latch) {
            super("COMMAND", latch, System.out::println);
            this.count = count;
            this.increment = (int) (count * (1.0f / 100.0f));
        }

        public double getProgress() {
            return ((double) getTotalCount() / (double) count) * 100d;
        }

        @Override
        protected void onStart(SourceRconCmdResponse res, Throwable error) {
            startTime = System.nanoTime();
        }

        @Override
        protected void onSuccess(SourceRconCmdResponse res) {
            byteCounter.addAndGet(res.getResult().length());
            commandCount.compute(res.getCommand(), successCounter);
        }

        @Override
        protected void onFail(Throwable error) {
            //commandCount.compute(res.getCommand(), successCounter);
        }

        @Override
        protected void onDone(SourceRconCmdResponse res, Throwable error) {
            int total = getTotalCount();
            int success = getSuccessCount();
            int fail = getFailCount();
            Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
            if (increment > 0 && (total % increment) != 0)
                return;
            print("%03d) Processed a total of % 6d queries (Progress: % 4.0f%%, Success: %05d, Fail: %05d, Batch Size: %03d, Elapsed: %s, Last Thread: %s)", iteration.incrementAndGet(), total, getProgress(), success, fail, increment, TimeUtil.getTimeDesc(duration.toMillis(), true), Thread.currentThread().getName());
        }

        @Override
        public void printStats() {
            super.printStats();
            Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
            String sizeDesc = ByteUtil.getSizeDescriptionSI(byteCounter.get());
            print("Total bytes received: %s (Duration: %s)", sizeDesc, TimeUtil.getTimeDesc(duration.toMillis()));
            for (String command : commandCount.keySet()) {
                print("Total successful '%s': %d", command, commandCount.get(command).success.get());
            }
        }
    }
}
