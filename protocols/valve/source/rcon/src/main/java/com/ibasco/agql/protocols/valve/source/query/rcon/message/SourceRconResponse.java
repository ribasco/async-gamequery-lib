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

import com.ibasco.agql.core.AbstractResponse;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.net.InetSocketAddress;

/**
 * <p>Abstract SourceRconResponse class.</p>
 *
 * @author Rafael Luis Ibasco
 */
abstract public class SourceRconResponse<T> extends AbstractResponse<T> {

    /**
     * <p>Constructor for SourceRconResponse.</p>
     *
     * @param result
     *         a T object
     */
    protected SourceRconResponse(T result) {
        super(result);
    }

    /** {@inheritDoc} */
    @Override
    public void setAddress(InetSocketAddress address) {
        super.setAddress(address);
    }

    /** {@inheritDoc} */
    @Override
    protected void buildToString(ToStringBuilder builder) {
        super.buildToString(builder);
        builder.append("requestId", getRequest() != null ? getRequest().id() : "N/A");
    }
}
