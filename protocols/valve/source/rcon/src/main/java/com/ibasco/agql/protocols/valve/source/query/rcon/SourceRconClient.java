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

import com.ibasco.agql.core.Credentials;
import com.ibasco.agql.core.CredentialsStore;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.NettySocketClient;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.*;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

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
 *     final String command = "status";
 *     //authenticate + execute
 *     CompletableFuture<SourceRconCmdResponse> responseFuture = rconClient.authenticate(serverAddress, password.getBytes())
 *                                                                         .thenCompose(authResponse -> {
 *                                                                             if (!authResponse.isAuthenticated())
 *                                                                                 throw new RconInvalidCredentialsException(String.format("Failed to authenticate address '%s' (Reason: %s)", authResponse.getAddress(), authResponse.getReason()), authResponse.getAddress());
 *                                                                             return rconClient.execute(authResponse.getAddress(), command);
 *                                                                         });
 *     //Check future if completed
 *     if (responseFuture.isDone()) {
 *         SourceRconCmdResponse response = responseFuture.getNow(null);
 *         System.out.println("RESPONSE: " + response.getResult());
 *     }
 *     //Register callback if not yet complete
 *     else {
 *         responseFuture.whenComplete((response, error) -> {
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
 *     final String command = "status";
 *     //authenticate + execute
 *     SourceRconCmdResponse response = rconClient.authenticate(serverAddress, password.getBytes())
 *                                                .thenCompose(authResponse -> {
 *                                                    if (!authResponse.isAuthenticated())
 *                                                        throw new RconInvalidCredentialsException(String.format("Failed to authenticate address '%s' (Reason: %s)", authResponse.getAddress(), authResponse.getReason()), authResponse.getAddress());
 *                                                    return rconClient.execute(authResponse.getAddress(), command);
 *                                                }).join();
 *     System.out.println("RESPONSE: " + response.getResult());
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
     * Create a new rcon client instance. By default, terminating packets are sent after every command
     */
    public SourceRconClient() {
        this(null);
    }

    /**
     * Create a new {@link SourceRconClient} using the provided configuration options.
     *
     * @param options
     *         The {@link Options} containing the configuration settings specific for this instance
     *
     * @see OptionBuilder
     */
    public SourceRconClient(Options options) {
        super(options);
    }

    /**
     * <p>Sends an authentication request to the specified address. If successful, the credentials for the specified address will be registered and stored in-memory.</p>
     *
     * <blockquote>
     * <strong>WARNING</strong>: <em>By default, the credentials stored in-memory are not encrypted, however you can implement and provide your own custom {@link CredentialsStore} which can be set via configuration.</em>
     * </blockquote>
     *
     * @param address
     *         The address of the source server
     * @param passphrase
     *         The rcon passphrase in byte array form
     *
     * @return A {@link CompletableFuture} when completed, returns a {@link SourceRconAuthResponse} that holds the status of the
     * authentication request.
     *
     * @throws RconAuthException
     *         When authentication fails. You need to check the concrete type of the exception to determine the root cause of the failure.
     * @throws IllegalArgumentException
     *         When the address or password supplied is empty or null
     * @see SourceRconOptions#CREDENTIALS_STORE
     * @see CredentialsStore
     * @see Credentials
     */
    public CompletableFuture<SourceRconAuthResponse> authenticate(InetSocketAddress address, byte[] passphrase) {
        if (passphrase == null || passphrase.length == 0)
            throw new IllegalArgumentException("Password is empty");
        return send(address, new SourceRconAuthRequest(passphrase), SourceRconAuthResponse.class);
    }

    /**
     * <p>Re-authenticate a previously registered address. The address should be authenticated (via {@link #authenticate(InetSocketAddress, byte[])}) and the credentials should still be valid, otherwise the returned future will fail.
     *
     * @param address
     *         The address of the source server
     *
     * @return A {@link CompletableFuture} when completed, returns a {@link SourceRconAuthResponse} which holds the status of the authentication request.
     *
     * @throws RconNotYetAuthException
     *         If the specified address has not yet been authenticated by the remote server. Authenticate with {@link #authenticate(InetSocketAddress, byte[])}
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public CompletableFuture<SourceRconAuthResponse> authenticate(InetSocketAddress address) throws RconAuthException {
        if (!getAuthenticationProxy().isAuthenticated(address))
            throw new RconNotYetAuthException(String.format("Address not yet authenticated by the server %s.", address), SourceRconAuthReason.NOT_AUTHENTICATED, address);
        return send(address, new SourceRconAuthRequest(), SourceRconAuthResponse.class);
    }

    /**
     * <p>Sends a command request to the server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param command
     *         The {@link String} containing the command to be issued on the server
     *
     * @return A {@link CompletableFuture} which contains a response {@link String} returned by the server
     *
     * @throws RconAuthException
     *         If the address is not yet authenticated by the server.
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public CompletableFuture<SourceRconCmdResponse> execute(InetSocketAddress address, String command) throws RconAuthException {
        if (!getAuthenticationProxy().isAuthenticated(address))
            return Concurrency.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not yet authenticated", address), SourceRconAuthReason.NOT_AUTHENTICATED, address));
        return send(address, new SourceRconCmdRequest(command), SourceRconCmdResponse.class);
    }

    /**
     * <p>Invalidates all connections for each registered address (registered via {@link #authenticate(InetSocketAddress, byte[])})</p>
     *
     * <p>
     * Once invalidated, the existing connections will be re-authenticated (assuming the credentials remain valid). If the credentials have changed, then you will need to explicitly call {@link #authenticate(InetSocketAddress, byte[])}.
     * </p>
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    @ApiStatus.Experimental
    public void invalidate() {
        getAuthenticationProxy().invalidate(true);
    }

    /**
     * Invalidates the specified address along with the previously authenticated connections associated with it. Once invalidated, the credentials associated with the address will be cleared,
     * so you will have to call {@link #authenticate(InetSocketAddress, byte[])} again.
     *
     * @param address
     *         The {@link InetSocketAddress} to invalidate.
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    @ApiStatus.Experimental
    public void invalidate(InetSocketAddress address) {
        getAuthenticationProxy().invalidate(address);
    }

    /**
     * Checks if the specified address is authenticated
     *
     * @param address
     *         An {@link InetSocketAddress} representing the server
     *
     * @return {@code true} if the address has been successfully been authenticated by the remote server
     *
     * @see #authenticate(InetSocketAddress, byte[])
     */
    public boolean isAuthenticated(InetSocketAddress address) {
        return getAuthenticationProxy().isAuthenticated(address);
    }

    @Override
    protected <V extends SourceRconResponse> CompletableFuture<V> send(InetSocketAddress address, SourceRconRequest request, Class<V> expectedResponse) {
        //generate a new rcon request id
        request.setRequestId(SourceRcon.createRequestId());
        log.debug("{} SEND => Creating new RCON request id '{}' ({})", Netty.id(request), request.getRequestId(), Math.abs(UUID.create().nextInteger()));
        return super.send(address, request, expectedResponse);
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void cleanup() {
        getAuthenticationProxy().cleanup();
    }

    @ApiStatus.Experimental
    public SourceRconAuthManager.Statistics getStatistics() {
        return getAuthenticationProxy().getStatistics();
    }

    private SourceRconAuthManager getAuthenticationProxy() {
        return getMessenger().getAuthManager();
    }

    @Override
    protected SourceRconMessenger getMessenger() {
        return (SourceRconMessenger) super.getMessenger();
    }

    @Override
    protected NettyMessenger<SourceRconRequest, SourceRconResponse> createMessenger(Options options) {
        return new SourceRconMessenger(options);
    }

}
