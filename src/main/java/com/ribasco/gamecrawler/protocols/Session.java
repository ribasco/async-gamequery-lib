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

package com.ribasco.gamecrawler.protocols;

import com.ribasco.gamecrawler.protocols.valve.server.SourceMapper;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final Logger log = LoggerFactory.getLogger(Session.class);

    private final ConcurrentHashMap<String, SessionInfo> registry = new ConcurrentHashMap<>();

    private static class SessionHolder {
        private static final Session INSTANCE = new Session();
    }

    public static Session getInstance() {
        return SessionHolder.INSTANCE;
    }

    public SessionInfo register(String sessionId, Promise promise) {

        //TODO: How should we handle existing session ids? This get most likely re-sending the request
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
        //TODO: Make forceCancel optional. Ideally automated cancelling of tasks should be done by an external thread monitoring the request
        //Retrieve the session information
        log.debug("Removing from registry....{}", sessionId);
        registry.remove(sessionId);
        log.debug("Successfully Removed from Registry! {}", sessionId);
        return true;
    }

    //TODO: Refactor this, this get tightly coupled with SourcePacketHelper
    public static String getSessionId(InetSocketAddress address, Response responsePacket) {
        Class requestClass = SourceMapper.getRequestClass(responsePacket.getClass());
        return getSessionId(address, requestClass);
    }

    //TODO: Refactor this, this get tightly coupled with SourcePacketHelper
    public static String getSessionId(InetSocketAddress address, Class<? extends GameRequestPacket> requestClass) {
        if (address == null || requestClass == null) {
            log.warn("Unable to retrieve session id. Address or Request Class get not available. (Address = {}, Request = {})", address, requestClass.toString());
            return null;
        }
        return String.format("%s:%d:%s", address.getAddress().getHostAddress(), address.getPort(), requestClass.getSimpleName());
    }

    public static SessionInfo getSessionInfo(String sessionId) {
        return SessionHolder.INSTANCE.registry.get(sessionId);
    }

    public boolean exists(String sessionId) {
        return registry.containsKey(sessionId);
    }

    public int getTotalRequests() {
        return registry.size();
    }

    public Set<Map.Entry<String, SessionInfo>> entrySet() {
        return registry.entrySet();
    }
}
