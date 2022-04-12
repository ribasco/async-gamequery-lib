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
import com.ibasco.agql.core.exceptions.ChannelClosedException;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconInvalidCredentialsException;
import com.ibasco.agql.protocols.valve.source.query.rcon.exceptions.RconNotYetAuthException;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconAuthResponse;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * The default {@link com.ibasco.agql.protocols.valve.source.query.rcon.RconAuthenticator}.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceRconAuthenticator implements RconAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(SourceRconAuthenticator.class);

    private final boolean reauthenticate;

    private final CredentialsStore credentialsStore;

    static final String INVALID_CREDENTIALS_MSG = "The credentials for address '%s' has been invalidated. Please re-authenticate with the server";

    static final String NOT_YET_AUTH_MSG = "Address '%s' has not yet been authenticated by the server. Use authenticate()";

    /**
     * <p>Constructor for SourceRconAuthenticator.</p>
     *
     * @param credentialsStore a {@link com.ibasco.agql.core.CredentialsStore} object
     * @param reauthenticate a boolean
     */
    public SourceRconAuthenticator(CredentialsStore credentialsStore, boolean reauthenticate) {
        this.credentialsStore = credentialsStore;
        this.reauthenticate = reauthenticate;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<SourceRconChannelContext> authenticate(SourceRconChannelContext context) {
        //check request type
        final InetSocketAddress address = context.remoteAddress();
        final SourceRconRequest request = context.properties().request();
        assert address.equals(context.remoteAddress());

        if (request == null)
            throw new IllegalStateException("Request is empty");

        //Make sure context is still valid
        if (!context.isValid()) {
            throw new IllegalStateException("Failed to authenticate (Connection has been closed)", new ChannelClosedException(context.channel()));
        }

        CompletableFuture<SourceRconChannelContext> future;
        //if we have an auth request, if successful, register/update the credentials
        if (request instanceof SourceRconAuthRequest) {
            SourceRconAuthRequest authRequest = (SourceRconAuthRequest) request;
            ///if no password was supplied, then we need to check the credentials manager
            // if we have an existing valid credentials registered for the address
            if (authRequest.getPassword() == null) {
                Credentials credentials = credentialsStore.get(address);
                if (credentials == null)
                    throw new RconNotYetAuthException(String.format(NOT_YET_AUTH_MSG, address), SourceRconAuthReason.NOT_AUTHENTICATED, address);
                //make sure the credentials are still valid
                if (!credentials.isValid())
                    throw new RconInvalidCredentialsException(String.format(INVALID_CREDENTIALS_MSG, address), address);
                //Update credentials
                authRequest.setPassword(credentials.getPassphrase());
            }
            //send authentication request, then update/save the credentials
            future = context.send().thenApply(this::processAuthResponse);
        } else if (request instanceof SourceRconCmdRequest) {
            //is the context/channel already authenticated?
            if (context.properties().authenticated()) {
                //if authenticated already, just return the context and let the caller send the message
                future = CompletableFuture.completedFuture(context);
            } else {
                final Credentials credentials = credentialsStore.get(address);
                //Do we a valid credential registered for the address
                if (credentials == null)
                    throw new RconNotYetAuthException(String.format(NOT_YET_AUTH_MSG, address), SourceRconAuthReason.NOT_AUTHENTICATED, address);
                //Are the credentials still valid?
                if (!credentials.isValid())
                    throw new RconInvalidCredentialsException(String.format(INVALID_CREDENTIALS_MSG, address), address);
                //make sure the passphrase is not null or empty
                if (credentials.isEmpty())
                    throw new IllegalStateException("Passphrase must not be null or empty");
                //is the re-authentication flag set?
                if (!reauthenticate)
                    throw new RconNotYetAuthException(String.format(NOT_YET_AUTH_MSG, address), SourceRconAuthReason.INVALIDATED, address);
                final SourceRconAuthRequest newAuthRequest = SourceRcon.createAuthRequest(credentials);
                log.debug("{} AUTHENTICATOR => Sending new AUTH request '{}'", context.id(), newAuthRequest);

                //We need to send a new AUTH request using the SAME context, thus we need to save the previous request and restore it later
                context.save().properties().request(newAuthRequest);
                future = context.send().thenApply(this::processAuthResponse).thenApply(SourceRconChannelContext::restore);
            }
        } else {
            throw new IllegalStateException("Invalid rcon request");
        }
        return future;
    }

    private SourceRconChannelContext processAuthResponse(final SourceRconChannelContext context) {
        final InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
        final SourceRconAuthRequest request = context.properties().request();
        if (!context.hasResponse())
            throw new IllegalStateException("Context does not contain a response");
        //make sure we have an auth response
        if (!(context.properties().response() instanceof SourceRconAuthResponse))
            throw new IllegalStateException("Request/Response Mismatch. Expected SourceRconAuthResponse (Actual: " + context.properties().response() + ")");

        final SourceRconAuthResponse response = context.properties().response();

        //Was the authentication succeessful?
        if (response.isAuthenticated()) {
            //Save or update credentials when successful (this will override existing credentials)
            if (context.channel().remoteAddress() == null)
                throw new IllegalStateException("No remote address present");

            //set context authentication property
            context.properties().authenticated(true);

            //save/update credentials
            credentialsStore.add(address, request.getPassword());

            log.debug("{} AUTH => Successfully authenticated address '{}' (Request: {})", context.id(), address, request);
        } else {
            //unset context authentication property
            context.properties().authenticated(false);

            final Credentials credentials = credentialsStore.get(address);
            boolean credentialsPresent = credentials != null;
            boolean credentialsValid = credentialsPresent && credentials.isValid();
            log.debug("{} AUTH => Authentication failed due to bad credentials. Invalidating credentials (Existing credentials? : {}, Credentials valid? {})", context.id(), credentialsPresent, credentialsValid);

            //if we have an existing credentials registered
            if (credentialsPresent && credentialsValid) {
                credentials.invalidate();
                throw new CompletionException(new RconInvalidCredentialsException(String.format(INVALID_CREDENTIALS_MSG, address), address));
            }
        }
        return context;
    }
}
