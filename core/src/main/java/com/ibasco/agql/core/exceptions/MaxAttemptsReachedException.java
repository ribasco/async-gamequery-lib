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

package com.ibasco.agql.core.exceptions;

import com.ibasco.agql.core.AbstractRequest;

import java.net.InetSocketAddress;

public class MaxAttemptsReachedException extends AsyncGameLibUncheckedException {

    private final int attemptCount;

    private final int maxAttemptCount;

    private final InetSocketAddress remoteAddress;

    private final AbstractRequest request;

    public MaxAttemptsReachedException(Throwable cause, InetSocketAddress remoteAddress, AbstractRequest request, int attemptCount, int maxAttemptCount) {
        super(cause);
        this.remoteAddress = remoteAddress;
        this.request = request;
        this.attemptCount = attemptCount;
        this.maxAttemptCount = maxAttemptCount;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public AbstractRequest getRequest() {
        return request;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getMaxAttemptCount() {
        return maxAttemptCount;
    }
}