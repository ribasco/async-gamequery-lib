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
import io.netty.channel.Channel;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * A Messenger is responsible for:
 *
 * <ul>
 *     <li>Creating, Acquiring, Closing or Releasing a network resource (e.g. {@link Channel}/Connection)</li>
 *     <li>Prepare and initialize the request for {@link Transport}</li>
 *     <li>Process the response received from the remote server and route it back to the {@link Client}</li>
 * </ul>
 *
 * @author Rafael Luis Ibasco
 */
public interface Messenger<R extends AbstractRequest, S extends AbstractResponse> extends Closeable, Configurable {

    CompletableFuture<S> send(InetSocketAddress address, R request);

    Transport<?, ?> getTransport();

    Executor getExecutor();
}
