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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
import com.ibasco.agql.core.transport.NettyChannelFactory;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.exceptions.ChannelRegistrationException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconInvalidCredentialsException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconMaxLoginAttemptsException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.message.*;
import dev.failsafe.ExecutionContext;
import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionCompletedEvent;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * <p>An proxy that ensures that requests are sent via an authenticated {@link Channel}
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Experimental
public final class SourceRconAuthProxy implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceRconAuthProxy.class);

    private static final RetryPolicy<Parcel> AUTH_CHANNEL_POLICY = RetryPolicy.<Parcel>builder()
                                                                              .withMaxRetries(1)
                                                                              .abortOn(ConnectTimeoutException.class)
                                                                              .build();

    private static final RetryPolicy<SourceRconResponse> AUTH_RETRY_POLICY = RetryPolicy.<SourceRconResponse>builder()
                                                                                        .withMaxRetries(1)
                                                                                        .abortOn(ConnectTimeoutException.class)
                                                                                        .build();

    private static final RetryPolicy<SourceRconResponse> CMD_RETRY_POLICY = RetryPolicy.<SourceRconResponse>builder().build();

    private static final RetryPolicy<Channel> ACQUIRE_RETRY_POLICY = RetryPolicy.<Channel>builder().handle(ConnectTimeoutException.class, ConnectException.class)
                                                                                .abortIf(c -> c.eventLoop().isShutdown() || c.eventLoop().isShuttingDown())
                                                                                .abortOn(RejectedExecutionException.class)
                                                                                .onRetry(event -> log.debug("AUTH (ACQUIRE) => Failed to enqueue channel. Retrying (Attempts: {}, Last Failure: {})", event.getAttemptCount(), event.getLastFailure() != null ? event.getLastFailure().getClass().getSimpleName() : "N/A"))
                                                                                .withMaxAttempts(3).build();

    private final Statistics statistics = new Statistics();

    private final SourceRconMessenger messenger;

    private final ScheduledExecutorService scheduler;

    private final FailsafeExecutor<Channel> acquireExecutor;

    private final CredentialsManager credentialsManager;

    private volatile boolean healthCheckStarted;

    private final boolean reauthenticate;

    private final RconHelper helper = new RconHelper();

    private final ThreadLocal<FailsafeExecutor<Parcel>> authExecutors = ThreadLocal.withInitial(() -> Failsafe.with(AUTH_CHANNEL_POLICY));

    private final InactivityCheckTask INACTIVITY_CHECK_TASK = new InactivityCheckTask();

    private final ChannelRegistry registry = new DefaultChannelRegistry();

    private static final ChannelFutureListener CLEANUP_ON_CLOSE = future -> {
        Channel channel = future.channel();
        log.debug("{} AUTH (CLEANUP) => Channel closed. Performing cleanup operation", NettyUtil.id(channel));
        channel.attr(SourceRcon.AUTHENTICATED).set(null);
        NettyChannelPool.tryRelease(channel);
    };

    /**
     * Create a new authentication proxy
     *
     * @param messenger
     *         The {@link SourceRconMessenger} that is used to relay the messages sent
     */
    public SourceRconAuthProxy(SourceRconMessenger messenger, CredentialsManager credentialsManager) {
        this.messenger = Objects.requireNonNull(messenger, "Messenger cannot be null");
        if (credentialsManager == null)
            credentialsManager = new DefaultCredentialsManager();
        EventLoopGroup group = messenger.getExecutor();
        this.credentialsManager = credentialsManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(getClass()));
        this.acquireExecutor = Failsafe.with(ACQUIRE_RETRY_POLICY).with(group.next());
        this.reauthenticate = messenger.getOrDefault(SourceRconOptions.REAUTHENTICATE);
    }

    /**
     * Register a newly created channel and ensures that it is authenticated by the server
     *
     * @param channel
     *         The newly created/acquired {@link Channel}
     *
     * @return {@code true} if the {@link Channel} has been registered otherwise {@code false} if the {@link Channel} has been registered already
     */
    private CompletableFuture<Channel> register(final Channel channel) {
        if (channel.eventLoop().inEventLoop()) {
            return CompletableFuture.completedFuture(register0(channel));
        } else {
            return CompletableFuture.completedFuture(channel).thenApplyAsync(this::register0, channel.eventLoop());
        }
    }

    private Channel register0(final Channel channel) {
        assert channel.eventLoop().inEventLoop();
        Objects.requireNonNull(channel, "Channel must not be null");
        if (registry.isRegistered(channel))
            return channel;
        if (!channel.isActive())
            throw new IllegalStateException("Can't register a channel that is inactive");
        if (!(channel.remoteAddress() instanceof InetSocketAddress))
            throw new IllegalStateException("Not a valid remote address. Either null or not an InetSocketAddress instance");
        final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        try {
            log.debug("{} AUTH => Registering channel '{}'", NettyUtil.id(channel), channel);
            registry.register(channel);
            log.debug("{} AUTH => Successfully registered channel (Total: {}, Address: {}, Authenticated: {})", NettyUtil.id(channel), registry.getChannels(address).size(), channel.remoteAddress(), isAuthenticated(channel));

            //perform additional initialization
            //perform cleanup operation on close
            channel.closeFuture().addListener(CLEANUP_ON_CLOSE);
            //Disable auto release of channel, we will release/close it manually
            channel.attr(NettyChannelAttributes.AUTO_RELEASE).set(false);
            //Initialize channel attribute
            channel.attr(SourceRcon.AUTHENTICATED).set(false);

            //Start inactivity check task once a channel has been registered
            startInactivityCheck();
        } catch (ChannelRegistrationException e) {
            throw new IllegalStateException(e);
        }
        return channel;
    }

    /**
     * Mark the address as authenticated and save it's credentials
     *
     * @param channel
     *         The {@link Channel} to mark as authenticated
     * @param request
     *         The {@link SourceRconAuthRequest} that was used to authenticate the {@link Channel}
     */
    private void markAuthenticated(final Channel channel, SourceRconAuthRequest request) {
        if (!registry.isRegistered(channel))
            throw new IllegalStateException(String.format("Channel '%s' is not registered (Request: %s)", channel, request));
        helper.validate(channel);
        final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        //we might have an existing entry for this address, if so, then we overwrite it
        if (request == null) {
            Envelope<AbstractRequest> envelope = channel.attr(NettyChannelAttributes.REQUEST).get();
            request = (SourceRconAuthRequest) envelope.content();
        }
        assert request != null;
        credentialsManager.add(address, request.getPassword());
    }

    /**
     * Checks if the specified address has been previously authenticated.
     *
     * @param address
     *         The {@link InetSocketAddress} to check
     *
     * @return {@code true} if the address has been previously authenticated.
     */
    public boolean isAuthenticated(SocketAddress address) {
        checkAddress(address);
        return credentialsManager.exists((InetSocketAddress) address);
    }

    /**
     * Checks the {@link Channel}'s current authentication status
     *
     * @param channel
     *         The {@link Channel} to check
     *
     * @return {@code true} if the {@link Channel} has been authenticated by the remote server
     */
    public boolean isAuthenticated(Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        if (!registry.isRegistered(channel))
            return false;
        if (!channel.hasAttr(SourceRcon.AUTHENTICATED))
            return false;
        return channel.attr(SourceRcon.AUTHENTICATED).get();
    }

    public boolean isValid(SocketAddress address) {
        checkAddress(address);
        Credentials credentials = this.credentialsManager.get((InetSocketAddress) address);
        return credentials != null && credentials.isValid();
    }

    private final RetryPolicy<SourceRconResponse> COMMAND_RETRY_POLICY = RetryPolicy
            .<SourceRconResponse>builder()
            .handle(ChannelClosedException.class)
            .onSuccess(new EventListener<ExecutionCompletedEvent<SourceRconResponse>>() {
                @Override
                public void accept(ExecutionCompletedEvent<SourceRconResponse> event) throws Throwable {

                }
            })
            .onRetry(event -> log.info("ON REEETRY"))
            .onFailure(event -> log.info("ON FAILUREE"))
            .withMaxAttempts(3)
            .build();

    /**
     * Route the rcon request to it's appropriate handler
     *
     * @param address
     *         The destination address
     * @param request
     *         The request to be sent to the server
     *
     * @return A {@link CompletableFuture} which is notified once a response has been received from the server
     */
    public CompletableFuture<SourceRconResponse> send(final InetSocketAddress address, final SourceRconRequest request) {
        Objects.requireNonNull(address, "Address must not be null");
        Objects.requireNonNull(request, "Request must not be null");
        CompletableFuture<Parcel> parcelFuture = createParcel(address, request).thenApply(helper::validate);
        return route(request, parcelFuture).handle(this::release);
    }

    private CompletableFuture<Parcel> route(final SourceRconRequest request, CompletableFuture<Parcel> parcelFuture) {
        if (parcelFuture.isCompletedExceptionally() || parcelFuture.isCancelled())
            return parcelFuture;
        if (request instanceof SourceRconAuthRequest) {
            parcelFuture = parcelFuture.thenCompose(this::sendAuthRequest);
        } else if (request instanceof SourceRconCmdRequest) {
            parcelFuture = parcelFuture.thenCompose(this::sendCmdRequest);
        } else
            throw new IllegalStateException("Invalid rcon request");
        return parcelFuture;
    }

    //handle client auth requsets
    private CompletableFuture<Parcel> sendAuthRequest(final Parcel parcel) {
        final SourceRconAuthRequest request = (SourceRconAuthRequest) parcel.request();
        //if no password was supplied, then we check
        //if we have credentials registered for this address
        if (request.getPassword() == null) {
            Credentials credentials = credentialsManager.get(parcel.address());
            if (credentials == null)
                return ConcurrentUtil.failedFuture(new RconNotYetAuthException("Address '" + parcel.address() + "' not yet authenticated by the remote server. Please authenticate with a provided passphrase", SourceRconAuthReason.NOT_AUTHENTICATED, parcel.address()));
            //make sure the credentials are still valid
            if (!credentials.isValid())
                return ConcurrentUtil.failedFuture(new RconNotYetAuthException("The credentials for address '" + parcel.address() + "' has been invalidated. Please re-authenticate with the server", SourceRconAuthReason.REAUTHENTICATE, parcel.address()));
            //Update credentials
            request.setPassword(credentials.getPassphrase());
        }
        return sendParcel(parcel).thenApply(this::authResponseHandler);
    }

    private CompletableFuture<Parcel> sendCmdRequest(final Parcel parcel) {
        //Is the address authenticated?
        if (!isAuthenticated(parcel.address())) {
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not yet authenticated. Make sure to call authenticate().", parcel.address()), SourceRconAuthReason.NOT_AUTHENTICATED, parcel.address()));
        }
        //Are the credentials still valid?
        if (!credentialsManager.isValid(parcel.address()))
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("The credentials for address '%s' is no longer valid. Re-authenticate with the server", parcel.address()), SourceRconAuthReason.REAUTHENTICATE, parcel.address()));
        //is the channel authenticated already?
        if (isAuthenticated(parcel.channel()))
            return sendParcel(parcel);
        if (!reauthenticate)
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("The connection for address '%s' needs to be authenticated by the server", parcel.address()), SourceRconAuthReason.REAUTHENTICATE, parcel.address()));

        log.debug("{} AUTH => Channel not yet authenticated. Attempting to authenticate with server", NettyUtil.id(parcel.channel()));
        //Send the request over the transport. If the channel is not yet authenticated, an authentication request will be sent first.
        return tryAuthenticate(parcel).thenCompose(this::sendParcel);
    }

    private Parcel authResponseHandler(Parcel parcel) {
        final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
        final SourceRconAuthRequest request = (SourceRconAuthRequest) parcel.envelope().content();
        final Channel channel = parcel.channel();

        /*if (parcel.hasError()) {
            Throwable cause = ConcurrentUtil.unwrap(parcel.error());
            if (cause instanceof ChannelClosedException) {
                //if the server forcibly closes the connection, without sending a response, then it can mean that:
                //then it could meean that:
                // - the passphrase is no longer valid
                // - the maximum login attempts have been reached
                // - the server dropped the connection due to physical connection or other reasons (e.g. firewall rules)
                final SourceRconAuthResponse authResponse = new SourceRconAuthResponse(
                        request.getRequestId(),
                        false,
                        "Connection was closed by remote server",
                        false,
                        cause, SourceRconAuthReason.CONNECTION_DROPPED);
                //clear error
                parcel.clearError();
                authResponse.setAddress(address);
                return parcel.response(authResponse);
            }
            cause.printStackTrace(System.err);
            throw new CompletionException(cause);
        }*/

        if (!parcel.hasResponse())
            throw new IllegalStateException("Parcel does not contain a response");

        //make sure we have an auth response
        if (!(parcel.response() instanceof SourceRconAuthResponse))
            throw new IllegalStateException("Request/Response Mismatch. Expected SourceRconAuthResponse (Actual: " + parcel.response() + ")");

        final SourceRconAuthResponse response = (SourceRconAuthResponse) parcel.response();
        if (response.isSuccess()) {
            if (response.isAuthenticated()) {
                //allow channel to be re-authenticated
                markAuthenticated(channel, request);
                log.debug("{} AUTH => Successfully authenticated address '{}' (Request: {})", NettyUtil.id(channel), address, request);
            } else {
                log.debug("{} AUTH => Authentication failed due to bad credentials. Invalidating credentials", NettyUtil.id(channel));
                invalidate(address);
            }
            return parcel;
        } else {
            log.debug("{} AUTH => Failed to authenticate channel", NettyUtil.id(channel));
            throw new CompletionException(parcel.response().getError());
        }
    }

    /**
     * Send parcel to the transport
     */
    private CompletableFuture<Parcel> sendParcel(final Parcel parcel) {
        assert parcel.channel() != null;
        assert parcel.channel().remoteAddress() != null;
        assert parcel.channel().remoteAddress().equals(parcel.envelope().recipient());
        log.debug("Sending request: {} (Channel Id: {}, Thread: {}, Request Id: {})", parcel.request().getClass().getSimpleName(), NettyUtil.id(parcel.channel()), NettyUtil.getThreadName(parcel.channel()), parcel.requestId());

        if (parcel.channel().eventLoop().inEventLoop()) {
            return messenger.send(parcel.envelope(), parcel.channel()).thenApply(parcel::response);
        } else {
            return CompletableFuture.completedFuture(parcel).thenComposeAsync(p -> messenger.send(p.envelope(), p.channel()).thenApply(p::response), parcel.channel().eventLoop());
        }
    }

    /**
     * Release the parcel and return the response. If the parcel contains an error, a {@link CompletionException} will be thrown.
     *
     * @param parcel
     *         The {@link Parcel} to release
     * @param error
     *         The error that occured that made the future transition to a failed state.
     *
     * @return The {@link SourceRconResponse} if no error.
     */
    private SourceRconResponse release(Parcel parcel, Throwable error) {
        if (error != null)
            throw new CompletionException(ConcurrentUtil.unwrap(error));
        assert parcel != null;
        try {
            assert parcel.hasResponse();
            return parcel.response();
        } finally {
            log.debug("RESPONSE RECEIVED. RELEASING: {}", parcel.response());
            parcel.release();
        }
    }

    //1) acquire a new channel
    //2) wrap channeel and enqueue into a parcel
    private CompletableFuture<Parcel> createParcel(final InetSocketAddress address, SourceRconRequest request) {
        final Envelope<SourceRconRequest> envelope = messenger.newEnvelope(address, request);
        return acquire(envelope).thenCombine(CompletableFuture.completedFuture(envelope), Parcel::new);
    }

    private CompletableFuture<Parcel> tryAuthenticate(Parcel parcel) {
        final Channel channel = parcel.channel();
        assert channel.eventLoop().inEventLoop();
        final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
        //make sure that the address has been authenticated, and we have credentials preset
        if (!isAuthenticated(address) || !isValid(address)) {
            log.warn("Address not authenticated or valid: {} (Authenticated: {}, Valid: {})", address, isAuthenticated(address), isValid(address));
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not authenticated or not valid", address), SourceRconAuthReason.NOT_AUTHENTICATED, parcel.address()));
        }
        //if the channel is authenticated, return immediately
        if (isAuthenticated(parcel.channel()))
            return CompletableFuture.completedFuture(parcel);
        assert channel.eventLoop().inEventLoop();
        //at this point, we need to authenticate the channel/connection with the remote server
        return authExecutors.get().with(channel.eventLoop()).getStageAsync(new AuthenticateParcelSupplier(parcel));
    }

    /**
     * Acquire a new/existing {@link Channel}
     *
     * @param envelope
     *         The {@link Envelope} containing the request and promise details
     *
     * @return A {@link CompletableFuture} that will be notified when the channel has been successfully acquired from the {@link NettyChannelFactory}
     */
    private CompletableFuture<Channel> acquire(final Envelope<SourceRconRequest> envelope) {
        log.debug("AUTH => Acquiring channel for envelope '{}'", envelope);
        EventLoop el = messenger.getExecutor().next();
        return acquireExecutor.with(el).getStageAsync(context -> messenger.getChannelFactory().create(envelope, el)).thenComposeAsync(this::register, el).handleAsync(statistics, el);
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Cleanup inactive connctions
     */
    public void cleanup() {
        this.scheduler.execute(new InactivityCheckTask(true));
    }

    /**
     * Invalidate all addresses and the connections associated with it
     */
    public void invalidate() {
        invalidate(false);
    }

    /**
     * Invalidates all registered addresses and all of it's associated connections.
     *
     * @param onlyConnections
     *         {@code true} if we should only invalidate the {@link Channel}'s for the specified address.
     */
    public void invalidate(boolean onlyConnections) {
        for (InetSocketAddress address : registry.getAddresses())
            invalidate(address, onlyConnections);
    }

    /**
     * Invalidate both credentials and it's {@link Channel}'s associated with it
     */
    public void invalidate(InetSocketAddress address) {
        invalidate(address, false);
    }

    /**
     * Invalidate the credentials and/or the {@link Channel}'s associated with it
     *
     * @param address
     *         The {@link InetSocketAddress} to invalidate
     * @param connectionsOnly
     *         {@code true} if we should only also invalidate the connections associated with the specified address. Credentials will remain valid.
     */
    public void invalidate(InetSocketAddress address, boolean connectionsOnly) {
        checkAddress(address);
        log.debug("AUTH => Invalidating address '{}'", address);
        if (!connectionsOnly)
            credentialsManager.invalidate(address);
        for (Channel channel : registry.getChannels(address)) {
            Objects.requireNonNull(channel);
            channel.attr(SourceRcon.AUTHENTICATED).set(false);
            log.debug("{} AUTH => Invalidated channel. Marked for re-authentication", NettyUtil.id(channel));
        }
    }

    @Override
    public void close() throws IOException {
        if (scheduler.isShutdown())
            return;
        log.debug("PROXY (CLOSE) => Requesting graceful shutdown");
        if (ConcurrentUtil.shutdown(scheduler)) {
            log.debug("PROXY (CLOSE) => Rcon auth proxy shutdown gracefully");
        } else {
            log.debug("PROXY (CLOSE) => Rcon auth proxy shutdown failed");
        }
    }

    private static void checkAddress(SocketAddress address) {
        if (!(address instanceof InetSocketAddress))
            throw new IllegalStateException("Address is not an IneteSocketAddress instance");
    }

    private void startInactivityCheck() {
        if (healthCheckStarted)
            return;
        scheduler.scheduleAtFixedRate(INACTIVITY_CHECK_TASK, 0, messenger.getOrDefault(SourceRconOptions.INACTIVE_CHECK_INTERVAL), TimeUnit.SECONDS);
        healthCheckStarted = true;
    }

    //<editor-fold desc="Async Tasks">
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
            final int closeDuration = messenger.getOrDefault(SourceRconOptions.CLOSE_INACTIVE_CHANNELS);
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
                    log.info("AUTH (CLEANUP) => ({}) Closing unused channel: ({}) (Acquire Count: {}, Last acquired: {}, Registered: {}, Remaining: {})", ++ctr, channel, metadata.getAcquireCount(), metadata.getLastAcquiredDuration(), registry.isRegistered(channel), cRemaining);
                    final String id = channel.id().asShortText();
                    NettyUtil.close(channel).thenAcceptAsync(unused -> log.debug("AUTH (CLEANUP) => Closed unused channel: {}", id), channel.eventLoop());
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Statistics">
    public static class Metadata {

        public enum Stats {
            ACQUIRE_COUNT(Integer.class, "channelStatsAcquireCount"),
            LAST_ACQUIRE_MILLIS(Long.class, "channelStatsLastAcquiredDurationMillis");

            private final Class<?> type;

            private final AttributeKey<?> key;

            Stats(Class<?> type, String key) {
                this.type = type;
                this.key = AttributeKey.valueOf(type, key);
            }

            public Class<?> type() {
                return type;
            }

            public <V> AttributeKey<V> key() {
                //noinspection unchecked
                return (AttributeKey<V>) key;
            }

            public <V> V value(Channel channel) {
                Objects.requireNonNull(channel, "Channel must not be null");
                //noinspection unchecked
                return (V) channel.attr(key()).get();
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
                return (V) NettyUtil.incrementAttrNumber(channel, this.key());
            }
        }

        private final WeakReference<Channel> channelRef;

        public Metadata(Channel channel) {
            this.channelRef = new WeakReference<>(channel);
        }

        private Channel channel() {
            Channel channel = this.channelRef.get();
            if (channel == null)
                throw new IllegalStateException("Channel no longer available");
            return channel;
        }

        public int getAcquireCount() {
            return (int) channel().attr(Stats.ACQUIRE_COUNT.key()).get();
        }

        public long getLastAcquiredDuration() {
            long lastAcquireMillis = Stats.LAST_ACQUIRE_MILLIS.value(channel());
            return getAcquireCount() > 0 ? Duration.ofMillis(System.currentTimeMillis() - lastAcquireMillis).getSeconds() : -1;
        }

        public long getLastAcquiredDurationMillis() {
            long lastAcquireMillis = Stats.LAST_ACQUIRE_MILLIS.value(channel());
            return getAcquireCount() > 0 ? System.currentTimeMillis() - lastAcquireMillis : -1;
        }
    }

    public class Statistics implements BiFunction<Channel, Throwable, Channel> {

        private final ChannelFutureListener REMOVE_ON_CLOSE = future -> {
            log.debug("STATISTICS (CLEANUP) => Removing channel '{}'", future.channel());
            remove(future.channel());
        };

        //lookup by channel id since PooledChannel and Channel equals are not symmetrical
        private Metadata getMetadata(Channel channel) {
            return new Metadata(channel);
        }

        @Override
        public Channel apply(Channel channel, Throwable error) {
            if (error != null) {
                if (error instanceof CompletionException)
                    throw (CompletionException) error;
                else
                    throw new CompletionException(error);
            } else {
                assert channel != null;
                //reset attributes if channel was closed
                if (!Metadata.Stats.ACQUIRE_COUNT.exists(channel)) {
                    log.debug("{} STATISTICS => Initializing stats for channel '{}'", NettyUtil.id(channel), channel);
                    //remove entry on close
                    channel.closeFuture().addListener(REMOVE_ON_CLOSE);
                }
                Metadata.Stats.ACQUIRE_COUNT.increment(channel);
                Metadata.Stats.LAST_ACQUIRE_MILLIS.value(channel, System.currentTimeMillis());
            }
            return channel;
        }

        public void print() {
            print(System.out::println);
        }

        public void print(Consumer<String> output) {

            final String LINE = StringUtils.repeat("=", 200);
            print(output, LINE);
            print(output, "Channel Statistics");
            print(output, LINE);
            print(output, "Custom executor: %s", messenger.get(TransportOptions.THREAD_POOL_EXECUTOR));
            print(output, "Connection pooling enabled: %s", messenger.getOrDefault(TransportOptions.CONNECTION_POOLING));
            print(output, "Max Pooled Connections: %d", messenger.getOrDefault(TransportOptions.POOL_MAX_CONNECTIONS));
            print(output, "Max Core Pool Size: %d", getCorePoolSize(messenger.getExecutor()));
            print(output, "Max Pending Acquires: %d", messenger.getOrDefault(TransportOptions.POOL_ACQUIRE_MAX));
            print(output, "Pool strategy: %s", messenger.getOrDefault(TransportOptions.POOL_STRATEGY).getName());
            print(output, "Tasks in queue: %d", Platform.getDefaultQueue().size());
            EventLoopGroup eventLoopGroup = messenger.getExecutor();
            print(output, "Executor Service: %s (Default executor: %s)", eventLoopGroup, (eventLoopGroup == Platform.getDefaultEventLoopGroup()) ? "YES" : "NO");

            print(output, LINE);
            print(output, "Event Loop Group: (Group: %s)", eventLoopGroup);
            print(output, LINE);
            int ctr = 0;
            for (EventExecutor ex : eventLoopGroup) {
                SingleThreadEventLoop el = (SingleThreadEventLoop) ex;
                print(output, "%02d) %s-%d [%s] (Pending Tasks: %d, Thread Id: %d)", ++ctr, ex.getClass().getSimpleName(), el.hashCode(), el.threadProperties().name(), el.pendingTasks(), el.threadProperties().id());
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
                    Metadata metadata = getMetadata(channel);//counter.get(channel);
                    print(output, "\t%d) Channel: %s, Acquire: %d, Active: %s, Authenticated: %s, Acquired: %s, Last Acquired: %s, Thread: %s", j + 1, channel, metadata.getAcquireCount(), channel.isActive(), SourceRconAuthProxy.this.isAuthenticated(channel), NettyUtil.isPooled(channel) ? "YES" : "NO", TimeUtil.getTimeDesc(metadata.getLastAcquiredDurationMillis(), true), NettyUtil.getThreadName(channel));
                }
            }
            print(output, LINE);
        }

        private void remove(Channel channel) {
            for (Metadata.Stats stat : Metadata.Stats.values()) {
                log.debug("{} STATISTICS => Clearing stats for channel '{}'", NettyUtil.id(channel), channel);
                channel.attr(stat.key()).set(null);
            }
        }

        private int getTotalAcquireCount(List<Channel> channels) {
            return channels.stream().filter(Metadata.Stats.ACQUIRE_COUNT::exists).mapToInt(Metadata.Stats.ACQUIRE_COUNT::value).sum();
        }

        private int getCorePoolSize(Executor executor) {
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor tpe = (ThreadPoolExecutor) executor;
                return tpe.getCorePoolSize();
            } else if (executor instanceof EventLoopGroup) {
                EventLoopGroup elg = (EventLoopGroup) executor;
                Iterator<EventExecutor> it = elg.iterator();
                int ctr = 0;
                while (it.hasNext()) {
                    it.next();
                    ctr++;
                }
                return ctr;
            } else {
                return -1;
            }
        }

        private void print(Consumer<String> out, String msg, Object... args) {
            out.accept(String.format(msg, args));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Contextual Suppliers">
    private class AuthenticateParcelSupplier implements ContextualSupplier<Parcel, CompletableFuture<Parcel>> {

        private final Parcel parcel;

        public AuthenticateParcelSupplier(final Parcel parcel) {
            this.parcel = Objects.requireNonNull(parcel, "Parcel is null");
        }

        @Override
        public CompletableFuture<Parcel> get(ExecutionContext<Parcel> context) throws Throwable {
            assert parcel.channel().eventLoop().inEventLoop();
            final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
            log.debug("{} AUTH => Authenticating channel (Attempts: {}, Channel: {}, Active: {})", NettyUtil.id(parcel.channel()), context.getAttemptCount(), parcel.channel(), parcel.channel().isActive());
            if (!parcel.channel().isActive() && !context.isRetry() && (context.getLastFailure() instanceof ChannelClosedException)) {
                log.debug(String.format("%s AUTH => Maximum number of login attempts reached (Last Result: %s, Last Failure: %s)", NettyUtil.id(parcel.channel()), context.getLastResult(), context.getLastFailure()));
                invalidate(address);
                return ConcurrentUtil.failedFuture(new RconMaxLoginAttemptsException(String.format("Maximum number of login attempts reached (%d). Connection has been dropped by the server", AUTH_RETRY_POLICY.getConfig().getMaxAttempts()), address));
            }
            final Credentials credentials = credentialsManager.get(address);
            if (!credentials.isValid())
                return ConcurrentUtil.failedFuture(new RconInvalidCredentialsException(String.format("The credentials for address '%s' is no longer valid and needs to be updated", address), address));
            return authenticate(parcel.channel(), credentials).thenApply(parcel::channel);
        }

        /**
         * Sends an authentication request for the specified {@link Channel}. If the authentication is successful, the {@link Channel}'s {@link SourceRcon#AUTHENTICATED} attribute will be updated to {@code true}
         *
         * @param channel
         *         The {@link Channel} to authenticate
         * @param credentials
         *         The {@link Credentials} that will be used for authenticating the specified {@link Channel}
         *
         * @return A {@link CompletableFuture} that will be notified if the authentication was a success.
         */
        private CompletableFuture<Channel> authenticate(Channel channel, Credentials credentials) {
            Objects.requireNonNull(credentials, "Credentials cannot be null");
            if (credentials.isEmpty())
                throw new IllegalStateException("Passphrase must not be null or empty");
            final SourceRconAuthRequest request = SourceRcon.createAuthRequest(credentials.getPassphrase());
            return messenger.send(request, channel).thenCombine(CompletableFuture.completedFuture(channel), this::authHandler);
        }

        private Channel authHandler(SourceRconResponse response, Channel channel) {
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            final SourceRconAuthResponse authResponse = (SourceRconAuthResponse) response;
            if (!authResponse.isSuccess())
                throw new CompletionException(ConcurrentUtil.unwrap(authResponse.getError()));
            if (!authResponse.isAuthenticated()) {
                String msg = String.format("Failed to authenticate with server using password (Reason: %s)", authResponse.getReason());
                //invalidate address credentials
                invalidate(address);
                throw new CompletionException(new RconNotYetAuthException(msg, SourceRconAuthReason.BAD_PASSWORD, (InetSocketAddress) channel.remoteAddress()));
            }
            return channel;
        }
    }
    //</editor-fold>

    @Deprecated
    private static class Parcel {

        private final Envelope<SourceRconRequest> envelope;

        private final Channel channel;

        private SourceRconResponse response;

        private Parcel(Channel channel, Envelope<SourceRconRequest> envelope) {
            this.channel = channel;
            this.envelope = envelope;
        }

        private SourceRconRequest request() {
            return envelope.content();
        }

        private String requestId() {
            if (request() == null)
                return "N/A";
            return request().id().toString();
        }

        private InetSocketAddress address() {
            if (channel == null || channel.remoteAddress() == null)
                return null;
            return (InetSocketAddress) channel.remoteAddress();
        }

        private boolean inEventLoop() {
            if (channel == null)
                return false;
            return channel.eventLoop().inEventLoop();
        }

        private Channel channel() {
            return this.channel;
        }

        private <V> CompletableFuture<V> promise() {
            return envelope.promise();
        }

        private Parcel channel(Channel channel) {
            if (this.channel != channel)
                throw new IllegalStateException("Channel not equals to the channel in the parcl: " + channel);
            return this;
        }

        private Envelope<SourceRconRequest> envelope() {
            return this.envelope;
        }

        private SourceRconResponse response() {
            return this.response;
        }

        private Parcel response(SourceRconResponse response) {
            this.response = response;
            return this;
        }

        private boolean hasResponse() {
            return this.response != null;
        }

        private void release() {
            Channel channel = channel();
            if (channel == null) {
                log.debug("AUTH (RELEASE) => No channel available to release (parcel: " + this + ")");
                return;
            }
            NettyUtil.releaseOrClose(channel);
        }
    }

    private static class RconHelper {

        public Parcel validate(Parcel parcel) {
            final Envelope<SourceRconRequest> envelope = parcel.envelope();
            final Channel channel = parcel.channel();

            Objects.requireNonNull(parcel, "Parcel must not be null");
            Objects.requireNonNull(envelope, "Envelope must not be null");
            Objects.requireNonNull(envelope.recipient(), "Destination address is missing in enqueue");
            Objects.requireNonNull(channel, "Channel must not be null");
            Objects.requireNonNull(channel.remoteAddress(), "Remote address missing in channel: " + channel);

            if (!envelope.recipient().equals(channel.remoteAddress()))
                throw new IllegalStateException("Envelope destination address is not equals to the remote address on channel: " + channel);

            return parcel;
        }

        public void validate(Channel channel) {
            if (channel.remoteAddress() == null)
                throw new IllegalStateException("No remote address present");
            if (!channel.hasAttr(NettyChannelAttributes.REQUEST) || channel.attr(NettyChannelAttributes.REQUEST).get() == null || !(channel.attr(NettyChannelAttributes.REQUEST).get().content() instanceof SourceRconAuthRequest))
                throw new IllegalStateException("No request attribute");
            if (!channel.hasAttr(SourceRcon.AUTHENTICATED) || !channel.attr(SourceRcon.AUTHENTICATED).get())
                throw new IllegalStateException("Channel not marked as authenticated");
        }
    }
}