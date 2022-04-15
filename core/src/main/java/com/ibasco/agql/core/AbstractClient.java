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

import com.ibasco.agql.core.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base implementation for all {@link com.ibasco.agql.core.Client} interfaces
 *
 * @param <R>
 *         A type of {@link com.ibasco.agql.core.AbstractRequest}
 * @param <S>
 *         A type of {@link com.ibasco.agql.core.AbstractResponse}
 *
 * @author Rafael Luis Ibasco.
 */
abstract public class AbstractClient<R extends AbstractRequest, S extends AbstractResponse, O extends Options> implements Client {

    static {
        Platform.initialize();
    }

    private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    private final UUID id = UUID.create();

    private final O options;

    //private Messenger<R, S, O> messenger;
    private AtomicReference<Messenger<R, S, O>> messenger = new AtomicReference<>();

    /**
     * Create a new client instance using the provided configuration options.
     *
     * @param options
     *         The {@link com.ibasco.agql.core.util.Options} containing the configuration options that will be used by the client
     *
     * @see OptionBuilder
     * @see Options
     */
    protected AbstractClient(O options) {
        this.options = options;
    }

    /**
     * <p>createMessenger.</p>
     *
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     *
     * @return a {@link com.ibasco.agql.core.Messenger} object
     */
    abstract protected Messenger<R, S, O> createMessenger(O options);

    /**
     * <p>send.</p>
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
    protected <V extends S> CompletableFuture<V> send(InetSocketAddress address, R request, Class<V> expectedResponse) {
        return send(address, request).thenApply(expectedResponse::cast);
    }

    /**
     * <p>Send request to messenger</p>
     *
     * @param address
     *         The {@link java.net.InetSocketAddress} destination
     * @param request
     *         The {@link AbstractRequest} to be sent
     *
     * @return A {@link java.util.concurrent.CompletableFuture} that is notified once a response has been received.
     */
    protected final CompletableFuture<S> send(InetSocketAddress address, R request) {
        Objects.requireNonNull(address, "Address cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");
        log.debug("{} SEND => Sending request '{}' to '{}' for messenger '{}' (Executor: {})", Netty.id(request), request, address, messenger().getClass().getSimpleName(), getExecutor());
        return messenger().send(address, request);
    }

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
     * <p>Getter for the field <code>messenger</code>.</p>
     *
     * @return a {@link com.ibasco.agql.core.Messenger} object
     */
    protected Messenger<R, S, O> getMessenger() {
        return messenger();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        Messenger<R, S, O> messenger = this.messenger.get();
        if (messenger == null)
            return;
        messenger.close();
    }

    /**
     * @return Returns an existing instance of the messenger. If no instance exists yet, initialize then return
     */
    private Messenger<R, S, O> messenger() {
        Messenger<R, S, O> messenger = this.messenger.get();
        if (messenger == null) {
            //Console.println("Initializing messenger");
            messenger = createMessenger(this.options);
            if (!this.messenger.compareAndSet(null, messenger)) {
                return this.messenger.get();
            }
        }
        return messenger;
    }
}
