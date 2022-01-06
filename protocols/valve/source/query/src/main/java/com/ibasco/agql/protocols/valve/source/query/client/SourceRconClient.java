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

package com.ibasco.agql.protocols.valve.source.query.client;

import com.ibasco.agql.core.NettyClient;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.util.*;
import com.ibasco.agql.protocols.valve.source.query.*;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.message.*;
import io.netty.channel.Channel;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * An RCON client based on Valve's Source RCON Protocol.
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol">Source RCON Protocol Specifications</a>
 */
public class SourceRconClient extends NettyClient<InetSocketAddress, SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconClient.class);

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    private Executor executor;

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    private Boolean sendTerminatingPacket;

    /**
     * Create a new rcon client instance. By default, terminating packets are sent after every command
     */
    public SourceRconClient() {
        this(null);
    }

    /**
     * Some games (e.g. Minecraft) do not support terminator packets, if this is the case and you get an
     * error after sending a command, try to disable this feature by setting the <code>sendTerminatingPacket</code> flag
     * to <code>false</code>.
     *
     * @param sendTerminatingPacket
     *         Set to <code>true</code> to send terminator packets for every command.
     *
     * @deprecated Use {@link #SourceRconClient(OptionMap)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public SourceRconClient(boolean sendTerminatingPacket) {
        this(sendTerminatingPacket, null);
    }

    /**
     * Some games (e.g. Minecraft) do not support terminator packets, if this is the case and you get an
     * error after sending a command, try to disable this feature by setting the <code>sendTerminatingPacket</code> flag
     * to <code>false</code>.
     *
     * @param sendTerminatingPacket
     *         Set to <code>true</code> to send terminator packets for every command.
     * @param executor
     *         The {@link Executor} to be used by the underlying transport
     *
     * @deprecated Use {@link #SourceRconClient(OptionMap)}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public SourceRconClient(boolean sendTerminatingPacket, Executor executor) {
        this(null);
        this.executor = executor;
        this.sendTerminatingPacket = sendTerminatingPacket;
    }

    /**
     * Create a new {@link SourceRconClient} using the provided configuration options.
     *
     * @param options
     *         The {@link OptionMap} containing the configuration settings specific for this instance
     *
     * @see OptionBuilder
     */
    public SourceRconClient(OptionMap options) {
        super(options);
    }

    @Override
    protected NettyMessenger<InetSocketAddress, SourceRconRequest, SourceRconResponse> createMessenger(OptionMap options) {
        //apply default options
        if (this.executor != null)
            options.add(TransportOptions.THREAD_POOL_EXECUTOR, this.executor);
        if (this.sendTerminatingPacket != null)
            options.add(SourceRconOptions.USE_TERMINATOR_PACKET, this.sendTerminatingPacket);
        return new SourceRconMessenger(options);
    }

    /**
     * <p>Send an authentication request to the Server for the specified address. Upon successful authentication, the credentials for the specified address will be registered and stored in-memory.
     * New connections will automatically be authenticated, so there is no need to call this method for every command request unless the credentials have been invalidated</p>
     *
     * @param address
     *         The address of the source server
     * @param password
     *         A non-empty password {@link String}
     *
     * @return A {@link CompletableFuture} when completed, returns a {@link SourceRconAuthStatus} that holds the status of the
     * authentication request.
     *
     * @throws IllegalArgumentException
     *         Thrown when the address or password supplied is empty or null
     */
    public CompletableFuture<SourceRconAuthStatus> authenticate(InetSocketAddress address, String password) {
        return authenticate(address, password.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * <p>Send an authentication request to the Server for the specified address. Upon successful authentication, the credentials for the specified address will be registered and stored in-memory.
     * New connections will automatically be authenticated, so there is no need to call this method for every command request unless the credentials have been invalidated</p>
     *
     * @param address
     *         The address of the source server
     * @param password
     *         A passphrase in byte array form
     *
     * @return A {@link CompletableFuture} when completed, returns a {@link SourceRconAuthStatus} that holds the status of the
     * authentication request.
     *
     * @throws IllegalArgumentException
     *         Thrown when the address or password supplied is empty or null
     */
    public CompletableFuture<SourceRconAuthStatus> authenticate(InetSocketAddress address, byte[] password) {
        if (password == null || password.length == 0)
            throw new IllegalArgumentException("Password is empty");
        return send(address, new SourceRconAuthRequest(password), SourceRconAuthResponse.class).thenApply(SourceRconAuthResponse::toAuthStatus);
    }

    /**
     * <p>Re-send an authentication request to the remote server. This assumes that th address has been previously authenticated.
     *
     * @param address
     *         The address of the source server
     *
     * @return A {@link CompletableFuture} when completed, returns a {@link SourceRconAuthStatus} which holds the status of the authentication request.
     *
     * @throws RconNotYetAuthException
     *         If the specified address has not yet been authenticated by the remote server. Please authenticate by {@link #authenticate(InetSocketAddress, byte[])}
     * @see #authenticate(InetSocketAddress, byte[])
     * @see #authenticate(InetSocketAddress, String)
     */
    public CompletableFuture<SourceRconAuthStatus> authenticate(InetSocketAddress address) throws RconNotYetAuthException {
        if (!getAuthenticationProxy().isAuthenticated(address))
            throw new RconNotYetAuthException(String.format("Address not yet authenticated by the server %s.", address), SourceRconAuthReason.NOT_AUTHENTICATED);
        return send(address, new SourceRconAuthRequest(), SourceRconAuthResponse.class).thenApply(SourceRconAuthResponse::toAuthStatus);
    }

    /**
     * <p>Sends a command to the Source server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param command
     *         The {@link String} containing the command to be issued on the server
     *
     * @return A {@link CompletableFuture} which contains a response {@link String} returned by the server
     *
     * @throws RconNotYetAuthException
     *         thrown if not yet authenticated to the server
     * @see #authenticate(InetSocketAddress, String)
     */
    public CompletableFuture<String> execute(InetSocketAddress address, String command) throws RconNotYetAuthException {
        return exec(address, command).thenApply(SourceRconCmdResponse::getResult);
    }

    /**
     * <p>Sends a command to the Source server</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     * @param command
     *         The {@link String} containing the command to be issued on the server
     *
     * @return A {@link CompletableFuture} which returns a {@link SourceRconCmdResponse} on completion
     *
     * @see #authenticate(InetSocketAddress, String)
     * @since 0.2.0
     */
    @ApiStatus.Experimental
    public CompletableFuture<SourceRconCmdResponse> exec(InetSocketAddress address, String command) {
        if (!getAuthenticationProxy().isAuthenticated(address))
            return ConcurrentUtil.failedFuture(new RconNotYetAuthException(String.format("Address '%s' not yet authenticated", address)));
        return send(address, new SourceRconCmdRequest(command), SourceRconCmdResponse.class);
    }

    /**
     * <p>Invalidates the connections for all registered addresses (registered via {@link #authenticate(InetSocketAddress, byte[])})</p>
     *
     * <p>
     * Once invalidated, the existing connections will be re-authenticated (assuming the credentials remain valid). If the credentials have changed, then you will need to explicitly call {@link #authenticate(InetSocketAddress, byte[])}.
     * </p>
     *
     * @see #authenticate(InetSocketAddress, byte[])
     * @see #authenticate(InetSocketAddress, String)
     * @since 0.2.0
     */
    public void invalidate() {
        getAuthenticationProxy().invalidate(true);
    }

    /**
     * Invalidates the specified address along with the previously authenticated connections associated with it. Once invalidated, the credentials stored in-memory for
     * the specified address will be cleared so you will have to call {@link #authenticate(InetSocketAddress, byte[])} again.
     *
     * @param address
     *         The {@link InetSocketAddress} to invalidate.
     *
     * @see #authenticate(InetSocketAddress, byte[])
     * @see #authenticate(InetSocketAddress, String)
     * @since 0.2.0
     */
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
     * @see #authenticate(InetSocketAddress, String)
     */
    public boolean isAuthenticated(InetSocketAddress address) {
        return getAuthenticationProxy().isAuthenticated(address);
    }

    /**
     * @return <code>true</code> if the client should re-authenticate on error
     */
    public boolean isReauthenticate() {
        return getMessenger().getOrDefault(SourceRconOptions.REAUTH);
    }

    /**
     * Re-authenticate from server on error
     *
     * @param reauthenticate
     *         Set to <code>true</code> to re-authenticate from server on error
     *
     * @see SourceRconOptions#REAUTH
     * @deprecated Use {@link #SourceRconClient(OptionMap)}
     */
    @Deprecated
    public void setReauthenticate(boolean reauthenticate) {
        getMessenger().set(SourceRconOptions.REAUTH, reauthenticate);
    }

    @Override
    protected <V extends SourceRconResponse> CompletableFuture<V> send(InetSocketAddress address, SourceRconRequest request, Class<V> expectedResponse) {
        //generate a new rcon request id
        request.setRequestId(SourceRcon.createRequestId());
        log.debug("{} SEND => Creating new RCON request id '{}' ({})", NettyUtil.id(request), request.getRequestId(), Math.abs(UUID.create().nextInteger()));
        return super.send(address, request).thenApply(expectedResponse::cast);
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void cleanup() {
        getAuthenticationProxy().cleanup();
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void printStatistics() {
        getAuthenticationProxy().printStatistics();
    }

    @ApiStatus.Experimental
    @ApiStatus.Internal
    public void printStatistics(Consumer<String> output) {
        getAuthenticationProxy().printStatistics(output);
    }

    @ApiStatus.Experimental
    public Map<Channel, SourceRconAuthProxy.ChannelMetadata> getChannelStatistics() {
        return getAuthenticationProxy().getStatistics();
    }

    private SourceRconAuthProxy getAuthenticationProxy() {
        return getMessenger().getAuthenticationProxy();
    }

    @Override
    protected SourceRconMessenger getMessenger() {
        return (SourceRconMessenger) super.getMessenger();
    }
}
