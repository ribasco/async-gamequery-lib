/*
 * MIT License
 * Copyright (c) 2021 Asynchronous Game Query Library
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core.session;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.agql.core.*;
import com.ibasco.agql.core.enums.RequestStatus;
import io.netty.util.HashedWheelTimer;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

abstract public class AbstractSessionManager<R extends AbstractRequest, S extends AbstractResponse> implements SessionManager<R, S> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSessionManager.class);

    private static final int DEFAULT_READ_TIMEOUT = 5;

    private final HashedWheelTimer sessionTimer;

    private final int sessionTimeoutValue;

    private final TimeUnit sessionTimeoutUnit;

    private final SessionIdFactory idFactory;

    private final Multimap<SessionId, SessionEntry<R, S>> entries = Multimaps.synchronizedSortedSetMultimap(TreeMultimap.create(new SessionIdComparator(), new SessionValueComparator()));

    private final AtomicLong indexCounter = new AtomicLong();

    public AbstractSessionManager(SessionIdFactory idFactory, int sessionTimeoutValue, TimeUnit sessionTimeoutUnit) {
        sessionTimer = new HashedWheelTimer(new ThreadFactoryBuilder().setNameFormat("timeout-%d").setDaemon(true).build());
        this.idFactory = (idFactory != null) ? idFactory : new DefaultSessionIdFactory();
        this.sessionTimeoutValue = (sessionTimeoutValue <= 0) ? DEFAULT_READ_TIMEOUT : sessionTimeoutValue;
        this.sessionTimeoutUnit = (sessionTimeoutUnit == null) ? TimeUnit.SECONDS : sessionTimeoutUnit;
    }

    public final int getSessionTimeoutValue() {
        return sessionTimeoutValue;
    }

    public final TimeUnit getSessionTimeoutUnit() {
        return sessionTimeoutUnit;
    }

    @Override
    public SessionEntry<R, S> getSession(AbstractMessage message) {
        SessionId id = getId(message);
        log.debug("Retrieving Session for {}", id);
        if (id != null)
            return getSession(id);
        return null;
    }

    @Override
    public SessionEntry<R, S> getSession(SessionId id) {
        Collection<SessionEntry<R, S>> c = entries.get(id);
        return (id != null && c.size() > 0) ? c.iterator().next() : null;
    }

    @Override
    public SessionId getId(AbstractMessage message) {
        if (idFactory == null)
            throw new IllegalStateException("No id factory assigned");
        SessionId id = idFactory.createId(message);
        log.debug("Checking if the Session Id is registered (id : {})", id);
        if (!entries.containsKey(id)) {
            log.debug("Did not find session id '{}' in the map", id);
            entries.entries().forEach(e -> log.debug(" # {} = {}", e.getKey(), e.getValue()));
            return null;
        }
        return entries.keySet().stream().filter(sessionId -> sessionId.equals(id)).findFirst().orElse(null);
    }

    @Override
    public SessionId create(RequestDetails<R, S> requestDetails) {
        SessionId id = idFactory.createId(requestDetails.getRequest());
        log.debug("Registering session with id '{}'", id);
        //Create our session store object and set it's properties
        SessionEntry<R, S> sessionEntry = new SessionEntry<>(id, requestDetails, indexCounter.incrementAndGet());
        //Add to the registry
        if (entries.put(id, sessionEntry)) {
            log.debug("Session ID '{}' successfully registered", id);
            requestDetails.setStatus(RequestStatus.REGISTERED);
        }
        return id;
    }

    @Override
    public void startSession(SessionId id) {
        SessionEntry<R, S> entry = getSession(id);
        if (entry == null)
            throw new IllegalStateException(String.format("No session found for '%s'", id));
        if (entry.getTimeout() != null)
            throw new IllegalStateException(String.format("Session is already active '%s'", id));
        entry.setTimeout(sessionTimer.newTimeout(new ReadRequestTimeoutTimerTask(id, this), sessionTimeoutValue, sessionTimeoutUnit));
        log.debug(String.format("Session started: '%s'", id));
    }

    @Override
    public void cancelSession(SessionId id) {
        SessionEntry<R, S> entry = getSession(id);
        if (entry == null)
            throw new IllegalStateException(String.format("No session found for '%s'", id));
        if (entry.getTimeout() == null)
            throw new IllegalStateException(String.format("Session not yet started for '%s'", id));
        entry.getTimeout().cancel();
        entry.setTimeout(null);
        log.debug(String.format("Session cancelled: '%s'", id));
    }

    @Override
    public boolean delete(SessionId id) {
        return delete(getSession(id));
    }

    @Override
    public boolean delete(SessionEntry sessionEntry) {
        log.debug("Unregistering session {}", sessionEntry.getId());
        //Cancel the timeout instance
        if (sessionEntry.getTimeout() != null) {
            sessionEntry.getTimeout().cancel();
        }
        return entries.remove(sessionEntry.getId(), sessionEntry);
    }

    @Override
    public boolean isRegistered(AbstractMessage message) {
        return getId(message) != null;
    }

    @Override
    public Collection<Map.Entry<SessionId, SessionEntry<R, S>>> getEntries() {
        return entries.entries();
    }

    @Override
    public SessionIdFactory getSessionIdFactory() {
        return this.idFactory;
    }

    @Override
    public void close() throws IOException {
        if (getEntries().size() > 0) {
            log.debug("Request to shutdown has been initiated but the session manager still contains " +
                              "pending entries that has not completed.");
        }
        log.debug("Stopping timer (pending timeouts: {})", sessionTimer.pendingTimeouts());
        sessionTimer.stop();
        entries.clear();
    }

    private static class SessionIdComparator implements Comparator<SessionId> {

        @Override
        public int compare(SessionId o1, SessionId o2) {
            return new CompareToBuilder()
                    .append(o1.getId(), o2.getId())
                    .toComparison();
        }
    }

    private static class SessionValueComparator implements Comparator<SessionEntry> {

        @Override
        public int compare(SessionEntry o1, SessionEntry o2) {
            return new CompareToBuilder()
                    .append(o1.getIndex(), o2.getIndex())
                    .toComparison();
        }
    }
}
