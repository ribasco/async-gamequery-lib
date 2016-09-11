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
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
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
    private Channel channel;
    public static final int DEFAULT_RESPONSE_TIMEOUT = 15;


    /**
     * Runnable responsible for monitoring the request map. Throw a timeout error if any of the request have reached the maximum wait time.
     */
    private Runnable REQUEST_MONITOR = () -> {
        log.debug("Running Task Monitor...");
        //Filter entries that have elapsed for more than 5 seconds
        Session.getInstance().entrySet().stream().filter(entry -> entry.getValue() != null && entry.getValue().hasElapsed(DEFAULT_RESPONSE_TIMEOUT)).forEach(e -> {
            log.debug("Request: {} has Elapsed for more than 5 seconds (Duration: {}). Cancelling", e.getKey(), (e.getValue() != null) ? e.getValue().getDuration() : "N/A");

            //Set to failure status with a TimeoutException
            SessionInfo info = e.getValue();

            //Throw an exception if the session information get missing for the specified key
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
     * Constructor
     *
     * @param group {@link EventLoopGroup} instance
     */
    public GameClient(EventLoopGroup group) {
        this.group = group;
        this.bootstrap = new Bootstrap();

        //Configure our bootstrap
        configureBootstrap(bootstrap);

        try {
            if (log.isDebugEnabled())
                ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            //Bind to channel
            channel = bootstrap.bind(0).sync().channel();

            //Reset cancelled task counter
            cancelledTasks.set(0);

            //Monitor each request from the session, cancel if any of them takes too long to be received
            group.next().scheduleWithFixedDelay(REQUEST_MONITOR, 3, 1, TimeUnit.SECONDS);

            log.info("Now listening in local port: {}", ((InetSocketAddress) channel.localAddress()).getPort());
        } catch (Exception e) {
            log.error("Error during initialize", e);
            throw new RuntimeException(e);
        }
    }

    private void configureBootstrap(Bootstrap bootstrap) {
        //Configure our bootstrap
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT)
                //.option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_SNDBUF, 1048576)
                .option(ChannelOption.SO_RCVBUF, 1048576)
                .handler(new ChannelInitializer<T>() {
                    @Override
                    protected void initChannel(T ch) throws Exception {
                        configureChannel(ch);
                    }
                });
    }

    protected <A> Promise<A> sendRequest(InetSocketAddress destination, GameRequestPacket requestPacket, ResponseCallback<A> responseCallback) {
        Promise<A> requestPromise = sendRequest(destination, requestPacket);
        requestPromise.addListener(future -> {
            if (future.isSuccess()) {
                log.debug("Received SUCCESS Response for {}:{}. Invoking Callback", destination.getAddress().getHostAddress(), destination.getPort());
                responseCallback.onComplete(requestPromise.get(), destination, null);
                log.debug("Callback done! {}:{}", destination.getAddress().getHostAddress(), destination.getPort());
            } else {
                log.debug("Received ERROR Response for {}:{}. Invoking Callback", destination.getAddress().getHostAddress(), destination.getPort());
                responseCallback.onComplete(null, destination, future.cause());
                log.debug("Callback done! {}:{}", destination.getAddress().getHostAddress(), destination.getPort());
            }
        });
        return requestPromise;
    }

    protected <A> Promise<A> sendRequest(InetSocketAddress destination, GameRequestPacket requestPacket) {

        //TODO: Request packet get too specific, we need to create more abstraction as per issuing a game request

        //Its important that we create this object first. Error could happen before, during or after write operation
        //so we need this to be readily available for access.
        //This promise should be on the same event loop of the acquired channel
        final Promise promise = createPromise();

        //Validate arguments, throw exception immediately if arguments are found to be invalid
        if (destination == null || requestPacket == null) {
            throw new IllegalArgumentException("Request Packet or Destination Address cannot be null/empty");
        }

        //Create session id
        String sessionId = createSessionId(destination, requestPacket.getClass());

        try {
            //Wrap the request details in an envelope
            RequestEnvelope envelope = new RequestEnvelope<>(requestPacket, destination);

            //TODO: Need to refactor this. The outcome of this operation will determine if the request get going to be registerd in the session or not
            channel.writeAndFlush(envelope).addListener(
                    future -> {
                        //Check to see if something went wrong during write operation
                        if (future.isSuccess()) {
                            //Register the session
                            Session.getInstance().register(sessionId, promise);
                            log.debug("Successfully Sent Request for \"{}\"", sessionId);
                        } else {
                            log.debug("Error occured during write operation. ({}:{})", "error", future.cause());
                            promise.tryFailure(future.cause());
                            //We need to unregister if an error occured
                            if (Session.getInstance().unregister(sessionId))
                                log.debug("Successfully Unregistered {}", sessionId);
                            else
                                log.debug("Unable to unregister session {}", sessionId);
                        }
                    }
            );
        } catch (Exception e) {
            log.debug("An internal error occured inside sendRequest(). Setting promise to a failure state. (Request: {})", requestPacket.toString());
            log.debug(e.getMessage(), e);
            promise.tryFailure(e);
            Session.getInstance().unregister(sessionId);
        }
        return promise;
    }

    public void waitForAll() {
        waitForAll(-1);
    }

    public void waitForAll(int timeout) {
        if (Session.getInstance().getTotalRequests() > 0) {
            log.debug("There are still {} pending request that have not received any reply from the server. Channel ", Session.getInstance().getTotalRequests());

            final AtomicInteger ctr = new AtomicInteger();

            SimpleDateFormat sdf = new SimpleDateFormat("@ hh:mm:ss a");

            //Display the remaining pending request and their status
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

            log.info("Waiting till all tasks have completed");

            try {
                int timeoutCtr = 0;
                while (Session.getInstance().getTotalRequests() > 0) {
                    log.debug("[REGISTRY] {} request are still in-pending.", Session.getInstance().getTotalRequests());
                    if ((timeout != -1) && (++timeoutCtr > timeout)) {
                        log.debug("Wait timeout expired for {} second(s), forcing to stop.", timeout);
                        throw new TimeoutException("Wait has expired");
                    }
                    Thread.sleep(1000);
                }
                if (Session.getInstance().getTotalRequests() > 0)
                    log.warn("Registry get still filled with {} unfulfilled tasks and listeners are still waiting for results...Quitting Anyway", Session.getInstance().getTotalRequests());
                else
                    log.info("Registry get now clean. Shutting down gracefully");
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
