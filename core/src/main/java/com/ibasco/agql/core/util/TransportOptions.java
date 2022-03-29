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

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.exceptions.IncompletePacketException;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.ChannelHealthChecker;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import io.netty.channel.EventLoopGroup;
import io.netty.util.ResourceLeakDetector;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.ExecutorService;

/**
 * <p>A collection of global configuration {@link Option}s to be used by the underlying {@link Transport}</p>
 *
 * <h3>Sample usage:</h3>
 *
 *
 * <pre>
 *  ThreadPoolExecutor executor = new ThreadPoolExecutor(9, Integer.MAX_VALUE,
 *                                                            60L, TimeUnit.SECONDS,
 *                                                            new LinkedBlockingQueue<>(),
 *                                                            new DefaultThreadFactory("agql-query"));
 *  Options queryOptions = OptionBuilder.newBuilder()
 *                                      .option(TransportOptions.READ_TIMEOUT, 3000)
 *                                      .option(TransportOptions.THREAD_EXECUTOR_SERVICE, executor)
 *                                      .option(SourceQueryOptions.FAILSAFE_ENABLED, true)
 *                                      .build();
 *
 *  SourceQueryClient client = new SourceQueryClient(queryOptions);
 *
 * </pre>
 *
 * @author Rafael Luis Ibasco
 * @see Option
 * @see OptionBuilder
 */
public final class TransportOptions {

    //<editor-fold desc="General">

    /**
     * Monitor resource usage leaks. Set the desired {@link ResourceLeakDetector}
     */
    public static final Option<ResourceLeakDetector.Level> RESOURCE_LEAK_DETECTOR_LEVEL = Option.createOption("resourceLeakDetectorLevel", ResourceLeakDetector.Level.PARANOID);

    /**
     * Number of milliseconds to wait before we throw a ReadTimeoutException (Default: 5 seconds)
     */
    public static final Option<Integer> READ_TIMEOUT = Option.createOption("readTimeOut", 5000, true, true);

    /**
     * Number of milliseconds to wait before we throw a WriteTimeoutException (Default: 5 seconds)
     */
    public static final Option<Integer> WRITE_TIMEOUT = Option.createOption("writeTimeout", 5000, true, true);

    /**
     * When {@code true}, the transport will throw an {@link IncompletePacketException} if the split-packets received from the game server are in incomplete state.
     */
    public static final Option<Boolean> REPORT_INCOMPLETE_PACKET = Option.createOption("reportIncompletePacket", false, true);

    /**
     * The maximum number of milliseconds to wait before timing out on close channel operation
     */
    public static final Option<Integer> CLOSE_TIMEOUT = Option.createOption("closeTimeout", 3000);
    //</editor-fold>

    //<editor-fold desc="Connection Pooling">

    /**
     * The {@link ChannelHealthChecker} that is used by the {@link io.netty.channel.pool.ChannelPool} implementation to check if the {@link io.netty.channel.Channel} can be acquired.
     */
    @ApiStatus.Internal
    public static final Option<ChannelHealthChecker> POOL_CHANNEL_HEALTH_CHECKER = Option.createOption("channelHealthChecker", ChannelHealthChecker.ACTIVE);

    /**
     * The action that will be executed once an acquire timeout is thrown
     *
     * @see com.ibasco.agql.core.transport.pool.FixedNettyChannelPool.AcquireTimeoutAction
     */
    @ApiStatus.Internal
    public static final Option<FixedNettyChannelPool.AcquireTimeoutAction> POOL_ACQUIRE_TIMEOUT_ACTION = Option.createOption("acquireTimeoutAction", FixedNettyChannelPool.AcquireTimeoutAction.FAIL);

    /**
     * The maximum number of milliseconds to wait before a timeout is triggered during {@link io.netty.channel.Channel} acquisition.
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_ACQUIRE_TIMEOUT = Option.createOption("acquireTimeout", Integer.MAX_VALUE);

    /**
     * Maximum number of allowed pending acquires. If the limit is reached, an exception is thrown for {@link io.netty.channel.Channel}
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_ACQUIRE_MAX = Option.createOption("maxPendingAcquires", Integer.MAX_VALUE);

    /**
     * Maximum number of connections to be maintained in the channel/connection pool. This option is only applicable when {@link #POOL_TYPE} is set to {@link ChannelPoolType#FIXED}
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_MAX_CONNECTIONS = Option.createOption("maxPooledConnections", 1, false, false);

    /**
     * The type of {@link io.netty.channel.pool.ChannelPool} implementation to use (bounded or unbounded)
     *
     * @see ChannelPoolType#ADAPTIVE
     * @see ChannelPoolType#FIXED
     */
    public static final Option<ChannelPoolType> POOL_TYPE = Option.createOption("poolType", ChannelPoolType.ADAPTIVE, false, false);

