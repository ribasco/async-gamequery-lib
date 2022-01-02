/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.UUID;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A netty based socket client.
 *
 * @param <A>
 *         The {@link SocketAddress} of the remote server
 * @param <R>
 *         A type of {@link AbstractRequest}
 * @param <S>
 *         A type of {@link AbstractResponse}
 *
 * @author Rafael Luis Ibasco
 */
abstract public class NettyClient<A extends SocketAddress, R extends AbstractRequest, S extends AbstractResponse> extends AbstractClient<A, R, S> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UUID id = UUID.create();

    /**
     * Create a new client instance using the default configuration options.
     */
    protected NettyClient() {
        this(null);
    }

    /**
     * Create a new client instance using the provided configuration options.
     *
     * @param options
     *         The {@link OptionMap} containing the configuration options that will be used by the client
     *
     * @see OptionBuilder
     */
    protected NettyClient(OptionMap options) {
        super(options);
        log.debug("[{}] CLIENT => Initialzied new client '{}' with ID '{}'", id.getInteger(), getClass().getSimpleName(), id().getInteger());
    }

    @Override
    abstract protected NettyMessenger<A, R, S> createMessenger(OptionMap options);

    //<editor-fold desc="Netty specific send functions">
    protected CompletableFuture<S> send(A address, R request, Channel channel) {
        return getMessenger().send(address, request, channel);
    }

    protected CompletableFuture<S> send(A address, R request, CompletableFuture<Channel> channelFuture) {
        return getMessenger().send(address, request, channelFuture);
    }

    protected CompletableFuture<S> failSafeSend(final A address, final R request, final Channel channel) {
        return failSafeExecutor().getStageAsync(context -> send(address, request, channel));
    }

    protected CompletableFuture<S> failSafeSend(final A address, R request, final CompletableFuture<Channel> channelFuture) {
        return failSafeExecutor().getStageAsync(context -> send(address, request, channelFuture));
    }
    //</editor-fold>

    @Override
    protected NettyMessenger<A, R, S> getMessenger() {
        return (NettyMessenger<A, R, S>) super.getMessenger();
    }

    @Override
    public EventLoopGroup getExecutor() {
        return (EventLoopGroup) super.getExecutor();
    }

    @Override
    public void close() throws IOException {
        log.debug("[{}] CLIENT => Closing '{}'", id.getInteger(), getClass().getSimpleName());
        super.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NettyClient)) return false;
        NettyClient<?, ?, ?> that = (NettyClient<?, ?, ?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
