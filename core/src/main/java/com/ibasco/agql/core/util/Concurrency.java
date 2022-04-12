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

package com.ibasco.agql.core.util;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * <p>Concurrency class.</p>
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class Concurrency {

    private static final Logger log = LoggerFactory.getLogger(Concurrency.class);

    /**
     * <p>wrap.</p>
     *
     * @param future a {@link java.util.concurrent.CompletableFuture} object
     * @param <V> a V class
     * @return a {@link java.util.concurrent.CompletionStage} object
     */
    public static <V> CompletionStage<V> wrap(CompletableFuture<V> future) {
        return future;
    }

    /**
     * <p>combine.</p>
     *
     * @param a a A object
     * @param b a B object
     * @param <A> a A class
     * @param <B> a B class
     * @return a B object
     */
    public static <A, B> B combine(A a, B b) {
        return b;
    }

    /**
     * <p>failedFuture.</p>
     *
     * @param error a {@link java.lang.Throwable} object
     * @param <V> a V class
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static <V> CompletableFuture<V> failedFuture(Throwable error) {
        return failedFuture(error, null);
    }

    /**
     * <p>failedFuture.</p>
     *
     * @param error a {@link java.lang.Throwable} object
     * @param executor a {@link java.util.concurrent.Executor} object
     * @param <V> a V class
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    public static <V> CompletableFuture<V> failedFuture(Throwable error, Executor executor) {
        if (executor == null) {
            CompletableFuture<V> future = new CompletableFuture<>();
            future.completeExceptionally(error);
            return future;
        }
        return CompletableFuture.supplyAsync(() -> {
            throw new CompletionException(error);
        }, executor);
    }

    /**
     * Shutdown an {@link java.util.concurrent.ExecutorService} the right way. This uses the default timeout parameters.
     *
     * @param executorService
     *         The {@link java.util.concurrent.ExecutorService} to shutdown
     * @return {@code true} if the {@link java.util.concurrent.ExecutorService} shutdown successfully {@code false} if executor service is {@code null}, timeout has expired or shutdown was interrupted
     */
    public static boolean shutdown(ExecutorService executorService) {
        if (executorService == null)
            return false;
        return shutdown(executorService, TransportOptions.CLOSE_TIMEOUT.getDefaultValue(), TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown an {@link java.util.concurrent.ExecutorService} the right way. This method blocks if there are still tasks running in the executor.
     *
     * @param executorService
     *         The {@link java.util.concurrent.ExecutorService} to shutdown
     * @param timeout
     *         The timeout value to wait for a successful termination
     * @param timeUnit
     *         The {@link java.util.concurrent.TimeUnit} of the timeout value
     * @return {@code true} if the {@link java.util.concurrent.ExecutorService} shutdown successfully {@code false} if executor service is {@code null}, timeout has expired or shutdown was interrupted
     */
    public static boolean shutdown(ExecutorService executorService, final int timeout, final TimeUnit timeUnit) {
        if (executorService == null)
            return false;
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(timeout, timeUnit)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(timeout, timeUnit)) {
                    log.debug("Executor service '{}' did not terminate", executorService);
                    return false;
                }
            }
            return true;
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * <p>sleepUninterrupted.</p>
     *
     * @param milliseconds a int
     */
    public static void sleepUninterrupted(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
