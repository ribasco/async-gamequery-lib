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

import com.ibasco.agql.core.enums.BufferAllocatorType;
import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.pool.ChannelHealthChecker;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import dev.failsafe.Failsafe;
import io.netty.channel.EventLoopGroup;
import io.netty.util.ResourceLeakDetector;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>A collection of global configuration {@link com.ibasco.agql.core.util.Option}s</p>
 * <br />
 * <h3>Sample usage:</h3>
 *
 *
 * <pre>
 *  ThreadPoolExecutor executor = new ThreadPoolExecutor(9, Integer.MAX_VALUE,
 *                                                            60L, TimeUnit.SECONDS,
 *                                                            new LinkedBlockingQueue<>(),
 *                                                            new DefaultThreadFactory("agql-query"));
 *  Options queryOptions = OptionBuilder.newBuilder(SourceQueryOptions.class) //this can be any class that implements the Options interface
 *                                      .option(GlobalOptions.READ_TIMEOUT, 3000)
 *                                      .option(GlobalOptions.THREAD_EXECUTOR_SERVICE, executor)
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
@Inherit(options = FailsafeOptions.class)
public final class GlobalOptions extends AbstractOptions {

    private static final AtomicReference<Options> globalOptions = new AtomicReference<>();

    /**
     * Prevent this from being instantiated
     */
    private GlobalOptions() {}

    //<editor-fold desc="General">

    /**
     * Monitor resource usage leaks. Set the desired {@link ResourceLeakDetector}
     */
    public static final Option<ResourceLeakDetector.Level> RESOURCE_LEAK_DETECTOR_LEVEL = Option.create("globalResourceLeakDetectorLevel", ResourceLeakDetector.Level.PARANOID);

    /**
     * Number of milliseconds to wait before we throw a ReadTimeoutException (Default: 5 seconds)
     */
    public static final Option<Integer> READ_TIMEOUT = Option.create("globalReadTimeOut", 5000, true, true);

    /**
     * Number of milliseconds to wait before we throw a WriteTimeoutException (Default: 5 seconds)
     */
    public static final Option<Integer> WRITE_TIMEOUT = Option.create("globalWriteTimeout", 5000, true, true);

    /**
     * The maximum number of milliseconds to wait before timing out on close channel operation
     */
    public static final Option<Integer> CLOSE_TIMEOUT = Option.create("globalCloseTimeout", 3000);
    //</editor-fold>

    //<editor-fold desc="Connection Pooling">

    /**
     * The {@link ChannelHealthChecker} that is used by the {@link io.netty.channel.pool.ChannelPool} implementation to check if the {@link io.netty.channel.Channel} can be acquired.
     */
    @ApiStatus.Internal
    public static final Option<ChannelHealthChecker> POOL_CHANNEL_HEALTH_CHECKER = Option.create("globalChannelHealthChecker", ChannelHealthChecker.ACTIVE);

    /**
     * The action that will be executed once an acquire timeout is thrown
     *
     * @see com.ibasco.agql.core.transport.pool.FixedNettyChannelPool.AcquireTimeoutAction
     */
    @ApiStatus.Internal
    public static final Option<FixedNettyChannelPool.AcquireTimeoutAction> POOL_ACQUIRE_TIMEOUT_ACTION = Option.create("globalAcquireTimeoutAction", FixedNettyChannelPool.AcquireTimeoutAction.FAIL);

    /**
     * The maximum number of milliseconds to wait before a timeout is triggered during {@link io.netty.channel.Channel} acquisition.
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_ACQUIRE_TIMEOUT = Option.create("globalAcquireTimeout", Integer.MAX_VALUE);

    /**
     * Maximum number of allowed pending acquires. If the limit is reached, an exception is thrown for {@link io.netty.channel.Channel}
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_ACQUIRE_MAX = Option.create("globalMaxPendingAcquires", Integer.MAX_VALUE);

    /**
     * Maximum number of connections to be maintained in the channel/connection pool. This option is only applicable when {@link #POOL_TYPE} is set to {@link ChannelPoolType#FIXED}
     *
     * @see io.netty.channel.pool.FixedChannelPool
     * @see ChannelPoolType
     */
    public static final Option<Integer> POOL_MAX_CONNECTIONS = Option.create("globalMaxPooledConnections", 1, false, false);

