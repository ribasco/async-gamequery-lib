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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.agql.core.enums.QueueStrategy;
import com.ibasco.agql.core.enums.RequestStatus;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.session.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * <p>The base implementation of the {@link Messenger} interface. Contains an internal queue for the requests and
 * process them based on priority</p>
 *
 * @param <R>
 *         The type of {@link AbstractRequest}
 * @param <S>
 *         The type {@link AbstractResponse}
 */
abstract public class AbstractMessenger<R extends AbstractRequest, S extends AbstractResponse> implements Messenger<R, S> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMessenger.class);

    private static final int DEFAULT_REQUEST_QUEUE_CAPACITY = 30;

    private final AtomicBoolean processRequests = new AtomicBoolean(false);

    private final ScheduledExecutorService messengerService;

    private final SessionManager<R, S> sessionManager;

    private final Transport<R> transport;

    private final BlockingDeque<RequestDetails<R, S>> requestQueue;

    private final Consumer<RequestDetails<R, S>> requestProcessor;

    private QueueStrategy queueStrategy;

    private final ExecutorService executorService;

    public AbstractMessenger(QueueStrategy queueStrategy) {
        this(new DefaultSessionIdFactory(), queueStrategy);
    }

    public AbstractMessenger(SessionIdFactory idFactory, QueueStrategy queueStrategy) {
        this(new DefaultSessionManager(idFactory), queueStrategy, DEFAULT_REQUEST_QUEUE_CAPACITY, null);
    }

    public AbstractMessenger(SessionIdFactory idFactory, QueueStrategy queueStrategy, ExecutorService executorService) {
        this(new DefaultSessionManager(idFactory), queueStrategy, DEFAULT_REQUEST_QUEUE_CAPACITY, executorService);
    }

    public AbstractMessenger(SessionManager sessionManager, QueueStrategy queueProcessingStrategy, int initQueueCapacity, ExecutorService executorService) {
        //Set processing mode
        this.queueStrategy = queueProcessingStrategy;
        log.debug("Using Processing Mode : {}", queueProcessingStrategy);
        //Use the default session manager if not specified
        this.executorService = executorService;
        //noinspection unchecked
        this.sessionManager = (sessionManager != null) ? sessionManager : new DefaultSessionManager<>();
        this.requestQueue = new LinkedBlockingDeque<>(initQueueCapacity);
        this.transport = createTransport();
        this.messengerService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("messenger-%d").build());
        this.requestProcessor = (queueProcessingStrategy == QueueStrategy.SYNCHRONOUS) ? this::processSync : this::processAsync;
    }

    /**
     * Call to start processing requests
     */
    private void start() {
        if (!messengerService.isShutdown() && !processRequests.getAndSet(true)) {
            messengerService.execute(() -> {
                try {
                    while (processRequests.get()) {
                        RequestDetails<R, S> details = requestQueue.take();
                        requestProcessor.accept(details);
                    }
                    log.debug("Gracefully exiting request processor");
                } catch (InterruptedException e) {
                    log.debug("Processing of requests has been interrupted", e);
                } finally {
                    processRequests.set(false);
                }
            });
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * <p>Let the concrete messenger create and initialize the transport</p>
     *
     * @return The {@link Transport} service that will be used by this messenger
     */
    abstract protected Transport<R> createTransport();

    /**
     * Process requests synchronously
     */
    private void processSync(final RequestDetails<R, S> requestDetails) {
        //Since we are processing synchronously, we will not remove the head of the queue immediately but rather
        //only remove the head once it completes

        try {
            //Do we have any requests to process?
            if (requestDetails == null) {
                return;
            }

            RequestStatus status = requestDetails.getStatus();

            //Only process new REQUESTS
            if (status == RequestStatus.NEW) {
                log.debug("Processing NEW request from Queue: {}", requestDetails);
                requestDetails.setStatus(RequestStatus.ACCEPTED);

                //Register the request to the session manager
                SessionId id = sessionManager.create(requestDetails);

                //Update the status to registered
                requestDetails.setStatus(RequestStatus.REGISTERED);

                Transport<R> transport = requestDetails.getTransport();

                CompletableFuture<Void> writeFuture = transport.send(requestDetails.getRequest());

                requestDetails.setStatus(RequestStatus.AWAIT);

                //Perform actions upon write completion
                writeFuture.whenComplete((aVoid, writeError) -> {
                    //If we encounter a write error, notify the listeners then immediately remove it from the queue
                    if (writeError != null) {
                        requestDetails.setStatus(RequestStatus.DONE);
                        requestDetails.getPromise().completeExceptionally(writeError);
                        requestQueue.remove(requestDetails);
                        performSessionCleanup(id);
                        log.debug("Error sending request : {}", requestDetails.getRequest());
                    }
                    //Write operation successful
                    else {
                        //start the session timer
                        sessionManager.startSession(id);

                        log.debug("Request Successfully Sent to the Transport : {}", requestDetails.getRequest());
                        //Update status to SENT
                        requestDetails.setStatus(RequestStatus.SENT);

                        //Requests will only be removed from the queue when it completes
                        requestDetails.getPromise().whenComplete((res, error) -> {
                            requestDetails.setStatus(RequestStatus.DONE);
                            //Only remove from the queue once the task completes
                            requestQueue.remove(requestDetails);
                            //Perform session cleanup
                            performSessionCleanup(id);
                        });
                    }
                });
            } else {
                requestQueue.putLast(requestDetails);
            }
        } catch (Exception e) {
            if (requestDetails.getPromise() != null)
                requestDetails.getPromise().completeExceptionally(e);
        }
    }

    /**
     * Process requests asynchronously
     */
    private void processAsync(final RequestDetails<R, S> requestDetails) {
        //Only process new requests
        if (requestDetails.getStatus() == RequestStatus.NEW) {
            try {
                //Set status to ACCEPTED
                requestDetails.setStatus(RequestStatus.ACCEPTED);

                //Register the session immediately, duplicate requests will be queued in the order they are sent.
                SessionId id = sessionManager.create(requestDetails);

                //Perform session cleanup operations on completion
                requestDetails.getPromise().whenComplete((response, throwable) -> performSessionCleanup(id));

                Transport<R> transport = requestDetails.getTransport();
                //Send then listen for the write completion status
                transport.send(requestDetails.getRequest()).whenComplete((aVoid, writeError) -> {
                    log.debug("Sent request '{}' to transport", getMessageName(requestDetails.getRequest()));
                    if (writeError != null) {
                        //If the write operation failed, we need to unregister from the session
                        log.error("Write operation failed, unregistering from session : {} = {}", id, writeError);
                        requestDetails.setStatus(RequestStatus.DONE);
                        //Notify listeners+
                        if (requestDetails.getPromise() != null)
                            requestDetails.getPromise().completeExceptionally(writeError);
                    } else {
                        //Update the request status
                        requestDetails.setStatus(RequestStatus.SENT);
                    }
                });
                requestDetails.setStatus(RequestStatus.AWAIT);
                sessionManager.startSession(id);
            } catch (Throwable e) {
                requestDetails.getPromise().completeExceptionally(e);
            }
        }
    }

    /**
     * Send the request to the transport
     *
     * @param request
     *         An instance of {@link AbstractRequest} to be sent
     *
     * @return A {@link CompletableFuture} containing a value of {@link AbstractResponse}
     */
    @Override
    public CompletableFuture<S> send(R request) {
        log.debug("Adding request '{}' to queue", request.getClass().getSimpleName());
        CompletableFuture<S> promise = new CompletableFuture<>();
        try {
            requestQueue.put(new RequestDetails<>(request, promise, this.transport));
            start(); //start if not yet started
        } catch (InterruptedException e) {
            promise.completeExceptionally(e);
        }
        return promise;
    }

    /**
     * <p>Called by the transport when a response has been received from the server</p>
     *
     * @param response
     *         The response received from the server
     * @param error
     *         Error thrown by the transport while processing the request. Otherwise null.
     */
    @Override
    public void receive(S response, Throwable error) {
        if (error != null) {
            log.error("receive(): An error was received from one of the response handlers", error);
        } else {
            log.debug("receive(): Receiving response '{}'", response);
        }

        if (error instanceof ResponseException) {
            ResponseException ex = (ResponseException) error;
            if (ex.getOriginatingRequest() != null) {
                SessionId id = sessionManager.getId(ex.getOriginatingRequest());
                SessionEntry<R, S> session = sessionManager.getSession(id);
                if (session != null) {
                    CompletableFuture<S> clientPromise = session.getClientPromise();
                    clientPromise.completeExceptionally(ex);
                } else {
                    log.debug("Session not found for response: {}", response);
                }
            }
            return;
        }

        //Retrieve the existing session for this response
        SessionEntry<R, S> session = lookupSessionFromResponse(response);

        if (session != null) {
            log.debug("Found matching request from response (Request: '{}', Response: '{}')", getMessageName(session.getRequest()), getMessageName(response));
            //1) Retrieve our client promise from the session
            CompletableFuture<S> clientPromise = session.getClientPromise();
            //2) Notify the client that we have successfully received a response from the server
            if (clientPromise.complete(response))
                log.debug("Notified client of completion event : {} - {}", session.getId(), session.getRequest().transactionId());
            else
                log.debug("Unable to transition session to completion state : {}", session.getId());
        } else {
            log.warn("No associated session is found for Response '{}' (Rmaining entries in session: {})", response, sessionManager.getEntries().size());
            throw new IllegalStateException(String.format("No associated session is found for Response '%s'", response));
        }
    }

    private static String getMessageName(AbstractMessage msg) {
        if (msg == null) {
            return "N/A (null)";
        }
        return String.format("%s (TID: %s, UID: %s)", msg.getMessage().getClass().getSimpleName(), msg.transactionId(), msg.id().toString());
    }

    protected SessionEntry<R, S> lookupSessionFromResponse(S response) {
        String responseId = response.transactionId();
        if (StringUtils.isBlank(responseId))
            throw new IllegalStateException(String.format("Missing transaction id for RESPONSE '%s'", getMessageName(response)));

        for (Map.Entry<SessionId, SessionEntry<R, S>> entry : sessionManager.getEntries()) {
            R request = entry.getValue().getRequest();
            String requestId = request.transactionId();
            if (!StringUtils.isBlank(requestId) && requestId.equalsIgnoreCase(responseId))
                return entry.getValue();
        }
        return null;
    }

    /**
     * Unregister from the session
     *
     * @param id
     *         The {@link SessionId} to unregister
     */
    private void performSessionCleanup(SessionId id) {
        SessionEntry session = sessionManager.getSession(id);
        if (session != null) {
            sessionManager.delete(session);
        }
    }

    protected SessionManager<R, S> getSessionManager() {
        return sessionManager;
    }

    /**
     * Retrieve the internal request queue
     *
     * @return The {@link BlockingDeque} of this messenger
     */
    public BlockingDeque<RequestDetails<R, S>> getRequestQueue() {
        return requestQueue;
    }

    /**
     * @return Processing mode of the messenger
     */
    public QueueStrategy getProcessingMode() {
        return queueStrategy;
    }

    /**
     * Sets the processing mode of the messenger
     *
     * @param queueStrategy
     *         The {@link QueueStrategy} for the messenger
     */
    public void setProcessingMode(QueueStrategy queueStrategy) {
        this.queueStrategy = queueStrategy;
    }

    /**
     * Returns the number of remaining requests in the session
     *
     * @return A {@link Collection} of session entries
     */
    public Collection<Map.Entry<SessionId, SessionEntry<R, S>>> getRemaining() {
        return sessionManager.getEntries();
    }

    /**
     * <p>Gracefully close/shutdown all expensive resources this messenger utilizes</p>
     *
     * @throws IOException
     *         Thrown when an attempt to close the resources managed by this Messenger has failed.
     */
    @Override
    public void close() throws IOException {
        String name = getClass().getSimpleName();
        log.debug("[{}] closing", name);
        if (!requestQueue.isEmpty()) {
            log.warn("[{}] Request queue is not yet empty (Total: {})", name, requestQueue.size());
            for (RequestDetails details : requestQueue) {
                log.warn("\t[{}] Request: {}", name, details);
            }
        }
        if (!messengerService.isTerminated())
            processRequests.set(false);
        try {
            log.debug("Attempting to shutdown messenger service '{}' gracefully", getClass().getSimpleName());
            messengerService.shutdown();
            if (!messengerService.awaitTermination(5, TimeUnit.SECONDS)) {
                log.debug("[{}] Timeout elapsed. The following tasks that didn't execute: ", name);
                List<Runnable> tasks = messengerService.shutdownNow();
                for (Runnable task : tasks) {
                    log.debug("- Tasks: {}", task);
                }
            }
        } catch (InterruptedException e) {
            log.error("[{}] Error on close: {}", name, e.getMessage());
        }
        sessionManager.close();
        transport.close();
    }
}
