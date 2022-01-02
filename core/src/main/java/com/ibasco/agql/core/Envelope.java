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

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

public interface Envelope<C> {

    C content();

    void content(C content);

    <A extends SocketAddress> A recipient();

    <A extends SocketAddress> void recipient(A recipient);

    <A extends SocketAddress> A sender();

    <A extends SocketAddress> void sender(A sender);

    <V extends CompletableFuture<A>, A> V promise();

    <A extends SocketAddress> Messenger<A, AbstractRequest, AbstractResponse> messenger();

    <A extends SocketAddress> void messenger(Messenger<A, AbstractRequest, AbstractResponse> messenger);

    default boolean isCompleted() {
        if (promise() == null)
            throw new IllegalStateException("Promise not assigned to this envelope: " + this);
        return promise().isDone();
    }

    default boolean isError() {
        if (promise() == null)
            throw new IllegalStateException("Promise not assigned to this envelope: " + this);
        return promise().isCompletedExceptionally();
    }

    default boolean isCancelled() {
        if (promise() == null)
            throw new IllegalStateException("Promise not assigned to this envelope: " + this);
        return promise().isCancelled();
    }
}