    /**
     * The type of {@link io.netty.channel.pool.ChannelPool} implementation to use (bounded or unbounded)
     *
     * @see ChannelPoolType#ADAPTIVE
     * @see ChannelPoolType#FIXED
     */
    public static final Option<ChannelPoolType> POOL_TYPE = Option.create("globalPoolType", ChannelPoolType.ADAPTIVE, false, false);

    /**
     * Set to {@code true} to enable channel/connection pooling
     */
    public static final Option<Boolean> CONNECTION_POOLING = Option.create("globalPooling", true, false, false);
    //</editor-fold>

    //<editor-fold desc="Concurrency">

    /**
     * A custom {@link ExecutorService} that will be used by the client. Set to {@code null} to use the global executor provided by the library which is shared across all clients by default. (Default: {@code null})
     *
     * <p>
     * <strong>Note:</strong> If you provide a custom {@link ExecutorService}, then you are responsible for closing it. Only the default executor is closed automatically.
     * </p>
     *
     * @see Platform#getDefaultExecutor()
     */
    public static final Option<ExecutorService> THREAD_EXECUTOR_SERVICE = Option.create("globalThreadExecutorService", null);

    /**
     * The number of threads to be used by the internal {@link EventLoopGroup}. This is usually less than or equals to the core pool size of the {@link ExecutorService} provided (Default: {@code null}).
     *
     * @see #THREAD_EXECUTOR_SERVICE
     */
    public static final Option<Integer> THREAD_CORE_SIZE = Option.create("globalThreadCorePoolSize", null);
    //</editor-fold>

    //<editor-fold desc="Socket/Channel">

    /**
     * The strategy for allocating netty's inbound/outbound pooled buffers. If you choose {@link BufferAllocatorType#FIXED}, make sure to also set an optimal value for the buffer size (Default is: {@link BufferAllocatorType#FIXED})
     *
     * @see io.netty.channel.FixedRecvByteBufAllocator
     * @see io.netty.channel.AdaptiveRecvByteBufAllocator
     * @see #SOCKET_ALLOC_FIXED_SIZE
     */
    public static final Option<BufferAllocatorType> SOCKET_RECVBUF_ALLOC_TYPE = Option.create("globalRecvBufferAllocatorType", BufferAllocatorType.FIXED);

    /**
     * The fixed receive buffer size to allocate during channel initialization. Please note this is only applicable if {@link #SOCKET_RECVBUF_ALLOC_TYPE} is set to {@link BufferAllocatorType#FIXED} (Default is: 9216 bytes)
     *
     * @see io.netty.channel.FixedRecvByteBufAllocator
     * @see BufferAllocatorType
     * @see #SOCKET_RECVBUF_ALLOC_TYPE
     */
    public static final Option<Integer> SOCKET_ALLOC_FIXED_SIZE = Option.create("globalRecvAllocFixedSize", 9216);

    /**
     * As per netty: the initial buffer size when no feed back was received. Applicable only if SOCKET_RECVBUF_ALLOCATOR_TYPE is set to {@link BufferAllocatorType#ADAPTIVE}. (Default is 2048 bytes)
     *
     * @see #SOCKET_RECVBUF_ALLOC_TYPE
     * @see BufferAllocatorType
     */
    public static final Option<Integer> SOCKET_ALLOC_ADAPTIVE_INIT_SIZE = Option.create("globalRecvAllocAdaptiveInitSize", 2048);

    /**
     * As per netty: the inclusive lower bound of the expected buffer size. Applicable only if SOCKET_RECVBUF_ALLOCATOR_TYPE is set to {@link BufferAllocatorType#ADAPTIVE}. (Default is 64 bytes)
     *
     * @see #SOCKET_RECVBUF_ALLOC_TYPE
     * @see BufferAllocatorType
     */
    public static final Option<Integer> SOCKET_ALLOC_ADAPTIVE_MIN_SIZE = Option.create("globalRecvAllocAdaptiveMinSize", 64);

