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

package com.ribasco.rglib.core.messenger;

import com.ribasco.rglib.core.AbstractWebRequest;
import com.ribasco.rglib.core.AbstractWebResponse;
import com.ribasco.rglib.core.Messenger;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Messenger using the TCP Transport Protocol
 */
public class WebServiceMessenger<Req extends AbstractWebRequest, Res extends AbstractWebResponse> implements Messenger<Req, Res> {

    private AsyncHttpClient httpTransport;
    private Map<Class<? extends Req>, Class<? extends Res>> lookupDirectory;

    public WebServiceMessenger() {
        this.httpTransport = new DefaultAsyncHttpClient();
        this.lookupDirectory = new ConcurrentHashMap<>();
    }

    @Override
    public CompletableFuture<Res> send(Req request) {
        return httpTransport.prepareRequest(request.getMessage()).execute(new AsyncCompletionHandler<Res>() {
            @Override
            public Res onCompleted(Response response) throws Exception {
                return null;
            }
        }).toCompletableFuture();
    }


    @Override
    public void receive(Res response) {

    }

    @Override
    public void close() throws IOException {

    }
}
