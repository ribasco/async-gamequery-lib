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

package com.ibasco.agql.core;

import com.ibasco.agql.core.util.Netty;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.Platform;
import com.ibasco.agql.core.util.UUID;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation for all {@link com.ibasco.agql.core.Client} interfaces.
 *
 * @param <R>
 *         A type of {@link com.ibasco.agql.core.AbstractRequest}
 * @param <S>
 *         A type of {@link com.ibasco.agql.core.AbstractResponse}
 *
 * @author Rafael Luis Ibasco.
 */
public abstract class AbstractClient<R extends AbstractRequest, S extends AbstractResponse> implements Client {

    private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    static {
        Platform.initialize();
    }

    private final UUID id = UUID.create();

    private final Options options;

    private final AtomicReference<Messenger<R, S>> messengerRef = new AtomicReference<>();

    /**
     * Create a new client instance using the provided configuration options.
     *
     * @param options
     *         The {@link com.ibasco.agql.core.util.Options} containing the configuration
     *         options that will be used by the client
     *
     * @see OptionBuilder
     * @see Options
     */
    protected AbstractClient(Options options) {
        this.options = options;
    }

    /**
     * Send request.
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param request
     *         a R object
     * @param expectedResponse
     *         a {@link java.lang.Class} object
     * @param <V>
     *         a V class
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    protected <V extends S> CompletableFuture<V> send(
            InetSocketAddress address, R request, Class<V> expectedResponse) {
        return send(address, request).thenApply(expectedResponse::cast);
    }

    /**
     * Send request to messenger.
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} destination
     * @param request
     *         The {@link com.ibasco.agql.core.AbstractRequest} to be sent
     *
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has
     * been received.
     */
    protected final CompletableFuture<S> send(InetSocketAddress address, R request) {
        Objects.requireNonNull(address, "Address cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");
        log.debug(
                "{} SEND => Sending request '{}' to '{}' for messenger '{}' (Executor: {})",
                Netty.id(request),
                request,
                address,
                messenger().getClass().getSimpleName(),
                getExecutor());
        return messenger().send(address, request);
    }

    /**
     * The underlying {@link Messenger}.
     *
     * @return Returns an existing instance of the messenger. If no instance exists yet, initialize
     * then return
     */
    private Messenger<R, S> messenger() {
        Messenger<R, S> messenger = this.messengerRef.get();
        if (messenger == null) {
            messenger = createMessenger(this.options);
            if (!this.messengerRef.compareAndSet(null, messenger)) {
                return this.messengerRef.get();
            }
        }
        return messenger;
    }

    /**
     * Factory method for {@link Messenger}.
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     *
     * @return a {@link com.ibasco.agql.core.Messenger} object
     */
    protected abstract Messenger<R, S> createMessenger(Options options);

    /** {@inheritDoc} */
    @Override
    public UUID id() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public Executor getExecutor() {
        return messenger().getExecutor();
    }

    /**
     * Getter for the field <code>messenger</code>.
     *
     * @return a {@link com.ibasco.agql.core.Messenger} object
     */
    protected Messenger<R, S> getMessenger() {
        return messenger();
    }

    /**
     * @return The configuration {@link Options} used by this instance
     */
    public final Options getOptions() {
        return options;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        Messenger<R, S> messenger = this.messengerRef.get();
        if (messenger == null) {
            return;
        }
        messenger.close();
    }
}
