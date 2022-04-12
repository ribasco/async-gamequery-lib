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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconPlayerItemEquipInfo class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconPlayerItemEquipInfo {
    @SerializedName("class")
    private int classId;
    private int slot;

    /**
     * <p>Getter for the field <code>classId</code>.</p>
     *
     * @return a int
     */
    public int getClassId() {
        return classId;
    }

    /**
     * <p>Setter for the field <code>classId</code>.</p>
     *
     * @param classId a int
     */
    public void setClassId(int classId) {
        this.classId = classId;
    }

    /**
     * <p>Getter for the field <code>slot</code>.</p>
     *
     * @return a int
     */
    public int getSlot() {
        return slot;
    }

    /**
     * <p>Setter for the field <code>slot</code>.</p>
     *
     * @param slot a int
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("class", getClass())
                .append("slot", getSlot())
                .toString();
    }
}
