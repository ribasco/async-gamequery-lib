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

@ApiStatus.Internal
public final class ConcurrentUtil {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentUtil.class);

    public static <A, B> B combine(A a, B b) {
        return b;
    }

    public static Throwable unwrap(Throwable error) {
        if (error instanceof CompletionException || error instanceof ExecutionException) {
            return error.getCause();
        } else {
            return error;
        }
    }

    public static void unwrapAndThrow(Throwable error) {
        if (error == null)
            return;
        if (error instanceof CompletionException)
            throw (CompletionException) error;
        throw new CompletionException(error);
    }

    public static <U> U unwrapAndThrow(U s, Throwable error) {
        unwrapAndThrow(error);
        return s;
    }

    public static <V> CompletableFuture<V> failedFuture(Throwable error) {
        CompletableFuture<V> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }

    public static <V> CompletableFuture<V> completeExceptionally(CompletableFuture<V> future, Throwable error) {
        future.completeExceptionally(error);
        return future;
    }

    /**
     * Shutdown an {@link ExecutorService} the right way. This uses the default timeout parameters.
     *
     * @param executorService
     *         The {@link ExecutorService} to shutdown
     *
     * @return {@code true} if the {@link ExecutorService} shutdown successfully {@code false} if executor service is {@code null}, timeout has expired or shutdown was interrupted
     */
    public static boolean shutdown(ExecutorService executorService) {
        if (executorService == null)
            return false;
        return shutdown(executorService, TransportOptions.CLOSE_TIMEOUT.getDefaultValue(), TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown an {@link ExecutorService} the right way. This method blocks if there are still tasks running in the executor.
     *
     * @param executorService
     *         The {@link ExecutorService} to shutdown
     * @param timeout
     *         The timeout value to wait for a successful termination
     * @param timeUnit
     *         The {@link TimeUnit} of the timeout value
     *
     * @return {@code true} if the {@link ExecutorService} shutdown successfully {@code false} if executor service is {@code null}, timeout has expired or shutdown was interrupted
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

    public static void sleepUninterrupted(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
