/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.agql.protocols.valve.source.query.client;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.ribasco.agql.core.AbstractClient;
import org.ribasco.agql.core.enums.RequestPriority;
import org.ribasco.agql.protocols.valve.source.query.SourceRconMessenger;
import org.ribasco.agql.protocols.valve.source.query.SourceRconRequest;
import org.ribasco.agql.protocols.valve.source.query.SourceRconResponse;
import org.ribasco.agql.protocols.valve.source.query.exceptions.RconNotYetAuthException;
import org.ribasco.agql.protocols.valve.source.query.request.SourceRconAuthRequest;
import org.ribasco.agql.protocols.valve.source.query.request.SourceRconCmdRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A client used for executing commands to the Source Server using the Valve RCON Protocol
 *
 * @see <a href="https://developer.valvesoftware.com/wiki/Source_RCON_Protocol">Source RCON Protocol Specifications</a>
 */
public class SourceRconClient extends AbstractClient<SourceRconRequest, SourceRconResponse, SourceRconMessenger> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconClient.class);

    /**
     * Contains a map of authenticated request ids with the server address as the key
     */
    private Map<InetSocketAddress, Integer> authMap;

    /**
     * Default Constructor
     */
    public SourceRconClient() {
        super(new SourceRconMessenger());
        authMap = new ConcurrentHashMap<>();
    }

    /**
     * <p>Establish an authentication request to the Server.</p>
     *
     * @param address  The {@link InetSocketAddress} of the source server
     * @param password A non-empty password {@link String}
     *
     * @return A {@link CompletableFuture} which contains a {@link Boolean} value indicating whether the authentication succeeded or not.
     *
     * @throws IllegalArgumentException Thrown when the address or password supplied is empty or null
     */
    public CompletableFuture<Boolean> authenticate(InetSocketAddress address, String password) {
        if (StringUtils.isEmpty(password) || address == null)
            throw new IllegalArgumentException("Password or Address is empty or null");
        int id = createRequestId();
        log.debug("Requesting with id: {}", id);
        CompletableFuture<Integer> authRequestFuture = sendRequest(new SourceRconAuthRequest(address, id, password), RequestPriority.HIGH);
        return authRequestFuture.thenApply(requestId -> {
            if (requestId != null && requestId != -1) {
                authMap.put(address, requestId);
                return true;
            }
            return false;
        });
    }

    /**
     * <p>Sends a command to the Source server. Authentication is REQUIRED</p>
     *
     * @param address The {@link InetSocketAddress} of the source server
     * @param command The {@link String} containing the command to be issued on the server
     *
     * @return A {@link CompletableFuture} which contains a response {@link String} returned by the server
     *
     * @throws RconNotYetAuthException thrown if not yet authenticated to the server
     * @see #authenticate(InetSocketAddress, String)
     */
    public CompletableFuture<String> execute(InetSocketAddress address, String command) throws RconNotYetAuthException {
        if (!isAuthenticated(address))
            throw new RconNotYetAuthException("You are not yet authorized to access the server's rcon interface. Please authenticate first.");
        final Integer id = createRequestId();
        log.debug("Executing command '{}' using request id: {}", command, id);
        return sendRequest(new SourceRconCmdRequest(address, id, command));
    }

    /**
     * Returns the authentication id (also a request id) associated with the address specified.
     *
     * @param server An {@link InetSocketAddress} representing the server
     *
     * @return an {@link Integer} representing the request id used to authenticate with the server
     */
    private Integer getAuthenticationId(InetSocketAddress server) {
        return authMap.get(server);
    }

    /**
     * Checks the internal authentication map if the specified address is authenticated by the server or not
     *
     * @param server An {@link InetSocketAddress} representing the server
     *
     * @return true if the address specified is already authenticated
     */
    private boolean isAuthenticated(InetSocketAddress server) {
        return authMap.containsKey(server) && (authMap.get(server) != null);
    }

    /**
     * A utility method to generate random request ids
     *
     * @return An random integer ranging from 100000000 to 999999999
     */
    private int createRequestId() {
        return RandomUtils.nextInt(100000000, 999999999);
    }
}
