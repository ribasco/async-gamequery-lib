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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconItemsSMPrefabConfig class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMPrefabConfig {
    @SerializedName("dropdown_id")
    private long dropdownId;
    private String name;
    private boolean enabled;
    @SerializedName("default_selection_id")
    private long defaultSelectionId;

    /**
     * <p>Getter for the field <code>dropdownId</code>.</p>
     *
     * @return a long
     */
    public long getDropdownId() {
        return dropdownId;
    }

    /**
     * <p>Setter for the field <code>dropdownId</code>.</p>
     *
     * @param dropdownId a long
     */
    public void setDropdownId(long dropdownId) {
        this.dropdownId = dropdownId;
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
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>isEnabled.</p>
     *
     * @return a boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Setter for the field <code>enabled</code>.</p>
     *
     * @param enabled a boolean
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>Getter for the field <code>defaultSelectionId</code>.</p>
     *
     * @return a long
     */
    public long getDefaultSelectionId() {
        return defaultSelectionId;
    }

    /**
     * <p>Setter for the field <code>defaultSelectionId</code>.</p>
     *
     * @param defaultSelectionId a long
     */
    public void setDefaultSelectionId(long defaultSelectionId) {
        this.defaultSelectionId = defaultSelectionId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
