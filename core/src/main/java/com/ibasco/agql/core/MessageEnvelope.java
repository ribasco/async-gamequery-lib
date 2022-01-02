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

import com.ibasco.agql.core.util.NettyUtil;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

public class MessageEnvelope<M extends Message> implements Envelope<M> {

    private final CompletableFuture<?> promise;

    private SocketAddress sender;

    private SocketAddress recipient;

    private Messenger<? extends SocketAddress, AbstractRequest, AbstractResponse> messenger;

    private M message;

    public MessageEnvelope(M message, SocketAddress sender, SocketAddress recipient, CompletableFuture<?> promise, Messenger<? extends SocketAddress, AbstractRequest, AbstractResponse> messenger) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.promise = promise;
        this.messenger = messenger;
    }

    @Override
    public M content() {
        return this.message;
    }

    @Override
    public void content(M content) {
        this.message = content;
    }

    @Override
    public <A extends SocketAddress> A recipient() {
        //noinspection unchecked
        return (A) this.recipient;
    }

    @Override
    public <A extends SocketAddress> void recipient(A recipient) {
        this.recipient = recipient;
    }

    @Override
    public <A extends SocketAddress> A sender() {
        //noinspection unchecked
        return (A) this.sender;
    }

    @Override
    public <A extends SocketAddress> void sender(A sender) {
        this.sender = sender;
    }

    @Override
    public <V extends CompletableFuture<A>, A> V promise() {
        //noinspection unchecked
        return (V) this.promise;
    }

    @Override
    public <A extends SocketAddress> Messenger<A, AbstractRequest, AbstractResponse> messenger() {
        //noinspection unchecked
        return (Messenger<A, AbstractRequest, AbstractResponse>) this.messenger;
    }

    @Override
    public <A extends SocketAddress> void messenger(Messenger<A, AbstractRequest, AbstractResponse> messenger) {
        this.messenger = messenger;
    }

    @Override
    public String toString() {
        String msgType = content() != null ? content().getClass().getSimpleName() : "N/A";
        return String.format("Msg: %s, Type: %s, From: %s, To: %s (Promise: %s)", NettyUtil.id(this), msgType, sender(), recipient(), promise());
    }
}
