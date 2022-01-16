/*
 * Copyright 2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.steam.master.client;

import com.ibasco.agql.core.NettyClient;
import com.ibasco.agql.core.NettyMessenger;
import com.ibasco.agql.core.exceptions.ReadTimeoutException;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.functions.TriConsumer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerMessenger;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerResponse;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;

import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Queries Valve Master Server to retrieve a list of game servers</p>
 *
 * @author Rafael Luis Ibasco
 */
public final class MasterServerQueryClient extends NettyClient<InetSocketAddress, MasterServerRequest, MasterServerResponse> {

    /**
     * Create a new {@link MasterServerQueryClient} client instance using the default configuration options.
     */
    public MasterServerQueryClient() {
        this(null);
    }

    /**
     * Create a new {@link MasterServerQueryClient} instance with the provided configuration options.
     *
     * @see OptionBuilder
     * @since 0.2.0
     */
    public MasterServerQueryClient(Options options) {
        super(options);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} representing a set of filters to be used by the query
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter, TriConsumer)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(MasterServerType type, MasterServerRegion region, MasterServerFilter filter) {
        return getServerList(type, region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server. Passing a callback will allow you to partially receive a batch of addresses in real-time as it queries the master server</p>
     *
     * @param type
     *         A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} representing a set of filters to be used by the query
     * @param callback
     *         A {@link TriConsumer} that will be invoked repeatedly for partial response
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(MasterServerType type, MasterServerRegion region, MasterServerFilter filter, TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        MasterServerRequest request = new MasterServerRequest();
        request.setRegion(region);
        request.setFilter(filter);
        request.setCallback(callback);
        return send(type.getMasterAddress(), request, MasterServerResponse.class).thenApply(MasterServerResponse::getServerList);
    }

    @Override
    protected void configureFailsafe(FailsafeExecutor<MasterServerResponse> executor) {
        RetryPolicy<MasterServerResponse> SWITCH_ADDR_ON_TIMEOUT = RetryPolicy.<MasterServerResponse>builder().handle(ReadTimeoutException.class).build();
        //executor.compose(SWITCH_ADDR_ON_TIMEOUT);
    }

    @Override
    protected NettyMessenger<InetSocketAddress, MasterServerRequest, MasterServerResponse> createMessenger(Options options) {
        return new MasterServerMessenger(options);
    }
}
