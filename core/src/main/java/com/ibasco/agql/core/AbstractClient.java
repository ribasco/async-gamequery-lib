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

package com.ibasco.agql.core;

import com.ibasco.agql.core.util.NettyUtil;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.UUID;
import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
import dev.failsafe.event.EventListener;
import dev.failsafe.event.ExecutionCompletedEvent;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;

/**
 * Base implementation for all {@link Client} interfaces
 *
 * @param <A>
 *         A type of {@link SocketAddress}
 * @param <R>
 *         A type of {@link AbstractRequest}
 * @param <S>
 *         A type of {@link AbstractResponse}
 *
 * @author Rafael Luis Ibasco.
 */
abstract public class AbstractClient<A extends SocketAddress, R extends AbstractRequest, S extends AbstractResponse> implements Client {

    private final Logger log;

    private final UUID id = UUID.create();

    private final OptionMap options;

    private Messenger<A, R, S> messenger;

    private final ClientStatistics<S> statsCollector = new ClientStatistics<>();

    protected final RetryPolicy<S> DEFAULT_RETRY_POLICY = RetryPolicy.<S>builder()
                                                                     .handle(ReadTimeoutException.class, WriteTimeoutException.class)
                                                                     .withMaxAttempts(3)
                                                                     .withBackoff(Duration.ofSeconds(3), Duration.ofSeconds(6))
                                                                     .abortOn(ConnectTimeoutException.class)
                                                                     .onFailure(statsCollector.onFailure)
                                                                     .onSuccess(statsCollector.onSuccess)
                                                                     .onAbort(statsCollector.onAbort)
                                                                     .onRetriesExceeded(statsCollector.onRetriesExceeded)
                                                                     .build();

    private FailsafeExecutor<S> failsafeExecutor;

    /**
     * Create a new client instance using the default configuration options.
     */
    protected AbstractClient() {
        this(null);
    }

    /**
     * Create a new client instance using the provided configuration options.
     *
     * @param options
     *         The {@link OptionMap} containing the configuration options that will be used by the client
     *
     * @see OptionBuilder
     * @see OptionMap
     */
    protected AbstractClient(OptionMap options) {
        if (options == null)
            options = new OptionMap(this.getClass());
        this.options = options;
        this.log = LoggerFactory.getLogger(getClass());
    }

    abstract protected Messenger<A, R, S> createMessenger(OptionMap options);

    protected final CompletableFuture<S> send(A address, R request) {
        Objects.requireNonNull(address, "Address cannot be null");
        Objects.requireNonNull(request, "Request cannot be null");
        log.debug("{} SEND => Sending request '{}' to '{}' for messenger '{}' (Executor: {})", NettyUtil.id(request), request, address, messenger().getClass().getSimpleName(), getExecutor());
        return messenger().send(address, request);
    }

    protected <V extends S> CompletableFuture<V> send(A address, R request, Class<V> expectedResponse) {
        log.debug("{} SEND => Sending request '{}' to '{}' for messenger '{}' (Executor: {})", NettyUtil.id(request), request, address, messenger().getClass().getSimpleName(), getExecutor());
        return failSafeSend(address, request).thenApply(expectedResponse::cast);
    }

    protected CompletableFuture<S> failSafeSend(A address, R request) {
        return failSafeExecutor().getStageAsync(context -> send(address, request));
    }

    protected void configureFailsafe(FailsafeExecutor<S> executor) {}

    protected FailsafeExecutor<S> createFailsafeExecutor() {
        return Failsafe.with(DEFAULT_RETRY_POLICY).with((ScheduledExecutorService) getExecutor());
    }

    protected final FailsafeExecutor<S> failSafeExecutor() {
        if (this.failsafeExecutor == null) {
            this.failsafeExecutor = createFailsafeExecutor();
            configureFailsafe(this.failsafeExecutor);
        }
        return this.failsafeExecutor;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public Executor getExecutor() {
        return messenger().getExecutor();
    }

    protected Messenger<A, R, S> getMessenger() {
        return messenger();
    }

    @Override
    public void close() throws IOException {
        if (messenger == null)
            return;
        messenger.close();
    }

    /**
     * @return Returns an existing instance of the messenger. If no instance exists yet, initialize then return
     */
    private Messenger<A, R, S> messenger() {
        if (this.messenger == null) {
            this.messenger = createMessenger(this.options);
        }
        return messenger;
    }

    public ClientStatistics<S> getClientStatistics() {
        return statsCollector;
    }

    public static class ClientStatistics<S> {

        public enum Stat {
            RETRY,
            SUCCESS,
            FAIL,
            ABORT,
            SUCCESS_ATTEMPT,
            RETRY_EXCEEDED
        }

        private final Map<Stat, Integer> counter = new HashMap<>();

        private final EventListener<ExecutionCompletedEvent<S>> onFailure = event -> increment(Stat.FAIL);

        private final EventListener<ExecutionCompletedEvent<S>> onSuccess = event -> {
            if (event.isFirstAttempt())
                increment(Stat.SUCCESS);
            else
                increment(Stat.SUCCESS_ATTEMPT);
        };

        private final EventListener<ExecutionCompletedEvent<S>> onRetry = event -> increment(Stat.RETRY);

        private final EventListener<ExecutionCompletedEvent<S>> onAbort = event -> increment(Stat.ABORT);

        private final EventListener<ExecutionCompletedEvent<S>> onRetriesExceeded = event -> increment(Stat.RETRY_EXCEEDED);

        public Map<Stat, Integer> getValues() {
            return new HashMap<>(counter);
        }

        public void reset() {
            counter.clear();
        }

        private void increment(Stat key) {
            counter.compute(key, new BiFunction<Stat, Integer, Integer>() {
                @Override
                public Integer apply(Stat stat, Integer integer) {
                    if (integer == null)
                        return 0;
                    return integer + 1;
                }
            });
        }
    }
}
