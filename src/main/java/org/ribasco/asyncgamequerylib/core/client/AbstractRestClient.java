/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

package org.ribasco.asyncgamequerylib.core.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.asynchttpclient.*;
import org.ribasco.asyncgamequerylib.core.AbstractWebRequest;
import org.ribasco.asyncgamequerylib.core.AbstractWebResponse;
import org.ribasco.asyncgamequerylib.core.Callback;
import org.ribasco.asyncgamequerylib.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 10/15/2016.
 */
public abstract class AbstractRestClient<Req extends AbstractWebRequest, Res extends AbstractWebResponse> implements Client<Req, Res> {
    private static final Logger log = LoggerFactory.getLogger(AbstractRestClient.class);
    private AsyncHttpClient httpClient;
    private JsonParser jsonParser = new JsonParser();
    private AsyncCompletionHandler<JsonObject> jsonResponseHandler = new AsyncCompletionHandler<JsonObject>() {
        @Override
        public JsonObject onCompleted(Response response) throws Exception {
            if (response.getStatusCode() == 200 && response.getHeader("Content-Type").contains("application/json")) {
                JsonObject root = jsonParser.parse(response.getResponseBody()).getAsJsonObject();
                return root;
            }
            throw new IllegalStateException(String.format("Invalid response received from server (Status Code: %d, Content-Type: %s)", response.getStatusCode(), response.getHeader("Content-Type")));
        }
    };

    public AbstractRestClient() {
        this(new DefaultAsyncHttpClientConfig.Builder().build());
    }

    public AbstractRestClient(AsyncHttpClientConfig config) {
        httpClient = new DefaultAsyncHttpClient(config);
    }

    protected void prepareRequest(BoundRequestBuilder builder) {
        //no implementation. meant to be overriden by concrete clients
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(Req message) {
        return sendRequest(message, null);
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(Req message, Callback<V> callback) {
        final BoundRequestBuilder builder = httpClient.prepareRequest(message.getMessage());
        //If we need to apply additional query parameters before we send the request (e.g. client specific webapi tokens)
        prepareRequest(builder);
        final Request request = builder.build();
        final InetSocketAddress recipient = new InetSocketAddress(request.getUri().getHost(), (request.getUri().getPort() > 0) ? request.getUri().getPort() : 80);
        log.info("Final Request URL: {}", request.getUrl());
        return (CompletableFuture<V>) builder.execute(jsonResponseHandler).toCompletableFuture().whenComplete((jsonElement, throwable) -> {
            if (callback != null)
                callback.onComplete((V) jsonElement, recipient, throwable);
        });
    }

    public JsonParser getJsonParser() {
        return jsonParser;
    }

    public void setJsonParser(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
