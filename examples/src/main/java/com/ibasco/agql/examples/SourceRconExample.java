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

import com.google.common.collect.SetMultimap;
import com.ibasco.agql.core.exceptions.InvalidCredentialsException;
import com.ibasco.agql.core.util.Concurrency;
import com.ibasco.agql.core.util.ConnectOptions;
import com.ibasco.agql.core.util.Console;
import com.ibasco.agql.core.util.Errors;
import com.ibasco.agql.core.util.FailsafeOptions;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.Strings;
import com.ibasco.agql.core.util.Time;
import com.ibasco.agql.examples.base.BaseExample;
import com.ibasco.agql.examples.rcon.CommandResponse;
import com.ibasco.agql.examples.rcon.RconResponseHandler;
import com.ibasco.agql.examples.rcon.SMParser;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconMessenger;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconOptions;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Advanced examples for Source RCON
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceRconExample.class);

    private static final String line = "\033[0;36m" + StringUtils.repeat("=", 158) + "\033[0m";

    private static final String[] COMMANDS = new String[] {"status", "sm plugins list", "cvarlist", "maps *", "meta list", "sm exts list", "sm version", "find sv", "help sv_cheats"};

    private static final String COMMAND_PREFIX = "!";

    private final ExecutorService commandExecutor = Executors.newCachedThreadPool(new DefaultThreadFactory("command"));

    private final AtomicBoolean authenticate = new AtomicBoolean();

    private final Map<String, Function<String[], CompletableFuture<CommandResponse>>> commandProcessors = new HashMap<>();

    private SourceRconClient rconClient;

    private SMParser sourceModParser;

    private InetSocketAddress serverAddress;

    private SourceRconOptions options;

    /** {@inheritDoc} */
    @Override
    public void run(String[] args) throws Exception {
        Boolean userTerminatorPackets = promptInputBool("Enable terminator packets? (Y for 'source based servers', N for non-source based servers)", true, "true", "sourceUseTerminatorPackets");
        Console.colorize(true).purple("[config] ").green("Terminator packets enabled: ").cyan("%s", userTerminatorPackets).println();
        options = SourceRconOptions.builder()
                                   .option(ConnectOptions.FAILSAFE_ENABLED, true)
                                   .option(ConnectOptions.FAILSAFE_RETRY_DELAY, 3000L)
                                   .option(ConnectOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false)
                                   .option(FailsafeOptions.FAILSAFE_RETRY_ENABLED, true)
                                   .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false)
                                   .option(FailsafeOptions.FAILSAFE_RETRY_DELAY, 3000L)
                                   .option(GeneralOptions.POOL_MAX_CONNECTIONS, 8)
                                   .build();
        initializeProcessors();
        rconClient = new SourceRconClient(options);
        sourceModParser = new SMParser(rconClient);
        printBanner();
        runTerminal();
    }

    private void initializeProcessors() {
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
        commandProcessors.put("plugins", this::commandListPlugins);
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
                if (!authenticate.get()) {
                    String password = promptInputPassword("Password", true, "", "sourceRconPass");
                    System.out.println();
                    System.out.printf("Connecting to server \033[1;96m%s:%d\033[0m with password = %s\n", address, port, RegExUtils.replaceAll(password, ".", "*"));
                    System.out.println();
                    try {
                        SourceRconAuthResponse authResponse = rconClient.authenticate(serverAddress, password.getBytes()).join();

                        authenticate.set(authResponse.isAuthenticated());
                        if (!authenticate.get()) {
                            Console.colorize(true)
                                   .red("[ERROR]: ")
                                   .white("Failed to authenticate with server. Server responded in error ")
                                   .yellow(" (Reason: '%s')", authResponse.getReason())
                                   .reset()
                                   .println();
                        }
                    } catch (Exception e) {
                        Throwable cause = Errors.unwrap(e);
                        if (cause instanceof RconException) {
                            RconException rconEx = (RconException) cause;
                            if (cause instanceof RconAuthException) {
                                RconAuthException authEx = (RconAuthException) cause;
                                Console.colorize(true)
                                       .red().text("[ERROR]: ")
                                       .white().text("Failed to authenticate with server '%s'", rconEx.getRemoteAddress())
                                       .yellow().text(" (Request: '%s', Cause: %s)", rconEx.getRequest(), e)
                                       .reset()
                                       .println();
                            } else {
                                Console.colorize(true)
                                       .red().text("[ERROR]: ")
                                       .white().text("Failed to authenticate with server '%s'", rconEx.getRemoteAddress())
                                       .yellow().text(" (Request: '%s', Cause: %s)", rconEx.getRequest(), e)
                                       .reset()
                                       .println();
                            }
                            authenticate.set(false);
                        } else {
                            System.err.printf("ERROR: Failed to authenticate with server '%s' using password with '%s' bytes (Reason: %s)\n", serverAddress, password.length(), cause);
                            authenticate.set(false);
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
                        authenticate.set(false);
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

    //<editor-fold desc="Commands">
    private CompletableFuture<CommandResponse> commandUsage(String[] args) {
        Console.Colorize console = Console.colorize(true).enableColors();
        console.yellow("-------------------------------------------------------------------------------------------").reset().line();
        console.yellow("List of Available Console Commands".toUpperCase()).line().reset();
        console.yellow("-------------------------------------------------------------------------------------------").reset().line();
        console.cyan("%-50s: ", "Rcon Command").white("%s", "<command>").reset().line();
        console.cyan("%-50s: ", "Rcon Batch Command").white("%s", "!batch <amount> [command]").reset().line();
        console.cyan("%-50s: ", "Help").white("%s", "!?, !h, !help").reset().line();
        console.cyan("%-50s: ", "Invalidate Active Connections").white("%s", "!invalidate").reset().line();
        console.cyan("%-50s: ", "Re-authenticate with new credentials").white("%s", "!newauth").reset().line();
        console.cyan("%-50s: ", "Re-authenticate with existing credentials").white("%s", "!reauth").reset().line();
        console.cyan("%-50s: ", "Cleanup inactive connections").white("%s", "!cleanup [force]").reset().line();
        console.cyan("%-50s: ", "List SourceMod Plugins (with Cvars and command)").white("%s", "!plugins").reset().line();
        console.cyan("%-50s: ", "Quit Program").white("%s", "!quit").reset().line();
        System.out.println(console);
        return success("", args[0]);
    }

    private CompletableFuture<CommandResponse> commandStats(final String[] args) {
        rconClient.printExecutorStats(System.out::println);
        SetMultimap<InetSocketAddress, SourceRconMessenger.ConnectionStats> stats = rconClient.getStatistics();
        final Supplier<Console.Colorize> console = () -> Console.colorize(true);
        int ctr = 1;
        for (InetSocketAddress address : stats.keySet()) {
            console.get().blue().text("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").println();
            console.get().blue().text("%02d)", ctr++).white().text(" Address ").reset().yellow().text("'%s'", address).println();
            console.get().blue().text("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").println();
            int connCtr = 1;
            for (SourceRconMessenger.ConnectionStats connStats : stats.get(address)) {
                Console.Colorize con = console.get();
                con
                        .purple("\t(%02d)", connCtr++).reset()
                        .cyan("\t[Connection Id]: ").white(connStats.getConnectionId()).reset()
                        .cyan("\t[Local Address]: ").white(connStats.getLocalAddress().toString()).reset()
                        .cyan("\t[Acquire Count]: ").white("%03d", connStats.getAcquireCount()).reset()
                        .cyan("\t[Last Acquired]: ").white("%s sec(s) ago", Time.getTimeDesc(connStats.getLastAcquiredMs(), true)).reset();

                if (connStats.isAcquired()) {
                    con.cyan().text("\t[Is Acquired]: ").reset().yellow().text("%s", connStats.isAcquired()).reset();
                } else {
                    con.cyan().text("\t[Is Acquired]: ").reset().green().text("%s", connStats.isAcquired()).reset();
                }

                if (connStats.isAuthenticated()) {
                    con.cyan("\t[Is Authenticated]: ").reset().green("%s", connStats.isAuthenticated()).reset();
                } else {
                    con.cyan("\t[Is Authenticated]: ").reset().red("%s", connStats.isAuthenticated()).reset();
                }

                con.cyan("\t[Thread Name]: ").reset()
                   .white("%s", connStats.getThreadName()).reset()
                   .println();
            }
        }
        return success("", args[0]);
    }

    private CompletableFuture<CommandResponse> commandBatch(String[] args) {
        if (args == null || args.length < 2)
            return Concurrency.failedFuture(new ParseException("Usage: !batch <amount> <command1>[;<command2>;<command3>]", 0));
        if (!"!batch".equalsIgnoreCase(args[0]))
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
        authenticate.set(false);
        return success("done", args[0]);
    }

    private CompletableFuture<CommandResponse> commandCleanup(String[] args) {
        boolean force = false;
        if (args.length >= 2)
            force = !Strings.isBlank(args[1]) && "force".equalsIgnoreCase(args[1]);
        rconClient.cleanup(force);
        return success("done", args[0]);
    }
    //</editor-fold>

    private CompletableFuture<CommandResponse> commandReauth(String[] args) {
        try {
            return rconClient.authenticate(serverAddress).thenCompose(s -> success("done", args[0]));
        } catch (RconNotYetAuthException e) {
            return error(e.getMessage());
        }
    }

    private CompletableFuture<CommandResponse> commandListPlugins(String[] args) {
        try {
            Console.colorize(true).yellow("Fetching plugin list. Please wait...").println();
            return sourceModParser.getPluginList(serverAddress)
                                  .thenApply(sourceModParser::prettyFormat)
                                  .thenCombine(CompletableFuture.completedFuture(args[0]), CommandResponse::new);
        } catch (RconNotYetAuthException e) {
            return error(e.getMessage());
        }
    }

    private CompletableFuture<String> promptUserInput() {
        String promptText = String.format("\033[0;33m[%s:%d]\033[0m ", serverAddress.getAddress().getHostAddress(), serverAddress.getPort());
        return CompletableFuture.completedFuture(promptInput(promptText, true));
    }

    private CompletableFuture<CommandResponse> parseCommand(String command) {
        if (command == null || command.trim().isEmpty())
            return Concurrency.failedFuture(new IllegalArgumentException("Command must not be empty"));
        command = command.trim();
        //handle built-in commands
        if (command.startsWith(COMMAND_PREFIX)) {
            String[] args = StringUtils.splitByWholeSeparatorPreserveAllTokens(command, StringUtils.SPACE, 3);
            String name;
            if (command.startsWith(COMMAND_PREFIX))
                name = RegExUtils.replaceAll(args[0], COMMAND_PREFIX, Strings.EMPTY).trim();
            else
                throw new IllegalStateException("Unsupported command prefix");
            if (commandProcessors.containsKey(name)) {
                return commandProcessors.get(name).apply(args);
            } else {
                return error("Unknown command '%s' (type %shelp for the commands available)", command, COMMAND_PREFIX);
            }
        }
        //any command that does not start with '/' should be treated as an rcon command by default
        else {
            return commandRcon(new String[] {command});
        }
    }

    private CompletableFuture<CommandResponse> success(String msg, final String command) {
        return CompletableFuture.completedFuture(new CommandResponse(msg, command));
    }

    private <V> CompletableFuture<V> error(String msg, Object... args) {
        return Concurrency.failedFuture(new ParseException(String.format(msg, args), 0));
    }
    //</editor-fold>

    private CompletableFuture<CommandResponse> executeBatch(final int count, final String command) {
        return CompletableFuture.runAsync(() -> {

            System.out.println(line);
            System.out.printf("Executing %d '%s' command(s)\n", count, command == null ? "random" : command);
            System.out.println(line);

            final CountDownLatch latch = new CountDownLatch(count);
            final RconResponseHandler handleResponse = new RconResponseHandler(count, latch);
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

    private CompletableFuture<CommandResponse> commandRcon(final String[] args) {
        final String command = args[0];
        return rconClient.execute(serverAddress, command).thenApply(r -> new CommandResponse(r.getResult(), command));
    }

    private CommandResponse successResponse(String command, String msg, Object... args) {
        return new CommandResponse(String.format(msg, args), command);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        close(rconClient, "RCON");
        close(commandExecutor, "Command");
    }
}
