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

import com.ribasco.rglib.core.session.SessionKey;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
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
        return sendRequest(message, (response, sender, error) -> {
            //do nothing?
        });
    }

    @Override
    public <V> Promise<V> sendRequest(Req message, Callback<V> callback) {
        //Promise containing the actual server message
        final Promise<V> clientPromise = messenger.getTransport().newPromise();

        try {
            //Promise retrieved from the messenger containing the AbstractResponse instance
            final Promise<Res> messengerPromise = messenger.send(message);

            //Listens for completion
            messengerPromise.addListener(future -> {
                if (future.isDone()) {
                    try {
                        if (future.isSuccess()) {
                            final Res r = (Res) future.get();
                            final V responseData = (V) r.getMessage();
                            clientPromise.trySuccess(responseData);
                            callback.onComplete(responseData, r.sender(), null);
                        } else if (future.isCancelled()) {
                            CancellationException ex = new CancellationException();
                            clientPromise.tryFailure(ex);
                            callback.onComplete(null, message.recipient(), ex);
                        } else if (!future.isSuccess() && future.cause() != null) {
                            clientPromise.tryFailure(future.cause());
                            callback.onComplete(null, message.recipient(), future.cause());
                        } else {
                            throw new IllegalStateException("Unhandled");
                        }
                    } catch (InterruptedException | IllegalStateException | ExecutionException e) {
                        log.error("Got an error while retrieving: {} for {}", e.getMessage(), message.recipient());
                        clientPromise.tryFailure(e);
                        callback.onComplete(null, message.recipient(), e);
                    }
                }
            });
            Thread.sleep(getSleepTime());
        } catch (Exception e) {
            clientPromise.tryFailure(e);
            callback.onComplete(null, message.recipient(), e);
        }

        return clientPromise;
    }

    public void waitForAll() {
        Set<Map.Entry<SessionKey, Promise<?>>> entries = messenger.getRemaining();
        try {
            log.debug("There are still {} requests that are pending", entries.size());
            while (entries.size() > 0) {
                log.debug("Waiting... Size: {}", entries.size());
                entries.stream().forEachOrdered(entry -> {
                    log.debug(">> Session Remaining: {}", entry.getKey());
                });
                Thread.sleep(1000);
            }
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
        if (messenger != null)
            messenger.close();
    }
}
