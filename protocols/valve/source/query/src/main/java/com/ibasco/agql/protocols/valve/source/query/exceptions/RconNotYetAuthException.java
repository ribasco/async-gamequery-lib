/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.source.query.exceptions;

import com.ibasco.agql.core.exceptions.AsyncGameLibCheckedException;
import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;

/**
 * Thrown when the current rcon connection requires authentication
 *
 * @author Rafael Luis Ibasco
 */
public class RconNotYetAuthException extends AsyncGameLibCheckedException {

    private final SourceRconAuthReason reason;

    public RconNotYetAuthException(String message) {
        this(message, SourceRconAuthReason.UNKNOWN);
    }

    public RconNotYetAuthException(String message, SourceRconAuthReason reason) {
        super(message);
        this.reason = reason;
    }

    public final SourceRconAuthReason getReason() {
        return reason;
    }
}
