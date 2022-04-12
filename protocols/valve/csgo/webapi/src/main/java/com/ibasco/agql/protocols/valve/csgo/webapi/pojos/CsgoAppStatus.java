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

package com.ibasco.agql.protocols.valve.csgo.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>CsgoAppStatus class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoAppStatus {
    private int version;
    private long timestamp;
    private String time;

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return a int
     */
    public int getVersion() {
        return version;
    }

    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version a int
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return a long
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp a long
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>Getter for the field <code>time</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTime() {
        return time;
    }

    /**
     * <p>Setter for the field <code>time</code>.</p>
     *
     * @param time a {@link java.lang.String} object
     */
    public void setTime(String time) {
        this.time = time;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
