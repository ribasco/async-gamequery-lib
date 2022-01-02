/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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
package com.ibasco.agql.core.transport.pool;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.Platform;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.ObjectUtil;
import static io.netty.util.internal.ObjectUtil.checkPositive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * An enhanced version of netty's {@link io.netty.channel.pool.FixedChannelPool}
 *
 * @author Rafael Luis Ibasco
 */
public class FixedNettyChannelPool extends SimpleNettyChannelPool {

    private static final Logger log = LoggerFactory.getLogger(FixedNettyChannelPool.class);

    public enum AcquireTimeoutAction {
        /**
         * Create a new connection when the timeout is detected.
         */
        NEW,

        /**
         * Fail the {@link CompletableFuture} of the acquire call with a {@link TimeoutException}.
         */
        FAIL
    }

    private final EventExecutor executor;

    private final long acquireTimeoutNanos;

    private final Runnable timeoutTask;

    // There is no need to worry about synchronization as everything that modified the queue or counts is done
    // by the above EventExecutor.
    private final Queue<AcquireTask> pendingAcquireQueue = new ArrayDeque<>();

    private final int maxConnections;

    private final int maxPendingAcquires;

    private final AtomicInteger acquiredChannelCount = new AtomicInteger();

    private int pendingAcquireCount;

    private boolean closed;

    /**
     * Creates a new instance using the {@link ChannelHealthChecker#ACTIVE}.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param maxConnections
     *         the number of maximal active connections, once this is reached new tries to acquire
     *         a {@link Channel} will be delayed until a connection is returned to the pool again.
     */
    public FixedNettyChannelPool(NettyChannelFactory channelFactory,
                                 ChannelPoolHandler handler, int maxConnections) {
        this(channelFactory, handler, maxConnections, Integer.MAX_VALUE);
    }

