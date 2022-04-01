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

import com.ibasco.agql.core.exceptions.InvalidCredentialsException;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.examples.query.ResponseHandler;
import com.ibasco.agql.protocols.valve.source.query.SourceRconAuthStatus;
import com.ibasco.agql.protocols.valve.source.query.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.client.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
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

    private static final String line = "\033[0;36m" + StringUtils.repeat("=", 158) + "\033[0m";

    private SourceRconClient rconClient;

    private InetSocketAddress serverAddress;

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
        final Options rconOptions = OptionBuilder.newBuilder()
                                                 .option(TransportOptions.POOL_MAX_CONNECTIONS, 8)
                                                 .option(SourceRconOptions.USE_TERMINATOR_PACKET, true)
                                                 .option(SourceRconOptions.STRICT_MODE, false)
                                                 .option(TransportOptions.POOL_ACQUIRE_TIMEOUT, Integer.MAX_VALUE)
                                                 .option(TransportOptions.CONNECTION_POOLING, true)
                                                 .option(TransportOptions.POOL_TYPE, ChannelPoolType.FIXED)
                                                 .option(TransportOptions.FAILSAFE_ENABLED, true)
                                                 .build();

        rconClient = new SourceRconClient(rconOptions);
        printConsoleBanner();
        runRconConsole();
    }

    @Override
    public void close() {
        if (rconClient == null)
            return;
        try {
            System.out.println("Closing rcon client");
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
            throw new RconNotYetAuthException("A server did not authenticate successfully", SourceRconAuthReason.NOT_AUTHENTICATED, null);
        }
    }

    private void printConsoleBanner() {
        System.out.println("\033[0;36m██████╗  ██████╗ ██████╗ ███╗   ██╗     ██████╗ ██████╗ ███╗   ██╗███████╗ ██████╗ ██╗     ███████╗\033[0m");
        System.out.println("\033[0;36m██╔══██╗██╔════╝██╔═══██╗████╗  ██║    ██╔════╝██╔═══██╗████╗  ██║██╔════╝██╔═══██╗██║     ██╔════╝\033[0m");
        System.out.println("\033[0;36m██████╔╝██║     ██║   ██║██╔██╗ ██║    ██║     ██║   ██║██╔██╗ ██║███████╗██║   ██║██║     █████╗  \033[0m");
        System.out.println("\033[0;36m██╔══██╗██║     ██║   ██║██║╚██╗██║    ██║     ██║   ██║██║╚██╗██║╚════██║██║   ██║██║     ██╔══╝  \033[0m");
        System.out.println("\033[0;36m██║  ██║╚██████╗╚██████╔╝██║ ╚████║    ╚██████╗╚██████╔╝██║ ╚████║███████║╚██████╔╝███████╗███████╗\033[0m");
        System.out.println("\033[0;36m╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝     ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚══════╝╚══════╝\033[0m");
        System.out.println("\033[0;36m                                               \033[0;33mPowered by Asynchronous Game Query Library\033[0m");
    }

    public void runRconConsole() {
        String address = promptInput("Enter server address", true, "", "sourceRconIp");
        int port = Integer.parseInt(promptInput("Enter server port", false, "27015", "sourceRconPort"));

        serverAddress = new InetSocketAddress(address, port);
        AtomicBoolean stop = new AtomicBoolean();
        final BiConsumer<SourceRconCmdResponse, Throwable> handler = (res, error) -> {
            if (error != null) {
                Throwable cause = error instanceof CompletionException || error instanceof ExecutionException ? error.getCause() : error;
                if (cause instanceof CancellationException) {
                    System.out.println("Shutting down example program");
                    stop.compareAndSet(false, true);
                } else if (cause instanceof InvalidCredentialsException) {
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
                System.out.printf("\n\033[0;37m%s\033[0m\n", res.getResult());
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
                    Throwable cause = ConcurrentUtil.unwrap(e);
                    if (cause instanceof RconInvalidCredentialsException) {
                        System.err.print("\nFailed to authenticate with server due to bad credentials\n");
                        cause.printStackTrace(System.err);
                        authenticated.set(false);
                    } else {
                        System.err.printf("\nAn unknown error occured while trying to authenticate with server (using password %s bytes)\n", password.length());
                        cause.printStackTrace(System.err);
                        throw e;
                    }
                } catch (Throwable error) {
                    System.err.println("Failed to authenticate with server");
                    error.printStackTrace(System.err);
                }
                continue;
            }
            String promptText = String.format("\033[0;33m[%s:%d]:\033[0m ", serverAddress.getAddress().getHostAddress(), serverAddress.getPort());
            String cmd = promptInput(promptText, true);
            parseCommand(cmd).whenComplete(handler).thenAccept(sourceRconCmdResponse -> System.out.print(promptText));
        }

        System.out.println("Rcon Console Exiting..");
        System.exit(0);
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
                rconClient.getStatistics().print(System.out::println);
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
            } else if (command.startsWith("/reauth")) {
                return commandReauth(args);
            } else {
                return error(String.format("Unknown command '%s' (type /help for the commands available)", command));
            }
            return CompletableFuture.completedFuture(null);
        }

        //System.out.printf("Command '%s' sent\n", command);
        return rconClient.exec(serverAddress, command);
    }

    private CompletableFuture<SourceRconCmdResponse> commandReauth(String[] args) {
        try {
            return rconClient.authenticate(serverAddress).thenCompose(s -> success("done", args[0]));
        } catch (RconNotYetAuthException e) {
            return error(e.getMessage());
        }
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

    private CompletableFuture<SourceRconCmdResponse> executeBatch(final int count, String command) {
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
            System.err.println("[CONSOLE] Failed to execute rcon commmand");
            error.printStackTrace(System.err);
        }

        @Override
        protected void onDone(SourceRconCmdResponse res, Throwable error) {
            int total = getTotalCount();
            int success = getSuccessCount();
            int fail = getFailCount();
            Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
            if (increment > 0 && (total % increment) != 0)
                return;
            //\033[0;37m
            //\033[0m
            print("\033[0;35m%03d)\033[0m Processed a total of \033[1;35m% 6d\033[0m queries (\033[0;37mProgress:\033[0m \033[1;36m% 4.0f%%\033[0m, \033[0;37mSuccess:\033[0m \033[1;36m%05d, \033[0;37mFail:\033[0m \033[1;36m%05d, \033[0;37mBatch Size:\033[0m \033[1;36m%03d, \033[0;37mElapsed:\033[0m \033[1;36m%s, \033[0;37mLast Thread Used:\033[0m \033[1;36m%s\033[0m\033[0;37m)\033[0m", iteration.incrementAndGet(), total, getProgress(), success, fail, increment, TimeUtil.getTimeDesc(duration.toMillis(), true), Thread.currentThread().getName());
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
