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

package com.ibasco.agql.protocols.valve.source.query;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.transport.NettyChannelAttributes;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconInvalidCredentialsException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconMaxLoginAttemptsException;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.message.*;
import dev.failsafe.ExecutionContext;
import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.*;
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
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>An proxy that ensures that requests are sent via an authenticated {@link Channel}
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Experimental
public final class SourceRconAuthProxy implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceRconAuthProxy.class);

    private static final RetryPolicy<RconParcel> AUTH_CHANNEL_POLICY = RetryPolicy.<RconParcel>builder()
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

    private final ChannelStatistics channelStatistics = new ChannelStatistics();

    private final SetMultimap<InetSocketAddress, Channel> channels = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private final SourceRconMessenger messenger;

    private final ScheduledExecutorService scheduler;

    private final FailsafeExecutor<Channel> acquireExecutor;

    private final CredentialsManager credentialsManager;

    private volatile boolean healthCheckStarted;

    private final RconHelper helper = new RconHelper();

    private final ChannelSupplier channelSupplier = new ChannelSupplier();

    private final ThreadLocal<FailsafeExecutor<RconParcel>> authExecutors = ThreadLocal.withInitial(() -> Failsafe.with(AUTH_CHANNEL_POLICY));

    /**
     * Create a new authentication proxy
     *
     * @param messenger
     *         The {@link SourceRconMessenger} that is used to relay the messages sent
     */
    public SourceRconAuthProxy(SourceRconMessenger messenger, CredentialsManager credentialsManager) {
        this.messenger = Objects.requireNonNull(messenger, "Messenger cannot be null");
        if (credentialsManager == null)
            credentialsManager = new RconCredentialsManager();
        EventLoopGroup group = messenger.getExecutor();
        this.credentialsManager = credentialsManager;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(getClass()));
        this.acquireExecutor = Failsafe.with(ACQUIRE_RETRY_POLICY).with(group.next());
    }

    /**
     * Register a newly created channel and ensures that it is authenticated by the server
     *
     * @param channel
     *         The newly created/acquired {@link Channel}
     *
     * @return {@code true} if the {@link Channel} has been registered otherwise {@code false} if the {@link Channel} has been registered already
     */
    public CompletableFuture<Channel> register(final Channel channel) {
        if (channel.eventLoop().inEventLoop()) {
            return CompletableFuture.completedFuture(register0(channel));
        } else {
            return CompletableFuture.supplyAsync(() -> register0(channel), channel.eventLoop());
        }
    }

    private Channel register0(Channel channel) {
        Objects.requireNonNull(channel, "Channel must not be null");
        if (isRegistered(channel))
            return channel;
        if (!channel.isActive())
            throw new IllegalStateException("Can't register a channel that is inactive");
        if (!(channel.remoteAddress() instanceof InetSocketAddress))
            throw new IllegalStateException("Not a valid remote address. Either null or not an InetSocketAddress instance");

        synchronized (channels) {
            log.debug("{} AUTH => Registering channel '{}'", NettyUtil.id(channel), channel);
            final InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            if (channels.put(address, channel)) {
                startInactivityCheck();
                //Disable auto release of channel, we will release/close it manually
                channel.attr(NettyChannelAttributes.AUTO_RELEASE).set(false);
                //Initialize channel attribute
                channel.attr(SourceRcon.AUTHENTICATED).set(false);
                if (log.isDebugEnabled())
                    log.debug("{} AUTH => Successfully registered channel (Total: {}, Address: {}, Authenticated: {})", NettyUtil.id(channel), channels.get(address).size(), channel.remoteAddress(), isAuthenticated(channel));
                unregisterOnClose(channel);
                return channel;
            }
            return null;
        }
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
        if (!isRegistered(channel))
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
        if (!isRegistered(channel))
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

    public CompletableFuture<SourceRconResponse> send(final InetSocketAddress address, final SourceRconRequest request) {
        Objects.requireNonNull(address, "Address must not be null");
        Objects.requireNonNull(request, "Request must not be null");
        CompletableFuture<RconParcel> parcelFuture = createParcel(address, request).thenApply(helper::validate);
        if (request instanceof SourceRconAuthRequest)
            parcelFuture = parcelFuture.thenCompose(this::sendAuthRequest);
        else if (request instanceof SourceRconCmdRequest)
            parcelFuture = parcelFuture.thenCompose(this::sendCmdRequest);
        else
            throw new IllegalStateException("Invalid rcon request");
        return parcelFuture.handle(this::release);
    }

    private CompletableFuture<RconParcel> sendAuthRequest(final RconParcel parcel) {
        if (parcel.hasError())
            return CompletableFuture.completedFuture(parcel);
        SourceRconAuthRequest request = (SourceRconAuthRequest) parcel.envelope().content();
        //if no password was supplied, then we check if we have credentials registered for this address
        if (request.getPassword() == null) {
            Credentials credentials = credentialsManager.get(parcel.address());
            if (credentials == null)
                return ConcurrentUtil.failedFuture(new RconNotYetAuthException("Address '" + parcel.address() + "' not yet authenticated by the remote server. Please authenticate with a provided passphrase", SourceRconAuthReason.NOT_AUTHENTICATED));
            if (!credentials.isValid())
                return ConcurrentUtil.failedFuture(new RconNotYetAuthException("The credentials for address '" + parcel.address() + "' has been invalidated. Please re-authenticate with the server", SourceRconAuthReason.REAUTH));
            //update request
            parcel.envelope().content(new SourceRconAuthRequest(request, credentials.getPassphrase()));
        }
        return sendParcel(parcel).thenApply(this::authResponseHandler);
    }

    private RconParcel authResponseHandler(RconParcel parcel) {
        final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
        final SourceRconAuthRequest request = (SourceRconAuthRequest) parcel.envelope().content();
        final Channel channel = parcel.channel();

        if (parcel.hasError()) {
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
        }

        if (!parcel.hasResponse())
            throw new IllegalStateException("Parcel does not contain a response");

        //make sure we have an auth response
        if (!(parcel.response() instanceof SourceRconAuthResponse))
            throw new IllegalStateException("Request/Response Mismatch. Expected SourceRconAuthResponse (Actual: " + parcel.response() + ")");

        final SourceRconAuthResponse authResponse = (SourceRconAuthResponse) parcel.response();

        if (authResponse.isSuccess()) {
            if (authResponse.isAuthenticated()) {
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

    private CompletableFuture<RconParcel> sendCmdRequest(final RconParcel parcel) {
        if (parcel.hasError())
            return CompletableFuture.completedFuture(parcel);
        //Is the address authenticated?
        if (!isAuthenticated(parcel.address()))
            return CompletableFuture.completedFuture(parcel.error(new RconNotYetAuthException(String.format("Address '%s' not yet authenticated. Make sure to call authenticate().", parcel.address()), SourceRconAuthReason.NOT_AUTHENTICATED)));
        //Are the credentials still valid?
        if (!credentialsManager.isValid(parcel.address()))
            return CompletableFuture.completedFuture(parcel.error(new RconNotYetAuthException(String.format("The credentials for address '%s' is no longer valid. Re-authenticate with the server", parcel.address()), SourceRconAuthReason.REAUTH)));
        //is the channel authenticated already?
        if (isAuthenticated(parcel.channel()))
            return sendParcel(parcel);
        log.debug("{} AUTH => Channel not yet authenticated. Attempting to authenticate with server", NettyUtil.id(parcel.channel()));
        //Send the request over the transport. If the channel is not yet authenticated, an authentication request will be sent first.
        return tryAuthenticate(parcel).thenCompose(this::sendParcel);
    }

    /**
     * Send parcel to the transport
     */
    private CompletableFuture<RconParcel> sendParcel(final RconParcel parcel) {
        if (parcel.hasError())
            return CompletableFuture.completedFuture(parcel);
        assert parcel.channel() != null;
        assert parcel.channel().remoteAddress() != null;
        assert parcel.channel().remoteAddress().equals(parcel.envelope().recipient());
        return messenger.send(parcel.envelope(), parcel.channel())
                        .thenApply(parcel::response)
                        .exceptionally(parcel::error);
    }

    /**
     * Release the parcel and return the response. If the parcel contains an error, a {@link CompletionException} will be thrown.
     *
     * @param parcel
     *         The {@link RconParcel} to release
     * @param error
     *         The error that occured that made the future transition to a failed state.
     *
     * @return The {@link SourceRconResponse} if no error.
     */
    private SourceRconResponse release(RconParcel parcel, Throwable error) {
        if (error != null)
            throw new CompletionException(ConcurrentUtil.unwrap(error));
        assert parcel != null;
        try {
            if (parcel.hasError()) {
                throw new CompletionException(ConcurrentUtil.unwrap(parcel.error()));
            } else {
                assert parcel.hasResponse();
                return parcel.response();
            }
        } finally {
            parcel.release();
        }
    }

    //1) enqueue a new channel
    //2) wrap channeel and enqueue into a parcel
    //3) if an error occurs, wrap the error into a parcel. the future should never complete exceptionally
    private CompletableFuture<RconParcel> createParcel(final InetSocketAddress address, SourceRconRequest request) {
        final Envelope<SourceRconRequest> envelope = messenger.newEnvelope(address, request);
        return acquire(envelope) //enqueue channel
                                 .thenCombine(CompletableFuture.completedFuture(envelope), RconParcel::new) //wrap acquired channel and enqueue in parcel
                                 .exceptionally(error -> new RconParcel(envelope, error)); //in case of exceptions, wrap it in a parcel, don't let this
    }

    private CompletableFuture<RconParcel> tryAuthenticate(RconParcel parcel) {
        if (parcel.hasError()) {
            log.warn("Rcon parcel in error", parcel.error());
            return CompletableFuture.completedFuture(parcel);
        }
        final Channel channel = parcel.channel();
        assert channel.eventLoop().inEventLoop();

        final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
        //make sure that the address has been authenticated and we have credentials preset
        if (!isAuthenticated(address) || !isValid(address)) {
            log.warn("Address not authenticated or valid: {} (Authenticated: {}, Valid: {})", address, isAuthenticated(address), isValid(address));
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not authenticated or not valid", address), SourceRconAuthReason.NOT_AUTHENTICATED));
        }
        //if the channel is authenticated, return immediately
        if (isAuthenticated(parcel.channel()))
            return CompletableFuture.completedFuture(parcel);
        //at this point, we need to authenticate the channel/connection with the remote server
        return authExecutors.get().with(channel.eventLoop()).getStageAsync(new AuthenticateParcelTaskSupplier(parcel));
    }

    /**
     * Acquire new/existing {@link Channel} from the factory then register it with the authentication manager.
     *
     * @param envelope
     *         The {@link Envelope} containing the request and promise details
     *
     * @return A {@link CompletableFuture} that will be notified when the channel has been successfully acquired from the {@link com.ibasco.agql.core.transport.ChannelFactory}
     */
    private CompletableFuture<Channel> acquire(final Envelope<SourceRconRequest> envelope) {
        log.debug("AUTH => Acquiring channel for enqueue '{}'", envelope);
        //channelSupplier.enqueue(envelope)
        return acquireExecutor.getStageAsync(new ChannelSupplierEx(envelope)).thenCompose(this::register).handle(channelStatistics);
    }

    @ApiStatus.Experimental
    public void printStatistics(Consumer<String> output) {
        channelStatistics.print(output);
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void printStatistics() {
        channelStatistics.print();
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void resetStatistics() {
        channelStatistics.reset();
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public Map<Channel, ChannelMetadata> getStatistics() {
        return channelStatistics.getStatistics();
    }

    /**
     * Removes a registered {@link Channel} from the registry.
     *
     * @param channel
     *         The {@link Channel} to be removed
     */
    public void unregister(Channel channel) {
        synchronized (channels) {
            if (!isRegistered(channel)) {
                return;
            }
            channel.attr(SourceRcon.AUTHENTICATED).set(null);
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            if (channels.remove(address, channel)) {
                log.debug("{} AUTH => Unregistered channel '{}' from address entry '{}' (Address Valids: {})", NettyUtil.id(channel), channel, channel.remoteAddress(), credentialsManager.get(address).isValid());
            } else {
                log.debug("FAILED TO UNREGISTER CHANNEL: {}", channel);
            }
        }
    }

    public void cleanup() {
        this.scheduler.execute(new ChannelInactiveCheckTask(true));
    }

    public boolean isRegistered(Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        return channels.containsValue(channel);
    }

    /**
     * Invalidate all addresses and the {@link Channel}'s associated with it
     */
    public void invalidate() {
        invalidate(false);
    }

    /**
     * Invalidates all registered address and it's {@link Channel}s under it.
     *
     * @param onlyChannels
     *         {@code true} if we should only invalidate the {@link Channel}'s for the specified address.
     */
    public void invalidate(boolean onlyChannels) {
        for (InetSocketAddress address : channels.keySet()) {
            invalidate(address, onlyChannels);
        }
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
     * @param onlyChannels
     *         {@code true} if we should only invalidate the {@link Channel}'s for the specified address. Credentials will remain valid.
     */
    public void invalidate(InetSocketAddress address, boolean onlyChannels) {
        checkAddress(address);
        log.debug("AUTH => Invalidating address '{}'", address);
        if (!onlyChannels)
            credentialsManager.invalidate(address);
        synchronized (channels) {
            for (Channel channel : channels.get(address)) {
                invalidate(channel);
            }
        }
    }

    private void invalidate(Channel channel) {
        Objects.requireNonNull(channel);
        channel.attr(SourceRcon.AUTHENTICATED).set(false);
        log.debug("{} AUTH => Invalidated channel. Marked for re-authentication", NettyUtil.id(channel));
    }

    /**
     * Remove the {@link Channel} from the registry on close
     *
     * @param channel
     *         The {@link Channel} to track
     */
    private void unregisterOnClose(Channel channel) {
        Objects.requireNonNull(channel, "Channel is null");
        if (channel.closeFuture().isDone()) {
            cleanup(channel);
        } else {
            channel.closeFuture().addListener((ChannelFutureListener) future -> cleanup(channel));
        }
    }

    private void cleanup(Channel channel) {
        assert !channel.isActive();
        if (channel.eventLoop().inEventLoop()) {
            cleanup0(channel);
        } else {
            channel.eventLoop().execute(() -> cleanup0(channel));
        }
    }

    private void cleanup0(Channel channel) {
        unregister(channel);
        channelStatistics.remove(channel);
        if (NettyUtil.isPooled(channel))
            NettyUtil.release(channel);
    }

    private static void checkAddress(SocketAddress address) {
        if (!(address instanceof InetSocketAddress))
            throw new IllegalStateException("Address is not an IneteSocketAddress instance");
    }

    private void startInactivityCheck() {
        if (healthCheckStarted)
            return;
        scheduler.scheduleAtFixedRate(new ChannelInactiveCheckTask(), 0, 1, TimeUnit.SECONDS);
        healthCheckStarted = true;
    }

    private class ChannelInactiveCheckTask implements Runnable {

        private final boolean force;

        private ChannelInactiveCheckTask() {
            this(false);
        }

        private ChannelInactiveCheckTask(boolean force) {
            this.force = force;
        }

        @Override
        public void run() {
            final int closeDuration = messenger.getOrDefault(SourceRconOptions.CLOSE_INACTIVE_CHANNELS);
            if (closeDuration < 0)
                return;
            //close unused connections
            synchronized (channels) {
                for (InetSocketAddress address : channels.keySet()) {
                    Set<Channel> channelList = channels.get(address);

                    int cRemaining = channelList.size();
                    int ctr = 0;

                    for (final Channel channel : channelList) {
                        //ignore channels that are currently in-use
                        if (NettyUtil.isPooled(channel))
                            continue;
                        ChannelMetadata metadata = channelStatistics.getMetadata(channel);
                        long lastAcquiredDuration = metadata.getLastAcquiredDuration();
                        int remaining = channels.get(address).size();
                        if (remaining == 1 || cRemaining == 1)
                            continue;
                        if (force || (metadata.getAcquireSuccess() == 0 || (lastAcquiredDuration >= 0 && lastAcquiredDuration >= closeDuration))) {
                            log.info("AUTH (CLEANUP) => ({}) Closing unused channel: ({}) (Acquire Count: {}, Last acquired: {}, Registered: {})", ++ctr, channel, metadata.getAcquireSuccess(), metadata.getLastAcquiredDuration(), isRegistered(channel));
                            final String id = channel.id().asShortText();
                            //NettyUtil.release(channel).thenAccept(channelPool -> log.debug("AUTH (CLEANUP) => Released channel '{}' from channel pool", id));
                            NettyUtil.close(channel).thenAccept(unused -> log.debug("AUTH (CLEANUP) => Closed unused channel: {}", id));
                            cRemaining--;
                        }
                    }
                }
            }
        }
    }

    private class AuthenticateParcelTaskSupplier implements ContextualSupplier<RconParcel, CompletableFuture<RconParcel>> {

        private final RconParcel parcel;

        public AuthenticateParcelTaskSupplier(RconParcel parcel) {
            this.parcel = parcel;
        }

        @Override
        public CompletableFuture<RconParcel> get(ExecutionContext<RconParcel> context) throws Throwable {
            assert parcel.channel().eventLoop().inEventLoop();

            final InetSocketAddress address = (InetSocketAddress) parcel.channel().remoteAddress();
            log.debug("{} AUTH => Re-authenticating channel (Attempts: {}, Channel: {}, Active: {})", NettyUtil.id(parcel.channel()), context.getAttemptCount(), parcel.channel(), parcel.channel().isActive());
            if (!parcel.channel().isActive() && !context.isRetry() && (context.getLastFailure() instanceof ClosedChannelException)) {
                log.debug(String.format("%s AUTH => Maximum number of login attempts reached (Last Result: %s, Last Failure: %s)", NettyUtil.id(parcel.channel()), context.getLastResult(), context.getLastFailure()));
                invalidate(address);
                return ConcurrentUtil.failedFuture(new RconMaxLoginAttemptsException(String.format("Maximum number of login attempts reached (%d). Connection has been dropped by the server", AUTH_RETRY_POLICY.getConfig().getMaxAttempts()), address));
            }
            final Credentials credentials = credentialsManager.get(address);
            if (!credentials.isValid())
                return ConcurrentUtil.failedFuture(new RconInvalidCredentialsException(String.format("The credentials for address '%s' is no longer valid and needs to be updated", address), address));

            log.info("{} AUTH => Authenticating parcel: {}", NettyUtil.id(parcel.channel()), parcel.channel());
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
            final SourceRconAuthRequest authRequest = SourceRcon.createAuthRequest(credentials.getPassphrase());
            return messenger.send(authRequest, channel).thenCombine(CompletableFuture.completedFuture(channel), this::authHandler);
        }

        private Channel authHandler(SourceRconResponse response, Channel channel) {
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            final SourceRconAuthResponse authResponse = (SourceRconAuthResponse) response;
            if (!authResponse.isSuccess())
                throw new CompletionException(authResponse.getError());
            if (!authResponse.isAuthenticated()) {
                String msg = String.format("Failed to authenticate with server using password (Reason: %s)", authResponse.getReason());
                //invalidate address credentials
                invalidate(address);
                throw new CompletionException(new RconNotYetAuthException(msg, SourceRconAuthReason.BAD_PASSWORD));
            }
            return channel;
        }
    }

    private static class RconParcel {

        private final Envelope<SourceRconRequest> envelope;

        private final Channel channel;

        private SourceRconResponse response;

        private Throwable error;

        private RconParcel(Envelope<SourceRconRequest> envelope, Throwable error) {
            this.envelope = envelope;
            this.error = error;
            this.channel = null;
        }

        private RconParcel(Channel channel, Envelope<SourceRconRequest> envelope) {
            this.channel = channel;
            this.envelope = envelope;
        }

        private boolean hasError() {
            return this.error != null;
        }

        private Throwable error() {
            return this.error;
        }

        private RconParcel error(Throwable error) {
            this.error = error;
            return this;
        }

        private void clearError() {
            error(null);
        }

        private SourceRconRequest request() {
            return envelope.content();
        }

        private InetSocketAddress address() {
            if (channel == null || channel.remoteAddress() == null)
                return null;
            return (InetSocketAddress) channel.remoteAddress();
        }

        private Channel channel() {
            return this.channel;
        }

        private <V> CompletableFuture<V> promise() {
            return envelope.promise();
        }

        private RconParcel channel(Channel channel) {
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

        private RconParcel response(SourceRconResponse response) {
            this.response = response;
            return this;
        }

        private boolean hasResponse() {
            return this.response != null;
        }

        private void release() {
            Channel channel = channel();
            if (channel == null) {
                log.debug("AUTH (RELEASE) => No channel available to release (parcel: " + this + ", has error: " + hasError() + ")", error);
                return;
            }
            NettyUtil.releaseOrClose(channel);
        }
    }

    public static class ChannelMetadata {

        private int acquireCount = 0;

        private int failCount = 0;

        private long lastAcquireMillis;

        private int getAcquireSuccess() {
            return acquireCount;
        }

        private int getAcquireFail() {
            return failCount;
        }

        private long getLastAcquiredDuration() {
            return acquireCount > 0 ? Duration.ofMillis(System.currentTimeMillis() - lastAcquireMillis).getSeconds() : -1;
        }

        private long getLastAcquiredDurationMillis() {
            return acquireCount > 0 ? System.currentTimeMillis() - lastAcquireMillis : -1;
        }
    }

    public class ChannelStatistics implements BiFunction<Channel, Throwable, Channel> {

        private final ConcurrentHashMap<Channel, ChannelMetadata> counter = new ConcurrentHashMap<>();

        @Override
        public Channel apply(Channel channel, Throwable error) {
            if (channel != null) {
                ChannelMetadata stat = counter.get(channel);
                if (stat == null) {
                    ChannelMetadata metadata = new ChannelMetadata();
                    if (error != null) {
                        metadata.failCount = 1;
                    } else {
                        metadata.acquireCount = 1;
                        metadata.lastAcquireMillis = System.currentTimeMillis();
                    }
                    counter.put(channel, metadata);
                } else {
                    if (error != null) {
                        stat.failCount = stat.failCount + 1;
                    } else {
                        stat.lastAcquireMillis = System.currentTimeMillis();
                        stat.acquireCount = stat.acquireCount + 1;
                    }
                }
            }
            //make sure we propagatee the error down the chain
            if (error != null && channel == null) {
                if (error instanceof CompletionException)
                    throw (CompletionException) error;
                else
                    throw new CompletionException(error);
            }
            return channel;
        }

        public void reset() {
            synchronized (counter) {
                counter.clear();
            }
        }

        public void remove(Channel channel) {
            synchronized (counter) {
                counter.remove(channel);
            }
        }

        public List<InetSocketAddress> getAddressList() {
            synchronized (counter) {
                return counter.keySet().stream().map(Channel::remoteAddress).map(InetSocketAddress.class::cast).distinct().collect(Collectors.toList());
            }
        }

        public List<Channel> getChannels(InetSocketAddress address) {
            synchronized (counter) {
                return counter.keySet().stream().filter(a -> a.remoteAddress().equals(address)).sorted().collect(Collectors.toList());
            }
        }

        public void print() {
            print(log::info);
        }

        public Map<Channel, ChannelMetadata> getStatistics() {
            return new HashMap<>(counter);
        }

        private int getTotalAcquireSuccess(List<Channel> channels) {
            return counter.entrySet().stream().filter(p -> channels.contains(p.getKey())).map(Map.Entry::getValue).mapToInt(ChannelMetadata::getAcquireSuccess).sum();
        }

        private int getTotalAcquireFail(List<Channel> channels) {
            return counter.entrySet().stream().filter(p -> channels.contains(p.getKey())).map(Map.Entry::getValue).mapToInt(ChannelMetadata::getAcquireFail).sum();
        }

        private long getLastAcquired(Channel channel) {
            ChannelMetadata channelStat = counter.get(channel);
            return channelStat.acquireCount > 0 ? Duration.ofMillis(System.currentTimeMillis() - channelStat.lastAcquireMillis).getSeconds() : -1;
        }

        private ChannelMetadata getMetadata(Channel channel) {
            return counter.get(channel);
        }

        public void print(Consumer<String> output) {

            synchronized (counter) {
                final String LINE = StringUtils.repeat("=", 200);
                print(output, LINE);
                print(output, "Channel Statistics");
                print(output, LINE);
                print(output, "Connection pooling enabled: %s", messenger.getOrDefault(TransportOptions.CONNECTION_POOLING));
                print(output, "Max Pooled Connections: %d", messenger.getOrDefault(TransportOptions.POOL_MAX_CONNECTIONS));
                print(output, "Max Core Pool Size: %d", Platform.getDefaultExecutor().getCorePoolSize());
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
                final List<InetSocketAddress> addressList = getAddressList();
                for (int i = 0, distinctAddressesSize = addressList.size(); i < distinctAddressesSize; i++) {
                    final InetSocketAddress address = addressList.get(i);
                    List<Channel> channels = getChannels(address);
                    int totalAcquireCount = getTotalAcquireSuccess(channels);
                    int totalFailedAquires = getTotalAcquireFail(channels);

                    print(output, "%d) Address: %s (Successful Acquires: %d, Failed Acquires: %d, Active Channels: %d, Authenticated: %s)", i + 1, address, totalAcquireCount, totalFailedAquires, channels.size(), isAuthenticated(address) ? "YES" : "NO");
                    for (int j = 0; j < channels.size(); j++) {
                        Channel channel = channels.get(j);
                        ChannelMetadata metadata = counter.get(channel);
                        print(output, "\t%d) Channel: %s, Acquire: [Success: %d, Fail: %d], Active: %s, Authenticated: %s, Acquired: %s, Last Acquired: %s, Thread: %s", j + 1, channel, metadata.acquireCount, metadata.failCount, channel.isActive(), SourceRconAuthProxy.this.isAuthenticated(channel), NettyUtil.isPooled(channel) ? "YES" : "NO", TimeUtil.getTimeDesc(metadata.getLastAcquiredDurationMillis(), true), NettyUtil.getThreadName(channel));
                    }
                }
                print(output, LINE);
            }
        }

        private void print(Consumer<String> out, String msg, Object... args) {
            out.accept(String.format(msg, args));
        }
    }

    private class ChannelSupplierEx implements ContextualSupplier<Channel, CompletableFuture<Channel>> {

        private final WeakReference<Envelope<SourceRconRequest>> envelopeRef;

        private ChannelSupplierEx(Envelope<SourceRconRequest> envelope) {
            this.envelopeRef = new WeakReference<>(envelope);
        }

        @Override
        public CompletableFuture<Channel> get(ExecutionContext<Channel> context) throws Throwable {
            if (envelopeRef.get() == null)
                throw new IllegalStateException("Envelope is null");
            return messenger.getTransport().getChannelFactory().create(envelopeRef.get());
        }
    }

    private class ChannelSupplier implements ContextualSupplier<Channel, CompletableFuture<Channel>> {

        private final Deque<Envelope<SourceRconRequest>> requestQueue = new ConcurrentLinkedDeque<>();

        private ChannelSupplier() {}

        private ChannelSupplier enqueue(Envelope<SourceRconRequest> envelope) {
            requestQueue.addFirst(envelope);
            return this;
        }

        @Override
        public CompletableFuture<Channel> get(ExecutionContext<Channel> context) throws Throwable {
            Envelope<SourceRconRequest> envelope = requestQueue.removeFirst();
            log.debug("({}) Acquiring channel for '{}'", context.getAttemptCount(), envelope);
            return messenger.getTransport().getChannelFactory().create(envelope);
        }
    }

    private static class RconHelper {

        public RconParcel validate(RconParcel parcel) {
            if (parcel.hasError() || parcel.channel() == null)
                return parcel;

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
}
