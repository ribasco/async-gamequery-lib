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

package com.ibasco.agql.core.transport.http;

import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.util.HttpOptions;
import com.ibasco.agql.core.util.Options;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper class for the {@link org.asynchttpclient.AsyncHttpClient}
 *
 * @author Rafael Luis Ibasco
 */
public final class AsyncHttpTransport implements Transport<Response, Request> {

    private static final Logger log = LoggerFactory.getLogger(AsyncHttpTransport.class);

    private final AsyncHttpClient httpClient;

    private final HttpOptions options;

    /**
     * <p>Constructor for AsyncHttpTransport.</p>
     *
     * @param transportConfig
     *         a {@link org.asynchttpclient.AsyncHttpClientConfig} object
     */
    public AsyncHttpTransport(final AsyncHttpClientConfig transportConfig, final HttpOptions options) {
        this.httpClient = new DefaultAsyncHttpClient(transportConfig);
        this.options = options;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Response> send(Request data) {
        log.debug("HTTP => Sending request {} via http transport", data);
        return httpClient.prepareRequest(data).execute().toCompletableFuture();
    }

    /** {@inheritDoc} */
    @Override
    public Options getOptions() {
        return options;
    }
}
