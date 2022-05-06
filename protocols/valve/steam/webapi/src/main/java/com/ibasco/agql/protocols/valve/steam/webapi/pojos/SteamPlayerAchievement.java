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

/**
 * <p>SteamPlayerAchievement class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamPlayerAchievement {

    @SerializedName("apiname")
    private String key;

    @SerializedName("achieved")
    private int achieved;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>Setter for the field <code>key</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setKey(String name) {
        this.key = name;
    }

    /**
     * <p>Getter for the field <code>achieved</code>.</p>
     *
     * @return a int
     */
    public int getAchieved() {
        return achieved;
    }

    /**
     * <p>Setter for the field <code>achieved</code>.</p>
     *
     * @param achieved
     *         a int
     */
    public void setAchieved(int achieved) {
        this.achieved = achieved;
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
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
