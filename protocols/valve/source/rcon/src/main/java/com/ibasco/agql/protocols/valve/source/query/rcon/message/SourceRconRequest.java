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

package com.ibasco.agql.protocols.valve.source.query.rcon.message;

import com.ibasco.agql.core.AbstractRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Abstract SourceRconRequest class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceRconRequest extends AbstractRequest {

    private int requestId;

    /**
     * <p>Getter for the field <code>requestId</code>.</p>
     *
     * @return a int
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * <p>Setter for the field <code>requestId</code>.</p>
     *
     * @param requestId a int
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /** {@inheritDoc} */
    @Override
    protected void buildToString(ToStringBuilder builder) {
        super.buildToString(builder);
        builder.append("requestId", getRequestId());
    }
}