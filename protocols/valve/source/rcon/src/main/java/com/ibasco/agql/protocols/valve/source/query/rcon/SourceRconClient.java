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

import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.ibasco.agql.core.Credentials;
import com.ibasco.agql.core.CredentialsStore;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.NettySocketClient;
import com.ibasco.agql.core.util.Concurrency;
import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconResponse;
import io.netty.channel.Channel;
import org.jetbrains.annotations.ApiStatus;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>An RCON client based on Valve's Source RCON Protocol.</p>
 *
 * <h3>Code example:</h3>
 *
 * <h4>Asynchronous (non-blocking)</h4>
 *
 * <pre>
 * try (SourceRconClient rconClient = new SourceRconClient()) {
 *     //status command
 *     final String command = &quot;status&quot;;
 *     //authenticate + execute
 *     CompletableFuture&lt;SourceRconCmdResponse&gt; responseFuture = rconClient.authenticate(serverAddress, password.getBytes())
 *                                                                         .thenCompose(authResponse -&gt; {
 *                                                                             if (!authResponse.isAuthenticated())
 *                                                                                 throw new CompletionException(String.format(&quot;Failed to authenticate address &#39;%s&#39; (Reason: %s)&quot;, authResponse.getAddress(), authResponse.getReason()), authResponse.getAddress());
 *                                                                             return rconClient.execute(authResponse.getAddress(), command);
 *                                                                         });
 *     //Check future if completed
 *     if (responseFuture.isDone()) {
 *         SourceRconCmdResponse response = responseFuture.getNow(null);
 *         System.out.println(&quot;RESPONSE: &quot; + response.getResult());
 *     }
 *     //Register callback if not yet complete
 *     else {
 *         responseFuture.whenComplete((response, error) -&gt; {
 *             if (error != null) {
 *                 error.printStackTrace(System.err);
 *                 return;
 *             }
 *             assert response != null;
 *             System.out.println(response.getResult());
 *         });
 *     }
 * }
 * </pre>
 *
 * <h4>Synchronous (blocking)</h4>
 *
 * <pre>
 * try (SourceRconClient rconClient = new SourceRconClient()) {
 *     //status command
 *     final String command = &quot;status&quot;;
 *     //authenticate + execute
 *     SourceRconCmdResponse response = rconClient.authenticate(serverAddress, password.getBytes())
 *                                                .thenCompose(authResponse -&gt; {
 *                                                    if (!authResponse.isAuthenticated())
 *                                                        throw new CompletionException(String.format(&quot;Failed to authenticate address &#39;%s&#39; (Reason: %s)&quot;, authResponse.getAddress(), authResponse.getReason()), authResponse.getAddress());
 *                                                    return rconClient.execute(authResponse.getAddress(), command);
 *                                                }).join();
 *     System.out.println(&quot;RESPONSE: &quot; + response.getResult());
 * }
 * </pre>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol">Source RCON Protocol Specifications</a>
 * @see SourceRconOptions
 */
public final class SourceRconClient extends NettySocketClient<SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconClient.class);

    /**
     * Create a new instance using the pre-defined configuration {@link com.ibasco.agql.core.util.Options} for this client
     */
    public SourceRconClient() {
        this(null);
    }

    /**
     * Create a new instance using the provided configuration {@link SourceRconOptions}
     *
     * @param options
     *         The {@link com.ibasco.agql.core.util.Options} instance containing the user-defined configuration options.
     *
     * @see Options
     * @see SourceRconOptions
     * @see OptionBuilder
     */
    public SourceRconClient(SourceRconOptions options) {
        super(options);
    }

    /**
     * <p>Sends an authentication request to the specified address. If successful, the credentials is then registered and the underlying connection is now managed internally. You will only need to call this once unless it has been invalidated. Use {@link #isAuthenticated(InetSocketAddress)} to check if the credentials of the address is still valid.</p>
     *
     * <blockquote>
     * <strong>WARNING</strong>: <em>By default, the credentials stored in-memory are not encrypted, however you can implement and provide your own custom {@link com.ibasco.agql.core.CredentialsStore} which can be set via configuration.</em>
     * </blockquote>
     *
     * @param address
     *         The address of the source server
     * @param passphrase
     *         The rcon passphrase in byte array form
     *
     * @return A {@link java.util.concurrent.CompletableFuture} when completed, returns a {@link com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse} that holds the status of the
     * authentication request.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the address or password supplied is empty or null
     * @see SourceRconOptions#CREDENTIALS_STORE
     * @see CredentialsStore
     * @see Credentials
     * @see #isAuthenticated(InetSocketAddress)
     */
    public CompletableFuture<SourceRconAuthResponse> authenticate(InetSocketAddress address, byte[] passphrase) {
        if (address == null)
            throw new IllegalArgumentException("Address must not be null");
        if (passphrase == null || passphrase.length == 0)
            throw new IllegalArgumentException("Password is empty");
        return send(address, new SourceRconAuthRequest(passphrase), SourceRconAuthResponse.class);
    }

    /** {@inheritDoc} */
    @Override
    protected <V extends SourceRconResponse> CompletableFuture<V> send(InetSocketAddress address, SourceRconRequest request, Class<V> expectedResponse) {
        //generate a new rcon request id
        request.setRequestId(SourceRcon.createRequestId());
        log.debug("{} SEND => Creating new RCON request id '{}'", Netty.id(request), request.getRequestId());
        return super.send(address, request, expectedResponse);
    }

    /**
     * <p>Re-authenticate a previously registered address. The address should be authenticated (via {@link #authenticate(InetSocketAddress, byte[])}) and the credentials should still be valid, or the returned future will fail. Use {@link #isAuthenticated(InetSocketAddress)} to check if the address is authenticated and registered.
     *
     * @param address
     *         The address of the source server
     *
     * @return A {@link java.util.concurrent.CompletableFuture} when completed, returns a {@link com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse} which holds the status of the authentication request.
     *
     * @throws com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException
     *         If the address has not yet been authenticated by the server
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public CompletableFuture<SourceRconAuthResponse> authenticate(InetSocketAddress address) {
        if (!isAuthenticated(address))
            throw new RconNotYetAuthException(String.format("Address not yet authenticated by the server %s.", address), null, address, SourceRconAuthReason.NOT_AUTHENTICATED);
        return send(address, new SourceRconAuthRequest(), SourceRconAuthResponse.class);
    }

    /**
     * Checks if the specified address is authenticated
     *
     * @param address
     *         An {@link java.net.InetSocketAddress} representing the server
     *
     * @return {@code true} if the address has been successfully been authenticated by the remote server
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public boolean isAuthenticated(InetSocketAddress address) {
        return getMessenger().isAuthenticated(address);
    }

    /**
     * <p>Sends a command request to the server</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} of the source server
     * @param command
     *         The {@link java.lang.String} containing the command to be issued on the server
     *
     * @return A {@link java.util.concurrent.CompletableFuture} which contains a response {@link java.lang.String} returned by the server
     *
     * @throws com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconAuthException
     *         If the address is not yet authenticated by the server.
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public CompletableFuture<SourceRconCmdResponse> execute(InetSocketAddress address, String command) throws RconAuthException {
        if (!isAuthenticated(address))
            return Concurrency.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not yet authenticated", address), null, address, SourceRconAuthReason.NOT_AUTHENTICATED));
        return send(address, new SourceRconCmdRequest(command), SourceRconCmdResponse.class);
    }

    /**
     * <p>Invalidates only the connections of all registered address (registered via {@link #authenticate(InetSocketAddress, byte[])}). The credentials registered with the address will remain valid. You do not need to call {@link #authenticate(InetSocketAddress, byte[])} unless the {@link Credentials} have been invalidated.</p>
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public void invalidate() {
        getMessenger().invalidate(true);
    }

    /**
     * <p>Invalidates only the connections of all registered address (registered via {@link #authenticate(InetSocketAddress, byte[])}). The credentials registered with the address will remain valid. You do not need to call {@link #authenticate(InetSocketAddress, byte[])} unless the {@link Credentials} have been invalidated.</p>
     *
     * @param onlyConnections
     *         {@code true} if we should only invalidate the {@link Channel}'s for the specified address.
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public void invalidate(boolean onlyConnections) {
        getMessenger().invalidate(onlyConnections);
    }

    /**
     * Invalidates the specified address along with the previously authenticated connections. Once invalidated, the credentials associated with the address will be invalidated and cleared,
     * so you will have to call {@link #authenticate(InetSocketAddress, byte[])} again.
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} to invalidate.
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public void invalidate(InetSocketAddress address) {
        getMessenger().invalidate(address);
    }

    /**
     * <p>Request to release/close inactive connections. This is similar to calling {@code cleanup(false)}</p>
     *
     * @see #cleanup(boolean)
     */
    public void cleanup() {
        getMessenger().cleanup(false);
    }

    /**
     * <p>Request to release/close connections</p>
     *
     * @param force
     *         {@code true} to force release/close all connections, otherwise {@code false} to only release/close inactive connections.
     *
     * @see #cleanup()
     */
    public void cleanup(boolean force) {
        getMessenger().cleanup(force);
    }

    /**
     * <p>Rcon connection statistics</p>
     *
     * @return A {@link Multimap} containing statistical information about the active connections for each registered address
     */
    @ApiStatus.Experimental
    public SetMultimap<InetSocketAddress, SourceRconMessenger.ConnectionStats> getStatistics() {
        return getMessenger().getStatistics().getConnectionStats();
    }

    @ApiStatus.Experimental
    public void printExecutorStats(Consumer<String> output) {
        getMessenger().getStatistics().printExecutorStats(output);
    }

    @ApiStatus.Experimental
    public void printConnectionStats(Consumer<String> output) {
        getMessenger().getStatistics().printConnectionStats(output);
    }

    /** {@inheritDoc} */
    @Override
    protected NettyMessenger<SourceRconRequest, SourceRconResponse> createMessenger(Options options) {
        return new SourceRconMessenger(options);
    }

    /** {@inheritDoc} */
    @Override
    protected SourceRconMessenger getMessenger() {
        return (SourceRconMessenger) super.getMessenger();
    }

}
