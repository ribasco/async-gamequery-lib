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

package com.ibasco.agql.examples.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Abstract ResponseHandler class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class ResponseHandler<T> implements BiConsumer<T, Throwable> {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);

    private final AtomicInteger successCount = new AtomicInteger();

    private final AtomicInteger failCount = new AtomicInteger();

    private final String description;

    private Phaser phaser;

    private CountDownLatch latch;

    private volatile boolean started;

    private Consumer<String> output;

    /**
     * <p>Constructor for ResponseHandler.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     */
    protected ResponseHandler(String description) {
        this(description, (CountDownLatch) null);
    }

    /**
     * <p>Constructor for ResponseHandler.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     * @param latch
     *         a {@link java.util.concurrent.CountDownLatch} object
     */
    protected ResponseHandler(String description, CountDownLatch latch) {
        this(description, latch, null);
    }

    /**
     * <p>Constructor for ResponseHandler.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     * @param latch
     *         a {@link java.util.concurrent.CountDownLatch} object
     * @param output
     *         a {@link java.util.function.Consumer} object
     */
    protected ResponseHandler(String description, CountDownLatch latch, Consumer<String> output) {
        this.description = description;
        this.latch = latch;
        this.phaser = null;
        if (output == null)
            this.output = log::info;
        else
            this.output = output;
    }

    /**
     * <p>Constructor for ResponseHandler.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     * @param phaser
     *         a {@link java.util.concurrent.Phaser} object
     */
    protected ResponseHandler(String description, Phaser phaser) {
        this(description, phaser, null);
    }

    /**
     * <p>Constructor for ResponseHandler.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     * @param phaser
     *         a {@link java.util.concurrent.Phaser} object
     * @param output
     *         a {@link java.util.function.Consumer} object
     */
    protected ResponseHandler(String description, Phaser phaser, Consumer<String> output) {
        this.description = description;
        this.phaser = phaser;
        if (output == null)
            this.output = log::info;
        else
            this.output = output;
    }

    /**
     * <p>reset.</p>
     */
    public void reset() {
        this.phaser = null;
        this.latch = null;
        this.successCount.set(0);
        this.failCount.set(0);
    }

    /** {@inheritDoc} */
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

    /**
     * <p>onStart.</p>
     *
     * @param res
     *         a T object
     * @param error
     *         a {@link java.lang.Throwable} object
     */
    protected void onStart(T res, Throwable error) {}

    /**
     * <p>onFail.</p>
     *
     * @param error
     *         a {@link java.lang.Throwable} object
     */
    protected void onFail(Throwable error) {
        log.error(String.format("Error occured in: %s", getClass().getSimpleName()), error);
    }

    /**
     * <p>onSuccess.</p>
     *
     * @param res
     *         a T object
     */
    abstract protected void onSuccess(T res);

    /**
     * <p>onDone.</p>
     *
     * @param res
     *         a T object
     * @param error
     *         a {@link java.lang.Throwable} object
     */
    protected void onDone(T res, Throwable error) {}

    private void invokeSyncBarrier() {
        if (phaser != null)
            phaser.arriveAndDeregister();
        if (latch != null)
            latch.countDown();
    }

    /**
     * <p>getTotalCount.</p>
     *
     * @return a int
     */
    public int getTotalCount() {
        return getSuccessCount() + getFailCount();
    }

    /**
     * <p>Getter for the field <code>successCount</code>.</p>
     *
     * @return a int
     */
    public int getSuccessCount() {
        return successCount.get();
    }

    /**
     * <p>Getter for the field <code>failCount</code>.</p>
     *
     * @return a int
     */
    public int getFailCount() {
        return failCount.get();
    }

    /**
     * <p>Getter for the field <code>output</code>.</p>
     *
     * @return a {@link java.util.function.Consumer} object
     */
    public Consumer<String> getOutput() {
        return output;
    }

    /**
     * <p>Setter for the field <code>output</code>.</p>
     *
     * @param output
     *         a {@link java.util.function.Consumer} object
     */
    public void setOutput(Consumer<String> output) {
        this.output = output;
    }

    /**
     * <p>printStats.</p>
     */
    public void printStats() {
        print("Total records succesfully retrieved: %d", getSuccessCount());
        print("Total records in error: %d", getFailCount());
    }

    /**
     * <p>printConnectionStats.</p>
     *
     * @param msg
     *         a {@link java.lang.String} object
     * @param args
     *         a {@link java.lang.Object} object
     */
    protected final void print(String msg, Object... args) {
        output.accept(String.format("[\033[0;33m%s]\033[0m: %s", getDescription(), String.format(msg, args)));
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }
}