    /**
     * Creates a new instance using the {@link ChannelHealthChecker#ACTIVE}.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param maxConnections
     *         the number of maximal active connections, once this is reached new tries to
     *         acquire a {@link Channel} will be delayed until a connection is returned to the
     *         pool again.
     * @param maxPendingAcquires
     *         the maximum number of pending acquires. Once this is exceed acquire tries will
     *         be failed.
     */
    public FixedNettyChannelPool(NettyChannelFactory channelFactory,
                                 ChannelPoolHandler handler, int maxConnections, int maxPendingAcquires) {
        this(channelFactory, handler, ChannelHealthChecker.ACTIVE, null, -1, maxConnections, maxPendingAcquires);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     * @param action
     *         the {@link AcquireTimeoutAction} to use or {@code null} if non should be used.
     *         In this case {@param acquireTimeoutMillis} must be {@code -1}.
     * @param acquireTimeoutMillis
     *         the time (in milliseconds) after which an pending acquire must complete or
     *         the {@link AcquireTimeoutAction} takes place.
     * @param maxConnections
     *         the number of maximal active connections, once this is reached new tries to
     *         acquire a {@link Channel} will be delayed until a connection is returned to the
     *         pool again.
     * @param maxPendingAcquires
     *         the maximum number of pending acquires. Once this is exceed acquire tries will
     *         be failed.
     */
    public FixedNettyChannelPool(NettyChannelFactory channelFactory,
                                 ChannelPoolHandler handler,
                                 ChannelHealthChecker healthCheck, AcquireTimeoutAction action,
                                 final long acquireTimeoutMillis,
                                 int maxConnections, int maxPendingAcquires) {
        this(channelFactory, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, true, NONE);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     * @param action
     *         the {@link AcquireTimeoutAction} to use or {@code null} if non should be used.
     *         In this case {@param acquireTimeoutMillis} must be {@code -1}.
     * @param acquireTimeoutMillis
     *         the time (in milliseconds) after which an pending acquire must complete or
     *         the {@link AcquireTimeoutAction} takes place.
     * @param maxConnections
     *         the number of maximal active connections, once this is reached new tries to
     *         acquire a {@link Channel} will be delayed until a connection is returned to the
     *         pool again.
     * @param maxPendingAcquires
     *         the maximum number of pending acquires. Once this is exceed acquire tries will
     *         be failed.
     * @param releaseHealthCheck
     *         will check channel health before offering back if this parameter set to
     *         {@code true}.
     */
    public FixedNettyChannelPool(NettyChannelFactory channelFactory,
                                 ChannelPoolHandler handler,
                                 ChannelHealthChecker healthCheck, AcquireTimeoutAction action,
                                 final long acquireTimeoutMillis,
                                 int maxConnections, int maxPendingAcquires, final boolean releaseHealthCheck, ReleaseStrategy releaseStrategy) {
        this(channelFactory, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, releaseHealthCheck, true, releaseStrategy);
    }

    /**
     * Creates a new instance.
     *
     * @param channelFactory
     *         callback which returns a {@link CompletableFuture}, when transitions to a complete state, the future returns a new connected {@link Channel}
     * @param handler
     *         the {@link ChannelPoolHandler} that will be notified for the different pool actions
     * @param healthCheck
     *         the {@link ChannelHealthChecker} that will be used to check if a {@link Channel} is
     *         still healthy when obtain from the {@link NettyChannelPool}
     * @param action
     *         the {@link AcquireTimeoutAction} to use or {@code null} if non should be used.
     *         In this case {@param acquireTimeoutMillis} must be {@code -1}.
     * @param acquireTimeoutMillis
     *         the time (in milliseconds) after which an pending acquire must complete or
     *         the {@link AcquireTimeoutAction} takes place.
     * @param maxConnections
     *         the number of maximal active connections, once this is reached new tries to
     *         acquire a {@link Channel} will be delayed until a connection is returned to the
     *         pool again.
     * @param maxPendingAcquires
     *         the maximum number of pending acquires. Once this is exceed acquire tries will
     *         be failed.
     * @param releaseHealthCheck
     *         will check channel health before offering back if this parameter set to
     *         {@code true}.
     * @param lastRecentUsed
     *         {@code true} {@link Channel} selection will be LIFO, if {@code false} FIFO.
     */
    public FixedNettyChannelPool(NettyChannelFactory channelFactory,
                                 ChannelPoolHandler handler,
                                 ChannelHealthChecker healthCheck, AcquireTimeoutAction action,
                                 final long acquireTimeoutMillis,
                                 int maxConnections, int maxPendingAcquires,
                                 boolean releaseHealthCheck, boolean lastRecentUsed, ReleaseStrategy releaseStrategy) {
        super(channelFactory, handler, healthCheck, releaseHealthCheck, lastRecentUsed, releaseStrategy);
        checkPositive(maxConnections, "maxConnections");
        checkPositive(maxPendingAcquires, "maxPendingAcquires");
        if (action == null && acquireTimeoutMillis == -1) {
            timeoutTask = null;
            acquireTimeoutNanos = -1;
        } else if (action == null && acquireTimeoutMillis != -1) {
            throw new NullPointerException("action");
        } else if (action != null && acquireTimeoutMillis < 0) {
            throw new IllegalArgumentException("acquireTimeoutMillis: " + acquireTimeoutMillis + " (expected: >= 0)");
        } else {
            acquireTimeoutNanos = TimeUnit.MILLISECONDS.toNanos(acquireTimeoutMillis);
            switch (action) {
                case FAIL:
                    timeoutTask = new TimeoutTask() {
                        @Override
                        public void onTimeout(AcquireTask task) {
                            log.info("ACQUIRE TIMEOUT!!!!!!: {}", task.expireNanoTime);
                            // Fail the promise as we timed out.
                            task.promise.completeExceptionally(new AcquireTimeoutException());
                        }
                    };
                    break;
                case NEW:
                    timeoutTask = new TimeoutTask() {
                        @Override
                        public void onTimeout(AcquireTask task) {
                            // Increment the acquire count and delegate to super to actually acquire a Channel which will
                            // create a new connection.
                            task.acquired();
                            FixedNettyChannelPool.super.acquire(task.envelope, task.promise);
                        }
                    };
                    break;
                default:
                    throw new Error();
            }
        }

        this.executor = Platform.getDefaultEventLoopGroup().next();
        this.maxConnections = maxConnections;
        this.maxPendingAcquires = maxPendingAcquires;
    }

    /** Returns the number of acquired channels that this pool thinks it has. */
    public int getTotalAcquiredChannels() {
        return acquiredChannelCount.get();
    }

    @Override
    public CompletableFuture<Channel> acquire(final Envelope<? extends AbstractRequest> envelope, final CompletableFuture<Channel> promise) {
        try {
            if (executor.inEventLoop()) {
                acquire0(envelope, promise);
            } else {
                executor.execute(() -> acquire0(envelope, promise));
            }
        } catch (Throwable cause) {
            promise.completeExceptionally(cause);
        }
        return promise;
    }

    private void acquire0(final Envelope<? extends AbstractRequest> envelope, final CompletableFuture<Channel> promise) {
        try {
            assert executor.inEventLoop();

            if (closed) {
                promise.completeExceptionally(new IllegalStateException("FixedChannelPool was closed"));
                return;
            }
            if (acquiredChannelCount.get() < maxConnections) {
                assert acquiredChannelCount.get() >= 0;
                // We need to create a new promise as we need to ensure the AcquireListener runs in the correct
                // EventLoop
                CompletableFuture<Channel> p = new CompletableFuture<>();
                AcquireListener l = new AcquireListener(promise);
                l.acquired();
                p.whenCompleteAsync(l, executor);
                super.acquire(envelope, p);
            } else {
                if (pendingAcquireCount >= maxPendingAcquires) {
                    tooManyOutstanding(promise);
                } else {
                    AcquireTask task = new AcquireTask(envelope, promise);
                    if (pendingAcquireQueue.offer(task)) {
                        ++pendingAcquireCount;
                        if (timeoutTask != null) {
                            task.timeoutFuture = executor.schedule(timeoutTask, acquireTimeoutNanos, TimeUnit.NANOSECONDS);
                        }
                    } else {
                        tooManyOutstanding(promise);
                    }
                }

                assert pendingAcquireCount > 0;
            }
        } catch (Throwable cause) {
            promise.completeExceptionally(cause);
        }
    }

    private void tooManyOutstanding(CompletableFuture<?> promise) {
        promise.completeExceptionally(new IllegalStateException("Too many outstanding acquire operations"));
    }

    @Override
    public CompletableFuture<Void> release(final Channel channel, final CompletableFuture<Void> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        CompletableFuture<Void> p = new CompletableFuture<>();
        p.whenCompleteAsync((unused, error) -> {
            try {
                assert executor.inEventLoop();
                if (closed) {
                    // Since the pool is closed, we have no choice but to close the channel
                    channel.close();
                    promise.completeExceptionally(new IllegalStateException("FixedChannelPool was closed"));
                    return;
                }

                if (error == null) {
                    decrementAndRunTaskQueue();
                    promise.complete(null);
                } else {
                    // Check if the exception was not because of we passed the Channel to the wrong pool.
                    if (!(error instanceof IllegalArgumentException)) {
                        decrementAndRunTaskQueue();
                    }
                    promise.completeExceptionally(error);
                }
            } catch (Throwable cause) {
                promise.completeExceptionally(cause);
            }
        }, executor);
        super.release(channel, p);
        return promise;
    }

    private void decrementAndRunTaskQueue() {
        // We should never have a negative value.
        int currentCount = acquiredChannelCount.decrementAndGet();
        assert currentCount >= 0;

        // Run the pending acquire tasks before notify the original promise so if the user would
        // try to acquire again from the ChannelFutureListener and the pendingAcquireCount is >=
        // maxPendingAcquires we may be able to run some pending tasks first and so allow to add
        // more.
        runTaskQueue();
    }

    private void runTaskQueue() {
        while (acquiredChannelCount.get() < maxConnections) {
            AcquireTask task = pendingAcquireQueue.poll();
            if (task == null) {
                break;
            }

            // Cancel the timeout if one was scheduled
            ScheduledFuture<?> timeoutFuture = task.timeoutFuture;
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
            }

            --pendingAcquireCount;
            task.acquired();

            super.acquire(task.envelope, task.promise);
        }

        // We should never have a negative value.
        assert pendingAcquireCount >= 0;
        assert acquiredChannelCount.get() >= 0;
    }

    // AcquireTask extends AcquireListener to reduce object creations and so GC pressure
    private final class AcquireTask extends AcquireListener {

        final Envelope<? extends AbstractRequest> envelope;

        final CompletableFuture<Channel> promise;

        final long expireNanoTime = System.nanoTime() + acquireTimeoutNanos;

        ScheduledFuture<?> timeoutFuture;

        AcquireTask(Envelope<? extends AbstractRequest> envelope, CompletableFuture<Channel> promise) {
            super(promise);
            this.envelope = envelope;
            // We need to create a new promise as we need to ensure the AcquireListener runs in the correct
            // EventLoop.
            CompletableFuture<Channel> cf = new CompletableFuture<>();
            cf.whenCompleteAsync(this, executor);
            this.promise = cf;
        }
    }

    private abstract class TimeoutTask implements Runnable {

        @Override
        public final void run() {
            assert executor.inEventLoop();
            long nanoTime = System.nanoTime();
            for (; ; ) {
                AcquireTask task = pendingAcquireQueue.peek();
                // Compare nanoTime as descripted in the javadocs of System.nanoTime()
                //
                // See https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#nanoTime()
                // See https://github.com/netty/netty/issues/3705
                if (task == null || nanoTime - task.expireNanoTime < 0) {
                    break;
                }
                log.info("ACQUIRE TASK EXPIRED: {}", task.expireNanoTime);
                pendingAcquireQueue.remove();

                --pendingAcquireCount;
                onTimeout(task);
            }
        }

        public abstract void onTimeout(AcquireTask task);
    }

    private class AcquireListener implements BiConsumer<Channel, Throwable> {

        private final CompletableFuture<Channel> originalPromise;

        protected boolean acquired;

        private AcquireListener(CompletableFuture<Channel> originalPromise) {
            this.originalPromise = originalPromise;
        }

        @Override
        public void accept(Channel channel, Throwable error) {
            try {
                //log.info("ACQUIRE LISTENER: (IN LOOP: {}, THREAD: {})", executor.inEventLoop(), Thread.currentThread().getName());
                assert executor.inEventLoop();
                boolean success = error == null && channel != null;

                if (closed) {
                    if (success) {
                        // Since the pool is closed, we have no choice but to close the channel
                        channel.close();
                    }
                    originalPromise.completeExceptionally(new IllegalStateException("FixedChannelPool was closed"));
                    return;
                }

                if (success) {
                    originalPromise.complete(channel);
                } else {
                    if (acquired) {
                        decrementAndRunTaskQueue();
                    } else {
                        runTaskQueue();
                    }
                    originalPromise.completeExceptionally(error);
                }
            } catch (Throwable cause) {
                originalPromise.completeExceptionally(cause);
            }
        }

        public void acquired() {
            if (acquired) {
                return;
            }
            acquiredChannelCount.incrementAndGet();
            acquired = true;
        }
    }

    @Override
    public void close() {
        try {
            closeAsync().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the pool in an async manner.
     *
     * @return Future which represents completion of the close task
     */
    @Override
    public CompletableFuture<Void> closeAsync() {
        if (executor.inEventLoop()) {
            return close0();
        } else {
            return CompletableFuture.runAsync(this::close0, executor);
        }
    }

    private CompletableFuture<Void> close0() {
        assert executor.inEventLoop();

        if (!closed) {
            closed = true;
            for (; ; ) {
                AcquireTask task = pendingAcquireQueue.poll();
                if (task == null) {
                    break;
                }
                ScheduledFuture<?> f = task.timeoutFuture;
                if (f != null) {
                    f.cancel(false);
                }
                task.promise.completeExceptionally(new ClosedChannelException());
            }
            acquiredChannelCount.set(0);
            pendingAcquireCount = 0;

            // Ensure we dispatch this on another Thread as close0 will be called from the EventExecutor and we need
            // to ensure we will not block in a EventExecutor.
            return NettyUtil.makeCompletable(GlobalEventExecutor.INSTANCE.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    FixedNettyChannelPool.super.close();
                    return null;
                }
            }));
        }

        return CompletableFuture.completedFuture(null);
    }

    private static final class AcquireTimeoutException extends TimeoutException {

        private AcquireTimeoutException() {
            super("Acquire operation took longer then configured maximum time");
        }

        // Suppress a warning since the method doesn't need synchronization
        @Override
        public Throwable fillInStackTrace() {   // lgtm[java/non-sync-override]
            return this;
        }
    }
}
