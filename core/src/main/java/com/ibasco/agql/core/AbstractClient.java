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

package com.ibasco.agql.core;

import com.ibasco.agql.core.enums.RequestPriority;
import com.ibasco.agql.core.utils.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

abstract public class AbstractClient<Req extends AbstractRequest,
        Res extends AbstractResponse,
        M extends AbstractMessenger<Req, Res>>
        implements Client<Req, Res> {
    private M messenger;

    private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    public AbstractClient(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(Req message) {
        return sendRequest(message, AbstractMessenger.DEFAULT_REQUEST_PRIORITY);
    }

    protected <V> CompletableFuture<V> sendRequest(Req message, RequestPriority priority) {
        log.debug("Client '{}' Sending request : {}", this.getClass().getSimpleName(), message);
        //Send the request then transform the result once a response is received
        ConcurrentUtils.sleepUninterrupted(10);
        return messenger.send(message, priority).thenApply(this::convertToResultType);
    }

    @SuppressWarnings("unchecked")
    private <V> V convertToResultType(Res message) {
        return (V) message.getMessage();
    }

    protected M getMessenger() {
        return messenger;
    }

    protected void setMessenger(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public void close() throws IOException {
        log.debug("Shutting down messenger");
        if (messenger != null)
            messenger.close();
    }
}
