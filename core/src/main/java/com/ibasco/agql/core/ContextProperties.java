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

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public interface ContextProperties {
    boolean autoRelease();

    void autoRelease(boolean autoRelease);

    InetSocketAddress localAddress();

    InetSocketAddress remoteAddress();

    <V extends AbstractRequest> V request();

    void request(AbstractRequest request);

    <V extends AbstractResponse> V response();

    Throwable error();

    <A extends AbstractRequest> Envelope<A> envelope();

    CompletableFuture<NettyChannelContext> writePromise();

    <V extends AbstractResponse> CompletableFuture<V> responsePromise();

    /**
     * Reset context properties. This should re-initialize the response and write promises, clear error
     */
    void reset();
}
