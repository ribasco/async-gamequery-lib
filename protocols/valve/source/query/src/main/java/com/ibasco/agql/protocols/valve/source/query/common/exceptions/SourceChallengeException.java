/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.common.exceptions;

import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;

/**
 * Thrown when a server requires a new/updated challenge number
 *
 * @author Rafael Luis Ibasco
 */
public class SourceChallengeException extends AsyncGameLibUncheckedException {

    private final int challenge;

    /**
     * <p>Constructor for SourceChallengeException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param challenge a int
     */
    public SourceChallengeException(String message, int challenge) {
        super(message);
        this.challenge = challenge;
    }

    /**
     * <p>Getter for the field <code>challenge</code>.</p>
     *
     * @return a int
     */
    public int getChallenge() {
        return challenge;
    }
}
