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

import com.ribasco.rglib.core.session.*;
import com.ribasco.rglib.core.transport.NettyTransport;
import io.netty.util.Timeout;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by raffy on 9/14/2016.
 */
public abstract class AbstractMessenger<A extends AbstractRequest, B extends AbstractResponse, T extends NettyTransport> implements Messenger<A, B> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMessenger.class);

    private SessionManager<A, B> sessionManager;
    private T transport;

    public AbstractMessenger(T transport) {
        this(transport, new DefaultSessionKeyFactory());
    }

    public AbstractMessenger(T transport, AbstractSessionKeyFactory keyFactory) {
        this(transport, new DefaultSessionManager(keyFactory));
    }

    public AbstractMessenger(T transport, SessionManager sessionManager) {
        this.sessionManager = (sessionManager != null) ? sessionManager : new DefaultSessionManager<A, B>(new DefaultSessionKeyFactory());
        this.transport = transport;
        //Configure transport before initialization
        configureTransport(transport);
        //Configure the mappings before initialization
        configureMappings(this.sessionManager.getDirectoryMap());
        //Initialize the transport
        transport.initialize();
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
     * Sends a request to it's destination
     *
     * @param request
     * @param <V>
     *
     * @return
     */
    @Override
    public <V> Promise<V> send(A request) {
        //Set local address for transport
        request.setSender(transport.localAddress());

        final Promise<V> transportPromise = transport.send(request, true);
        final Promise<V> clientPromise = transport.newPromise();

        //Add a listener for write i/o operation status
        transportPromise.addListener(future -> {
            //If write is successful, we can now register the session
            if (future.isSuccess()) {
                sessionManager.register(request, clientPromise);
            } else {
                clientPromise.tryFailure(future.cause());
            }
        });
        //Return the promise instance back to the client
        return clientPromise;
    }

    /**
     * This method is called by the netty handlers once a complete response has been received and ready to be processed
     *
     * @param response
     * @param
     */
    @Override
    public void receive(B response) {
        log.debug("Receiving '{}' from {}", response.getClass().getSimpleName(), response.sender());

        synchronized (this) {
            //Retrieve the existing session for this response
            final SessionKey key = sessionManager.retrieveKey(response);
            if (key != null) {
                //1) Retrieve our client promise from the session registry
                final Promise<Object> clientPromise = sessionManager.retrievePromise(key);
                //2) Cancel the timeout for the given response
                final Timeout responseTimeout = key.getTimeout();
                if (responseTimeout != null && !responseTimeout.isCancelled()) {
                    log.debug("Cancelled Timeout for \"{}\" = {}", response.getClass().getSimpleName(), response.sender());
                    responseTimeout.cancel();
                }
                //3) Unregister from the session
                sessionManager.unregister(key);
                //4) Notify our listeners that we have successfully received a response from the server
                clientPromise.trySuccess(response);
            } else {
                log.info("Did not find the response for: {} with message: {}", response, response.getMessage());
            }
        }
    }

    /**
     * Returns the number of remaining requests in the session
     *
     * @return
     */
    public Set<Map.Entry<SessionKey, Promise<?>>> getRemaining() {
        return sessionManager.getPendingEntries();
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

    @Override
    public void close() throws IOException {
        transport.close();
        sessionManager.close();
    }
}
