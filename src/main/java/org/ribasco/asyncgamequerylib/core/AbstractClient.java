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

import org.ribasco.asyncgamequerylib.core.enums.RequestPriority;
import org.ribasco.asyncgamequerylib.core.session.SessionId;
import org.ribasco.asyncgamequerylib.core.session.SessionValue;
import org.ribasco.asyncgamequerylib.core.transport.NettyTransport;
import org.ribasco.asyncgamequerylib.core.utils.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by raffy on 9/14/2016.
 */
public abstract class AbstractClient<Req extends AbstractRequest,
        Res extends AbstractResponse,
        M extends AbstractMessenger<Req, Res, ? extends NettyTransport<Req>>>
        implements Client<Req, Res> {
    private M messenger;

    private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    private int sleepTime;

    public AbstractClient(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(Req message) {
        return sendRequest(message, AbstractMessenger.DEFAULT_REQUEST_PRIORITY);
    }

    public <V> CompletableFuture<V> sendRequest(Req message, RequestPriority priority) {
        return sendRequest(message, null, priority);
    }

    @Override
    public <V> CompletableFuture<V> sendRequest(Req message, Callback<V> callback) {
        return sendRequest(message, callback, AbstractMessenger.DEFAULT_REQUEST_PRIORITY);
    }

    public <V> CompletableFuture<V> sendRequest(Req message, Callback<V> callback, RequestPriority priority) {
        log.debug("Sending request : {}", message);
        //Send the request then transform the result once a response is received
        final CompletableFuture<V> messengerPromise = messenger.send(message, priority)
                .thenApply((Res response) -> (V) response.getMessage());
        if (callback != null) {
            messengerPromise.whenComplete((result, throwable) -> callback.onComplete(result, message.recipient(), throwable));
        }
        ConcurrentUtils.sleepUninterrupted(getSleepTime());
        return messengerPromise;
    }

    public void waitForAll() {
        final Collection<Map.Entry<SessionId, SessionValue<Req, Res>>> entries = messenger.getRemaining();
        try {
            log.debug("There are still {} requests that are pending", entries.size());
            while (messenger.hasPendingRequests()
                    || messenger.getRemaining().size() > 0) {
                log.debug("Waiting... Session Size: {} - Request Size: {}", entries.size(), messenger.getPendingRequestSize());
                synchronized (messenger.getSessionManager()) {
                    entries.stream().forEachOrdered(entry -> log.debug(">> Pending Session Id: {}, Promise: {}, Status: {}", entry.getKey(), entry.getValue().getClientPromise(), entry.getValue().getRequestDetails().getStatus()));
                }
                Thread.sleep(1000);
            }
            log.debug("No more pending requests found");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public M getMessenger() {
        return messenger;
    }

    public void setMessenger(M messenger) {
        this.messenger = messenger;
    }

    @Override
    public void close() throws IOException {
        if (getMessenger().hasPendingRequests()) {
            log.warn("There are still entries pending");
            waitForAll();
        }
        log.debug("Shutting down messenger");
        if (messenger != null)
            messenger.close();
    }
}
