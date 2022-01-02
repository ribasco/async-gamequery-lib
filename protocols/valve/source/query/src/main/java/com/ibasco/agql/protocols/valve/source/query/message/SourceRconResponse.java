/*
 * Copyright 2022-2022 Asynchronous Game Query Library
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

package com.ibasco.agql.protocols.valve.source.query.message;

import com.ibasco.agql.protocols.valve.source.SourceResponse;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.InetSocketAddress;

abstract public class SourceRconResponse extends SourceResponse {

    private final int requestId;

    private final boolean success;

    private final Throwable error;

    protected SourceRconResponse(int requestId, boolean success) {
        this(requestId, success, null);
    }

    protected SourceRconResponse(int requestId, boolean succeess, Throwable error) {
        this.requestId = requestId;
        this.success = succeess;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getError() {
        return error;
    }

    public int getRequestId() {
        return requestId;
    }

    @Override
    public void setAddress(InetSocketAddress address) {
        super.setAddress(address);
    }

    @Override
    protected void buildToString(ToStringBuilder builder) {
        super.buildToString(builder);
        builder.append("requestId", getRequestId());
    }
}
