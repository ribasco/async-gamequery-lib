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

import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.UUID;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * A netty based socket client.
 *
 * @param <R>
 *         A type of {@link com.ibasco.agql.core.AbstractRequest}
 * @param <S>
 *         A type of {@link com.ibasco.agql.core.AbstractResponse}
 * @author Rafael Luis Ibasco
 */
abstract public class NettySocketClient<R extends AbstractRequest, S extends AbstractResponse> extends AbstractClient<R, S> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UUID id = UUID.create();

    /**
     * Create a new client instance using the default configuration options.
     */
    protected NettySocketClient() {
        this(null);
    }

    /**
     * Create a new client instance using the provided configuration options.
     *
     * @param options
     *         The {@link com.ibasco.agql.core.util.Options} containing the configuration options that will be used by the client
     * @see OptionBuilder
     */
    protected NettySocketClient(Options options) {
        super(options);
        log.debug("[{}] CLIENT => Initialzied new client '{}' with ID '{}'", id.getInteger(), getClass().getSimpleName(), id().getInteger());
    }

    /** {@inheritDoc} */
    @Override
    abstract protected NettyMessenger<R, S> createMessenger(Options options);
    //</editor-fold>

    /** {@inheritDoc} */
    @Override
    protected NettyMessenger<R, S> getMessenger() {
        return (NettyMessenger<R, S>) super.getMessenger();
    }

    /** {@inheritDoc} */
    @Override
    public EventLoopGroup getExecutor() {
        return (EventLoopGroup) super.getExecutor();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        log.debug("[{}] CLIENT => Closing '{}'", id.getInteger(), getClass().getSimpleName());
        super.close();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NettySocketClient)) return false;
        NettySocketClient<?, ?> that = (NettySocketClient<?, ?>) o;
        return id.equals(that.id);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
