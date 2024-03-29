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

package com.ibasco.agql.core.exceptions;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.NettyChannelContext;
import org.jetbrains.annotations.ApiStatus;
import java.net.InetSocketAddress;

/**
 * <p>Represents a transaction error. Provides the underlying context that was used for the transaction. This should not be made available to the user. Concrete messengers should wrap this exception to avoid exposing the context to the user.</p>
 *
 * @author Rafael Luis Ibasco.
 */
@ApiStatus.Internal
public class MessengerException extends AgqlRuntimeException {

    private final NettyChannelContext context;

    /**
     * <p>Constructor for MessengerException.</p>
     *
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public MessengerException(NettyChannelContext context) {
        this.context = context;
    }

    /**
     * <p>Constructor for MessengerException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public MessengerException(String message, NettyChannelContext context) {
        super(message);
        this.context = context;
    }

    /**
     * <p>Constructor for MessengerException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public MessengerException(String message, Throwable cause, NettyChannelContext context) {
        super(message, cause);
        this.context = context;
    }

    /**
     * <p>Constructor for MessengerException.</p>
     *
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public MessengerException(Throwable cause, NettyChannelContext context) {
        super(cause);
        this.context = context;
    }

    /**
     * <p>Constructor for MessengerException.</p>
     *
     * @param message
     *         a {@link java.lang.String} object
     * @param cause
     *         a {@link java.lang.Throwable} object
     * @param enableSuppression
     *         a boolean
     * @param writableStackTrace
     *         a boolean
     * @param context
     *         a {@link com.ibasco.agql.core.NettyChannelContext} object
     */
    public MessengerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, NettyChannelContext context) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.context = context;
    }

    /**
     * The originating request of the transaction
     *
     * @param <R>
     *         a R class
     *
     * @return A request of type {@link com.ibasco.agql.core.AbstractRequest}
     */
    public <R extends AbstractRequest> R getRequest() {
        return context.properties().request();
    }

    /**
     * The local address of the connection used for this transaction
     *
     * @return An {@link java.net.InetSocketAddress} containing the host and port
     */
    public final InetSocketAddress getLocalAddress() {
        return context.localAddress();
    }

    /**
     * The remote address of the connection used for this transaction
     *
     * @return An {@link java.net.InetSocketAddress} containing the host and port
     */
    public final InetSocketAddress getRemoteAddress() {
        return context.remoteAddress();
    }

    /**
     * The underlying {@link com.ibasco.agql.core.NettyChannelContext} used for this transaction.
     *
     * @param <C>
     *         a C class
     *
     * @return The {@link com.ibasco.agql.core.NettyChannelContext} that was used for this transaction. {@code null} if the exception occured before a context has been acquired.
     */
    public final <C extends NettyChannelContext> C getContext() {
        //noinspection unchecked
        return (C) this.context;
    }
}
