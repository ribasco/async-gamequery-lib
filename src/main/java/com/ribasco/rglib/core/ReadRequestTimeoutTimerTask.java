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

import com.ribasco.rglib.core.exceptions.ReadTimeoutException;
import com.ribasco.rglib.core.session.SessionKey;
import com.ribasco.rglib.core.session.SessionManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by raffy on 9/20/2016.
 */
public class ReadRequestTimeoutTimerTask implements TimerTask {
    private static final Logger log = LoggerFactory.getLogger(ReadRequestTimeoutTimerTask.class);
    private SessionKey key;
    private SessionManager sessionManager;

    public ReadRequestTimeoutTimerTask(SessionKey key, SessionManager sessionManager) {
        this.key = key;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        log.debug("Timeout occured for Session {}", key);
        //Notify the listener that timeout has occured
        synchronized (this) {
            final Promise clientPromise = sessionManager.retrievePromise(key);
            //Check first if the promise has been completed
            if (clientPromise != null && !clientPromise.isSuccess()) {
                //Send a ReadTimeoutException to the client
                clientPromise.tryFailure(new ReadTimeoutException(sessionManager.getSessionKeyFactory().duplicate(key), String.format("Timeout occured for Message=%s, Address=%s", key.getResponseClass().getSimpleName(), key.getResponseAddress())));
            }
            //Unregister from the session
            sessionManager.unregister(key);
        }
    }
}
