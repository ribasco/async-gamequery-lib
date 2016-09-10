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

package com.ribasco.gamecrawler.clients;

import com.ribasco.gamecrawler.protocols.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by raffy on 8/27/2016.
 */
public abstract class GameClient<T extends Channel> implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(GameClient.class);

    protected AtomicInteger cancelledTasks = new AtomicInteger();
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    public static final int DEFAULT_RESPONSE_TIMEOUT = 15;

    /**
     * Runnable responsible for monitoring the request map. Throw a timeout error if any of the requests have reached the maximum wait time.
     */
    private Runnable REQUEST_MONITOR = () -> {
        //Filter entries that have elapsed for more than 5 seconds
        Session.getInstance().entrySet().stream().filter(entry -> entry.getValue() != null && entry.getValue().hasElapsed(DEFAULT_RESPONSE_TIMEOUT)).forEach(e -> {
            log.debug("Request: {} has Elapsed for more than 5 seconds (Duration: {}). Cancelling", e.getKey(), (e.getValue() != null) ? e.getValue().getDuration() : "N/A");

            //Set to failure status with a TimeoutException
            SessionInfo info = e.getValue();

            //Throw an exception if the session information is missing for the specified key
            if (info == null)
                throw new RuntimeException(String.format("Missing SessionInfo for %s", e.getKey()));

            Promise requestPromise = info.getPromise();

            //If no promise has been assigned for this request, throw an error
            if (requestPromise == null)
                throw new RuntimeException(String.format("Missing Promise instance for %s", e.getKey()));

            //Pass and alert the promise that the request has timed out
            if (requestPromise.tryFailure(new TimeoutException(String.format("Request '%s' has timed out (Run Duration: %f) ", info.getSessionId(), info.getDuration()))))
                log.debug("Successfully marked {} as a failure. Notifying Listeners for Promise: {}", e.getKey(), requestPromise);
            else
                log.debug("{} already marked as done. Unable to set to failure state.", e.getKey());

            //Remove from the registry
            Session.getInstance().unregister(e.getKey());
            cancelledTasks.incrementAndGet();
        });
    };

    /**
     * Default Pool Handler
     */
    private ChannelPoolHandler DEFAULT_POOL_HANDLER = new AbstractChannelPoolHandler() {
        @Override
        public void channelCreated(Channel ch) throws Exception {
            //log.debug("[CHANNEL CREATED] {}, Is Open: {}, Is Active: {}, Is Registered: {}", ch, ch.isOpen(), ch.isActive(), ch.isRegistered());
            configureChannel((T) ch);
        }
    };

    private ChannelPoolMap<InetSocketAddress, SimpleChannelPool> DEFAULT_POOLMAP = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
        @Override
        protected SimpleChannelPool newPool(InetSocketAddress key) {
            log.debug("Creating simple channel poool for {}", key);
            return new SimpleChannelPool(bootstrap.remoteAddress(key).localAddress(0), DEFAULT_POOL_HANDLER);
        }
    };

    public GameClient(EventLoopGroup group) {
        this.group = group;
        this.bootstrap = new Bootstrap();
        //Initialize the Client
        initialize();
    }

    protected void initialize() {
        //Configure our bootstrap
        configureBootstrap(bootstrap);

        try {
            if (log.isDebugEnabled())
                ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            //Reset cancelled task counter
            cancelledTasks.set(0);

            //Monitor each request from the session, cancel if any of them takes too long to be received
            group.next().scheduleWithFixedDelay(REQUEST_MONITOR, 1, 2, TimeUnit.SECONDS);

            //log.info("Now listening in local port: {}", ((InetSocketAddress) channel.localAddress()).getPort());
        } catch (Exception e) {
            log.error("Error during initialize", e);
        }
    }

    protected void configureBootstrap(Bootstrap bootstrap) {
        //Configure our bootstrap
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                //.option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_SNDBUF, 1048576)
                .option(ChannelOption.SO_RCVBUF, 1048576);
    }

    protected T acquireChannel(InetSocketAddress address) throws TimeoutException {
        T c = null;
        try {
            ChannelPool pool = DEFAULT_POOLMAP.get(address);
            c = (T) pool.acquire().get(); //5, TimeUnit.SECONDS
            if (c == null)
                log.debug("Unable to acquire channel for address {}", address);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Problem acquiring channel from pool", e);
        } catch (Exception e) {
            throw e;
        }
        return c;
    }

    protected void releaseChannel(InetSocketAddress address, T channel) {
        try {
            if (channel != null) {
                DEFAULT_POOLMAP.get(address).release(channel).sync();
                return;
            }
            log.debug("Unable to release channel for '{}'. Null instance.", address);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    protected <A> Promise<A> sendGameServerRequest(InetSocketAddress destination, GameRequestPacket requestPacket, ResponseCallback<A> responseCallback) {
        Promise<A> requestPromise = sendGameServerRequest(destination, requestPacket);
        requestPromise.addListener(future -> {
            if (future.isSuccess()) {
                responseCallback.onComplete(requestPromise.get(), destination, null);
            } else {
                responseCallback.onComplete(null, destination, future.cause());
            }
        });
        return requestPromise;
    }

    protected <A> Promise<A> sendGameServerRequest(InetSocketAddress destination, GameRequestPacket requestPacket) {

        //TODO: Request packet is too specific, we need to create more abstraction as per issuing a game request

        //Its important that we create this object first. Error could happen before, during or after write operation
        //so we need this to be readily available for access.
        //This promise should be on the same event loop of the acquired channel
        final Promise promise = createPromise();

        // ---------------------------------------------------------------------
        // Some things we need to consider regarding proper exception handling
        // ---------------------------------------------------------------------
        // Possible Scenarios
        //
        // 1) Exception occuring anywhere on this method
        // 2) Exception occuring during/after write operation
        // 3) Exception occuring inbound or outbound handlers

        //Validate arguments, throw exception immediately if arguments are found to be invalid
        if (destination == null || requestPacket == null) {
            throw new IllegalArgumentException("Request Packet or Destination Address cannot be null/empty");
        }

        Channel c = null;

        //Create session id
        String sessionId = createSessionId(destination, requestPacket.getClass());

        try {
            //Acquire a channel from the pool
            c = acquireChannel(destination);

            //Wrap the request details in an envelope
            GameRequestEnvelope envelope = new GameRequestEnvelope<>(requestPacket, destination);

            //TODO: Need to refactor this. The outcome of this operation will determine if the request is going to be registerd in the session or not
            c.writeAndFlush(envelope).addListener(
                    (ChannelFutureListener) future -> {
                        //Check to see if something went wrong during write operation
                        if (future.isSuccess()) {
                            //Register the session
                            Session.getInstance().register(sessionId, promise);
                            log.debug("Successfully Sent Request for \"{}\"", sessionId);
                        } else {
                            try {
                                log.debug("Error occured during write operation. ({}:{})", "error", future.cause());
                                if (!promise.isDone() && promise.cause() == null) {
                                    promise.setFailure(future.cause());
                                }
                            } finally {
                                //We need to unregister if an error occured
                                if (Session.getInstance().unregister(sessionId))
                                    log.debug("Successfully Unregistered {}", sessionId);
                            }
                        }
                    }
            );
        } catch (Exception e) {
            log.debug("An internal error occured inside sendGameServerRequest(). Setting promise to a failure state. (Request: {})", requestPacket.toString());
            promise.tryFailure(e);
            Session.getInstance().unregister(sessionId);
        } finally {
            releaseChannel(destination, (T) c);
        }
        return promise;
    }

    public void waitForAll() {
        waitForAll(-1);
    }

    public void waitForAll(int timeout) {
        if (Session.getInstance().size() > 0) {
            log.debug("There are still {} pending requests that have not received any reply from the server. Channel ", Session.getInstance().size());

            final AtomicInteger ctr = new AtomicInteger();

            SimpleDateFormat sdf = new SimpleDateFormat("@ hh:mm:ss a");

            //Display the remaining pending requests and their status
            Session.getInstance().entrySet().forEach(entry -> {
                SessionInfo details = entry.getValue();
                String countVal = String.format("%05d", ctr.incrementAndGet());
                if (details != null) {
                    String sessionId = details.getSessionId();
                    String isSuccess = (details.getPromise() != null) ? Boolean.toString(details.getPromise().isSuccess()) : "N/A";
                    String isDone = (details.getPromise() != null) ? Boolean.toString(details.getPromise().isDone()) : "N/A";
                    String hasError = (details.getPromise() != null) ? Boolean.toString(details.getPromise().cause() != null) : "N/A";
                    String timeRegistered = sdf.format(details.getTimeRegistered());
                    double runDuration = details.getDuration();

                    log.debug("{}) PENDING REQUEST for \"{}\" = (Is Done: {}, Is Success: {}, Has Error: {}, Time Registered: {}, Run Duration: {})", countVal, sessionId, isDone, isSuccess, hasError, timeRegistered, runDuration);
                } else
                    log.debug("{}) PENDING REQUEST for \"{}\" = (N/A)", countVal, entry.getKey());
            });

            try {
                int timeoutCtr = 0;
                while (Session.getInstance().size() > 0) {
                    log.warn("[REGISTRY] {} requests are still in-pending.", Session.getInstance().size());
                    if ((timeout != -1) && (++timeoutCtr > timeout)) {
                        log.debug("Wait timeout expired for {} second(s), forcing to stop.", timeout);
                        throw new TimeoutException("Wait has expired");
                    }
                    Thread.sleep(1000);
                }
                if (Session.getInstance().size() > 0)
                    log.warn("Registry is still filled with {} unfulfilled tasks and listeners are still waiting for results...Quitting Anyway", Session.getInstance().size());
                else
                    log.info("Registry is now clean. Shutting down gracefully");
            } catch (TimeoutException | InterruptedException e) {
                log.error("Timed Out or Interrupted : {}", e.getMessage());
            }
        }
    }

    <V> Promise<V> createPromise() {
        return this.group.next().newPromise();
    }

    private String createSessionId(InetSocketAddress destination, Class<? extends GameRequestPacket> requestClass) {
        return Session.getSessionId(destination, requestClass);
    }

    @Override
    public void close() throws IOException {
        log.debug("Performing a graceful shutdown for client..");
        group.shutdownGracefully();
    }

    protected abstract void configureChannel(T ch);
}
