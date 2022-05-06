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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconKillEaterRanks class.</p>
 *
 * @author Rafael Luis Ibasco
 * @see <a href="https://wiki.teamfortress.com/wiki/Talk:WebAPI/GetSchema">Talk:WebAPI/GetSchema</a>
 */
@Deprecated
public class SteamEconKillEaterRanks {

    private int level;

    private int requiredScore;

    private String name;

    /**
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return a int
     */
    public int getLevel() {
        return level;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level
     *         a int
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * <p>Getter for the field <code>requiredScore</code>.</p>
     *
     * @return a int
     */
    public int getRequiredScore() {
        return requiredScore;
    }

    /**
     * <p>Setter for the field <code>requiredScore</code>.</p>
     *
     * @param requiredScore
     *         a int
     */
    public void setRequiredScore(int requiredScore) {
        this.requiredScore = requiredScore;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
