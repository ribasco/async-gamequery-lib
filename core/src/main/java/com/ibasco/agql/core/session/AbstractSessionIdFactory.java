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

package com.ibasco.agql.core.session;

import com.google.common.collect.Multimap;
import com.ibasco.agql.core.AbstractMessage;
import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;

import java.net.InetSocketAddress;
import java.util.Collection;

abstract public class AbstractSessionIdFactory<
        R extends AbstractRequest,
        S extends AbstractResponse>
        implements SessionIdFactory {

    protected static final String DEFAULT_ID_FORMAT = "%s:%s:%s";

    private Multimap<Class<? extends R>, Class<? extends S>> lookup;

    protected Class<? extends S> getResponseClass(R request) {
        if (lookup == null)
            throw new IllegalStateException("Lookup map has not been initialized");
        if (request != null) {
            //noinspection unchecked
            Collection<Class<? extends S>> s = lookup.get((Class<? extends R>) request.getClass());
            return s.stream().findFirst().get();
        }
        return null;
    }

    /**
     * <p>Create a generic id based on the message</p>
     *
     * @param message The {@link AbstractMessage} instance to be converted to an Id
     *
     * @return A {@link String} id representation of the {@link AbstractMessage} provided
     */
    protected String createIdStringFromMsg(AbstractMessage message) {
        //Format: <response class name> : <ip address> : <port>
        if (message == null)
            throw new IllegalArgumentException("Message not specified");

        InetSocketAddress address;
        Class messageClass;

        if (message instanceof AbstractRequest) {
            messageClass = getResponseClass((R) message);
            address = message.recipient();
        } else {
            messageClass = message.getClass();
            address = message.sender();
        }

        return String.format(DEFAULT_ID_FORMAT,
                messageClass.getSimpleName(),
                address.getAddress().getHostAddress(),
                             address.getPort());
    }

    public Multimap<Class<? extends R>, Class<? extends S>> getLookup() {
        return lookup;
    }

    public void setLookup(Multimap<Class<? extends R>, Class<? extends S>> lookup) {
        this.lookup = lookup;
    }
}
