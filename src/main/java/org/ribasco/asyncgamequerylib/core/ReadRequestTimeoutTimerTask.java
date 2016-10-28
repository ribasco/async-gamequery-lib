/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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

package org.ribasco.asyncgamequerylib.core;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.ribasco.asyncgamequerylib.core.exceptions.ReadTimeoutException;
import org.ribasco.asyncgamequerylib.core.session.SessionId;
import org.ribasco.asyncgamequerylib.core.session.SessionManager;
import org.ribasco.asyncgamequerylib.core.session.SessionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Created by raffy on 9/20/2016.
 */
public class ReadRequestTimeoutTimerTask implements TimerTask {
    private static final Logger log = LoggerFactory.getLogger(ReadRequestTimeoutTimerTask.class);
    private SessionId id;
    private SessionManager sessionManager;

    public ReadRequestTimeoutTimerTask(SessionId sessionId, SessionManager sessionManager) {
        this.id = sessionId;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        log.debug("Timeout occured for Session {}", id);
        //Notify the listener that timeout has occured
        final SessionValue session = sessionManager.getSession(id);

        //Do not proceed if the session is null
        if (session == null) {
            log.error("could not find session value for id {}. Registry Size : {}", id, sessionManager.getSessionEntries().size());
            return;
        }

        //Check first if the promise has been completed
        if (session.getClientPromise() != null && !session.getClientPromise().isDone() && !session.getClientPromise().isCancelled() && !timeout.isCancelled()) {
            //Send a ReadTimeoutException to the client
            session.getClientPromise().completeExceptionally(new ReadTimeoutException(id, String.format("Timeout occured for '%s' Started: %d minutes ago", id, Duration.ofMillis(System.currentTimeMillis() - session.getTimeRegistered()).toMinutes())));
        }
    }
}
