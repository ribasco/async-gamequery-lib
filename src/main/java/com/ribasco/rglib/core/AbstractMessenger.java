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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ribasco.rglib.core.enums.ProcessingMode;
import com.ribasco.rglib.core.enums.RequestPriority;
import com.ribasco.rglib.core.enums.RequestStatus;
import com.ribasco.rglib.core.session.*;
import com.ribasco.rglib.core.transport.NettyTransport;
import com.ribasco.rglib.core.utils.ConcurrentUtils;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by raffy on 9/14/2016.
 */
public abstract class AbstractMessenger<A extends AbstractRequest, B extends AbstractResponse, T extends NettyTransport> implements Messenger<A, B> {

    public static final RequestPriority DEFAULT_REQUEST_PRIORITY = RequestPriority.MEDIUM;
    public static final ProcessingMode DEFAULT_PROCESSING_MODE = ProcessingMode.ASYNCHRONOUS;
    public static final int DEFAULT_MAX_RETRIES = 3;

    private static final Logger log = LoggerFactory.getLogger(AbstractMessenger.class);
    private ExecutorService messengerService;

    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private SessionManager<A, B> sessionManager;
    private T transport;
    private PriorityBlockingQueue<RequestDetails<A>> requestQueue;
    private ProcessingMode processingMode;
    private int maxRetries;

    /**
     * Comparator class to be used by our priority queue for the natural ordering of requests
     */
    private static class RequestComparator implements Comparator<RequestDetails> {
        @Override
        public int compare(RequestDetails o1, RequestDetails o2) {
            return o2.getPriority().compareTo(o1.getPriority());
        }
    }

    /**
     * Handle timeout occurences
     */
    private final TimeoutCallback TIMEOUT_CALLBACK = (sessMan, sessId) -> true;


    /**
     * A runnable that process requests synchronously. A request wont be removed from the queue until it completes.
     */
    private Runnable syncRequestProcessor = () -> {
        log.info("Running sync processor for {}. Request Queue = {}", this.getClass().getSimpleName(), requestQueue.hashCode());
        while (!stopped.get()) {
            //Since we are processing synchronously, we will not remove the head of the queue immediately but rather
            //only remove the head once it completes
            final RequestDetails<A> requestDetails = requestQueue.peek();

            try {
                //Do we have any requests to process?
                if (requestDetails == null) {
                    ConcurrentUtils.sleepUninterrupted(50);
                    continue;
                }

                final RequestStatus status = requestDetails.getStatus();

                if (status == RequestStatus.NEW) {
                    requestDetails.setStatus(RequestStatus.ACCEPTED);
                    requestDetails.getRequest().setSender(transport.localAddress());

                    //Register the request to the session manager
                    final SessionId id = sessionManager.register(requestDetails);

                    //Update the status to registered
                    requestDetails.setStatus(RequestStatus.REGISTERED);

                    if (id != null) {
                        transport.send(requestDetails.getRequest(), true).addListener(future -> {
                            //Did our write operation succeed?
                            if (future.isSuccess()) {
                                //Set the status as sent
                                requestDetails.setStatus(RequestStatus.SENT);
                                //If the write is successful, listen for event completion
                                requestDetails.getPromise().addListener(future1 -> {
                                    //Update the status
                                    requestDetails.setStatus(RequestStatus.DONE);
                                    //Only remove from the queue once the task completes
                                    requestQueue.remove(requestDetails);
                                });
                            } else {
                                //If write is a failure, notify the listeners
                                requestDetails.getPromise().tryFailure(future.cause());
                                requestQueue.remove(requestDetails);
                            }
                        });
                    }
                }
                ConcurrentUtils.sleepUninterrupted(5);
            } catch (Exception e) {
                if (requestDetails != null && requestDetails.getPromise() != null)
                    requestDetails.getPromise().tryFailure(e);
            }
        }
    };

    /**
     * A Runnable that process requests asynchronously. Requests are immediately removed and processed from the queue.
     */
    private Runnable asyncRequestProcessor = () -> {
        log.info("Running async processor for {}. Request Queue: {}", this.getClass().getSimpleName(), requestQueue.hashCode());
        while (!stopped.get()) {
            //Remove the head of the queue immediately and process accordingly
            final RequestDetails<A> requestDetails = requestQueue.poll();

            //Only process new requests
            if (requestDetails != null && (requestDetails.getStatus() == RequestStatus.NEW
                    || requestDetails.getStatus() == RequestStatus.RETRY)) {
                try {
                    //Set status to ACCEPTED
                    requestDetails.setStatus(RequestStatus.ACCEPTED);

                    final A request = requestDetails.getRequest();
                    final Promise clientPromise = requestDetails.getPromise();

                    //Set local address for transport
                    request.setSender(transport.localAddress());

                    //Register the session immediately, duplicate requests will be queued in the order they are sent.
                    final SessionId id = sessionManager.register(requestDetails);

                    //Update the status to registered
                    requestDetails.setStatus(RequestStatus.REGISTERED);

                    //Send then listen for the write completion status
                    transport.send(request, true).addListener(future -> {
                        //Assuming that the request has been registered in the session,
                        // check if the write operation is successful
                        if (future.isSuccess()) {
                            //Update the request status
                            requestDetails.setStatus(RequestStatus.SENT);
                        } else {
                            //If the write operation failed, we need to unregister from the session
                            log.debug("Write operation failed, unregistering from session : {}", id);
                            requestDetails.setStatus(RequestStatus.DONE);
                            //Remove from the registry
                            sessionManager.unregister(id);
                            clientPromise.tryFailure(future.cause());
                        }
                    });
                    requestDetails.setStatus(RequestStatus.SENDING);
                } catch (Exception e) {
                    requestDetails.getPromise().tryFailure(e);
                }
            }
        }
    };

