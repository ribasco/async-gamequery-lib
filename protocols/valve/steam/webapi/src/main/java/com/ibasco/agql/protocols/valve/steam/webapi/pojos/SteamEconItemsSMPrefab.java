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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>SteamEconItemsSMPrefab class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMPrefab {

    private long id;

    private String name;

    @SerializedName("config")
    private List<SteamEconItemsSMPrefabConfig> configs = new ArrayList<>();

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a long
     */
    public long getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a long
     */
    public void setId(long id) {
        this.id = id;
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

    /**
     * <p>Getter for the field <code>configs</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMPrefabConfig> getConfigs() {
        return configs;
    }

    /**
     * <p>Setter for the field <code>configs</code>.</p>
     *
     * @param configs
     *         a {@link java.util.List} object
     */
    public void setConfigs(List<SteamEconItemsSMPrefabConfig> configs) {
        this.configs = configs;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
