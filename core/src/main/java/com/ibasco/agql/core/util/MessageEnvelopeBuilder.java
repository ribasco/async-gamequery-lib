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

package com.ibasco.agql.core.util;

import com.ibasco.agql.core.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

public class MessageEnvelopeBuilder<M extends Message> {

    private SocketAddress recipient;

    private SocketAddress sender;

    private M message;

    private CompletableFuture<?> promise;

    private Messenger<?, ? extends AbstractRequest, ? extends AbstractResponse> messenger;

    private MessageEnvelopeBuilder(SocketAddress recipient) {
        this.recipient = recipient;
    }

    public static <V extends Message> MessageEnvelopeBuilder<V> createNew() {
        return new MessageEnvelopeBuilder<>(null);
    }

    public <A extends SocketAddress> MessageEnvelopeBuilder<M> recipient(A address) {
        this.recipient = address;
        return this;
    }

    public MessageEnvelopeBuilder<M> fromAnyAddress() {
        this.sender(new InetSocketAddress(0));
        return this;
    }

    public <A extends SocketAddress> MessageEnvelopeBuilder<M> sender(A address) {
        this.sender = address;
        return this;
    }

    public MessageEnvelopeBuilder<M> message(M content) {
        this.message = content;
        return this;
    }

    public <C> MessageEnvelopeBuilder<M> promise(CompletableFuture<C> promise) {
        this.promise = promise;
        return this;
    }

    public MessageEnvelopeBuilder<M> promise() {
        if (this.promise == null)
            promise = new CompletableFuture<>();
        return this;
    }

    public <A extends SocketAddress> MessageEnvelopeBuilder<M> messenger(Messenger<A, ? extends AbstractRequest, ? extends AbstractResponse> messenger) {
        this.messenger = messenger;
        return this;
    }

    public <A extends Message> Envelope<A> build() {
        //noinspection unchecked
        return new MessageEnvelope<A>((A) message, sender, recipient, promise, (Messenger<? extends SocketAddress, AbstractRequest, AbstractResponse>) messenger);
    }
}