    /**
     * As per netty: the inclusive upper bound of the expected buffer size. Applicable only if SOCKET_RECVBUF_ALLOCATOR_TYPE is set to {@link BufferAllocatorType#ADAPTIVE}. (Default is 65536 bytes)
     *
     * @see #SOCKET_RECVBUF_ALLOC_TYPE
     * @see BufferAllocatorType
     */
    public static final Option<Integer> SOCKET_ALLOC_ADAPTIVE_MAX_SIZE = Option.create("globalRecvAllocAdaptiveMaxSize", 65536);

    /**
     * A channel option that specifies the total per-socket buffer space reserved for sends
     */
    public static final Option<Integer> SOCKET_SNDBUF = Option.create("globalSndBuf", 1048576);

    /**
     * A channel option that specifies the total per-socket buffer space reserved for receives
     */
    public static final Option<Integer> SOCKET_RECVBUF = Option.create("globalRecvBuf", 1048576);

    /**
     * A channel option, when enabled, allows the application to enable keep-alive packets for a socket connection.
     */
    public static final Option<Boolean> SOCKET_KEEP_ALIVE = Option.create("globalKeepAlive", true);

    /**
     * The connection timeout value
     */
    public static final Option<Integer> SOCKET_CONNECT_TIMEOUT = Option.create("globalConnectTimeoutMillis", 3000);

    //</editor-fold>

    //<editor-fold desc="Failsafe Options">

    //<editor-fold desc="Failsafe - General Options">

    /**
     * Enable {@link Failsafe} integration for Source Query module
     *
     * @see <a href="https://failsafe.dev/">Failsafe</a>
     */
    public static final Option<Boolean> FAILSAFE_ENABLED = Option.create(FailsafeProperties.FAILSAFE_ENABLED, true);
    //</editor-fold>

    //<editor-fold desc="Failsafe - Rate Limit Options">

    /**
     * Enable Rate Limiter (Failsafe)
     *
     * @see #FAILSAFE_RATELIMIT_MAX_EXEC
     * @see #FAILSAFE_RATELIMIT_PERIOD
     * @see #FAILSAFE_RATELIMIT_MAX_WAIT_TIME
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static final Option<Boolean> FAILSAFE_RATELIMIT_ENABLED = Option.create(FailsafeProperties.FAILSAFE_RATELIMIT_ENABLED, true);

    /**
     * Maximum number of executions within the specified period (Default is 650 executions per minute)
     *
     * @see #FAILSAFE_RATELIMIT_PERIOD
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static final Option<Long> FAILSAFE_RATELIMIT_MAX_EXEC = Option.create(FailsafeProperties.FAILSAFE_RATELIMIT_MAX_EXEC, 650L);

    /**
     * The period after which permitted executions are reset to the max executions. (Default is 60000 ms or 1 minute)
     *
     * @see #FAILSAFE_RATELIMIT_MAX_EXEC
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static final Option<Long> FAILSAFE_RATELIMIT_PERIOD = Option.create(FailsafeProperties.FAILSAFE_RATELIMIT_PERIOD, 5000L);

    /**
     * Maximum waiting time for permits to be available (Default is 10000 ms)
     *
     * @see <a href="https://failsafe.dev/rate-limiter/#waiting">Failsafe's Rate Limiter (Waiting)</a>
     */
    public static final Option<Long> FAILSAFE_RATELIMIT_MAX_WAIT_TIME = Option.create(FailsafeProperties.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, 10000L);

    /**
     * Specifies the rate limiting method to use (Default is Smooth)
     *
     * @see RateLimitType
     * @see <a href="https://failsafe.dev/rate-limiter/#rate-limiter">Failsafe's Rate Limiter</a>
     * @see <a href="https://failsafe.dev/rate-limiter/">Failsafe's Rate Limiter</a>
     */
    public static final Option<RateLimitType> FAILSAFE_RATELIMIT_TYPE = Option.create(FailsafeProperties.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH);
    //</editor-fold>

    //<editor-fold desc="Failsafe - Retry Policy Options">

    /**
     * Enable retry policy
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see #FAILSAFE_RETRY_MAX_ATTEMPTS
     * @see <a href="https://failsafe.dev/retry/">Failsafe's Retry Policy</a>
     */
    public static final Option<Boolean> FAILSAFE_RETRY_ENABLED = Option.create(FailsafeProperties.FAILSAFE_RETRY_ENABLED, true);