    /**
     * Set to {@code true} to enable channel/connection pooling
     */
    public static final Option<Boolean> CONNECTION_POOLING = Option.createOption("pooling", true, false, false);
    //</editor-fold>

    //<editor-fold desc="Failsafe Integration">

    /**
     * Enable/disable {@link dev.failsafe.Failsafe} support (This is to ensure that a connection is successfully established and will re-attempt to connect once it encounters an error)
     *
     * @see com.ibasco.agql.core.transport.FailsafeChannelFactory
     */
    public static final Option<Boolean> FAILSAFE_ENABLED = Option.createOption("failSafeEnabled", true, false, false);

    /**
     * The maximum number of connection attempts for acquiring a {@link io.netty.channel.Channel} / Connection
     *
     * @see com.ibasco.agql.core.transport.FailsafeChannelFactory
     */
    public static final Option<Integer> FAILSAFE_ACQUIRE_MAX_CONNECT = Option.createOption("failSafeMaxConnectAttempts", 3, false, false);

    /**
     * Sets the minimum delay between retries, exponentially backing off to the maxDelay and multiplying successive delays by a factor of 2.
     *
     * @see com.ibasco.agql.core.transport.FailsafeChannelFactory
     */
    public static final Option<Integer> FAILSAFE_ACQUIRE_BACKOFF_MIN = Option.createOption("failSafeBackoffMin", 1);

    /**
     * Sets the maximum delay between retries, exponentially backing off to the maxDelay and multiplying successive delays by a factor of 2.
     *
     * @see com.ibasco.agql.core.transport.FailsafeChannelFactory
     */
    public static final Option<Integer> FAILSAFE_ACQUIRE_BACKOFF_MAX = Option.createOption("failSafeBackoffMax", 5);
    //</editor-fold>

    //<editor-fold desc="Concurrency">

    /**
     * When enabled, the library will attempt to utilize platform specific transport(s) (e.g. epoll for linux or kqueue for mac osx)
     * otherwise the default java NIO transport will be used.
     */
    public static final Option<Boolean> USE_NATIVE_TRANSPORT = Option.createOption("useNativeTransport", true);

    /**
     * A custom {@link ExecutorService} that will be used by the client. Set to {@code null} to use the global executor provided by the library which is shared across all clients by default. (Default: {@code null})
     *
     * <p>
     * <strong>Note:</strong> If you provide a custom {@link ExecutorService}, then you are responsible for closing it. Only the default executor is closed automatically.
     * </p>
     *
     * @see Platform#getDefaultExecutor()
     * @see Platform#getDefaultEventLoopGroup()
     */
    public static final Option<ExecutorService> THREAD_EXECUTOR_SERVICE = Option.createOption("threadExecutorService", null);

    /**
     * The number of threads to be used by the internal {@link EventLoopGroup}. This is usually less than or equals to the core pool size of the {@link ExecutorService} provided (Default: {@code null}).
     *
     * @see #THREAD_EXECUTOR_SERVICE
     */
    public static final Option<Integer> THREAD_CORE_SIZE = Option.createOption("threadCorePoolSize", null);

    /**
     * When the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating. Time unit is in milliseconds.
     */
    public static final Option<Long> THREAD_POOL_KEEP_ALIVE = Option.createOption("threadKeepAlive", Long.MAX_VALUE);
    //</editor-fold>

    //<editor-fold desc="Socket/Channel">

    /**
     * A channel option that specifies the total per-socket buffer space reserved for sends
     */
    public static final Option<Integer> SOCKET_SNDBUF = Option.createOption("sndBuf", 1048576);

    /**
     * A channel option that specifies the total per-socket buffer space reserved for receives
     */
    public static final Option<Integer> SOCKET_RECVBUF = Option.createOption("recvBuf", 1048576);

    /**
     * A channel option, when enabled, allows the application to enable keep-alive packets for a socket connection.
     */
    public static final Option<Boolean> SOCKET_KEEP_ALIVE = Option.createOption("keepAlive", true);

    /**
     * The connection timeout value
     */
    public static final Option<Integer> SOCKET_CONNECT_TIMEOUT = Option.createOption("connectTimeoutMillis", 3000);

    //</editor-fold>
}
