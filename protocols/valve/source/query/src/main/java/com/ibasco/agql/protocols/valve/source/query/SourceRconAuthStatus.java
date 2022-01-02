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

package com.ibasco.agql.protocols.valve.source.query;

import com.ibasco.agql.protocols.valve.source.query.enums.SourceRconAuthReason;

import java.net.InetSocketAddress;

public final class SourceRconAuthStatus {

    private final int requestId;

    private final boolean authenticated;

    private final String reason;

    private final SourceRconAuthReason reasonCode;

    private final InetSocketAddress address;

    public SourceRconAuthStatus(InetSocketAddress address, int requestId, boolean authenticated, String reason, SourceRconAuthReason reasonCode) {
        this.address = address;
        this.requestId = requestId;
        this.authenticated = authenticated;
        this.reason = reason;
        this.reasonCode = reasonCode;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getReason() {
        return reason;
    }

    public int getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "SourceRconAuthStatus{" +
                "authenticated=" + authenticated +
                ", reason='" + reason + '\'' +
                '}';
    }
}
