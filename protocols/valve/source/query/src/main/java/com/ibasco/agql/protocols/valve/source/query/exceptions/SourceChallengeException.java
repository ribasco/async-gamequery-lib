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

package com.ibasco.agql.protocols.valve.source.query.exceptions;

import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;

/**
 * Indicates that the server responded with a challenge number. Use the provided challenge number in this instance for your next info query.
 *
 * @author Rafael Luis Ibasco
 */
public class SourceChallengeException extends AsyncGameLibUncheckedException {

    private final Integer challengeNumber;

    public SourceChallengeException(Integer challengeNumber) {
        super(String.format("Server requires a new challenge number: %d", challengeNumber));
        this.challengeNumber = challengeNumber;
    }

    public Integer getChallengeNumber() {
        return challengeNumber;
    }
}
