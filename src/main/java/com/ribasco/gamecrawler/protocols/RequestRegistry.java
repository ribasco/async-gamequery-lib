/*
 * MIT License
 *
 * Copyright (c) [year] [fullname]
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
 */

package com.ribasco.gamecrawler.protocols;

import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by raffy on 9/6/2016.
 */
public class RequestRegistry {
    private static final Logger log = LoggerFactory.getLogger(RequestRegistry.class);

    private final ConcurrentHashMap<String, SessionInfo> registry = new ConcurrentHashMap<>();

    public SessionInfo register(String sessionId, Promise promise) {
        //TODO: How should we handle existing session ids? This is most likely re-sending the request
        /**
         * On the event that this request attempts to replace another, we need to cancel the previous one first, mark it as 'cancelled', remove from the registry then re-register it.
         */
        if (exists(sessionId)) {
            //REVIEW: Should we remove the previous and replace it with the new one?
            log.warn("EXISTING SESSION IN REGISTRY : {}", sessionId);
        }

        log.debug("Registering Session: {}", sessionId);
        SessionInfo details = new SessionInfo(sessionId, promise, System.currentTimeMillis());
        registry.put(sessionId, details);
        return details;
    }

    public boolean unregister(String sessionId) {
        return unregister(sessionId, false);
    }

    public boolean unregister(String sessionId, boolean forceCancel) {
        //TODO: Make forceCancel optional. Ideally automated cancelling of tasks should be done by an external thread monitoring the requests
        log.debug("Unregistering Session: {}", sessionId);
        //Retrieve the session information
        SessionInfo details = registry.get(sessionId);
        if (details != null) {
            Promise p = details.getPromise();
            //Only force cancel tasks that aren't done yet
            if (forceCancel && (p != null && !p.isDone())) {
                log.debug("Forcing to cancel Session {}", sessionId);
                if (p.cancel(false))
                    log.debug("Session Cancelled Sucessfully {}", sessionId);
                else
                    log.debug("Unable to cancel session {}", sessionId);
            }
        }
        return (registry.remove(sessionId) != null);
    }

    public Promise getPromise(String sessionId) {
        SessionInfo details = registry.get(sessionId);
        if (details != null)
            return details.getPromise();
        return null;
    }

    public boolean exists(String sessionId) {
        return registry.containsKey(sessionId);
    }

    public int size() {
        return registry.size();
    }

    public Set<Map.Entry<String, SessionInfo>> entrySet() {
        return registry.entrySet();
    }
}
