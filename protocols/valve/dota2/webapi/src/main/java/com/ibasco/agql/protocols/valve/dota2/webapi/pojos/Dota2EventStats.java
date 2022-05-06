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

package com.ibasco.agql.protocols.valve.dota2.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2EventStats class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2EventStats {

    private int eventPoints;

    /**
     * <p>Getter for the field <code>eventPoints</code>.</p>
     *
     * @return a int
     */
    public int getEventPoints() {
        return eventPoints;
    }

    /**
     * <p>Setter for the field <code>eventPoints</code>.</p>
     *
     * @param eventPoints
     *         a int
     */
    public void setEventPoints(int eventPoints) {
        this.eventPoints = eventPoints;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
