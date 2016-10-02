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

package com.ribasco.rglib.core.session;

import com.ribasco.rglib.core.AbstractRequest;
import com.ribasco.rglib.core.AbstractResponse;
import com.ribasco.rglib.core.ReadRequestTimeoutTimerTask;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by raffy on 9/25/2016.
 */
public class DefaultSessionManager<T extends AbstractRequest, A extends AbstractResponse> implements SessionManager<T, A> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSessionManager.class);
    private final ConcurrentHashMap<SessionKey, Promise<?>> session = new ConcurrentHashMap<>();
    private static final int DEFAULT_READ_TIMEOUT = 5;
    private HashedWheelTimer sessionTimer;
    private SessionKeyFactory factory;
    private Map<Class<? extends T>, Class<? extends A>> directory = null;

    public DefaultSessionManager(AbstractSessionKeyFactory factory) {
        sessionTimer = new HashedWheelTimer();
        directory = new HashMap<>();
        this.factory = (factory != null) ? factory : new DefaultSessionKeyFactory();
        factory.setLookup(directory);
    }

    @Override
    public Promise<?> retrievePromise(SessionKey key) {
        return session.get(key);
    }

    @Override
    public SessionKey retrieveKey(A response) {
        SessionKey key = factory.createKey(response);
        if (!session.containsKey(key)) {
            return null;
        }
        return session.keySet().stream()
                .filter(keyEntry -> keyEntry.equals(key))
                .findFirst()
                .orElse(null);
    }

    @Override
    public SessionKey createKey(T request) {
        return factory.createKey(request);
    }

    @Override
    public SessionKey createKey(A response) {
        return factory.createKey(response);
    }

    @Override
    public SessionKey register(T request, Promise<?> promise) {
        SessionKey key = createKey(request);
        if (!session.containsKey(key)) {
            key.setTimeout(sessionTimer.newTimeout(new ReadRequestTimeoutTimerTask(key, this), DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS));
            session.put(key, promise);
            log.debug("Registered Session: {}", key);
            return key;
        }
        return null;
    }

    @Override
    public Set<Map.Entry<SessionKey, Promise<?>>> getPendingEntries() {
        return session.entrySet();
    }

    @Override
    public Class<? extends A> lookupResponseClass(T request) {
        return directory.get(request.getClass());
    }

    @Override
    public Map<Class<? extends T>, Class<? extends A>> getDirectoryMap() {
        return directory;
    }

    @Override
    public SessionKeyFactory getSessionKeyFactory() {
        return this.factory;
    }

    @Override
    public void unregister(SessionKey key) {
        //Remove the key from the session map
        session.remove(key);
        log.debug("Unregistered session '{}'", key);
    }

    @Override
    public void close() throws IOException {
        sessionTimer.stop();
        session.clear();
    }
}
