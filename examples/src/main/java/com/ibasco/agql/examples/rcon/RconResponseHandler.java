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

package com.ibasco.agql.examples.rcon;

import com.ibasco.agql.core.util.Bytes;
import com.ibasco.agql.core.util.Console;
import static com.ibasco.agql.core.util.Console.color;
import com.ibasco.agql.core.util.Time;
import com.ibasco.agql.examples.base.ResponseHandler;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

public class RconResponseHandler extends ResponseHandler<SourceRconCmdResponse> {

    private final BiFunction<String, CommandStats, CommandStats> successCounter = (comamnd, stats) -> {
        if (stats == null)
            stats = new CommandStats();
        stats.success.incrementAndGet();
        return stats;
    };

    private final BiFunction<String, CommandStats, CommandStats> failCounter = (command, stats) -> {
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

    public RconResponseHandler(int count, CountDownLatch latch) {
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

    private class CommandStats {

        private final AtomicInteger success = new AtomicInteger();

        private final AtomicInteger fail = new AtomicInteger();
    }
}