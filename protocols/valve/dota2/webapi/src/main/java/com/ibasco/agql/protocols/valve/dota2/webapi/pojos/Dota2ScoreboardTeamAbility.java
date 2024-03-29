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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Dota2ScoreboardTeamAbility class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class Dota2ScoreboardTeamAbility {

    @SerializedName("ability_id")
    @Expose
    private int abilityId;

    @SerializedName("ability_level")
    @Expose
    private int abilityLevel;

    /**
     * <p>Getter for the field <code>abilityId</code>.</p>
     *
     * @return The abilityId
     */
    public int getAbilityId() {
        return abilityId;
    }

    /**
     * <p>Setter for the field <code>abilityId</code>.</p>
     *
     * @param abilityId
     *         The ability_id
     */
    public void setAbilityId(int abilityId) {
        this.abilityId = abilityId;
    }

    /**
     * <p>Getter for the field <code>abilityLevel</code>.</p>
     *
     * @return The abilityLevel
     */
    public int getAbilityLevel() {
        return abilityLevel;
    }

    /**
     * <p>Setter for the field <code>abilityLevel</code>.</p>
     *
     * @param abilityLevel
     *         The ability_level
     */
    public void setAbilityLevel(int abilityLevel) {
        this.abilityLevel = abilityLevel;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
