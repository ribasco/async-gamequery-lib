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

package com.ibasco.agql.protocols.valve.steam.master.client;

import com.ibasco.agql.core.NettySocketClient;
import com.ibasco.agql.core.util.OptionBuilder;
import com.ibasco.agql.core.util.Options;
import com.ibasco.agql.core.util.functions.TriConsumer;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerMessenger;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerOptions;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.message.MasterServerResponse;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Queries Valve Master Server to retrieve a list of game servers. Note that the master servers are rate-limited by default.</p>
 *
 * @author Rafael Luis Ibasco
 * @see MasterServerOptions
 */
public final class MasterServerQueryClient extends NettySocketClient<MasterServerRequest, MasterServerResponse> {

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
     *         A {@link MasterServerFilter} containing the filters to be applied in the query
     *
     * @return A {@link CompletableFuture} which is notified once the request has been marked as complete. Returns a {@link Vector} containing the {@link InetSocketAddress} instances of the servers.
     *
     * @see #getServers(MasterServerType, MasterServerRegion, MasterServerFilter, TriConsumer)
     */
    public CompletableFuture<Set<InetSocketAddress>> getServers(MasterServerType type, MasterServerRegion region, MasterServerFilter filter) {
        return getServers(type, region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server. Passing a callback will allow you to partially receive a batch of addresses in real-time as it queries the master server</p>
     *
     * @param type
     *         The {@link MasterServerType} describing the type of master server to query
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} containing the filters to be applied in the query
     * @param callback
     *         Accepts a {@link TriConsumer} callback that will be called repeatedly for every batch of addresses received from the master server. (Parameters: Server Address, Master Server Address, Exception)
     *
     * @return A {@link CompletableFuture} which is notified once the request has been marked as complete. Returns a {@link Vector} containing the {@link InetSocketAddress} instances of the servers.
     *
     * @see #getServers(MasterServerType, MasterServerRegion, MasterServerFilter)
     */
    public CompletableFuture<Set<InetSocketAddress>> getServers(MasterServerType type, MasterServerRegion region, MasterServerFilter filter, TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        MasterServerRequest request = new MasterServerRequest();
        request.setType(type);
        request.setRegion(region);
        request.setFilter(filter);
        request.setCallback(callback);
        return getMessenger().send(request).thenApply(MasterServerResponse::getServerList);
    }

    @Override
    protected MasterServerMessenger getMessenger() {
        return (MasterServerMessenger) super.getMessenger();
    }

    @Override
    protected MasterServerMessenger createMessenger(Options options) {
        return new MasterServerMessenger(options);
    }
}
