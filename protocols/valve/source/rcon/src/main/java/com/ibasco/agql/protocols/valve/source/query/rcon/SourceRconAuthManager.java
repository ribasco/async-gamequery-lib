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
import com.ibasco.agql.core.NettyChannelContext;
import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.core.exceptions.ChannelRegistrationException;
import com.ibasco.agql.core.transport.FailsafeChannelFactory;
import com.ibasco.agql.core.transport.pool.NettyChannelPool;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconInvalidCredentialsException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconMaxLoginAttemptsException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconResponse;
import dev.failsafe.ExecutionContext;
import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

//TODO: Move all this to SourceRconMessenger
/**
 * <p>The default Source RCON authentication manager. This also serves as a messenger proxy that allows rcon requests to passthrough.</p>
 *
 * @author Rafael Luis Ibasco
 */
@ApiStatus.Experimental
public final class SourceRconAuthManager implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SourceRconAuthManager.class);

    private final Statistics statistics = new Statistics();

    private final SourceRconMessenger messenger;

    private final ScheduledExecutorService jobScheduler;

    private final CredentialsStore credentialsStore;

    private volatile boolean healthCheckStarted;

    private final boolean reauthenticate;

    private final InactivityCheckTask INACTIVITY_CHECK_TASK = new InactivityCheckTask();

    private final ChannelRegistry registry = new SourceRconChannelRegistry();

    private final SourceRconChannelFactory channelFactory;

    private final RconAuthenticator authenticator;

    private final RetryPolicy<SourceRconChannelContext> rconRequestRetryPolicy;

    private final FailsafeExecutor<SourceRconChannelContext> executor;

    /**
     * Create a new authentication proxy
     *
     * @param messenger
     *         The {@link SourceRconMessenger} that is used to relay the messages sent
     */
    public SourceRconAuthManager(SourceRconMessenger messenger, CredentialsStore credentialsStore) {
        this.messenger = Objects.requireNonNull(messenger, "Messenger cannot be null");
        if (credentialsStore == null)
            credentialsStore = new InMemoryCredentialsStore();
        this.credentialsStore = credentialsStore;
        this.jobScheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("agql-auth-job"));
        this.reauthenticate = messenger.getOrDefault(SourceRconOptions.REAUTHENTICATE);
        this.authenticator = new SourceRconAuthenticator(credentialsStore, reauthenticate);

        this.rconRequestRetryPolicy = RetryPolicy.<SourceRconChannelContext>builder()
                                                 .abortOn(ConnectTimeoutException.class)
                                                 .withDelay(Duration.ofSeconds(messenger.getOrDefault(SourceRconOptions.FAILSAFE_RETRY_DELAY)))
                                                 .withMaxAttempts(messenger.getOrDefault(SourceRconOptions.FAILSAFE_RETRY_MAX_ATTEMPTS))
                                                 .build();

        this.executor = Failsafe.with(rconRequestRetryPolicy).with(messenger.getExecutor());
        this.channelFactory = (SourceRconChannelFactory) messenger.getChannelFactory();
    }

    public boolean isReauthenticate() {
        return reauthenticate;
    }

    public ChannelRegistry getRegistry() {
        return registry;
    }

    public CredentialsStore getCredentialsStore() {
        return credentialsStore;
    }

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
            log.debug("{} AUTH => Successfully registered channel (Total: {}, Address: {}, Authenticated: {})", context.id(), registry.getChannels(address).size(), channel.remoteAddress(), isAuthenticated(channel));

            //perform additional initialization
            //Initialize context attribute
            context.properties().autoRelease(false); //Disable auto release of channel, we will release/close it manually
            context.properties().authenticated(false);

            //Start inactivity check task once a channel has been registered
            startInactivityCheck();
        } catch (ChannelRegistrationException e) {
            throw new IllegalStateException(e);
        }
        return channel;
    }

    /**
     * Checks if the specified {@link InetSocketAddress} has been previously authenticated.
     *
     * @param address
     *         The {@link InetSocketAddress} to check
     *
     * @return {@code true} if the address has been previously authenticated.
     */
    public boolean isAuthenticated(InetSocketAddress address) {
        checkAddress(address);
        return credentialsStore.exists(address);
    }

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
        CompletableFuture<SourceRconChannelContext> future;
        if (request instanceof SourceRconAuthRequest) {
            future = failSafeExecute(address, request, this::sendAuthRequest);
        } else if (request instanceof SourceRconCmdRequest) {
            future = failSafeExecute(address, request, this::sendCmdRequest);
        } else {
            throw new IllegalStateException("Invalid rcon request");
        }
        return future.handle(this::response);
    }

    private CompletableFuture<SourceRconChannelContext> failSafeExecute(final InetSocketAddress address, final SourceRconRequest request, Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> callback) {
        return executor.getStageAsync(new RconContextualSupplier(address, request, callback));
    }

    private SourceRconResponse response(NettyChannelContext context, Throwable error) {
        if (error != null)
            throw new CompletionException(Errors.unwrap(error));
        assert context != null;
        try {
            return context.properties().response();
        } finally {
            context.close();
        }
    }

    private CompletableFuture<SourceRconChannelContext> sendAuthRequest(final SourceRconChannelContext context) {
        log.debug("{} AUTH => Sending AUTH request '{}'", context.id(), context.properties().request());
        return authenticator.authenticate(context).handle(this::invalidateOnError);
    }

    private CompletableFuture<SourceRconChannelContext> sendCmdRequest(final SourceRconChannelContext context) {
        log.debug("{} AUTH => Sending COMMAND request '{}'", context.id(), context.properties().request());
        final InetSocketAddress address = context.properties().envelope().recipient();
        //Do we a valid credential registered for the address
        if (!isAuthenticated(address))
            throw new RconNotYetAuthException(String.format(SourceRconAuthenticator.NOT_YET_AUTH_MSG, address), SourceRconAuthReason.NOT_AUTHENTICATED, address);
        //Are the credentials still valid?
        if (!isValidAddress(address))
            throw new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), address);
        log.debug("{} AUTH => Found existing valid credentials for address '{}' (Authenticated: {})", context.id(), address, context.properties().authenticated());
        //is the channel authenticated already?
        if (context.properties().authenticated())
            return context.send();
        if (!reauthenticate)
            throw new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), address);

        log.debug("{} AUTH => Channel not yet authenticated. Attempting to authenticate the underlying connection with remote server", context.id());
        //Send the request over the transport. If the channel is not yet authenticated, an authentication request will be sent first.
        return authenticator.authenticate(context).handle(this::invalidateOnError).thenCompose(SourceRconChannelContext::send);
    }

    private SourceRconChannelContext invalidateOnError(SourceRconChannelContext context, Throwable error) {
        if (error != null) {
            error = Errors.unwrap(error);
            if (error instanceof RconInvalidCredentialsException)
                invalidate(((RconInvalidCredentialsException) error).getAddress());
            throw new CompletionException(error);
        }
        return context;
    }

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
     *         {@code true} if we should only invalidate the {@link Channel}'s for the specified address.
     */
    public void invalidate(boolean onlyConnections) {
        for (InetSocketAddress address : registry.getAddresses())
            invalidate(address, onlyConnections);
    }

    /**
     * Invalidate both credentials and all it's {@link Channel}'s associated with it
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

    @Override
    public void close() throws IOException {
        if (!jobScheduler.isShutdown()) {
            log.debug("AUTH (CLOSE) => Requesting graceful shutdown");
            if (Concurrency.shutdown(jobScheduler)) {
                log.debug("AUTH (CLOSE) => Job scheduler shutdown gracefully");
            } else {
                log.debug("AUTH (CLOSE) => Failed to shutdown job scheduler");
            }
        }
    }

    private static void checkAddress(SocketAddress address) {
        if (!(address instanceof InetSocketAddress))
            throw new IllegalStateException("Address is not an IneteSocketAddress instance");
    }

    private void startInactivityCheck() {
        if (healthCheckStarted)
            return;
        jobScheduler.scheduleAtFixedRate(INACTIVITY_CHECK_TASK, 0, messenger.getOrDefault(SourceRconOptions.INACTIVE_CHECK_INTERVAL), TimeUnit.SECONDS);
        healthCheckStarted = true;
    }

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
                    log.debug("AUTH (CLEANUP) => ({}) Closing unused channel: ({}) (Acquire Count: {}, Last acquired: {}, Registered: {}, Remaining: {})", ++ctr, channel, metadata.getAcquireCount(), metadata.getLastAcquiredDuration(), registry.isRegistered(channel), cRemaining);
                    final String id = channel.id().asShortText();
                    Netty.close(channel).thenAcceptAsync(unused -> log.debug("AUTH (CLEANUP) => Closed unused channel: {}", id), channel.eventLoop());
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Statistics">
    public static class Metadata {

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
                return (V) Netty.incrementAttrNumber(channel, this.key());
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
            Object acquireCount = channel().attr(Stats.ACQUIRE_COUNT.key()).get();
            if (acquireCount == null)
                return 0;
            return (int) acquireCount;
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
            log.debug("{} STATISTICS (CLEANUP) => Removing channel '{}'", Netty.id(future.channel()), future.channel());
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
                    log.debug("{} STATISTICS => Initializing stats for channel '{}'", Netty.id(channel), channel);
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

            final String LINE = "\033[0;36m" + StringUtils.repeat("=", 200) + "\033[0m";
            print(output, LINE);
            print(output, "Channel Statistics");
            print(output, LINE);
            print(output, "Connection pooling enabled: %s", messenger.getOrDefault(TransportOptions.CONNECTION_POOLING));
            print(output, "Max Pooled Connections: %d", messenger.getOrDefault(TransportOptions.POOL_MAX_CONNECTIONS));
            print(output, "Max Core Pool Size: %d", getCorePoolSize(messenger.getExecutor()));
            print(output, "Max Pending Acquires: %d", messenger.getOrDefault(TransportOptions.POOL_ACQUIRE_MAX));
            print(output, "Tasks in queue: %d", Platform.getDefaultQueue().size());
            EventLoopGroup eventLoopGroup = messenger.getExecutor();
            print(output, "Executor Service: %s (Default executor: %s)", eventLoopGroup, (eventLoopGroup == Platform.getDefaultEventLoopGroup()) ? "YES" : "NO");

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
                    Metadata metadata = getMetadata(channel);//counter.get(channel);
                    print(output, "\t%d) Channel: %s, Acquire: %d, Active: %s, Authenticated: %s, Acquired: %s, Last Acquired: %s, Thread: %s", j + 1, channel, metadata.getAcquireCount(), channel.isActive(), SourceRconAuthManager.this.isAuthenticated(channel), NettyChannelPool.isPooled(channel) ? "YES" : "NO", Time.getTimeDesc(metadata.getLastAcquiredDurationMillis(), true), Netty.getThreadName(channel));
                }
            }
            print(output, LINE);
        }

        private void remove(Channel channel) {
            for (Metadata.Stats stat : Metadata.Stats.values()) {
                log.debug("{} STATISTICS => Clearing stats for channel '{}'", Netty.id(channel), channel);
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

    private class RconContextualSupplier implements ContextualSupplier<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> {

        private final InetSocketAddress address;

        private final SourceRconRequest request;

        private final Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> callback;

        private RconContextualSupplier(InetSocketAddress address, SourceRconRequest request, Function<SourceRconChannelContext, CompletableFuture<SourceRconChannelContext>> callback) {
            if (address == null)
                throw new IllegalStateException("Address must not be null");
            if (request == null)
                throw new IllegalStateException("Request must not be null");
            if (callback == null)
                throw new IllegalStateException("Callback must not be null");
            this.address = address;
            this.request = request;
            this.callback = callback;
        }

        @Override
        public CompletableFuture<SourceRconChannelContext> get(ExecutionContext<SourceRconChannelContext> context) throws Throwable {
            Credentials credentials = credentialsStore.get(address);
            Throwable lastError = Errors.unwrap(context.getLastException());

            //have we reached the maximum number of login attempts?
            if (lastError instanceof ChannelClosedException && context.getAttemptCount() >= (rconRequestRetryPolicy.getConfig().getMaxAttempts() - 1)) {
                SourceRconChannelContext ctx = SourceRconChannelContext.getContext(((ChannelClosedException) lastError).getChannel());
                if (ctx.properties().request() instanceof SourceRconAuthRequest) {
                    if (credentials != null && credentials.isValid()) {
                        credentials.invalidate();
                        throw new RconInvalidCredentialsException(String.format(SourceRconAuthenticator.INVALID_CREDENTIALS_MSG, address), address);
                    }
                    throw new RconMaxLoginAttemptsException("Failed to authenticate with server. Maximum number of login attempts has been reached (" + rconRequestRetryPolicy.getConfig().getMaxAttempts() + ")", address);
                }
            }

            log.debug("AUTH => Sending RCON Request '{}' to address '{}' (Valid Credentials: {}, Attempts: {}, Cancelled: {}, Last Failure: {}, Last Result: {})", request, address, credentials != null && credentials.isValid(), context.getAttemptCount(), context.isCancelled(), context.getLastException(), context.getLastResult());
            return acquire().thenCompose(callback);
        }

        /**
         * Acquire a new/existing {@link Channel}
         *
         * @return A {@link CompletableFuture} that will be notified when the channel has been successfully acquired from the {@link FailsafeChannelFactory}
         */
        private CompletableFuture<SourceRconChannelContext> acquire() {
            return channelFactory.create(address)
                                 .thenCompose(SourceRconAuthManager.this::register)
                                 .handle(statistics)
                                 .thenApply(SourceRconChannelContext::getContext)
                                 .thenCombine(CompletableFuture.completedFuture(request), this::initializeContext);
        }

        private SourceRconChannelContext initializeContext(SourceRconChannelContext context, SourceRconRequest request) {
            log.debug("{} AUTH => Attaching request '{}' to context", context.id(), request);
            context.properties().request(request);
            return context;
        }
    }
}