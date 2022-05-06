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

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple delegate class wrapped as a {@link com.ibasco.agql.core.util.ManagedResource} type.
 *
 * @author Rafael Luis Ibasco
 * @see ManagedResource
 */
public class AgqlManagedExecutorService extends AbstractManagedResource<ThreadPoolExecutor> implements ExecutorService {

    AgqlManagedExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        super(threadPoolExecutor);
    }

    /** {@inheritDoc} */
    @Override
    public void shutdown() {
        getResource().shutdown();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        return getResource().shutdownNow();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isShutdown() {
        return getResource().isShutdown();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTerminated() {
        return getResource().isTerminated();
    }

    /** {@inheritDoc} */
    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return getResource().awaitTermination(timeout, unit);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return getResource().submit(task);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return getResource().submit(task, result);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return getResource().submit(task);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return getResource().invokeAll(tasks);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return getResource().invokeAll(tasks, timeout, unit);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return getResource().invokeAny(tasks);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return getResource().invokeAny(tasks, timeout, unit);
    }

    /** {@inheritDoc} */
    @Override
    public void execute(@NotNull Runnable command) {
        getResource().execute(command);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        try {
            Console.println("Shutting down executor service '%s' (Reference count: %d)", this, getReferenceCount());
            Concurrency.shutdown(getResource());
        } finally {
            if (getReferenceCount() > 0)
                release();
        }
    }
}
