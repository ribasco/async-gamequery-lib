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

package com.ibasco.agql.core.transport;

import dev.failsafe.ExecutionContext;
import dev.failsafe.function.ContextualSupplier;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FailsafeChannelSupplier implements ContextualSupplier<Channel, CompletableFuture<Channel>> {

    private static final Logger log = LoggerFactory.getLogger(FailsafeChannelSupplier.class);

    private static final ConcurrentMap<InetSocketAddress, ContextualSupplier<Channel, CompletableFuture<Channel>>> channelSupplierMap = new ConcurrentHashMap<>();

    private final InetSocketAddress address;

    private final FailsafeChannelFactory channelFactory;

    FailsafeChannelSupplier(final InetSocketAddress address, FailsafeChannelFactory channelFactory) {
        this.address = address;
        this.channelFactory = channelFactory;
    }

    @Override
    public CompletableFuture<Channel> get(ExecutionContext<Channel> context) throws Throwable {
        log.debug("AUTH => Acquiring channel for address '{}' with assigned event loop: '{}' (Attempt: {}, Executions: {})", address, null, context.getAttemptCount(), context.getExecutionCount());
        return channelFactory.create(address);
    }
}