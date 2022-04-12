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
 * <p>CsgoDatacenterStatus class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class CsgoDatacenterStatus {
    private String location;
    private String capacity;
    private String load;

    /**
     * <p>Getter for the field <code>location</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLocation() {
        return location;
    }

    /**
     * <p>Setter for the field <code>location</code>.</p>
     *
     * @param location a {@link java.lang.String} object
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * <p>Getter for the field <code>capacity</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * <p>Setter for the field <code>capacity</code>.</p>
     *
     * @param capacity a {@link java.lang.String} object
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /**
     * <p>Getter for the field <code>load</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLoad() {
        return load;
    }

    /**
     * <p>Setter for the field <code>load</code>.</p>
     *
     * @param load a {@link java.lang.String} object
     */
    public void setLoad(String load) {
        this.load = load;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
