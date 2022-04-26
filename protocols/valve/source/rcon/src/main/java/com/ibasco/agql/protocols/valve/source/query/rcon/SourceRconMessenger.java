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

package com.ibasco.agql.protocols.valve.source.query.rcon;

import com.ibasco.agql.core.ChannelRegistry;
import com.ibasco.agql.core.Credentials;
import com.ibasco.agql.core.CredentialsStore;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.exceptions.TimeoutException;
import com.ibasco.agql.core.exceptions.*;
import com.ibasco.agql.core.transport.FailsafeChannelFactory;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.NettyContextChannelFactory;
import com.ibasco.agql.core.transport.enums.ChannelPoolType;
import com.ibasco.agql.core.transport.enums.TransportType;
import com.ibasco.agql.core.transport.pool.FixedNettyChannelPool;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.Properties;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.*;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconResponse;
import dev.failsafe.*;
import dev.failsafe.event.ExecutionAttemptedEvent;
import dev.failsafe.function.CheckedFunction;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>RCON Messenger</p>
 *
 * @author Rafael Luis Ibasco
 */
@SuppressWarnings("FieldCanBeLocal")
@MessengerProperties(optionClass = SourceRconOptions.class)
public final class SourceRconMessenger extends NettyMessenger<SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    //<editor-fold desc="Private Fields">
    private final Statistics statistics = new Statistics();

    private final ScheduledExecutorService jobScheduler;

    private final CredentialsStore credentialsStore;

    private final boolean reauthenticate;

    private final InactivityCheckTask INACTIVITY_CHECK_TASK = new InactivityCheckTask();

    private final ChannelRegistry registry = new SourceRconChannelRegistry();

    private final SourceRconChannelFactory channelFactory;

    private final RconAuthenticator authenticator;

    private RetryPolicy<SourceRconChannelContext> retryPolicy;

    private Fallback<SourceRconChannelContext> fallbackPolicy;

    private CircuitBreaker<SourceRconChannelContext> circuitBreakerPolicy;

    private FailsafeExecutor<SourceRconChannelContext> executor;

    private volatile boolean healthCheckStarted;
    //</editor-fold>

    //<editor-fold desc="Constructor">

    /**
     * <p>Constructor for SourceRconMessenger.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    public SourceRconMessenger(Options options) {
        super(options);
        this.credentialsStore = get(SourceRconOptions.CREDENTIALS_STORE, new InMemoryCredentialsStore());
        this.jobScheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("agql-jobs-auth"));
        this.reauthenticate = getOrDefault(SourceRconOptions.REAUTHENTICATE);
        this.authenticator = new SourceRconAuthenticator(credentialsStore, reauthenticate);
        this.channelFactory = (SourceRconChannelFactory) getChannelFactory();
        initFailSafe(getOptions());
    }
    //</editor-fold>

    private void initFailSafe(final Options rconOptions) {
        this.circuitBreakerPolicy = buildCircuitBreakerPolicy(rconOptions);
        this.fallbackPolicy = buildFallbackPolicy();
        this.retryPolicy = buildRetryPolicy(rconOptions);
        this.executor = Failsafe.with(fallbackPolicy, retryPolicy, circuitBreakerPolicy).with(getExecutor());
    }

    //<editor-fold desc="Failsafe Policy Builder">
    private CircuitBreaker<SourceRconChannelContext> buildCircuitBreakerPolicy(final Options options) {
        CircuitBreakerBuilder<SourceRconChannelContext> builder = FailsafeBuilder.buildCircuitBreaker(FailsafeOptions.class, options);
        builder.handleIf(e -> {
            Throwable cause = Errors.unwrap(e);
            return cause instanceof TimeoutException || cause instanceof io.netty.handler.timeout.TimeoutException;
        });
        return builder.build();
    }

    /** {@inheritDoc} */
    @Override
    protected void configure(final Options options) {
        //global defaults
        applyDefault(GeneralOptions.CONNECTION_POOLING, true);
        applyDefault(GeneralOptions.POOL_TYPE, ChannelPoolType.FIXED);
        applyDefault(GeneralOptions.POOL_ACQUIRE_TIMEOUT_ACTION, FixedNettyChannelPool.AcquireTimeoutAction.FAIL);

        //rcon defaults
        applyDefault(SourceRconOptions.USE_TERMINATOR_PACKET, true);
        applyDefault(SourceRconOptions.STRICT_MODE, false);

        //rcon failsafe defaults
        applyDefault(FailsafeOptions.FAILSAFE_ENABLED, true);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_ENABLED, true);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_DELAY, 3000L);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_MAX_ATTEMPTS, 3);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY, 50L);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_MAX_DELAY, 5000L);
        applyDefault(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_DELAY_FACTOR, 1.5d);

        //connect failsafe defaults
        applyDefault(ConnectOptions.FAILSAFE_ENABLED, true);
        applyDefault(ConnectOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_ENABLED, true);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_DELAY, 1000);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLD, Properties.getDefaultPoolSize());
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_FAILURE_THRESHOLDING_CAP, Properties.getDefaultPoolSize() * 2);
        applyDefault(ConnectOptions.FAILSAFE_CIRCBREAKER_SUCCESS_THRESHOLD, 1);
        Console.println("%s: Applied default option values", getClass().getSimpleName());
    }

    private Fallback<SourceRconChannelContext> buildFallbackPolicy() {
        FallbackBuilder<SourceRconChannelContext> builder = Fallback.builderOfException(new CheckedFunction<ExecutionAttemptedEvent<? extends SourceRconChannelContext>, Exception>() {
            @Override
            public Exception apply(ExecutionAttemptedEvent<? extends SourceRconChannelContext> event) throws Throwable {
                Throwable error = event.getLastException();
                Throwable cause = Errors.unwrap(error);
                Exception newException;

                //We still need to pass the MessengerException, we only update the underlying cause if necessary
                if (error instanceof MessengerException) {
                    MessengerException mException = (MessengerException) error;
                    int maxAttempts = retryPolicy.getConfig().getMaxAttempts();
                    //have we reached the max login attempts limit
                    if (cause instanceof ChannelClosedException && event.getAttemptCount() >= (maxAttempts - 1)) {
                        SourceRconChannelContext context = mException.getContext();
                        Throwable newCause = null;
                        if (context.properties().request() instanceof SourceRconAuthRequest) {
                            SourceRconAuthRequest authRequest = context.properties().request();
                            InetSocketAddress address = context.properties().remoteAddress();
                            Credentials credentials = credentialsStore.get(address);
                            if (credentials != null && credentials.isValid()) {
                                credentials.invalidate();
                                newCause = new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), cause, authRequest, address, SourceRconAuthReason.INVALIDATED);
                            }
                        }
                        if (newCause == null)
                            newCause = new RconMaxLoginAttemptsException(mException.getRequest(), mException.getRemoteAddress(), SourceRconAuthReason.CONNECTION_DROPPED, event.getAttemptCount(), maxAttempts);
                        newException = new MessengerException(newCause, mException.getContext());
                    } else {
                        newException = mException;
                    }
                } else if (event.getLastException() instanceof CircuitBreakerOpenException) {
                    CircuitBreakerOpenException openException = (CircuitBreakerOpenException) event.getLastException();
                    return new RejectedRequestException("The internal circuit-breaker has been OPENED. Temporarily not accepting any more requests", openException.getCause());
                } else {
                    newException = new CompletionException(cause);
                }
                return newException;
            }
        });
        return builder.build();
    }

    private RetryPolicy<SourceRconChannelContext> buildRetryPolicy(final Options options) {
        RetryPolicyBuilder<SourceRconChannelContext> builder = FailsafeBuilder.buildRetryPolicy(FailsafeOptions.class, options);
        builder.abortIf((context, error) -> error instanceof RconAuthException || Errors.unwrap(error) instanceof ConnectException);
        builder.handleIf(e -> {
            Throwable error = Errors.unwrap(e);
            return error instanceof TimeoutException || error instanceof IOException;
        });
        if (Properties.isVerbose()) {
            builder.onRetry(event -> {
                //Console.println("[RCON] Retrying (error: %s, attempts: %d))", event.getLastException(), event.getAttemptCount());
                Console.colorize().blue().text("[RCON] ").reset()
                       .text(">> Last request ").red().text("FAILED").reset()
                       .text(" retrying request")
                       .text(" (")
                       .text("Last Error: %s, Attempts: %d", event.getLastException(), event.getAttemptCount())
                       .text(")")
                       .println();
            });
            builder.onRetriesExceeded(event -> {
                //Console.println("[RCON] Retries exceeded (Attempts: %d, Last result: '%s', Last Error: %s)\n", event.getAttemptCount(), event.getResult(), event.getException());
                Console.colorize().blue().text("[RCON] ").reset().text(">> Retries ").yellow().text("EXCEEDED").reset().text(" (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount()).println();
            });
            builder.onFailure(event -> Console.colorize().blue().text("[RCON] ").reset().text(">> Request now in ").red().text("FAILED").reset().text(" state. Reporting exception back to client. (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount()).println());
            builder.onAbort(event -> Console.colorize().blue().text("[RCON] ").reset().text(">> Request ").red().text("ABORTED").reset().text(" due to failure (Error: %s, Attempts: %d)", event.getException(), event.getAttemptCount()).println());
        }
        return builder.build();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">

    /**
     * <p>Check if reauthenticate flag is set</p>
     *
     * @return {@code true} if automatic re-authentication is enabled.
     */
    public boolean isReauthenticate() {
        return reauthenticate;
    }

    /**
     * <p>Getter for the field <code>registry</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.ChannelRegistry} object
     */
    public ChannelRegistry getChannelRegistry() {
        return registry;
    }

    /**
     * <p>Getter for the field <code>credentialsStore</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.CredentialsStore} object
     */
    public CredentialsStore getCredentialsStore() {
        return credentialsStore;
    }
    //</editor-fold>

    //<editor-fold desc="Channel Registration">

    /**
     * Register and initialize a newly created channel
     *
     * @param channel
     *         The newly created/acquired {@link Channel}
     *
     * @return {@code true} if the {@link Channel} has been registered otherwise {@code false} if the {@link Channel} has been registered already
     */
    private CompletableFuture<Channel> register(final Channel channel) {
        if (channel.eventLoop().inEventLoop()) {
            return CompletableFuture.completedFuture(registerEL(channel));
        } else {
            return CompletableFuture.completedFuture(channel).thenApplyAsync(this::registerEL, channel.eventLoop());
        }
    }

    private Channel registerEL(final Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        assert channel.eventLoop().inEventLoop();

        //return immediately if the channel is already registered
        if (registry.isRegistered(channel))
            return channel;

        if (!channel.isActive())
            throw new IllegalStateException("Failed to register and initialize channel (Reason: Channel is closed)");
        if (!(channel.remoteAddress() instanceof InetSocketAddress))
            throw new IllegalStateException("Not a valid remote address. Either null or not an InetSocketAddress instance");

        final SourceRconChannelContext context = SourceRconChannelContext.getContext(channel);
        final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        try {
            log.debug("{} AUTH => Registering channel '{}'", Netty.id(channel), channel);
            registry.register(channel);
            if (log.isDebugEnabled()) {
                log.debug("{} AUTH => Successfully registered channel (Total: {}, Address: {}, Authenticated: {})", context.id(), registry.getChannels(address).size(), channel.remoteAddress(), isAuthenticated(channel));
            }

            //perform additional initialization
            //Initialize context attribute
            context.properties().autoRelease(false); //Disable auto release of channel, we will release/close it manually (requires to explicitly call context.close())
            context.properties().authenticated(false);

            //Start inactivity check task once a channel has been registered
            startInactivityCheck();
        } catch (ChannelRegistrationException e) {
            throw new IllegalStateException(e);
        }

        return channel;
    }
    //</editor-fold>

    /**
     * Checks the {@link Channel}'s current authentication status
     *
     * @param channel
     *         The {@link Channel} to check
     *
     * @return {@code true} if the {@link Channel} has been authenticated by the remote server
     */
    private boolean isAuthenticated(Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        if (!registry.isRegistered(channel))
            return false;
        SourceRconChannelContext context = SourceRconChannelContext.getContext(channel);
        return context.properties().authenticated();
    }

    private void startInactivityCheck() {
        if (healthCheckStarted)
            return;
        synchronized (this) {
            jobScheduler.scheduleAtFixedRate(INACTIVITY_CHECK_TASK, 0, getOrDefault(SourceRconOptions.INACTIVE_CHECK_INTERVAL), TimeUnit.SECONDS);
            healthCheckStarted = true;
        }
    }

    /**
     * Checks if the specified {@link java.net.InetSocketAddress} has been previously authenticated.
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} to check
     *
     * @return {@code true} if the address has been previously authenticated.
     */
    public boolean isAuthenticated(InetSocketAddress address) {
        checkAddress(address);
        return credentialsStore.exists(address);
    }

    //<editor-fold desc="Overriden members">

    /**
     * Check if the registered {@link Credentials} is still valid for address.
     *
     * @param address
     *         The {@link SocketAddress} of the remote server
     *
     * @return {@code true} if valid
     */
    private boolean isValidAddress(SocketAddress address) {
        checkAddress(address);
        Credentials credentials = this.credentialsStore.get((InetSocketAddress) address);
        return credentials != null && credentials.isValid();
    }

    /** {@inheritDoc} */
    @Override
    protected NettyChannelFactory createChannelFactory() {
        final NettyContextChannelFactory channelFactory = getFactoryProvider().getContextualFactory(TransportType.TCP, getOptions(), new SourceRconChannelContextFactory(this));
        return new SourceRconChannelFactory(channelFactory);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Route the rcon request to it's appropriate handler
     */
    @Override
    public CompletableFuture<SourceRconResponse> send(final InetSocketAddress address, final SourceRconRequest request) {
        Objects.requireNonNull(address, "Address must not be null");
        Objects.requireNonNull(request, "Request must not be null");
        Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> method;
        if (request instanceof SourceRconAuthRequest) {
            method = this::sendAuthRequest;
        } else if (request instanceof SourceRconCmdRequest) {
            method = this::sendCmdRequest;
        } else {
            throw new IllegalStateException("Invalid rcon request");
        }
        final RequestContext details = new RequestContext(address, request, method);
        return failSafeExecute(details).handle(details::collectAndRelease);
    }

    private CompletableFuture<SourceRconChannelContext> sendAuthRequest(final SourceRconChannelContext context) {
        log.debug("{} AUTH => Sending AUTH request '{}'", context.id(), context.properties().request());
        return authenticator.authenticate(context).handle(Pair::new).thenCombine(CompletableFuture.completedFuture(context), this::wrapOnError);
    }

    private CompletableFuture<SourceRconChannelContext> sendCmdRequest(final SourceRconChannelContext context) {
        log.debug("{} AUTH => Sending COMMAND request '{}'", context.id(), context.properties().request());
        final SourceRconRequest request = context.properties().request();
        final InetSocketAddress address = context.properties().envelope().recipient();
        //Do we a valid credential registered for the address
        if (!isAuthenticated(address))
            throw new RconNotYetAuthException(String.format(SourceRconAuthenticator.NOT_YET_AUTH_MSG, address), request, address, SourceRconAuthReason.NOT_AUTHENTICATED);
        //Are the credentials still valid?
        if (!isValidAddress(address))
            throw new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), request, address, SourceRconAuthReason.INVALIDATED);
        log.debug("{} AUTH => Found existing valid credentials for address '{}' (Authenticated: {})", context.id(), address, context.properties().authenticated());
        //is the channel authenticated already?
        if (context.properties().authenticated())
            return context.send();
        if (!reauthenticate)
            throw new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), request, address, SourceRconAuthReason.INVALIDATED);

        log.debug("{} AUTH => Channel not yet authenticated. Attempting to authenticate the underlying connection with remote server", context.id());
        //Send the request over the transport. If the channel is not yet authenticated, an authentication request will be sent first.
        return authenticator.authenticate(context).handle(Pair::new).thenCombine(CompletableFuture.completedFuture(context), this::wrapOnError).thenCompose(SourceRconChannelContext::send);
    }

    private SourceRconChannelContext wrapOnError(Pair<SourceRconChannelContext, Throwable> response, SourceRconChannelContext context) {
        assert context != null;
        if (response.getSecond() != null) {
            Throwable cause = Errors.unwrap(response.getSecond());
            if (cause instanceof RconInvalidCredentialsException)
                invalidate(((RconInvalidCredentialsException) cause).getRemoteAddress());
            throw new MessengerException(cause, context);
        }
        assert response.getFirst() != null;
        assert context == response.getFirst();
        return context;
    }
    //</editor-fold>

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (!jobScheduler.isShutdown()) {
                log.debug("AUTH (CLOSE) => Requesting graceful shutdown");
                if (Concurrency.shutdown(jobScheduler)) {
                    log.debug("AUTH (CLOSE) => Job scheduler shutdown gracefully");
                } else {
                    log.debug("AUTH (CLOSE) => Failed to shutdown job scheduler");
                }
            }
        }
    }

    private CompletableFuture<SourceRconChannelContext> failSafeExecute(final RequestContext request) {
        return executor.getStageAsync(request::execute);
    }

    /**
     * <p>Getter for the field <code>statistics</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconMessenger.Statistics} object
     */
    @ApiStatus.Experimental
    @ApiStatus.Internal
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Cleanup inactive connections
     */
    public void cleanup() {
        this.jobScheduler.execute(new InactivityCheckTask(true));
    }

    /**
     * Invalidate all registered addresses and the connections associated with it
     */
    public void invalidate() {
        invalidate(false);
    }

    /**
     * Invalidates all registered addresses and all of it's associated connections.
     *
     * @param onlyConnections
     *         {@code true} if we should only invalidate the {@link io.netty.channel.Channel}'s for the specified address.
     */
    public void invalidate(boolean onlyConnections) {
        for (InetSocketAddress address : registry.getAddresses())
            invalidate(address, onlyConnections);
    }

    /**
     * Invalidate the credentials and/or the {@link io.netty.channel.Channel}'s associated with it
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} to invalidate
     * @param connectionsOnly
     *         {@code true} if we should only also invalidate the connections associated with the specified address. Credentials will remain valid.
     */
    public void invalidate(InetSocketAddress address, boolean connectionsOnly) {
        checkAddress(address);
        log.debug("AUTH => Invalidating address '{}'", address);
        if (!connectionsOnly) {
            Credentials credentials = credentialsStore.get(address);
            if (credentials != null)
                credentials.invalidate();
        }
        //also invalidate all the other Channels associated with the address
        for (Channel channel : registry.getChannels(address)) {
            SourceRconChannelContext context = SourceRconChannelContext.getContext(channel);
            context.properties().authenticated(false);
            log.debug("{} AUTH => Invalidated channel '{}'. Marked for re-authentication", context.id(), channel);
        }
    }

    private static void checkAddress(SocketAddress address) {
        if (!(address instanceof InetSocketAddress))
            throw new IllegalStateException("Address is not an IneteSocketAddress instance");
    }

    /**
     * Invalidate both credentials and all it's {@link io.netty.channel.Channel}'s associated with it
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     */
    public void invalidate(InetSocketAddress address) {
        invalidate(address, false);
    }

    //<editor-fold desc="Statistics">
    public static class Metadata {

        private final WeakReference<Channel> channelRef;

        public Metadata(Channel channel) {
            this.channelRef = new WeakReference<>(channel);
        }

        public long getLastAcquiredDuration() {
            long lastAcquireMillis = Metadata.Stats.LAST_ACQUIRE_MILLIS.value(channel());
            return getAcquireCount() > 0 ? Duration.ofMillis(System.currentTimeMillis() - lastAcquireMillis).getSeconds() : -1;
        }

        private Channel channel() {
            Channel channel = this.channelRef.get();
            if (channel == null)
                throw new IllegalStateException("Channel no longer available");
            return channel;
        }

        public int getAcquireCount() {
            Object acquireCount = channel().attr(Metadata.Stats.ACQUIRE_COUNT.key()).get();
            if (acquireCount == null)
                return 0;
            return (int) acquireCount;
        }

        public long getLastAcquiredDurationMillis() {
            long lastAcquireMillis = Metadata.Stats.LAST_ACQUIRE_MILLIS.value(channel());
            return getAcquireCount() > 0 ? System.currentTimeMillis() - lastAcquireMillis : -1;
        }

        @SuppressWarnings("UnusedReturnValue")
        public enum Stats {
            ACQUIRE_COUNT(Integer.class, "channelStatsAcquireCount"),
            LAST_ACQUIRE_MILLIS(Long.class, "channelStatsLastAcquiredDurationMillis");

            private final Class<?> type;

            private final AttributeKey<?> key;

            Stats(Class<?> type, String key) {
                this.type = type;
                this.key = AttributeKey.valueOf(type, key);
            }

            public <V> V value(Channel channel) {
                Objects.requireNonNull(channel, "Channel must not be null");
                //noinspection unchecked
                return (V) channel.attr(key()).get();
            }

            public <V> AttributeKey<V> key() {
                //noinspection unchecked
                return (AttributeKey<V>) key;
            }

            public <V> void value(Channel channel, V value) {
                channel.attr(key()).set(value);
            }

            public boolean exists(Channel channel) {
                return channel.hasAttr(key()) && channel.attr(key()).get() != null;
            }

            public <V extends Number> V increment(Channel channel) {
                if (!(Number.class.isAssignableFrom(type())))
                    throw new IllegalStateException("Cannot incremement a stat that is not a number type");
                //noinspection unchecked
                return (V) Netty.incrementAttrNumber(channel, this.key());
            }

            public Class<?> type() {
                return type;
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Background Tasks">
    private class InactivityCheckTask implements Runnable {

        private final boolean force;

        private InactivityCheckTask() {
            this(false);
        }

        private InactivityCheckTask(boolean force) {
            this.force = force;
        }

        @Override
        public void run() {
            final int closeDuration = getOrDefault(SourceRconOptions.CLOSE_INACTIVE_CHANNELS);
            if (closeDuration < 0)
                return;
            final Set<Map.Entry<InetSocketAddress, Channel>> entries = registry.getEntries();
            for (Map.Entry<InetSocketAddress, Channel> entry : entries) {
                InetSocketAddress address = entry.getKey();
                Channel channel = entry.getValue();
                int cRemaining = registry.getCount(address);
                int ctr = 0;

                //ignore channels that are currently in-use
                if (NettyChannelPool.isPooled(channel))
                    continue;

                Metadata metadata = statistics.getMetadata(channel);
                long lastAcquiredDuration = metadata.getLastAcquiredDuration();
                int remaining = registry.getChannels(address).size();
                if (remaining == 1 || cRemaining == 1)
                    continue;

                //close channels having:
                //- 0 acquire count
                //- Last acquired duration >= closeDuration
                if (force || (metadata.getAcquireCount() == 0 || (lastAcquiredDuration >= 0 && lastAcquiredDuration >= closeDuration))) {
                    log.debug("AUTH (CLEANUP) => ({}) Closing unused channel: ({}) (Acquire Count: {}, Last acquired: {}, Registered: {}, Remaining: {})", ++ctr, channel, metadata.getAcquireCount(), metadata.getLastAcquiredDuration(), registry.isRegistered(channel), cRemaining);
                    final String id = channel.id().asShortText();
                    Netty.close(channel).thenAcceptAsync(unused -> log.debug("AUTH (CLEANUP) => Closed unused channel: {}", id), channel.eventLoop());
                }
            }
        }
    }

    public class Statistics {

        private final ChannelFutureListener REMOVE_ON_CLOSE = future -> {
            log.debug("{} STATISTICS (CLEANUP) => Removing channel '{}'", Netty.id(future.channel()), future.channel());
            remove(future.channel());
        };

        private Channel record(Channel channel, Throwable error) {
            if (error != null) {
                if (error instanceof CompletionException)
                    throw (CompletionException) error;
                else
                    throw new CompletionException(error);
            } else {
                assert channel != null;
                //reset attributes if channel was closed
                if (!Metadata.Stats.ACQUIRE_COUNT.exists(channel)) {
                    log.debug("{} STATISTICS => Initializing stats for channel '{}'", Netty.id(channel), channel);
                    //remove entry on close
                    channel.closeFuture().addListener(REMOVE_ON_CLOSE);
                }
                Metadata.Stats.ACQUIRE_COUNT.increment(channel);
                Metadata.Stats.LAST_ACQUIRE_MILLIS.value(channel, System.currentTimeMillis());
            }
            return channel;
        }

        public void print(Consumer<String> output) {

            final String LINE = "\033[0;36m" + StringUtils.repeat("=", 200) + "\033[0m";
            print(output, LINE);
            print(output, "Channel Statistics");
            print(output, LINE);
            print(output, "Connection pooling enabled: %s", getOrDefault(GeneralOptions.CONNECTION_POOLING));
            print(output, "Max Pooled Connections: %d", getOrDefault(GeneralOptions.POOL_MAX_CONNECTIONS));
            print(output, "Max Core Pool Size: %d", Concurrency.getCorePoolSize(getExecutor()));
            print(output, "Max Pending Acquires: %d", getOrDefault(GeneralOptions.POOL_ACQUIRE_MAX));
            print(output, "Tasks in queue: %d", Platform.getDefaultQueue().size());
            EventLoopGroup eventLoopGroup = getExecutor();
            print(output, "Executor Service: %s", eventLoopGroup);

            print(output, LINE);
            print(output, "\033[0;33mEvent Loop Group: (Group: %s)\033[0m", eventLoopGroup);
            print(output, LINE);
            int ctr = 0;
            for (EventExecutor ex : eventLoopGroup) {
                SingleThreadEventLoop el = (SingleThreadEventLoop) ex;
                print(output, "\033[0;33m%02d)\033[0m \033[0;36m%s-%-15d\033[0m \033[0;34m[%s]\033[0m (\033[0;37mPending Tasks:\033[0m \033[0;36m%d\033[0m, \033[0;37mThread Id:\033[0m \033[0;36m%d\033[0m)", ++ctr, ex.getClass().getSimpleName(), el.hashCode(), el.threadProperties().name(), el.pendingTasks(), el.threadProperties().id());
            }

            print(output, LINE);
            final List<InetSocketAddress> addressList = new ArrayList<>(registry.getAddresses());
            for (int i = 0, distinctAddressesSize = addressList.size(); i < distinctAddressesSize; i++) {
                final InetSocketAddress address = addressList.get(i);
                List<Channel> channels = new ArrayList<>(registry.getChannels(address));
                int totalAcquireCount = getTotalAcquireCount(channels);
                print(output, "%d) Address: %s (Successful Acquires: %d, Active Channels: %d, Authenticated: %s)", i + 1, address, totalAcquireCount, channels.size(), isAuthenticated(address) ? "YES" : "NO");
                for (int j = 0; j < channels.size(); j++) {
                    Channel channel = channels.get(j);
                    Metadata metadata = getMetadata(channel);
                    print(output, "\t%d) Channel: %s, Acquire: %d, Active: %s, Authenticated: %s, Acquired: %s, Last Acquired: %s, Thread: %s", j + 1, channel, metadata.getAcquireCount(), channel.isActive(), isAuthenticated(channel), NettyChannelPool.isPooled(channel) ? "YES" : "NO", Time.getTimeDesc(metadata.getLastAcquiredDurationMillis(), true), Netty.getThreadName(channel));
                }
            }
            print(output, LINE);
        }

        private void print(Consumer<String> out, String msg, Object... args) {
            out.accept(String.format(msg, args));
        }

        private int getTotalAcquireCount(List<Channel> channels) {
            return channels.stream().filter(Metadata.Stats.ACQUIRE_COUNT::exists).mapToInt(Metadata.Stats.ACQUIRE_COUNT::value).sum();
        }

        //lookup by channel id since PooledChannel and Channel equals are not symmetrical
        private Metadata getMetadata(Channel channel) {
            return new Metadata(channel);
        }

        private void remove(Channel channel) {
            for (Metadata.Stats stat : Metadata.Stats.values()) {
                log.debug("{} STATISTICS => Clearing stats for channel '{}'", Netty.id(channel), channel);
                channel.attr(stat.key()).set(null);
            }
        }
    }
    //</editor-fold>

    private class RequestContext {

        private final InetSocketAddress address;

        private final SourceRconRequest request;

        private WeakReference<SourceRconChannelContext> contextRef;

        private final Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> method;

        private RequestContext(InetSocketAddress address, SourceRconRequest request, Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> method) {
            this.address = address;
            this.request = request;
            this.method = method;
        }

        private SourceRconResponse collectAndRelease(SourceRconChannelContext context, Throwable error) {
            try {
                if (error != null) {
                    Throwable cause = Errors.unwrap(error);
                    //we wrap every error with RconException
                    if (cause instanceof RconException)
                        throw (RconException) cause;
                    else
                        throw new RconException(cause, request, address);
                }
                assert context != null && context == getContext();
                return context.properties().response();
            } finally {
                //note: we use getContext() instead of context to ensure we always have the reference for context.
                //its possible that the context variable would be null in certain occasions. Even though we have not confirmed this yet, better to be safe than sorry
                SourceRconChannelContext ctx = getContext(); //if this returns null, then at least we know that the context was not successfully acquired, so we do nothing.
                if (ctx != null) {
                    ctx.close();
                } else if (error instanceof MessengerException) {
                    MessengerException ex = (MessengerException) error;
                    if (ex.getContext() != null)
                        ex.getContext().close();
                }
            }
        }

        private CompletableFuture<SourceRconChannelContext> execute(ExecutionContext<SourceRconChannelContext> context) throws Throwable {
            if (Properties.isVerbose()) {
                Console.colorize().blue().text("[RCON] ")
                       .reset().text("[%s] ", Thread.currentThread().getName())
                       .reset().text("[%02d] Sending request ", context.getAttemptCount())
                       .cyan().text("'%s'", request).reset()
                       .text(" to address ")
                       .cyan().text("'%s'", address).reset()
                       .text(" (Last Error: %s)", context.getLastException())
                       .println();
            }
            Credentials credentials = credentialsStore.get(address);
            log.debug("AUTH => Sending RCON Request '{}' to address '{}' (Valid Credentials: {}, Attempts: {}, Cancelled: {}, Last Failure: {}, Last Result: {})", request, address, credentials != null && credentials.isValid(), context.getAttemptCount(), context.isCancelled(), context.getLastException(), context.getLastResult());
            return acquire().thenCompose(method);
        }

        /**
         * Acquire a new/existing {@link SourceRconChannelContext}
         *
         * @return A {@link CompletableFuture} that will be notified when the channel has been successfully acquired from the {@link FailsafeChannelFactory}
         */
        private CompletableFuture<SourceRconChannelContext> acquire() {
            CompletableFuture<Channel> channelFuture = channelFactory.create(address);
            channelFuture.thenAccept(this::updateContext);
            return channelFuture
                    .thenCompose(SourceRconMessenger.this::register)
                    .handle(statistics::record)
                    .thenApply(SourceRconChannelContext::getContext)
                    .thenCombine(CompletableFuture.completedFuture(request), this::initializeContext);
        }

        private void updateContext(Channel channel) {
            this.contextRef = new WeakReference<>(SourceRconChannelContext.getContext(channel));
        }

        private SourceRconChannelContext getContext() {
            if (this.contextRef == null)
                return null;
            return this.contextRef.get();
        }

        private SourceRconChannelContext initializeContext(SourceRconChannelContext context, SourceRconRequest request) {
            log.debug("{} AUTH => Attaching request '{}' to context", context.id(), request);
            context.properties().request(request);
            return context;
        }

        private InetSocketAddress getAddress() {
            return address;
        }

        private SourceRconRequest getRequest() {
            return request;
        }

    }
}
