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

import com.ibasco.agql.core.AbstractMessage;
import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.RequestDetails;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;

public interface SessionManager<R extends AbstractRequest, S extends AbstractResponse> extends Closeable {
    /**
     * Returns the {@link SessionEntry} based on the {@link SessionId} provided.
     * If multiple values exists for the same key, only the head {@link SessionEntry} element will be retrieved.
     * This is somehow similar to peek() in Queue based implementations. The ordering is guaranteed by the {@link SessionEntry} index.
     *
     * @param id The {@link SessionId} instance to be used as the lookup key.
     *
     * @return A {@link SessionEntry} or NULL if no session found for the specified id
     */
    SessionEntry<R, S> getSession(SessionId id);

    /**
     * Get session based on the {@link AbstractMessage} instance
     *
     * @param message The {@link AbstractMessage} instance to use for the lookup reference
     *
     * @return A {@link SessionEntry} instance if found. Null if a session does not exists for the specified {@link AbstractMessage}
     */
    SessionEntry<R, S> getSession(AbstractMessage message);

    /**
     * Returns the session id from the registry.
     *
     * @param message An instance of {@link AbstractMessage} to use as a reference for the {@link SessionId} lookup.
     *
     * @return The {@link SessionId} instance retrieved from the registry. Null if no {@link SessionId} has been found in the registry for the message.
     */
    SessionId getId(AbstractMessage message);

    /**
     * Registers the request to the session registry. This should be used once a request has been delivered via the underlying transport.
     * If the same request type already exists in the registry, it will not replace the existing, rather it will be placed within the same collection under the same {@link SessionId}.
     *
     * @param requestDetails A {@link RequestDetails} instance containing the details of the request.
     *
     * @return A {@link SessionId} if the request has been successfully registered.
     */
    SessionId create(RequestDetails<R, S> requestDetails);

    /**
     * Removes the associated session from the registry (if available) using a {@link SessionId} instance.
     * This will try to retrieve the existing {@link SessionEntry} from the registry using the id provided.
     * If multiple sessions exists for this id, then only the top {@link SessionEntry} will be removed.
     *
     * @param id The {@link SessionId} instance to be used as the lookup reference for the session.
     *
     * @return <code>true</code> if the operation has been successful.
     */
    boolean delete(SessionId id);

    /**
     * Removes the associated session from the registry (if available) using the {@link SessionEntry} provided.
     *
     * @param value The {@link SessionEntry} instance to be used as the lookup reference for the session.
     *
     * @return <code>true</code> if the operation has been successful.
     */
    boolean delete(SessionEntry value);

    /**
     * This lookups the session registry map and indicates whether or not the message is in the registry or not
     *
     * @param message An instance of {@link AbstractMessage} to be used as the lookup reference in the registry.
     *
     * @return <code>true</code> if the specified message is registered in the session registry otherwise false.
     */
    boolean isRegistered(AbstractMessage message);

    /**
     * <p>Returns a flat representation of the existing entries in the session registry</p>
     *
     * @return a collection containing entries of {@link SessionId} and {@link SessionEntry} pair.
     */
    Collection<Map.Entry<SessionId, SessionEntry<R, S>>> getEntries();

    SessionIdFactory getSessionIdFactory();

    void startSession(SessionId id);

    void cancelSession(SessionId id);
}
