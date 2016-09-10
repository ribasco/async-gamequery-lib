/***************************************************************************************************
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
 **************************************************************************************************/

package com.ribasco.gamecrawler.protocols;

import io.netty.util.concurrent.Promise;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by raffy on 9/8/2016.
 */
public class SessionInfo {
    private String sessionId;
    private Promise promise;
    private long timeRegistered;
    private AtomicBoolean accepted;

    public SessionInfo(String sessionId, Promise promise, long timeRegistered) {
        this.sessionId = sessionId;
        this.promise = promise;
        this.timeRegistered = timeRegistered;
    }

    public Promise getPromise() {
        return this.promise;
    }

    public boolean hasElapsed(long seconds) {
        long elapsed = (System.currentTimeMillis() - timeRegistered) / 1000;
        return (elapsed > seconds);
    }

    /**
     * Returns the number of seconds that has elapsed since this session was registered
     *
     * @return
     */
    public double getDuration() {
        return (System.currentTimeMillis() - timeRegistered) / 1000.0d;
    }

    /**
     * The time this session was registered
     *
     * @return Returns the time in milliseconds
     */
    public long getTimeRegistered() {
        return this.timeRegistered;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isAccepted() {
        return accepted.get();
    }

    public void setAccepted(AtomicBoolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("SessionId=").append(sessionId)
                .append(",")
                .append("Promise=").append(promise)
                .append(",")
                .append("Time Registered=")
                .toString();
    }
}
