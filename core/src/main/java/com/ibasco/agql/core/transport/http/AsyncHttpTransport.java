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

package com.ibasco.agql.core.transport.http;

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.util.OptionMap;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * A wrapper class for the {@link AsyncHttpClient}
 */
public class AsyncHttpTransport implements Transport<Response, Request> {

    private AsyncHttpClient httpTransport;

    private AsyncHttpClientConfig transportConfig;

    private static final Logger log = LoggerFactory.getLogger(AsyncHttpTransport.class);

    public AsyncHttpTransport() {
        this(new DefaultAsyncHttpClientConfig.Builder().build());
    }

    public AsyncHttpTransport(AsyncHttpClientConfig transportConfig) {
        this.transportConfig = transportConfig;
        this.httpTransport = new DefaultAsyncHttpClient(this.transportConfig);
    }

    public AsyncHttpClientConfig getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(AsyncHttpClientConfig transportConfig) {
        this.transportConfig = transportConfig;
    }

    public AsyncHttpClient getHttpTransport() {
        return httpTransport;
    }

    public void setHttpTransport(AsyncHttpClient httpTransport) {
        this.httpTransport = httpTransport;
    }

    @Override
    public void close() throws IOException {
        httpTransport.close();
    }

    @Override
    public void initialize() {

    }

    @Override
    public CompletableFuture<Response> send(Request data) {
        log.debug("Sending via transport : {}", data);
        return httpTransport.prepareRequest(data).execute().toCompletableFuture();
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public OptionMap getOptions() {
        return null;
    }
}
