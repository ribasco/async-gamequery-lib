/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
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

package com.ribasco.rglib.core.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ribasco.rglib.core.AbstractRequest;
import com.ribasco.rglib.core.AbstractResponse;
import com.ribasco.rglib.core.Callback;
import com.ribasco.rglib.core.Client;
import com.ribasco.rglib.core.enums.RequestPriority;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 10/15/2016.
 */
public abstract class RestClient implements Client<AbstractRequest, AbstractResponse> {
    protected final AsyncHttpClient httpClient = new DefaultAsyncHttpClient();
    private final Gson gson = new GsonBuilder().create();

    @Override
    public <V> CompletableFuture<V> sendRequest(AbstractRequest message) {
        return null;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(AbstractRequest message, RequestPriority priority) {
        return null;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(AbstractRequest message, Callback<V> callback) {
        return null;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(AbstractRequest message, Callback<V> callback, RequestPriority priority) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
