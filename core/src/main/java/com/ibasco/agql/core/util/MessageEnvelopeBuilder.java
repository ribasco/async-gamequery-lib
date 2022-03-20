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

package com.ibasco.agql.core.util;

import com.ibasco.agql.core.Envelope;
import com.ibasco.agql.core.Message;
import com.ibasco.agql.core.MessageEnvelope;
import org.jetbrains.annotations.ApiStatus;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@ApiStatus.Internal
public class MessageEnvelopeBuilder<M extends Message> {

    private SocketAddress recipient;

    private SocketAddress sender;

    private M message;

    private MessageEnvelopeBuilder(SocketAddress recipient) {
        this.recipient = recipient;
    }

    public static <V extends Message> MessageEnvelopeBuilder<V> createNew() {
        return new MessageEnvelopeBuilder<>(null);
    }

    public static <V extends Message> MessageEnvelopeBuilder<V> createFrom(Envelope<V> envelope) {
        return new MessageEnvelopeBuilder<V>(envelope.recipient())
                .sender(envelope.sender() == null ? new InetSocketAddress(0) : envelope.sender())
                .message(envelope.content());
    }

    public static <V extends Message> MessageEnvelopeBuilder<V> createFrom(Envelope<V> envelope, V message) {
        return new MessageEnvelopeBuilder<V>(envelope.recipient())
                .sender(envelope.sender() == null ? new InetSocketAddress(0) : envelope.sender())
                .message(message);
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

    public <A extends Message> Envelope<A> build() {
        //noinspection unchecked
        return new MessageEnvelope<A>((A) message, sender, recipient);
    }
}