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

package org.ribasco.asyncgamequerylib.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ribasco.asyncgamequerylib.core.client.AbstractRestClient;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractWebApiInterface<T extends AbstractRestClient, R extends AbstractWebRequest> {
    private T client;
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson jsonBuilder;

    /**
     * Used by the underlying concrete classes for api versioning
     */
    protected static final int VERSION_1 = 1, VERSION_2 = 2, VERSION_3 = 3;

    /**
     * <p>Default Constructor</p>
     *
     * @param client A {@link AbstractRestClient} instance
     */
    public AbstractWebApiInterface(T client) {
        this.client = client;
    }

    /**
     * Lazy-Inititalization
     *
     * @return A {@link Gson} instance
     */
    protected Gson builder() {
        if (jsonBuilder == null) {
            configureBuilder(gsonBuilder);
            jsonBuilder = gsonBuilder.create();
        }
        return jsonBuilder;
    }

    /**
     * <p>Sends a requests to the internal client</p>
     *
     * @param request An instance of {@link AbstractWebRequest}
     *
     * @return A {@link CompletableFuture} that will hold the expected value once a response has been received by the server
     */
    protected <T> CompletableFuture<T> sendRequest(R request) {
        return client.sendRequest(request);
    }

    /**
     * <p>Override this method if you need to perform additional configurations against the builder (e.g. Register custom deserializers)</p>
     *
     * @param builder A {@link GsonBuilder} instance that will be accessed and configured by a concrete {@link AbstractWebApiInterface} implementation
     */
    protected void configureBuilder(GsonBuilder builder) {
        //no implementation
    }
}
