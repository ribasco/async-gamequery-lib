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
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ibasco.agql.core.util.Console.BLUE;
import static com.ibasco.agql.core.util.Console.CYAN;
import static com.ibasco.agql.core.util.Console.YELLOW;
import static com.ibasco.agql.core.util.Console.color;
import static com.ibasco.agql.core.util.Console.printLine;
import static com.ibasco.agql.core.util.Console.println;

/**
 * Platform specific implementation
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Internal
public final class Platform {

    public static final ThreadGroup DEFAULT_THREAD_GROUP = new ThreadGroup("agql");

    private static final Logger log = LoggerFactory.getLogger(Platform.class);

    private static final List<Queue<Runnable>> TASK_QUEUE_LIST = new ArrayList<>();

    private static final String GLOBAL_DEFAULT_EXECUTOR_KEY = "globalDefaultExecutor";

    private static final Supplier<AgqlManagedExecutorService> DEFAULT_EXECUTOR_SUPPLIER;

    //initialize resource provider for the default global executor service
    private static final ManagedResourceProvider<AgqlManagedExecutorService> DEFAULT_EXECUTOR_PROVIDER;

    private static final Object lock = new Object();

    private static volatile ThreadFactory DEFAULT_THREAD_FACTORY;

    private static volatile EventLoopGroup DEFAULT_EVENT_LOOP_GROUP;

    private static volatile EventLoopTaskQueueFactory DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY;

    private static volatile BlockingQueue<Runnable> DEFAULT_QUEUE;

    private static volatile boolean initialized;

    private static final ConcurrentHashMap<ExecutorService, EventLoopGroup> eventLoopGroupMap = new ConcurrentHashMap<>();

    static {
        DEFAULT_EXECUTOR_SUPPLIER = () -> new AgqlManagedExecutorService(new ThreadPoolExecutor(Properties.getDefaultPoolSize(), Integer.MAX_VALUE, Long.MAX_VALUE, TimeUnit.MILLISECONDS, Platform.getDefaultQueue(), Platform.getDefaultThreadFactory()));
        DEFAULT_EXECUTOR_PROVIDER = new ManagedResourceProvider<>(DEFAULT_EXECUTOR_SUPPLIER); //provider for default executor service
    }

    private Platform() {}

    /**
     * <p>Initialize platform and properties. This can only be called once.</p>
     */
    public static void initialize() {
        if (initialized)
            return;
        initialized = true;
        try {
            //this is redundant, but necessary to ensure that the static block initializer of Properties is called first
            boolean verbose = Properties.isVerbose();
            if (verbose)
                Console.println("Initializing Platform");

            //we initialize options from here to ensure the order of initialization
            Option.initialize();
            //once we have created all available options, update it's field names via reflection
            //Option.updateFieldNames();
            //global
            GeneralOptions.getInstance();

            if (verbose) {
                printLine();
                println(color(BLUE, "Library Default Properties"));
                printLine();
                printProperty("Verbose", "true");
                printProperty("Native Transport Enabled", Properties.useNativeTransport());
                printProperty("Default core pool size", Properties.getDefaultPoolSize());
                printLine();
            }

            //ensure shutdown is called
            Runtime.getRuntime().addShutdownHook(new Thread(Platform::shutdown));
            log.debug("PLATFORM => Registered global shutdown hook for shared executor service(s)");
        } catch (Exception e) {
            initialized = false;
            throw new RuntimeException(e);
        }
    }

    private static void printProperty(String name, Object value) {
        println("%s: %s", color(CYAN, "%-25s", true, name), color(YELLOW, "%s", true, value));
    }

    /**
     * Perform a graceful shutdown. This will attempt to shutdown the internal default global executors provided the library.
     */
    public static void shutdown() {
        ExecutorService defaultExecutor = getDefaultExecutor();
        if (defaultExecutor != null) {
            if (Concurrency.shutdown(defaultExecutor)) {
                log.debug("PLATFORM => Default global executor has shutdown gracefully");
            }
        }
    }

    /**
     * <p>
     * The global {@link java.util.concurrent.ExecutorService} used by all clients by default. To obtain the underlying {@link java.util.concurrent.ThreadPoolExecutor} cast the return value to ({@link com.ibasco.agql.core.util.AgqlManagedExecutorService} and call {@link com.ibasco.agql.core.util.AgqlManagedExecutorService#getResource()}. (For internal use only, use at your own risk)
     * </p>
     * <blockquote>
     * <strong>IMPORTANT:</strong> The executor service returned by this function is reference counted (See {@link com.ibasco.agql.core.util.ManagedResource}). Each invocation of this function will increase it's reference count. So make sure to call {@link com.ibasco.agql.core.util.ManagedResource#release()} the on the resource after use.
     * </blockquote>
     *
     * @return The default global {@link java.util.concurrent.ExecutorService}
     *
     * @see ManagedResource
     * @see ManagedResource#release()
     */
    public static ExecutorService getDefaultExecutor() {
        return DEFAULT_EXECUTOR_PROVIDER.acquire(GLOBAL_DEFAULT_EXECUTOR_KEY);
    }

    /**
     * <p>getDefaultQueue.</p>
     *
     * @return a {@link java.util.concurrent.BlockingQueue} object
     */
    public static BlockingQueue<Runnable> getDefaultQueue() {
        if (DEFAULT_QUEUE == null) {
            synchronized (lock) {
                if (DEFAULT_QUEUE == null) {
                    DEFAULT_QUEUE = new LinkedBlockingQueue<>();
                }
            }
        }
        return DEFAULT_QUEUE;
    }

    /**
     * <p>getDefaultThreadFactory.</p>
     *
     * @return The default {@link java.util.concurrent.ThreadFactory} used by this library
     */
    public static ThreadFactory getDefaultThreadFactory() {
        if (DEFAULT_THREAD_FACTORY == null) {
            synchronized (lock) {
                if (DEFAULT_THREAD_FACTORY == null) {
                    DEFAULT_THREAD_FACTORY = new DefaultThreadFactory("agql-el", false, Thread.NORM_PRIORITY, DEFAULT_THREAD_GROUP);
                }
            }
        }
        return DEFAULT_THREAD_FACTORY;
    }

    /**
     * Check if the specified {@link java.util.concurrent.Executor} is a shared global executor provided by the library
     *
     * @param executor
     *         The {@link java.util.concurrent.Executor} to check
     *
     * @return {@code true} if the {@link java.util.concurrent.Executor} is global
     */
    public static boolean isDefaultExecutor(Executor executor) {
        if (executor == null)
            return false;
        AgqlManagedExecutorService svc = (AgqlManagedExecutorService) getDefaultExecutor();
        try {
            return svc == executor || svc.getResource() == executor;
        } finally {
            svc.release();
        }
    }

    /**
     * Get the core number of threads specified in {@link Options} if available. If not specified in options, it will attempt to detect it via heuristics.
     *
     * @param options
     *         The {@link Options} instance to be used as lookup
     * @param executorService
     *         The {@link ExecutorService} to be used for reference in-case value is not specified in the provided {@link Options}
     *
     * @return The core number of threads to be used by the {@link EventLoopGroup}
     *
     * @throws IllegalStateException
     *         If the number of core threads could not be determined.
     */
    public static Integer getCoreThreadCount(Options options, ExecutorService executorService) {
        Integer nThreads = options.get(GeneralOptions.THREAD_CORE_SIZE);
        //since we are dealing with a user provided executor service the option 'GeneralOptions.THREAD_CORE_SIZE' is required
        // unless we are able to automatically determine it's core pool size
        //Attempt to determine the number of threads supported by the executor service
        if (nThreads == null) {
            if (executorService instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
                nThreads = tpe.getCorePoolSize();
            } else if (executorService instanceof AgqlManagedExecutorService) {
                ThreadPoolExecutor tpe = ((AgqlManagedExecutorService) executorService).getResource();
                nThreads = tpe.getCorePoolSize();
            } else {
                throw new IllegalStateException("Please specify the core pool size for the  (See GeneralOptions.THREAD_CORE_SIZE)");
            }
        }
        return nThreads;
    }

    /**
     * The global {@link io.netty.channel.EventLoopGroup} shared across all clients by default. Upon shutdown, the default executor will also be automatically closed.
     *
     * @return The global {@link io.netty.channel.EventLoopGroup}
     */
    public static EventLoopGroup getDefaultEventLoopGroup() {
        if (DEFAULT_EVENT_LOOP_GROUP == null) {
            synchronized (lock) {
                if (DEFAULT_EVENT_LOOP_GROUP == null) {
                    AgqlManagedExecutorService svc = (AgqlManagedExecutorService) getDefaultExecutor();
                    try {
                        final ThreadPoolExecutor executor = svc.getResource();
                        DEFAULT_EVENT_LOOP_GROUP = createEventLoopGroup(executor, executor.getCorePoolSize(), Properties.useNativeTransport());
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
                    } finally {
                        svc.release();
                    }
                }
            }
        }
        return DEFAULT_EVENT_LOOP_GROUP;
    }

    /**
     * Creates a new {@link io.netty.channel.EventLoopGroup} instance. The default is {@link io.netty.channel.nio.NioEventLoopGroup}
     *
     * @param executor
     *         The {@link java.util.concurrent.Executor} to be used by the {@link io.netty.channel.EventLoopGroup}
     * @param nThreads
     *         The number of threads to be used by the {@link io.netty.channel.EventLoopGroup}. If a custom {@link java.util.concurrent.Executor} is provided, then the value should be less than or equals to the maximum number of threads supported by the provided {@link java.util.concurrent.Executor}. Set to 0 to use the value defined in system property {@code -Dio.netty.eventLoopThreads} (if present) or the default value defined by netty (num of processors x 2).
     * @param useNative
     *         {@code true} to use native transports when available (e.g. epoll for linux, kqueue for osx).
     *
     * @return A new {@link io.netty.channel.EventLoopGroup} instance
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
        log.debug("createEventLoopGroup(): Created event loop group with: {} threads (Executor Service: {})", nThreads, executor);
        return elg;
    }

    /**
     * Creates a new {@link io.netty.channel.EventLoopGroup} instance. The default is {@link io.netty.channel.nio.NioEventLoopGroup}
     *
     * @param channelClass
     *         The netty channel {@link java.lang.Class} that will be used as a referece to lookup the {@link io.netty.channel.EventLoopGroup}
     * @param executor
     *         The {@link java.util.concurrent.Executor} to be used by the {@link io.netty.channel.EventLoopGroup}
     * @param nThreads
     *         The number of threads to be used by the {@link io.netty.channel.EventLoopGroup}. If a custom {@link java.util.concurrent.Executor} is provided, then the value should be less than or equals to the maximum number of threads supported by the provided {@link java.util.concurrent.Executor}. Set to 0 to use the value defined in system property {@code -Dio.netty.eventLoopThreads} (if present) or the default value defined by netty (num of processors x 2).
     *
     * @return A new {@link io.netty.channel.EventLoopGroup} instance
     *
     * @throws java.lang.IllegalStateException
     *         If channelClass is not supported
     * @throws java.lang.IllegalArgumentException
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

    public static EventLoopGroup getOrCreateEventLoopGroup(Class<? extends Channel> channelClass, ExecutorService executor, int nThreads) {
        return eventLoopGroupMap.computeIfAbsent(executor, exec -> {
            log.debug("getOrCreateEventLoopGroup(): Creating new Event Loop Group instance for executor service '{}' (Channel class: {}, Num of Threads: {})", exec, channelClass, nThreads);
            EventLoopGroup group = createEventLoopGroup(channelClass, exec, nThreads);
            removeOnTermination(group, exec);
            return group;
        });
    }

    public static EventLoopGroup getOrCreateEventLoopGroup(ExecutorService executor, int nThreads, boolean useNative) {
        return eventLoopGroupMap.computeIfAbsent(executor, exec -> {
            log.debug("getOrCreateEventLoopGroup(): Creating new Event Loop Group instance for executor service '{}' (Num of Threads: {}, Use Native: {})", exec, nThreads, useNative);
            EventLoopGroup group = createEventLoopGroup(exec, nThreads, useNative);
            removeOnTermination(group, exec);
            return group;
        });
    }

    private static void removeOnTermination(EventLoopGroup group, ExecutorService exec) {
        //noinspection Convert2Lambda,unchecked,rawtypes
        group.terminationFuture().addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (eventLoopGroupMap.remove(exec, group)) {
                    log.debug("Event loop group has been terminated. Removed from map: (Executor Service: {}, Group: {})", exec, group);
                } else {
                    log.debug("Event loop group has been terminated. Failed to remove from map: (Executor Service: {}, Group: {})", exec, group);
                }
            }
        });
    }

    private static EventLoopTaskQueueFactory getEventLoopTaskQueueFactory() {
        if (DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY == null) {
            synchronized (lock) {
                if (DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY == null) {
                    DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY = maxCapacity -> {
                        Queue<Runnable> queue = maxCapacity == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxCapacity);
                        log.debug("Creating new task queue: {} ({})", queue, queue.hashCode());
                        TASK_QUEUE_LIST.add(queue);
                        return queue;
                    };
                }
            }
        }
        return DEFAULT_EVENT_QUEUE_TASKQUEUE_FACTORY;
    }

    /**
     * <p>creeateThreadGroup.</p>
     *
     * @param cls
     *         a {@link java.lang.Class} object
     *
     * @return a {@link java.lang.ThreadGroup} object
     */
    public static ThreadGroup creeateThreadGroup(Class<?> cls) {
        return creeateThreadGroup(cls, null);
    }

    /**
     * <p>creeateThreadGroup.</p>
     *
     * @param cls
     *         a {@link java.lang.Class} object
     * @param parent
     *         a {@link java.lang.ThreadGroup} object
     *
     * @return a {@link java.lang.ThreadGroup} object
     */
    public static ThreadGroup creeateThreadGroup(Class<?> cls, ThreadGroup parent) {
        String name = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(cls.getSimpleName()), "-").toLowerCase();
        if (parent == null) {
            return new ThreadGroup(name);
        } else {
            return new ThreadGroup(parent, name);
        }
    }

    /**
     * <p>getTaskQueueList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public static List<Queue<Runnable>> getTaskQueueList() {
        return TASK_QUEUE_LIST;
    }

    /**
     * <p>Retrieves the channel class based on the provided {@link io.netty.channel.EventLoopGroup}.</p>
     *
     * @param type
     *         a {@link com.ibasco.agql.core.transport.enums.TransportType} object
     * @param group
     *         a {@link io.netty.channel.EventLoopGroup} object
     *
     * @return a {@link java.lang.Class} object
     */
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

    /**
     * <p>Retrieves the channel class based on the detected platform</p>
     *
     * @param type
     *         a {@link com.ibasco.agql.core.transport.enums.TransportType}
     *
     * @return a {@link java.lang.Class} object
     */
    public static Class<? extends Channel> getChannelClass(TransportType type) {
        return getChannelClass(type, Properties.useNativeTransport());
    }

    /**
     * <p>Retrieves the channel class based on the detected platform</p>
     *
     * @param type
     *         a {@link com.ibasco.agql.core.transport.enums.TransportType} object
     * @param useNativeTransport
     *         {@code true} to use native transport
     *
     * @return a {@link java.lang.Class} object
     */
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
