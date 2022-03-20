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

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The {@link Messenger} acts as the middle man between the {@link Client} and the {@link Transport}.
 *
 * @author Rafael Luis Ibasco
 */
public interface Messenger<R extends AbstractRequest, S extends AbstractResponse> extends Closeable, Configurable {

    CompletableFuture<S> send(InetSocketAddress address, R request);

    Transport<?, ?> getTransport();

    Executor getExecutor();
}
