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

import com.ibasco.agql.core.transport.http.AsyncHttpTransport;
import com.ibasco.agql.core.util.GeneralOptions;
import com.ibasco.agql.core.util.HttpOptions;
import com.ibasco.agql.core.util.Platform;
import com.ibasco.agql.core.util.Properties;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.filter.ThrottleRequestFilter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Messenger responsible for handling Web Requests and Responses.
 *
 * @author Rafael Luis Ibasco
 */
public final class HttpMessenger implements Messenger<AbstractWebRequest, AbstractWebResponse> {

    private static final Logger log = LoggerFactory.getLogger(HttpMessenger.class);

    private final AsyncHttpTransport transport;

    private final Function<Response, AbstractWebResponse> responseFactory;

    private final HttpOptions options;

    private final EventLoopGroup eventLoopGroup;

    private ExecutorService executorService;

    /**
     * <p>Constructor for HttpMessenger.</p>
     *
     * @param responseFactory
     *         a {@link java.util.function.Function} object
     * @param options
     *         a {@link com.ibasco.agql.core.util.Options} object
     */
    public HttpMessenger(Function<Response, AbstractWebResponse> responseFactory, HttpOptions options) {
        this.options = options;
        this.eventLoopGroup = initializeEventLoopGroup();
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder();
        configBuilder.setKeepAlive(options.getOrDefault(GeneralOptions.SOCKET_KEEP_ALIVE));
        configBuilder.addRequestFilter(new ThrottleRequestFilter(options.getOrDefault(GeneralOptions.POOL_MAX_CONNECTIONS)));
        configBuilder.setEventLoopGroup(this.eventLoopGroup);
        this.transport = new AsyncHttpTransport(configBuilder.build(), options);
        this.responseFactory = responseFactory;
    }

    private EventLoopGroup initializeEventLoopGroup() {
        ExecutorService executor = options.get(GeneralOptions.THREAD_EXECUTOR_SERVICE);
        if (executor == null)
            executor = Platform.getDefaultExecutor();
        Integer nThreads = Platform.getCoreThreadCount(getOptions(), executor);
        EventLoopGroup group = Platform.isDefaultExecutor(executor) ? Platform.getDefaultEventLoopGroup() : Platform.getOrCreateEventLoopGroup(executor, nThreads, Properties.useNativeTransport());
        log.debug("HTTP_MESSENGER (INIT) => Executor Service: '{}'", executor);
        log.debug("HTTP_MESSENGER (INIT) => Event Loop Group: '{}' (Event Loop Threads: {})", group, nThreads);
        this.executorService = executor;
        return group;
    }

    /** {@inheritDoc} */
    @Override
    public HttpOptions getOptions() {
        return options;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        transport.close();
        if (eventLoopGroup != null)
            eventLoopGroup.shutdownGracefully();
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<AbstractWebResponse> send(InetSocketAddress address, AbstractWebRequest request) {
        log.debug("Sending request with url : {}", request.getMessage().getUri());
        CompletableFuture<Response> res = transport.send(request.getMessage());
        //transform the raw Response type to an instance of AbstractWebResponse using the supplied factory method
        return res.thenApply(responseFactory);
    }

    /** {@inheritDoc} */
    @Override
    public AsyncHttpTransport getTransport() {
        return transport;
    }

    /** {@inheritDoc} */
    @Override
    public EventLoopGroup getExecutor() {
        return this.eventLoopGroup;
    }
}
