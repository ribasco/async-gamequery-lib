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

import com.ibasco.agql.core.transport.enums.TransportType;
import io.netty.channel.Channel;
import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorChooserFactory;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Platform specific implementation
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class Platform {

    private static final Logger log = LoggerFactory.getLogger(Platform.class);

    public static final int DEFAULT_THREAD_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    public static final ThreadGroup DEFAULT_THREAD_GROUP = new ThreadGroup("agql");

    private static ThreadPoolExecutor DEFAULT_EXECUTOR;

    private static ThreadFactory DEFAULT_THREAD_FACTORY;

    private static EventLoopGroup DEFAULT_EVENT_LOOP_GROUP;

    private static EventLoopTaskQueueFactory DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY;

    private static BlockingQueue<Runnable> DEFAULT_QUEUE;

    private static final List<Queue<Runnable>> TASK_QUEUE_LIST = new ArrayList<>();

    private Platform() {}

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (DEFAULT_EXECUTOR != null) {
                    if (Concurrency.shutdown(DEFAULT_EXECUTOR)) {
                        log.debug("PLATFORM => Default global executor has shutdown gracefully");
                    }
                }
                if (DEFAULT_EVENT_LOOP_GROUP != null) {
                    if (Concurrency.shutdown(DEFAULT_EVENT_LOOP_GROUP)) {
                        log.debug("PLATFORM => Default global event loop group has shutdown gracefully");
                    }
                }
            }
        });
        log.debug("PLATFORM => Registered global shutdown hook for shared executor service(s)");
    }

    public static synchronized BlockingQueue<Runnable> getDefaultQueue() {
        if (DEFAULT_QUEUE == null) {
            DEFAULT_QUEUE = new LinkedBlockingQueue<>();
        }
        return DEFAULT_QUEUE;
    }

    /**
     * @return The default {@link ThreadFactory} used by this library
     */
    public static synchronized ThreadFactory getDefaultThreadFactory() {
        if (DEFAULT_THREAD_FACTORY == null)
            DEFAULT_THREAD_FACTORY = new DefaultThreadFactory("agql-el", false, Thread.NORM_PRIORITY, DEFAULT_THREAD_GROUP);
        return DEFAULT_THREAD_FACTORY;
    }

    /**
     * Check if the specified {@link Executor} is a shared global executor provided by the library
     *
     * @param executor
     *         The {@link Executor} to check
     *
     * @return {@code true} if the {@link Executor} is global
     */
    public static boolean isGlobal(Executor executor) {
        if (executor == null)
            return false;
        return DEFAULT_EXECUTOR == executor || DEFAULT_EVENT_LOOP_GROUP == executor;
    }

    /**
     * The global {@link ExecutorService} used by all clients by default. This is shutdown automatically
     *
     * @return The default global {@link ExecutorService}
     */
    public static synchronized ThreadPoolExecutor getDefaultExecutor() {
        if (DEFAULT_EXECUTOR == null) {
            DEFAULT_EXECUTOR = new ThreadPoolExecutor(DEFAULT_THREAD_SIZE,
                                                      Integer.MAX_VALUE,
                                                      Long.MAX_VALUE,
                                                      TimeUnit.MILLISECONDS,
                                                      getDefaultQueue(),
                                                      getDefaultThreadFactory());
        }
        return DEFAULT_EXECUTOR;
    }

    /**
     * The global {@link EventLoopGroup} shared across all clients by default. Upon shutdown, the default executor will also be automatically closed.
     *
     * @return The global {@link EventLoopGroup}
     */
    public static synchronized EventLoopGroup getDefaultEventLoopGroup() {
        if (DEFAULT_EVENT_LOOP_GROUP == null) {
            final ThreadPoolExecutor executor = getDefaultExecutor();
            DEFAULT_EVENT_LOOP_GROUP = createEventLoopGroup(executor, executor.getCorePoolSize(), true);
            //noinspection unchecked
            DEFAULT_EVENT_LOOP_GROUP.terminationFuture().addListener((GenericFutureListener) future -> {
                if (future.isSuccess()) {
                    new Thread(() -> {
                        log.debug("PLATFORM => Shutting down default global executor");
                        Concurrency.shutdown(executor);
                    }, "agql-shutdown").start();
                } else {
                    throw new IllegalStateException(future.cause());
                }
            });
        }
        return DEFAULT_EVENT_LOOP_GROUP;
    }

    public static int getDefaultPoolSize() {
        return getDefaultExecutor().getCorePoolSize();
    }

    public static ThreadGroup creeateThreadGroup(Class<?> cls) {
        return creeateThreadGroup(cls, null);
    }

    public static ThreadGroup creeateThreadGroup(Class<?> cls, ThreadGroup parent) {
        String name = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(cls.getSimpleName()), "-").toLowerCase();
        if (parent == null) {
            return new ThreadGroup(name);
        } else {
            return new ThreadGroup(parent, name);
        }
    }

    public static List<Queue<Runnable>> getTaskQueueList() {
        return TASK_QUEUE_LIST;
    }

    private static EventLoopTaskQueueFactory getEventLoopTaskQueueFactory() {
        if (DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY == null) {
            DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY = maxCapacity -> {
                Queue<Runnable> queue = maxCapacity == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxCapacity);
                log.debug("Creating new task queue: {} ({})", queue, queue.hashCode());
                TASK_QUEUE_LIST.add(queue);
                return queue;
            };
        }
        return DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY;
    }

    /**
     * Creates a new {@link EventLoopGroup} instance. The default is {@link NioEventLoopGroup}
     *
     * @param executor
     *         The {@link Executor} to be used by the {@link EventLoopGroup}
     * @param nThreads
     *         The number of threads to be used by the {@link EventLoopGroup}. If a custom {@link Executor} is provided, then the value should be less than or equals to the maximum number of threads supported by the provided {@link Executor}. Set to 0 to use the value defined in system property {@code -Dio.netty.eventLoopThreads} (if present) or the default value defined by netty (num of processors x 2).
     * @param useNative
     *         {@code true} to use native transports when available (e.g. epoll for linux, kqueue for osx).
     *
     * @return A new {@link EventLoopGroup} instance
     */
    public static EventLoopGroup createEventLoopGroup(ExecutorService executor, int nThreads, boolean useNative) {
        EventLoopGroup elg = null;
        if (useNative) {
            if (Epoll.isAvailable()) {
                elg = new EpollEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
            } else if (KQueue.isAvailable()) {
                elg = new KQueueEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
            }
        }
        if (elg == null)
            elg = new NioEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, SelectorProvider.provider(), DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
        log.debug("Created default event loop group with: {} threads", nThreads);
        return elg;
    }

    /**
     * Creates a new {@link EventLoopGroup} instance. The default is {@link NioEventLoopGroup}
     *
     * @param channelClass
     *         The netty channel {@link Class} that will be used as a referece to lookup the {@link EventLoopGroup}
     * @param executor
     *         The {@link Executor} to be used by the {@link EventLoopGroup}
     * @param nThreads
     *         The number of threads to be used by the {@link EventLoopGroup}. If a custom {@link Executor} is provided, then the value should be less than or equals to the maximum number of threads supported by the provided {@link Executor}. Set to 0 to use the value defined in system property {@code -Dio.netty.eventLoopThreads} (if present) or the default value defined by netty (num of processors x 2).
     *
     * @return A new {@link EventLoopGroup} instance
     *
     * @throws IllegalStateException
     *         If channelClass is not supported
     * @throws IllegalArgumentException
     *         If channelClass is {@code null}
     */
    public static EventLoopGroup createEventLoopGroup(Class<? extends Channel> channelClass, Executor executor, int nThreads) {
        if (channelClass == null)
            throw new IllegalArgumentException("Channel class must not be null");
        if (NioSocketChannel.class.isAssignableFrom(channelClass) || NioDatagramChannel.class.isAssignableFrom(channelClass)) {
            return new NioEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, SelectorProvider.provider(), DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
        } else if (EpollSocketChannel.class.isAssignableFrom(channelClass) || EpollDatagramChannel.class.isAssignableFrom(channelClass)) {
            return new EpollEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
        } else if (KQueueSocketChannel.class.isAssignableFrom(channelClass) || KQueueDatagramChannel.class.isAssignableFrom(channelClass)) {
            return new KQueueEventLoopGroup(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, DefaultSelectStrategyFactory.INSTANCE, RejectedExecutionHandlers.reject(), getEventLoopTaskQueueFactory());
        } else {
            throw new IllegalStateException("Unsupported channel class: " + channelClass);
        }
    }

    public static Class<? extends Channel> getChannelClass(TransportType type, EventLoopGroup group) {
        Objects.requireNonNull(type, "Transport type not specified");
        Objects.requireNonNull(group, "Event Loop Group must not be null");
        if (group instanceof NioEventLoopGroup) {
            return TransportType.TCP.equals(type) ? NioSocketChannel.class : NioDatagramChannel.class;
        } else if (group instanceof EpollEventLoopGroup) {
            return TransportType.TCP.equals(type) ? EpollSocketChannel.class : EpollDatagramChannel.class;
        } else if (group instanceof KQueueEventLoopGroup) {
            return TransportType.TCP.equals(type) ? KQueueSocketChannel.class : KQueueDatagramChannel.class;
        } else {
            throw new IllegalStateException("Unsupported event loop group type: " + group);
        }
    }

    public static Class<? extends Channel> getChannelClass(TransportType type, boolean useNativeTransport) {
        Class<? extends Channel> channelClass = null;
        if (useNativeTransport) {
            //linux native transport
            if (Epoll.isAvailable()) {
                log.debug("[UTIL] CHANNEL_CLASS => Found linux epoll native transport");
                if (TransportType.TCP.equals(type)) {
                    channelClass = EpollSocketChannel.class;
                } else if (TransportType.UDP.equals(type)) {
                    channelClass = EpollDatagramChannel.class;
                } else {
                    throw new IllegalStateException("Unsupported channel type");
                }
            }
            //osx native transport
            else if (KQueue.isAvailable()) {
                log.debug("[UTIL] CHANNEL_CLASS => Found macosx kqueue native transport");
                if (TransportType.TCP.equals(type)) {
                    channelClass = KQueueSocketChannel.class;
                } else if (TransportType.UDP.equals(type)) {
                    channelClass = KQueueDatagramChannel.class;
                } else {
                    throw new IllegalStateException("Unsupported channel type");
                }
            }
        }
        if (channelClass == null) {
            log.debug("[UTIL] CHANNEL_CLASS => Falling back to java NIO transport");
            //fallback to nio
            if (TransportType.TCP.equals(type)) {
                channelClass = NioSocketChannel.class;
            } else if (TransportType.UDP.equals(type)) {
                channelClass = NioDatagramChannel.class;
            } else {
                throw new IllegalStateException("Unsupported channel type");
            }
        }
        return channelClass;
    }
}
