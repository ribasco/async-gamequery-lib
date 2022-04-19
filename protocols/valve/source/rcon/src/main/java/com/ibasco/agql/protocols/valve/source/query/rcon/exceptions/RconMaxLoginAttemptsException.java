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

package com.ibasco.agql.protocols.valve.source.query.rcon.exceptions;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.protocols.valve.source.query.rcon.enums.SourceRconAuthReason;

import java.net.InetSocketAddress;

/**
 * Thrown when the maximum number of login attempts has been reached
 *
 * @author Rafael Luis Ibasco
 */
public class RconMaxLoginAttemptsException extends RconAuthException {

    private final int attemptCount;

    private final int maxAttemptCount;

    public RconMaxLoginAttemptsException(AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason, int attemptCount, int maxAttemptCount) {
        super(request, address, reason);
        this.attemptCount = attemptCount;
        this.maxAttemptCount = maxAttemptCount;
    }

    public RconMaxLoginAttemptsException(String message, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason, int attemptCount, int maxAttemptCount) {
        super(message, request, address, reason);
        this.attemptCount = attemptCount;
        this.maxAttemptCount = maxAttemptCount;
    }

    public RconMaxLoginAttemptsException(String message, Throwable cause, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason, int attemptCount, int maxAttemptCount) {
        super(message, cause, request, address, reason);
        this.attemptCount = attemptCount;
        this.maxAttemptCount = maxAttemptCount;
    }

    public RconMaxLoginAttemptsException(Throwable cause, AbstractRequest request, InetSocketAddress address, SourceRconAuthReason reason, int attemptCount, int maxAttemptCount) {
        super(cause, request, address, reason);
        this.attemptCount = attemptCount;
        this.maxAttemptCount = maxAttemptCount;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getMaxAttemptCount() {
        return maxAttemptCount;
    }
}
