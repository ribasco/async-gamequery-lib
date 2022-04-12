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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>Dota2MatchAbilityUpgrade class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2MatchAbilityUpgrade {

    @SerializedName("ability")
    private int ability;
    @SerializedName("time")
    private int time;
    @SerializedName("level")
    private int level;

    /**
     * <p>Getter for the field <code>ability</code>.</p>
     *
     * @return The ability
     */
    public int getAbility() {
        return ability;
    }

    /**
     * <p>Setter for the field <code>ability</code>.</p>
     *
     * @param ability
     *         The ability
     */
    public void setAbility(int ability) {
        this.ability = ability;
    }

    /**
     * <p>Getter for the field <code>time</code>.</p>
     *
     * @return The time
     */
    public int getTime() {
        return time;
    }

    /**
     * <p>Setter for the field <code>time</code>.</p>
     *
     * @param time
     *         The time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return The level
     */
    public int getLevel() {
        return level;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level
     *         The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
