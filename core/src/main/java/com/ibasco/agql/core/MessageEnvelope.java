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

import java.net.SocketAddress;

/**
 * <p>MessageEnvelope class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class MessageEnvelope<M extends Message> implements Envelope<M> {

    private SocketAddress sender;

    private SocketAddress recipient;

    private M message;

    /**
     * <p>Constructor for MessageEnvelope.</p>
     *
     * @param message a M object
     * @param sender a {@link java.net.SocketAddress} object
     * @param recipient a {@link java.net.SocketAddress} object
     */
    public MessageEnvelope(M message, SocketAddress sender, SocketAddress recipient/*, CompletableFuture<?> promise, Messenger<? extends SocketAddress, AbstractRequest, AbstractResponse> messenger*/) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    /** {@inheritDoc} */
    @Override
    public M content() {
        return this.message;
    }

    /** {@inheritDoc} */
    @Override
    public void content(M content) {
        this.message = content;
    }

    /** {@inheritDoc} */
    @Override
    public <A extends SocketAddress> A recipient() {
        //noinspection unchecked
        return (A) this.recipient;
    }

    /** {@inheritDoc} */
    @Override
    public <A extends SocketAddress> void recipient(A recipient) {
        this.recipient = recipient;
    }

    /** {@inheritDoc} */
    @Override
    public <A extends SocketAddress> A sender() {
        //noinspection unchecked
        return (A) this.sender;
    }

    /** {@inheritDoc} */
    @Override
    public <A extends SocketAddress> void sender(A sender) {
        this.sender = sender;
    }

    /** {@inheritDoc} */
    @Override
    public Envelope<M> get() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        String msgType = content() != null ? content().getClass().getSimpleName() : "N/A";
        return String.format("Msg: %s, Type: %s, From: %s, To: %s", Netty.id(this), msgType, sender(), recipient());
    }
}
