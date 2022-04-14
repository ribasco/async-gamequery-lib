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

import com.ibasco.agql.core.util.Configurable;
import com.ibasco.agql.core.util.Options;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The following are characteristics/roles of a Messenger
 *
 * <ul>
 *     <li>Responsible for creating, acquiring, closing or releasing a network resource (e.g. {@link io.netty.channel.Channel}/Connection)</li>
 *     <li>Prepares and initialize the request for {@link com.ibasco.agql.core.Transport}</li>
 *     <li>Receives a response from the remote server, performs additional processing (if needed) and routes the response back to the {@link com.ibasco.agql.core.Client}</li>
 *     <li>Has a 1 to 1 relationship with the {@link com.ibasco.agql.core.Client}</li>
 * </ul>
 *
 * @author Rafael Luis Ibasco
 */
public interface Messenger<R extends AbstractRequest, S extends AbstractResponse, O extends Options> extends Closeable, Configurable<O> {

    /**
     * <p>send.</p>
     *
     * @param address
     *         a {@link java.net.InetSocketAddress} object
     * @param request
     *         a R object
     *
     * @return a {@link java.util.concurrent.CompletableFuture} object
     */
    CompletableFuture<S> send(InetSocketAddress address, R request);

    /**
     * <p>getTransport.</p>
     *
     * @return a {@link com.ibasco.agql.core.Transport} object
     */
    Transport<?, ?> getTransport();

    /**
     * <p>getExecutor.</p>
     *
     * @return a {@link java.util.concurrent.Executor} object
     */
    Executor getExecutor();
}
