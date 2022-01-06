/*
 * Copyright 2018-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.core;

import com.ibasco.agql.core.transport.http.AsyncHttpTransport;
import com.ibasco.agql.core.util.OptionMap;
import com.ibasco.agql.core.util.TransportOptions;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.filter.ThrottleRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A Messenger responsible for handling Web Requests and Responses.
 *
 * @author Rafael Luis Ibasco
 */
public final class HttpMessenger implements Messenger<InetSocketAddress, AbstractWebRequest, AbstractWebResponse> {

    private static final Logger log = LoggerFactory.getLogger(HttpMessenger.class);

    private final AsyncHttpTransport transport;

    private final Function<Response, AbstractWebResponse> responseFactory;

    private final OptionMap options;

    private final EventLoopGroup eventLoopGroup;

    public HttpMessenger(Function<Response, AbstractWebResponse> responseFactory) {
        this(responseFactory, new OptionMap(HttpMessenger.class));
    }

    public HttpMessenger(Function<Response, AbstractWebResponse> responseFactory, OptionMap options) {
        this.options = options;
        this.eventLoopGroup = options.getOrDefault(TransportOptions.THREAD_EL_GROUP);
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder();
        configBuilder.setKeepAlive(options.getOrDefault(TransportOptions.SOCKET_KEEP_ALIVE));
        configBuilder.addRequestFilter(new ThrottleRequestFilter(options.getOrDefault(TransportOptions.POOL_MAX_CONNECTIONS)));
        configBuilder.setEventLoopGroup(this.eventLoopGroup);
        this.transport = new AsyncHttpTransport(configBuilder.build());
        this.responseFactory = responseFactory;
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }

    @Override
    public CompletableFuture<AbstractWebResponse> send(InetSocketAddress address, AbstractWebRequest request) {
        log.debug("Sending request with url : {}", request.getMessage().getUri());
        CompletableFuture<Response> res = transport.send(request.getMessage());
        //transform the raw Response type to an instance of AbstractWebResponse using the supplied factory method
        return res.thenApply(responseFactory);
    }

    @Override
    public AsyncHttpTransport getTransport() {
        return transport;
    }

    @Override
    public EventLoopGroup getExecutor() {
        return this.eventLoopGroup;
    }

    @Override
    public OptionMap getOptions() {
        return options;
    }
}