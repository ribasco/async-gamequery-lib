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

package com.ibasco.agql.core.client;

import com.ibasco.agql.core.AbstractWebRequest;
import com.ibasco.agql.core.AbstractWebResponse;
import com.ibasco.agql.core.Client;
import com.ibasco.agql.core.messenger.WebMessenger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * A generic Http Client base
 *
 * @param <R> Type of {@link AbstractWebRequest}
 * @param <S> Type of {@link AbstractWebResponse}
 */
abstract class AbstractWebClient<R extends AbstractWebRequest, S extends AbstractWebResponse>
        implements Client<R, S> {

    private final WebMessenger<R, S> messenger;

    AbstractWebClient() {
        this.messenger = createWebMessenger();
    }

    abstract public WebMessenger<R, S> createWebMessenger();

    @Override
    @SuppressWarnings("unchecked")
    public <V> CompletableFuture<V> sendRequest(R message) {
        return (CompletableFuture<V>) messenger.send(message);
    }

    @Override
    public void close() throws IOException {
        messenger.close();
    }
}
