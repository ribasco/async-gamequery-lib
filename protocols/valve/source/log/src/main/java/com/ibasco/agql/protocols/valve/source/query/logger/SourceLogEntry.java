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

package com.ibasco.agql.protocols.valve.source.query.logger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.net.InetSocketAddress;

/**
 * A class representing a Raw Log Entry from a Source Server
 *
 * @author Rafael Luis Ibasco
 */
public final class SourceLogEntry {
    private String message;
    private InetSocketAddress sourceAddress;

    /**
     * <p>Constructor for SourceLogEntry.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param sourceAddress a {@link java.net.InetSocketAddress} object
     */
    public SourceLogEntry(String message, InetSocketAddress sourceAddress) {
        this.message = message;
        this.sourceAddress = sourceAddress;
    }

    /**
     * <p>Getter for the field <code>message</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMessage() {
        return message;
    }

    /**
     * <p>Setter for the field <code>message</code>.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * <p>Getter for the field <code>sourceAddress</code>.</p>
     *
     * @return a {@link java.net.InetSocketAddress} object
     */
    public InetSocketAddress getSourceAddress() {
        return sourceAddress;
    }

    /**
     * <p>Setter for the field <code>sourceAddress</code>.</p>
     *
     * @param sourceAddress a {@link java.net.InetSocketAddress} object
     */
    public void setSourceAddress(InetSocketAddress sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("source", sourceAddress)
                .append("msg", message)
                .toString();
    }
}
