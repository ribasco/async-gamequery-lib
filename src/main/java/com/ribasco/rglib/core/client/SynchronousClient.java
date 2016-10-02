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

import com.ribasco.rglib.core.*;
import io.netty.util.concurrent.Promise;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by raffy on 9/30/2016.
 */
public abstract class SynchronousClient<Req extends AbstractRequest,
        Res extends AbstractResponse,
        M extends AbstractMessenger>
        extends AbstractClient<Req, Res, M> {

    private static final Logger log = LoggerFactory.getLogger(SynchronousClient.class);

    private AtomicBoolean started = new AtomicBoolean();
    private ScheduledExecutorService scheduledService;
    private ConcurrentHashMap<InetSocketAddress, Deque<Task>> requestMap;

    private enum RequestStatus {
        NEW, SENT, DONE
    }

    private final class Task<T> implements Comparable<Task> {
        private Callback<T> callback;
        private Req request;
        private Promise<T> promise;
        private RequestStatus status;
        private long timeRegistered;

        public Task(Req request, Callback<T> callback, Promise<T> promise) {
            this.callback = callback;
            this.request = request;
            this.promise = promise;
            this.status = RequestStatus.NEW;
            this.timeRegistered = System.currentTimeMillis();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj == this)
                return true;
            if (obj instanceof Task) {
                Task rhs = (Task) obj;
                return new EqualsBuilder()
                        .append(request.recipient(), rhs.request.recipient())
                        .append(request.getClass(), rhs.request.getClass())
                        .isEquals();
            } else if (obj instanceof AbstractRequest) {
                AbstractRequest req = (AbstractRequest) obj;
                return new EqualsBuilder()
                        .append(request.recipient(), req.recipient())
                        .append(request.getClass(), req.getClass())
                        .isEquals();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(71, 95)
                    .append(request.recipient())
                    .append(request.getClass())
                    .hashCode();
        }

        @Override
        public int compareTo(Task o) {
            return new CompareToBuilder()
                    .append(request.recipient(), o.request.recipient())
                    .append(timeRegistered, o.timeRegistered)
                    .toComparison();
        }
    }

    public SynchronousClient(M messenger) {
        super(messenger);
        scheduledService = new ScheduledThreadPoolExecutor(1);
        requestMap = new ConcurrentHashMap<>();
    }

    @Override
    public <V> Promise<V> sendRequest(Req message, Callback<V> callback) {
        if (message == null)
            throw new NullPointerException();

        final Promise<V> rPromise = super.getMessenger().getTransport().newPromise();

        //Check if we already have an existing queue for this address
        if (!requestMap.containsKey(message.recipient())) {
            requestMap.put(message.recipient(), new LinkedList<>());
        }

        final Deque<Task> taskQueue = requestMap.get(message.recipient());
        log.debug("Adding request {} to the queue", message);
        taskQueue.add(new Task<>(message, callback, rPromise));
        open(); //lazy start

        return rPromise;
    }

    protected void open() {
        if (!started.get()) {
            //Schedule the recurring task
            scheduledService.scheduleAtFixedRate(() -> {
                //Loop through all addresses in the map and process each queue
                requestMap.forEachEntry(10, inetSocketAddressDequeEntry -> {
                    //Retrieve the queue
                    Deque<Task> requestQueue = requestMap.get(inetSocketAddressDequeEntry.getKey());

                    //Do not continue if there is no queue available
                    if (requestQueue == null) {
                        log.error("No queue found for address {}. Unable to process.", inetSocketAddressDequeEntry.getKey());
                        return;
                    }

                    log.debug("Processing Queue for address '{}'", inetSocketAddressDequeEntry.getKey());

                    //Check the status of the task located on the head of the queue
                    final Task rh = requestQueue.peek();

                    log.debug("There are currently {} items in the queue", requestQueue.size());
                    if (rh == null)
                        return;

                    //Check if we have pending request
                    if (rh.status == RequestStatus.NEW) {
                        log.debug("Found a request in the queue. Sending request for transport : {}", rh.request);
                        super.sendRequest((Req) rh.request, rh.callback).addListener(future -> {
                            log.debug("Request done: {}", rh.request);
                            //Set status to done
                            rh.status = RequestStatus.DONE;
                            if (future.isSuccess())
                                rh.promise.trySuccess(future.get());
                            else
                                rh.promise.tryFailure(future.cause());
                            //Remove the request from the queue
                            requestQueue.remove();
                        });
                        //Set status to sent
                        rh.status = RequestStatus.SENT;
                    } else if (rh.status == RequestStatus.SENT) {
                        log.debug("There is still a request pending for completion : {} with status: {}", rh.request, rh.status);
                    }
                });
            }, 500, 500, TimeUnit.MILLISECONDS);
            //Flag as started
            started.set(true);
        }
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing Client");
        super.close();
        scheduledService.shutdown();
        started.set(false);
        requestMap.clear();
    }
}
