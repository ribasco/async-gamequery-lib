/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.enums.QueueStrategy;
import com.ibasco.agql.core.exceptions.MessengerException;
import com.ibasco.agql.core.messenger.GameServerMessenger;
import com.ibasco.agql.core.session.DefaultSessionIdFactory;
import com.ibasco.agql.core.transport.tcp.NettyPooledTcpTransport;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconRequestType;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconAuthRequest;
import com.ibasco.agql.protocols.valve.source.query.request.SourceRconCmdRequest;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SourceRconMessenger extends GameServerMessenger<SourceRconRequest, SourceRconResponse> {

    private static final Logger log = LoggerFactory.getLogger(SourceRconMessenger.class);

    private final Map<Integer, SourceRconRequestType> requestTypeMap = new LinkedHashMap<>();

    private final boolean terminatingPacketsEnabled;

    private final ExecutorService executorService;

    public SourceRconMessenger(boolean terminatingPacketsEnabled, ExecutorService executorService) {
        super(new DefaultSessionIdFactory(), QueueStrategy.SYNCHRONOUS, executorService);
        this.terminatingPacketsEnabled = terminatingPacketsEnabled;
        this.executorService = executorService;
    }

    @Override
    protected Transport<SourceRconRequest> createTransport() {
        NettyPooledTcpTransport<SourceRconRequest> transport = new NettyPooledTcpTransport<>(executorService);
        transport.setChannelInitializer(new SourceRconChannelInitializer(this));
        transport.addChannelOption(ChannelOption.SO_KEEPALIVE, true);
        return transport;
    }

    @Override
    public CompletableFuture<SourceRconResponse> send(SourceRconRequest request) {
        int requestId = request.getRequestId();

        SourceRconRequestType type = getRequestType(request);

        //Make sure we have a request type
        if (type == null)
            throw new MessengerException("Unrecognized rcon request");

        //Add the request type to the map
        requestTypeMap.put(requestId, getRequestType(request));

        CompletableFuture<SourceRconResponse> futureResponse = super.send(request);
        //Make sure to remove the requestId once the response future is completed
        futureResponse.whenComplete((response, error) -> {
            log.debug("Removing request id '{}' from type map", requestId);
            requestTypeMap.remove(requestId);
        });
        return futureResponse;
    }

    public boolean isTerminatingPacketsEnabled() {
        return terminatingPacketsEnabled;
    }

    public SourceRconRequestType getRequestType(SourceRconRequest request) {
        if (request instanceof SourceRconAuthRequest) {
            return SourceRconRequestType.AUTH;
        } else if (request instanceof SourceRconCmdRequest) {
            return SourceRconRequestType.COMMAND;
        }
        return null;
    }

    public Map<Integer, SourceRconRequestType> getRequestTypeMap() {
        return requestTypeMap;
    }
}
