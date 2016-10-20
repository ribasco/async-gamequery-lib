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

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import com.ribasco.rglib.core.*;
import io.netty.util.HashedWheelTimer;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by raffy on 9/25/2016.
 */
public class DefaultSessionManager<Req extends AbstractRequest,
        Res extends AbstractResponse>
        implements SessionManager<Req, Res> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSessionManager.class);
    private static final int DEFAULT_READ_TIMEOUT = 15;
    private HashedWheelTimer sessionTimer;
    private SessionIdFactory factory;
    private final Multimap<SessionId, SessionValue<Req, Res>> session = Multimaps.synchronizedSortedSetMultimap(TreeMultimap.create(new SessionIdComparator(), new SessionValueComparator()));
    private Map<Class<? extends Req>, Class<? extends Res>> directory = null;
    private final AtomicLong indexCounter = new AtomicLong();

    private class SessionIdComparator implements Comparator<SessionId> {
        @Override
        public int compare(SessionId o1, SessionId o2) {
            return new CompareToBuilder()
                    .append(o1.getId(), o2.getId())
                    .toComparison();
        }
    }

    private class SessionValueComparator implements Comparator<SessionValue> {
        @Override
        public int compare(SessionValue o1, SessionValue o2) {
            return new CompareToBuilder()
                    .append(o1.getIndex(), o2.getIndex())
                    .toComparison();
        }
    }

    public DefaultSessionManager(AbstractSessionIdFactory factory) {
        sessionTimer = new HashedWheelTimer();
        directory = new HashMap<>();
        this.factory = (factory != null) ? factory : new DefaultSessionIdFactory();
        factory.setLookup(directory);
    }

    @Override
    public SessionValue<Req, Res> getSession(AbstractMessage message) {
        final SessionId id = getId(message);
        if (id != null)
            return getSession(id);
        return null;
    }

    @Override
    public SessionValue<Req, Res> getSession(SessionId id) {
        final Collection<SessionValue<Req, Res>> c = session.get(id);
        synchronized (this) {
            return (id != null && c.size() > 0) ? c.iterator().next() : null;
        }
    }

    @Override
    public SessionId getId(AbstractMessage message) {
        if (factory == null)
            throw new IllegalStateException("No id factory assigned");
        final SessionId id = factory.createId(message);
        synchronized (this) {
            if (!session.containsKey(id)) {
                return null;
            }
            return session.keySet().stream()
                    .filter(sessionId -> sessionId.equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public SessionId register(RequestDetails<Req, Res> requestDetails) {
        log.debug("Registering session : {}", requestDetails.getRequest());
        final SessionId id = factory.createId(requestDetails.getRequest());
        //Create our session store object and set it's properties
        SessionValue<Req, Res> sessionValue = new SessionValue<>(id, requestDetails, indexCounter.incrementAndGet());
        sessionValue.setExpectedResponse(findResponseClass(requestDetails.getRequest()));
        sessionValue.setTimeout(sessionTimer.newTimeout(new ReadRequestTimeoutTimerTask(id, this), DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS));
        //Add to the registry
        synchronized (this) {
            session.put(id, sessionValue);
        }
        return id;
    }

    @Override
    public boolean unregister(SessionId id) {
        return unregister(getSession(id));
    }

    @Override
    public synchronized boolean unregister(SessionValue sessionValue) {
        log.debug("Unregistering session {}", sessionValue.getId());
        if (sessionValue == null) {
            log.error("Session Value is null {}", session.entries().size());
            return false;
        }
        synchronized (this) {
            //Cancel the timeout instance
            if (sessionValue.getTimeout() != null &&
                    !sessionValue.getTimeout().isCancelled() &&
                    !sessionValue.getTimeout().isExpired()) {
                sessionValue.getTimeout().cancel();
            }
            return session.remove(sessionValue.getId(), sessionValue);
        }
    }

    @Override
    public boolean isRegistered(AbstractMessage message) {
        return getId(message) != null;
    }

    @Override
    public synchronized Collection<Map.Entry<SessionId, SessionValue<Req, Res>>> getSessionEntries() {
        return session.entries();
    }

    @Override
    public Class<? extends Res> findResponseClass(Req request) {
        return directory.get(request.getClass());
    }

    @Override
    public Map<Class<? extends Req>, Class<? extends Res>> getLookupMap() {
        return directory;
    }

    @Override
    public SessionIdFactory getSessionIdFactory() {
        return this.factory;
    }

    @Override
    public void close() throws IOException {
        if (getSessionEntries().size() > 0) {
            log.warn("Request to shutdown has been initiated but the session manager still contains " +
                    "pending entries that has not completed. Notifying all pending entries " +
                    "that shutdown has been initiated.");
        }
        sessionTimer.stop();
        session.clear();
    }
}
