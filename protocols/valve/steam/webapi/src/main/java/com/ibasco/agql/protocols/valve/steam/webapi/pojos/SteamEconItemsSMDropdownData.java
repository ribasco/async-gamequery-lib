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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>SteamEconItemsSMDropdownData class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMDropdownData {
    private List<SteamEconItemsSMDropdown> dropdowns = new ArrayList<>();
    private List<SteamEconItemsSMPrefab> prefabs = new ArrayList<>();

    /**
     * <p>Getter for the field <code>dropdowns</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMDropdown> getDropdowns() {
        return dropdowns;
    }

    /**
     * <p>Setter for the field <code>dropdowns</code>.</p>
     *
     * @param dropdowns a {@link java.util.List} object
     */
    public void setDropdowns(List<SteamEconItemsSMDropdown> dropdowns) {
        this.dropdowns = dropdowns;
    }

    /**
     * <p>Getter for the field <code>prefabs</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMPrefab> getPrefabs() {
        return prefabs;
    }

    /**
     * <p>Setter for the field <code>prefabs</code>.</p>
     *
     * @param prefabs a {@link java.util.List} object
     */
    public void setPrefabs(List<SteamEconItemsSMPrefab> prefabs) {
        this.prefabs = prefabs;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