    public AbstractMessenger(T transport) {
        this(transport, DEFAULT_PROCESSING_MODE);
    }

    public AbstractMessenger(T transport, ProcessingMode processingMode) {
        this(transport, new DefaultSessionIdFactory(), processingMode);
    }

    public AbstractMessenger(T transport, AbstractSessionIdFactory keyFactory, ProcessingMode processingMode) {
        this(transport, new DefaultSessionManager(keyFactory), processingMode);
    }

    public AbstractMessenger(T transport, SessionManager sessionManager, ProcessingMode processingMode) {
        //Set processing mode
        this.processingMode = processingMode;
        //Set the session manager, use default if not specified
        this.sessionManager = (sessionManager != null) ? sessionManager : new DefaultSessionManager<>(new DefaultSessionIdFactory());
        //Set messenger transport
        this.transport = transport;
        //Create the queue
        requestQueue = new PriorityBlockingQueue<>(50, new RequestComparator());
        //Configure transport before initialization
        configureTransport(transport);
        //Configure the mappings before initialization
        configureMappings(this.sessionManager.getLookupMap());
        //Initialize the transport
        transport.initialize();
        //Initialize server
        messengerService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("messenger-service-%d").build());
        //Start our request scheduler
        messengerService.execute((processingMode == ProcessingMode.SYNCHRONOUS) ? syncRequestProcessor : asyncRequestProcessor);
    }

    /**
     * Configure the underlying {@link NettyTransport}
     *
     * @param transport
     */

    public abstract void configureTransport(T transport);

    /**
     * Configure request <-> response mappings
     *
     * @param map
     */
    public abstract void configureMappings(Map<Class<? extends A>, Class<? extends B>> map);

    /**
     * Sends a request to it's destination using the default priority
     *
     * @param request
     * @param <V>
     *
     * @return
     */
    @Override
    public <V> Promise<V> send(A request) {
        return send(request, DEFAULT_REQUEST_PRIORITY);
    }

    /**
     * Sends a request using the specified priority level
     *
     * @param request
     * @param priority
     * @param <V>
     *
     * @return
     */
    public <V> Promise<V> send(A request, RequestPriority priority) {
        final Promise<V> clientPromise = transport.newPromise();
        log.debug("Adding request '{}' to queue", request.getClass().getSimpleName());
        //Add to the request queue
        if (!requestQueue.add(new RequestDetails<>(request, clientPromise, priority, TIMEOUT_CALLBACK, getTransport()))) {
            log.error("Unable to add to the queue");
        }
        //Return the promise instance back to the client
        return clientPromise;
    }

    /**
     * Meant to be called by the handlers to indicate that a response message is ready to be received and processed
     *
     * @param response
     * @param
     */
    @Override
    public void receive(B response) {
        log.debug("Receiving '{}' from {}", response.getClass().getSimpleName(), response.sender());
        synchronized (this) {
            //Retrieve the existing session for this response
            SessionValue session = sessionManager.getSession(response);
            if (session != null) {
                //1) Retrieve our client promise from the session
                final Promise clientPromise = session.getClientPromise();
                try {
                    //2) Unregister the session
                    sessionManager.unregister(session);
                    //4) Notify the client that we have successfully received a response from the server
                    clientPromise.trySuccess(response);
                } catch (Exception e) {
                    clientPromise.tryFailure(e);
                }
            } else {
                log.warn("Did not find a session for response {} with message: {}", response, response.getMessage());
            }
        }
    }

    public PriorityBlockingQueue<RequestDetails<A>> getRequestQueue() {
        return requestQueue;
    }

    /**
     * @return Processing mode of the messenger
     */
    public ProcessingMode getProcessingMode() {
        return processingMode;
    }

    /**
     * Sets the processing mode of the messenger
     *
     * @param processingMode
     */
    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode;
    }

    /**
     * Returns the number of remaining requests in the session
     *
     * @return
     */
    public Collection<Map.Entry<SessionId, SessionValue<A, B>>> getRemaining() {
        return sessionManager.getSessionEntries();
    }

    public int getPendingRequestSize() {
        return requestQueue.size();
    }

    public boolean hasPendingRequests() {
        return requestQueue.size() > 0 && sessionManager.getSessionEntries().size() > 0;
    }

    /**
     * Returns the underlying transport
     *
     * @return Instance of {@link NettyTransport}
     */
    public T getTransport() {
        return transport;
    }

    /**
     * Returns the underlying session manager for this instance
     *
     * @return
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * @return The maximum number of retries after a read timeout occurs
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets the maximum number of retries a messenger will re-send the request to the queue once a timeout occurs
     *
     * @param maxRetries
     */
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Gracefully close/shutdown all expensive resources this messenger utilizes
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (!requestQueue.isEmpty()) {
            log.warn("Request queue is not yet empty");
        }
        stopped.set(true);
        try {
            messengerService.shutdown();
            messengerService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Error on close", e);
        }
        sessionManager.close();
        transport.close();
    }
}