    /**
     * Delay between retries (In milliseconds. Use -1 to disable. Default is 1000ms)
     *
     * @see <a href="https://failsafe.dev/retry">Failsafe's Retry Policy</a>
     */
    public static final Option<Long> FAILSAFE_RETRY_DELAY = Option.create(FailsafeProperties.FAILSAFE_RETRY_DELAY, 1000L);

    /**
     * Enable Failsafe's Retry Backoff Feature
     *
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static final Option<Boolean> FAILSAFE_RETRY_BACKOFF_ENABLED = Option.create(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_ENABLED, true);

    /**
     * Sets the delay between retries (milliseconds), exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays.
     *
     * @see #FAILSAFE_RETRY_BACKOFF_MAX_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static final Option<Long> FAILSAFE_RETRY_BACKOFF_DELAY = Option.create(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_DELAY, 50L);

    /**
     * Sets the delay between retries (milliseconds), exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays. (Default is 5000 ms or 5 seconds)
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY
     * @see #FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static final Option<Long> FAILSAFE_RETRY_BACKOFF_MAX_DELAY = Option.create(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L);

    /**
     * Sets the delay between retries, exponentially backing off to the maxDelay and multiplying successive delays by the delayFactor. Replaces any previously configured fixed or random delays. (Default is 5.0)
     *
     * @see #FAILSAFE_RETRY_BACKOFF_ENABLED
     * @see <a href="https://failsafe.dev/retry/#delays">Failsafe's Retry Policy (Backoff)</a>
     */
    public static final Option<Double> FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR = Option.create(FailsafeProperties.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d);

    /**
     * Sets the max number of execution attempts to perform. -1 indicates no limit (Default is 3 attempts)
     *
     * @see <a href="https://failsafe.dev/retry">Failsafe's Retry Policy</a>
     */
    public static final Option<Integer> FAILSAFE_RETRY_MAX_ATTEMPTS = Option.create(FailsafeProperties.FAILSAFE_RETRY_MAX_ATTEMPTS, 3);
    //</editor-fold>

    //<editor-fold desc="Failsafe - Circuit Breaker Options">

    /**
     * Enable/disable Circuit breaker failsafe policy (Default: true)
     */
    public static final Option<Boolean> FAILSAFE_CIRCBREAKER_ENABLED = Option.create(FailsafeProperties.FAILSAFE_CIRCBREAKER_ENABLED, true);

    /**
     * <p>After opening, a breaker will delay for 1 second(s) by default before before transitioning to <a href="https://failsafe.dev/circuit-breaker/#half-opening">half-open</a>.
     * You can change to different delay by setting this configuration option. (Unit: milliseconds, Default: 1000 ms)</p>
     *
     * @see <a href="https://failsafe.dev/circuit-breaker/#half-opening">Circuit Breaker - Half-opening</a>
     */
    public static final Option<Integer> FAILSAFE_CIRCBREAKER_DELAY = Option.create(FailsafeProperties.FAILSAFE_CIRCBREAKER_DELAY, 1000);

    /**
     *
     */
    public static final Option<Integer> FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD = Option.create(FailsafeProperties.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD, Properties.getDefaultPoolSize());

    public static final Option<Integer> FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP = Option.create(FailsafeProperties.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP, Properties.getDefaultPoolSize() * 2);

    public static final Option<Integer> FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD = Option.create(FailsafeProperties.FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD, 1);
    //</editor-fold>
    //</editor-fold>

    /**
     * The global {@link Options} container initially populated with the default values but can be overriden at runtime.
     *
     * @return The global {@link Options} container
     */
    public static Options getContainer() {
        Options options = globalOptions.get();
        if (options == null) {
            options = newGlobalOptions();
            if (!globalOptions.compareAndSet(null, options)) {
                return globalOptions.get();
            }
        }
        return options;
    }

    private static Options newGlobalOptions() {
        Options options = new GlobalOptions();
        for (Option<?> opt : Option.getOptions().get(GlobalOptions.class)) {
            //noinspection unchecked
            options.add((Option<Object>) opt, opt.getDefaultValue());
        }
        //TODO: read system properties

        Console.println("Added %d global options in the container", options.size());
        return options;
    }
}
