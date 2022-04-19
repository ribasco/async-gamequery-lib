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
import static com.ibasco.agql.core.util.Console.color;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.examples.query.ResponseHandler;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RegExUtils;
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
import java.util.function.Function;

/**
 * Advanced examples for Source RCON
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceRconExample.class);

    private static final String line = "\033[0;36m" + StringUtils.repeat("=", 158) + "\033[0m";

    private static final String[] COMMANDS = new String[] {"status", "sm plugins list", "cvarlist", "maps *", "meta list", "sm exts list", "sm version", "find sv", "help sv_cheats"};

    private final ExecutorService commandExecutor = Executors.newCachedThreadPool(new DefaultThreadFactory("command"));

    private final AtomicBoolean authenticated = new AtomicBoolean();

    private final Map<String, Function<String[], CompletableFuture<CommandResponse>>> commandProcessors = new HashMap<>();

    private SourceRconClient rconClient;

    private InetSocketAddress serverAddress;

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        //register command processors
        commandProcessors.put("?", this::commandUsage);
        commandProcessors.put("h", this::commandUsage);
        commandProcessors.put("help", this::commandUsage);
        commandProcessors.put("stats", this::commandStats);
        commandProcessors.put("batch", this::commandBatch);
        commandProcessors.put("quit", this::commandQuit);
        commandProcessors.put("invalidate", this::commandInvalidate);
        commandProcessors.put("newauth", this::commandAuthenticate);
        commandProcessors.put("cleanup", this::commandCleanup);
        commandProcessors.put("reauth", this::commandReauth);

        final SourceRconOptions rconOptions = SourceRconClient.newOptionsBuilder()
                                                              .option(GlobalOptions.POOL_MAX_CONNECTIONS, 8)
                                                              .option(SourceRconOptions.USE_TERMINATOR_PACKET, true)
                                                              .option(SourceRconOptions.STRICT_MODE, false)
                                                              .option(GlobalOptions.POOL_ACQUIRE_TIMEOUT, Integer.MAX_VALUE)
                                                              .option(GlobalOptions.CONNECTION_POOLING, true)
                                                              .option(GlobalOptions.POOL_TYPE, ChannelPoolType.FIXED)
                                                              .option(GlobalOptions.FAILSAFE_ENABLED, true)
                                                              .option(SourceRconOptions.FAILSAFE_RETRY_DELAY, 1500L)
                                                              .build();

        rconClient = new SourceRconClient(rconOptions);
        //clearConsole();
        printBanner();
        runTerminal();
    }

    private CompletableFuture<CommandResponse> commandUsage(String[] args) {
        System.out.println(line);
        System.out.println("List of Available Console Commands".toUpperCase());
        System.out.println(line);
        System.out.println("\033[0;34mRcon Command:\033[0m \033[0;36m<command>\033[0m");
        System.out.println("\033[0;34mRcon Batch Command:\033[0m \033[0;36m/batch <amount> [command]\033[0m");
        System.out.println("\033[0;34mHelp:\033[0m \033[0;36m/help, /h, /?\033[0m");
        System.out.println("\033[0;34mInvalidate connections:\033[0m \033[0;36m/invalidate\033[0m");
        System.out.println("\033[0;34mThread/Connection Statistics:\033[0m \033[0;36m/stats\033[0m");
        System.out.println("\033[0;34mRe-authenticateBatch with server with new password:\033[0m \033[0;36m/newauth\033[0m");
        System.out.println("\033[0;34mRe-authenticateBatch with server using registered password:\033[0m \033[0;36m/reauth\033[0m");
        System.out.println("\033[0;34mCleanup unsused channels:\033[0m \033[0;36m/cleanup\033[0m");
        System.out.println("\033[0;34mQuit Console:\033[0m \033[0;36m/quit\033[0m");
        return success("", args[0]);
    }

    private CompletableFuture<CommandResponse> commandStats(final String[] args) {
        rconClient.getStatistics().print(System.out::println);
        return success("", args[0]);
    }

    private CompletableFuture<CommandResponse> commandBatch(String[] args) {
        if (args == null || args.length < 2)
            return Concurrency.failedFuture(new ParseException("Usage: /batch <amount> <command1>[;<command2>;<command3>]", 0));
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

    //<editor-fold desc="Console Command Handlers">
    private CompletableFuture<CommandResponse> commandQuit(final String[] args) {
        return error(new CancellationException());
    }

    private CompletableFuture<CommandResponse> commandInvalidate(String[] args) {
        if (args.length < 1)
            return error("Arguments must be at least 1");
        rconClient.invalidate();
        return success("done", args[0]);
    }

    private CompletableFuture<CommandResponse> commandAuthenticate(String[] args) {
        authenticated.set(false);
        return success("done", args[0]);
    }

    private CompletableFuture<CommandResponse> commandCleanup(String[] args) {
        rconClient.cleanup();
        return success("done", args[0]);
    }

    private CompletableFuture<CommandResponse> commandReauth(String[] args) {
        try {
            return rconClient.authenticate(serverAddress).thenCompose(s -> success("done", args[0]));
        } catch (RconNotYetAuthException e) {
            return error(e.getMessage());
        }
    }

    private void printBanner() {
        System.out.println("\033[0;36m██████╗  ██████╗ ██████╗ ███╗   ██╗     ██████╗ ██████╗ ███╗   ██╗███████╗ ██████╗ ██╗     ███████╗\033[0m");
        System.out.println("\033[0;36m██╔══██╗██╔════╝██╔═══██╗████╗  ██║    ██╔════╝██╔═══██╗████╗  ██║██╔════╝██╔═══██╗██║     ██╔════╝\033[0m");
        System.out.println("\033[0;36m██████╔╝██║     ██║   ██║██╔██╗ ██║    ██║     ██║   ██║██╔██╗ ██║███████╗██║   ██║██║     █████╗  \033[0m");
        System.out.println("\033[0;36m██╔══██╗██║     ██║   ██║██║╚██╗██║    ██║     ██║   ██║██║╚██╗██║╚════██║██║   ██║██║     ██╔══╝  \033[0m");
        System.out.println("\033[0;36m██║  ██║╚██████╗╚██████╔╝██║ ╚████║    ╚██████╗╚██████╔╝██║ ╚████║███████║╚██████╔╝███████╗███████╗\033[0m");
        System.out.println("\033[0;36m╚═╝  ╚═╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝     ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚══════╝╚══════╝\033[0m");
        System.out.println("\033[0;36m                                               \033[0;33mPowered by Asynchronous Game Query Library\033[0m");
    }

    /**
     * <p>runTerminal.</p>
     */
    public void runTerminal() {
        String address = promptInput("Enter server address", true, "", "sourceRconIp");
        int port = Integer.parseInt(promptInput("Enter server port", false, "27015", "sourceRconPort"));

        serverAddress = new InetSocketAddress(address, port);
        boolean stop = false;

        try {
            //loop until stop is set
            while (!stop) {
                if (!authenticated.get()) {
                    String password = promptInputPassword("Password", true, "", "sourceRconPass");
                    System.out.println();
                    System.out.printf("Connecting to server \033[1;96m%s:%d\033[0m with password = %s\n", address, port, RegExUtils.replaceAll(password, ".", "*"));
                    System.out.println();
                    try {
                        SourceRconAuthResponse authResponse = rconClient.authenticate(serverAddress, password.getBytes()).join();
                        authenticated.set(authResponse.isAuthenticated());
                        if (!authenticated.get()) {
                            Console.colorize()
                                   .red().text("[ERROR]: ")
                                   .white().text("Failed to authenticate with server. Server responded in error ")
                                   .yellow().text(" (Reason: '%s')", authResponse.getReason())
                                   .reset()
                                   .println();
                        }
                    } catch (Exception e) {
                        Throwable cause = Errors.unwrap(e);
                        if (cause instanceof RconException) {
                            RconException rconEx = (RconException) cause;
                            if (cause instanceof RconAuthException) {
                                RconAuthException authEx = (RconAuthException) cause;
                                Console.colorize()
                                       .red().text("[CLIENT ERROR]: ")
                                       .white().text("Failed to authenticate with server '%s'", rconEx.getRemoteAddress())
                                       .yellow().text(" (Request: '%s', Cause: %s)", rconEx.getRequest(), e)
                                       .reset()
                                       .println();
                            } else {
                                Console.colorize()
                                       .red().text("[CLIENT ERROR]: ")
                                       .white().text("Failed to authenticate with server '%s'", rconEx.getRemoteAddress())
                                       .yellow().text(" (Request: '%s', Cause: %s)", rconEx.getRequest(), e)
                                       .reset()
                                       .println();
                            }
                            authenticated.set(false);
                        } else {
                            System.err.printf("ERROR: Failed to authenticate with server '%s' using password with '%s' bytes (Reason: %s)\n", serverAddress, password.length(), cause);
                            authenticated.set(false);
                            throw e;
                        }
                    }
                    continue;
                }

                try {
                    CommandResponse response = promptUserInput().thenCompose(this::parseCommand).join();
                    System.out.printf("\n\033[0;37m%s\033[0m\n", response.getResult());
                } catch (Exception error) {
                    Throwable cause = Errors.unwrap(error);
                    if (cause instanceof CancellationException) {
                        stop = true;
                    } else if (cause instanceof InvalidCredentialsException || cause instanceof RconAuthException) {
                        System.err.println(cause.getMessage());
                        authenticated.set(false);
                    } else if (cause instanceof ParseException) {
                        System.err.println(cause.getMessage());
                    } else {
                        System.err.println("Error during execution of command: ");
                        cause.printStackTrace(System.err);
                    }
                    System.out.flush();
                    //add a slight delay
                    Concurrency.sleepUninterrupted(100);
                }
            }
        } finally {
            System.out.println("(CLOSE) \033[0;35mRcon console exiting\033[0m");
        }
    }

    private CompletableFuture<CommandResponse> success(String msg, final String command) {
        return CompletableFuture.completedFuture(new CommandResponse(command, msg));
    }

    private <V> CompletableFuture<V> error(String msg, Object... args) {
        return Concurrency.failedFuture(new ParseException(String.format(msg, args), 0));
    }

    private CompletableFuture<CommandResponse> executeBatch(final int count, final String command) {
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
                    rconClient.execute(serverAddress, cmd1).whenComplete(handleResponse);
                }
                latch.await();
            } catch (InterruptedException e) {
                throw new CompletionException(e);
            } finally {
                System.out.println(line);
                handleResponse.printStats();
                System.out.println(line);
            }
        }).thenApply(unsusd -> successResponse(command, "DONE: Executed a total of %d command(s) to %s", count, serverAddress));
    }

    private <V> CompletableFuture<V> error(Throwable error) {
        return Concurrency.failedFuture(error);
    }
    //</editor-fold>

    private CompletableFuture<String> promptUserInput() {
        String promptText = String.format("\033[0;33m[%s:%d]\033[0m ", serverAddress.getAddress().getHostAddress(), serverAddress.getPort());
        return CompletableFuture.completedFuture(promptInput(promptText, true));
    }

    private CompletableFuture<CommandResponse> parseCommand(String command) {
        if (command == null || command.trim().isEmpty())
            return Concurrency.failedFuture(new IllegalArgumentException("Command must not be empty"));
        command = command.trim();
        //handle built-in commands
        if (command.startsWith("/")) {
            String[] args = StringUtils.splitByWholeSeparatorPreserveAllTokens(command, StringUtils.SPACE, 3);
            String name = RegExUtils.replaceAll(args[0], "/", Strings.EMPTY).trim();
            if (commandProcessors.containsKey(name)) {
                return commandProcessors.get(name).apply(args);
            } else {
                return error("Unknown command '%s' (type /help for the commands available)", command);
            }
        }
        //any command that does not start with '/' should be treated as an rcon command by default
        else {
            return commandRcon(new String[] {command});
        }
    }

    private CommandResponse successResponse(String command, String msg, Object... args) {
        return new CommandResponse(command, String.format(msg, args));
    }

    private CompletableFuture<CommandResponse> commandRcon(final String[] args) {
        final String command = args[0];
        return rconClient.execute(serverAddress, command).thenApply(r -> new CommandResponse(command, r.getResult()));
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        close(rconClient, "RCON");
        close(commandExecutor, "Command");
    }

    private void authenticateBatch(Map<InetSocketAddress, String> auth) throws RconNotYetAuthException {
        final Phaser phaser = new Phaser();
        //authenticateBatch
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

        /*if (result.containsValue(false)) {
            result.entrySet().stream().filter(p -> !p.getValue()).forEach(e -> log.error("Server {} did not authenticateBatch successfully", e.getKey()));
            throw new RconNotYetAuthException(null, "A server did not authenticateBatch successfully", SourceRconAuthReason.NOT_AUTHENTICATED, null);
        }*/
    }

    private void sendCommandBatch(int count, Set<InetSocketAddress> addressSet, BiConsumer<SourceRconCmdResponse, Throwable> handler, Phaser phaser) throws Exception {
        phaser.register();
        for (InetSocketAddress address : addressSet)
            sendCommand(count, address, handler, phaser);
        phaser.arriveAndAwaitAdvance();
    }

    private void sendCommand(int count, InetSocketAddress address, BiConsumer<SourceRconCmdResponse, Throwable> handler, Phaser phaser) {
        for (int i = 0; i < count; i++) {
            phaser.register();
            String command = COMMANDS[RandomUtils.nextInt(0, COMMANDS.length)];
            rconClient.execute(address, command).whenComplete(handler);
        }
    }

    private static class RconCommandHandler extends ResponseHandler<SourceRconCmdResponse> {

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

        private final AtomicLong byteCounter = new AtomicLong();

        private final AtomicInteger iteration = new AtomicInteger();

        private final ConcurrentHashMap<String, CommandStats> commandCount = new ConcurrentHashMap<>();

        private final int increment;

        private final int count;

        private long startTime;

        protected RconCommandHandler(int count, CountDownLatch latch) {
            super("COMMAND", latch, System.out::println);
            this.count = count;
            this.increment = (int) (count * (1.0f / 100.0f));
        }

        @Override
        protected void onSuccess(SourceRconCmdResponse res) {
            byteCounter.addAndGet(res.getResult().length());
            commandCount.compute(res.getCommand(), successCounter);
        }

        @Override
        protected void onStart(SourceRconCmdResponse res, Throwable error) {
            startTime = System.nanoTime();
        }

        @Override
        protected void onFail(Throwable error) {
            assert error instanceof RconException;
            Console.println(color(Console.RED, "[RCON]") + " Failed to execute commmand (Error: %s)", error);
            //error.printStackTrace(System.err);
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
            print("\033[0;35m%03d)\033[0m Processed a total of \033[1;35m% 6d\033[0m queries (\033[0;37mProgress:\033[0m \033[1;36m% 4.0f%%\033[0m, \033[0;37mSuccess:\033[0m \033[1;36m%05d, \033[0;37mFail:\033[0m \033[1;36m%05d, \033[0;37mBatch Size:\033[0m \033[1;36m%03d, \033[0;37mElapsed:\033[0m \033[1;36m%s, \033[0;37mLast Thread Used:\033[0m \033[1;36m%s\033[0m\033[0;37m)\033[0m", iteration.incrementAndGet(), total, getProgress(), success, fail, increment, Time.getTimeDesc(duration.toMillis(), true), Thread.currentThread().getName());
        }

        public double getProgress() {
            return ((double) getTotalCount() / (double) count) * 100d;
        }

        @Override
        public void printStats() {
            super.printStats();
            Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
            String sizeDesc = Bytes.getSizeDescriptionSI(byteCounter.get());
            print("Total bytes received: %s (Duration: %s)", sizeDesc, Time.getTimeDesc(duration.toMillis()));
            for (String command : commandCount.keySet()) {
                print("Total successful '%s': %d", command, commandCount.get(command).success.get());
            }
        }

        private static class CommandStats {

            private final AtomicInteger success = new AtomicInteger();

            private final AtomicInteger fail = new AtomicInteger();
        }
    }

    private static class CommandException extends Exception {

        private final String command;

        private CommandException(String command, String message) {
            super(message);
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    private static class CommandResponse {

        private final String command;

        private final String result;

        private CommandResponse(String command, String result) {
            this.command = command;
            this.result = result;
        }

        public String getCommand() {
            return command;
        }

        public boolean isEmptyResult() {
            return Strings.isBlank(result);
        }

        public String getResult() {
            return result;
        }
    }
}
