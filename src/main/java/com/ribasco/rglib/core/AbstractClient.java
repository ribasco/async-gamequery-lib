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

package com.ribasco.rglib.core;

import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.core.session.SessionId;
import com.ribasco.rglib.core.session.SessionValue;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by raffy on 9/14/2016.
 */
public abstract class AbstractClient<Req extends AbstractRequest,
        Res extends AbstractResponse,
        M extends AbstractMessenger>
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
    public <V> Promise<V> sendRequest(Req message) {
        return sendRequest(message, AbstractMessenger.DEFAULT_REQUEST_PRIORITY);
    }

    @Override
    public <V> Promise<V> sendRequest(Req message, RequestPriority priority) {
        return sendRequest(message, null, priority);
    }

    @Override
    public <V> Promise<V> sendRequest(Req message, Callback<V> callback) {
        return sendRequest(message, callback, AbstractMessenger.DEFAULT_REQUEST_PRIORITY);
    }

    @Override
    public <V> Promise<V> sendRequest(Req message, Callback<V> callback, RequestPriority priority) {
        //Promise containing the actual server message
        final Promise<V> clientPromise = messenger.getTransport().newPromise();

        try {
            log.debug("Sending request : {}", message);
            //Promise retrieved from the messenger containing the AbstractResponse instance
            final Promise<Res> messengerPromise = messenger.send(message, priority);

            //Listen for completion events then notify the client accordingly
            messengerPromise.addListener(future -> {
                if (future.isDone()) {
                    V responseData = null;
                    Throwable exception = null;
                    try {
                        if (future.isSuccess())
                            responseData = (V) ((Res) future.get()).getMessage();
                        else
                            exception = future.cause();
                    } catch (InterruptedException | IllegalStateException | ExecutionException e) {
                        log.error("Got an error while retrieving: {} for {}", e.getMessage(), message.recipient());
                        exception = e;
                    } finally {
                        //#1) Invoke the callback for completion first
                        if (callback != null)
                            callback.onComplete(responseData, message.recipient(), exception);
                        //#2) Alert all registered listeners
                        if (exception != null)
                            clientPromise.tryFailure(exception);
                        else
                            clientPromise.trySuccess(responseData);
                    }
                }
            });
            Thread.sleep(getSleepTime());
        } catch (Exception e) {
            if (callback != null)
                callback.onComplete(null, message.recipient(), e);
            clientPromise.tryFailure(e);
        }
        return clientPromise;
    }

    public void waitForAll() {
        final Collection<Map.Entry<SessionId, SessionValue<Req, Res>>> entries = messenger.getRemaining();
        try {
            log.debug("There are still {} requests that are pending", entries.size());
            while (messenger.hasPendingRequests()
                    || messenger.getRemaining().size() > 0) {
                log.debug("Waiting... Sesion Size: {} - Request Size: {}", entries.size(), messenger.getPendingRequestSize());
                synchronized (messenger.getSessionManager()) {
                    entries.stream().forEachOrdered(entry -> {
                        log.debug(">> Session Remaining: {}", entry.getKey());
                    });
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
