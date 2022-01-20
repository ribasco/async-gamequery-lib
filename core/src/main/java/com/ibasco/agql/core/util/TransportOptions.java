/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core.util;

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.exceptions.IncompletePacketException;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.ChannelHealthChecker;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import com.ibasco.agql.core.transport.pool.NettyPoolingStrategy;
import io.netty.channel.EventLoopGroup;
import io.netty.util.ResourceLeakDetector;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.Executor;

/**
 * A collection of global configuration {@link Option} instances for the underlying {@link Transport}
 *
 * @author Rafael Luis Ibasco
 */
public final class TransportOptions {

    //<editor-fold desc="General">

    /**
     * When enabled, the library will attempt to utilize platform specific transport implementation (e.g. epoll for linux or kqueue for mac osx)
     * otherwise the default java NIO transport implementation will be used.
     */
    public static final Option<Boolean> USE_NATIVE_TRANSPORT = Option.createOption("useNativeTransport", true);

    /**
     * Monitor resource usage leaks. Set the desired {@link ResourceLeakDetector.Level}
     */
    public static final Option<ResourceLeakDetector.Level> RESOURCE_LEAK_DETECTOR_LEVEL = Option.createOption("resourceLeakDetectorLevel", ResourceLeakDetector.Level.PARANOID);

    /**
     * Number of milliseconds to wait before we throw a ReadTimeoutException
     */
    public static final Option<Integer> READ_TIMEOUT = Option.createOption("readTimeOut", 5000, true, true);

    /**
     * Number of milliseconds to wait before we throw a WriteTimeoutException
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
    public static final Option<ChannelHealthChecker> POOL_CHANNEL_HEALTH_CHECKER = Option.createOption("channelHealthChecker", ChannelHealthChecker.ACTIVE);

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

    public static final Option<FixedNettyChannelPool.AcquireTimeoutAction> POOL_ACQUIRE_TIMEOUT_ACTION = Option.createOption("acquireTimeoutAction", FixedNettyChannelPool.AcquireTimeoutAction.FAIL);

    /**
     * Maximum number of connections to be maintained in the channel/connection pool. This option is only applicable when {@link #POOL_TYPE} is set to {@link ChannelPoolType#FIXED}
     *
     * @see #THREAD_EL_SIZE
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

    /**
     * Use this option to specify the connection pooling strategy to be used by the underlying transport.
     * <p>
     * A connection pooling strategy determines how a channel pool instance is obtained from a request.
     * <p>
     * Note: This is only applicable if connection pooling is enabled (See {@link #CONNECTION_POOLING}).
     *
     * @see #CONNECTION_POOLING
     * @see NettyPoolingStrategy
     * @see NettyPoolingStrategy#MESSAGE_TYPE
     * @see NettyPoolingStrategy#ADDRESS
     */
    public static final Option<NettyPoolingStrategy> POOL_STRATEGY = Option.createOption("poolingStrategy", NettyPoolingStrategy.ADDRESS, false, false);
    //</editor-fold>

    //<editor-fold desc="Concurrency">

    /**
     * Set the default executor service that should be utilized by netty.
     */
    public static final Option<Executor> THREAD_POOL_EXECUTOR = Option.createOption("threadExecutor");

    /**
     * Use a custom {@link EventLoopGroup} that will be used by the underlying {@link Transport}
     *
     * @see #THREAD_EL_SIZE
     */
    public static final Option<EventLoopGroup> THREAD_EL_GROUP = Option.createOption("eventLoopGroup");

    /**
     * The number of threads that will be used by the {@link EventLoopGroup}. If providing a custom {@link Executor}, make sure that the value is not greater than the {@link Executor}'s core pool size.
     * If 0 is provided, the value will automatically be computed based on the number of available cores of the current system.
     *
     * @see #POOL_MAX_CONNECTIONS
     * @see #THREAD_POOL_EXECUTOR
     */
    @ApiStatus.Experimental
    public static final Option<Integer> THREAD_EL_SIZE = Option.createOption("threadElSize", Platform.DEFAULT_THREAD_SIZE);

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
