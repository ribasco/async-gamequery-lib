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

package com.ibasco.agql.core.messenger;

import com.ibasco.agql.core.AbstractWebRequest;
import com.ibasco.agql.core.AbstractWebResponse;
import com.ibasco.agql.core.Messenger;
import com.ibasco.agql.core.Transport;
import com.ibasco.agql.core.transport.http.AsyncHttpTransport;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.asynchttpclient.filter.ThrottleRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A Messenger responsible for handling Web Requests and Responses.
 */
//TODO: Implement a generic request/response lookup service
public class WebMessenger<R extends AbstractWebRequest, S extends AbstractWebResponse> implements Messenger<R, S> {

    private static final Logger log = LoggerFactory.getLogger(WebMessenger.class);
    private final Transport<Request> transport;
    private Function<Response, S> responseFactory;

    public WebMessenger(Function<Response, S> responseFactory) {
        this(responseFactory, null);
    }

    public WebMessenger(Function<Response, S> responseFactory, EventLoopGroup eventLoopGroup) {
        DefaultAsyncHttpClientConfig.Builder configBuilder = new DefaultAsyncHttpClientConfig.Builder();
        configBuilder.setKeepAlive(true);
        configBuilder.addRequestFilter(new ThrottleRequestFilter(40));
        if (eventLoopGroup != null) {
            configBuilder.setEventLoopGroup(eventLoopGroup);
        }
        this.transport = new AsyncHttpTransport(configBuilder.build());
        this.responseFactory = responseFactory;
    }

    @Override
    public CompletableFuture<S> send(R request) {
        log.debug("Sending request with url : {}", request.getMessage().getUri());
        CompletableFuture<Response> res = transport.send(request.getMessage());
        //transform the raw Response type to an instance of AbstractWebResponse using the supplied factory method
        return res.thenApply(responseFactory);
    }

    public Function<Response, S> getResponseFactory() {
        return responseFactory;
    }

    public void setResponseFactory(Function<Response, S> responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public void receive(S s, Throwable throwable) {
        //do nothing
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }
}
