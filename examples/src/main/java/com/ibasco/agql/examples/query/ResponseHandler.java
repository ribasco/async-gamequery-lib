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

package com.ibasco.agql.examples.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

abstract public class ResponseHandler<T> implements BiConsumer<T, Throwable> {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private final AtomicInteger successCount = new AtomicInteger();

    private final AtomicInteger failCount = new AtomicInteger();

    private final String description;

    private Phaser phaser;

    private CountDownLatch latch;

    private volatile boolean started;

    private Consumer<String> output;

    protected ResponseHandler(String description) {
        this(description, (CountDownLatch) null);
    }

    protected ResponseHandler(String description, Phaser phaser) {
        this(description, phaser, null);
    }

    protected ResponseHandler(String description, Phaser phaser, Consumer<String> output) {
        this.description = description;
        this.phaser = phaser;
        if (output == null)
            this.output = log::info;
        else
            this.output = output;
    }

    protected ResponseHandler(String description, CountDownLatch latch) {
        this(description, latch, null);
    }

    protected ResponseHandler(String description, CountDownLatch latch, Consumer<String> output) {
        this.description = description;
        this.latch = latch;
        this.phaser = null;
        if (output == null)
            this.output = log::info;
        else
            this.output = output;
    }

    abstract protected void onSuccess(T res);

    protected void onStart(T res, Throwable error) {}

    protected void onFail(Throwable error) {
        log.error(String.format("Error occured in: %s", getClass().getSimpleName()), error);
    }

    protected void onDone(T res, Throwable error) {}

    public String getDescription() {
        return description;
    }

    public void reset() {
        this.phaser = null;
        this.latch = null;
        this.successCount.set(0);
        this.failCount.set(0);
    }

    @Override
    public synchronized final void accept(T res, Throwable error) {
        if (!started) {
            onStart(res, error);
            started = true;
        }
        try {
            if (error != null) {
                failCount.incrementAndGet();
                onFail(error);
                return;
            }
            successCount.incrementAndGet();
            onSuccess(res);
        } finally {
            try {
                onDone(res, error);
            } finally {
                invokeSyncBarrier();
            }
        }
    }

    private void invokeSyncBarrier() {
        if (phaser != null)
            phaser.arriveAndDeregister();
        if (latch != null)
            latch.countDown();
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getFailCount() {
        return failCount.get();
    }

    public int getTotalCount() {
        return getSuccessCount() + getFailCount();
    }

    public Consumer<String> getOutput() {
        return output;
    }

    public void setOutput(Consumer<String> output) {
        this.output = output;
    }

    public void printStats() {
        print("Total records succesfully retrieved: %d", getSuccessCount());
        print("Total records in error: %d", getFailCount());
    }

    protected final void print(String msg, Object... args) {
        output.accept(String.format("[\033[0;33m%s]\033[0m: %s", getDescription(), String.format(msg, args)));
    }
}
