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

import com.ribasco.gamecrawler.protocols.valve.server.SourcePacketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by raffy on 9/4/2016.
 */
@Deprecated
//TODO: Just move this entirely to the RequestRegistry
public class Session {
    private static final Logger log = LoggerFactory.getLogger(Session.class);

    private final RequestRegistry registry;

    private Session() {
        registry = new RequestRegistry();
    }

    private static class SessionHolder {
        private static final Session INSTANCE = new Session();
    }

    public static RequestRegistry getRegistry() {
        return SessionHolder.INSTANCE.registry;
    }

    public static String getSessionId(InetSocketAddress address, Response responsePacket) {
        Class requestClass = SourcePacketHelper.getRequestClass(responsePacket.getClass());
        return getSessionId(address, requestClass);
    }

    public static String getSessionId(InetSocketAddress address, Class<? extends GameRequestPacket> requestClass) {
        if (address == null || requestClass == null) {
            log.warn("Unable to retrieve session id. Address or Request Class is not available. (Address = {}, Request = {})", address, requestClass.toString());
            return null;
        }
        return String.format("%s:%d:%s", address.getAddress().getHostAddress(), address.getPort(), requestClass.getSimpleName());
    }
}
