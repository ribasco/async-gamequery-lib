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

package org.ribasco.asyncgamequerylib.core.messenger;

import org.asynchttpclient.*;
import org.ribasco.asyncgamequerylib.core.AbstractWebRequest;
import org.ribasco.asyncgamequerylib.core.AbstractWebResponse;
import org.ribasco.asyncgamequerylib.core.Messenger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Messenger using the TCP Transport Protocol
 */
//TODO: Re-evaluate the need for this
public class WebServiceMessenger<Req extends AbstractWebRequest, Res extends AbstractWebResponse> implements Messenger<Req, Res> {

    private AsyncHttpClient httpTransport;
    private Map<Class<? extends Req>, Class<? extends Res>> lookupDirectory;

    public WebServiceMessenger() {
        this(new DefaultAsyncHttpClientConfig.Builder().build());
    }

    public WebServiceMessenger(AsyncHttpClientConfig config) {
        this.httpTransport = new DefaultAsyncHttpClient(config);
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
