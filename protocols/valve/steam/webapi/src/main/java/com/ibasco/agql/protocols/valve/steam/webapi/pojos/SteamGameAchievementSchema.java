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
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamGameAchievementSchema {

    @SerializedName("name")
    private String name;

    @SerializedName("defaultvalue")
    private int defaultValue;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("hidden")
    private int hidden;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    @SerializedName("icongray")
    private String icongray;

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
     * <p>Getter for the field <code>defaultValue</code>.</p>
     *
     * @return a int
     */
    public int getDefaultValue() {
        return defaultValue;
    }

    /**
     * <p>Setter for the field <code>defaultValue</code>.</p>
     *
     * @param defaultValue
     *         a int
     */
    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * <p>Getter for the field <code>displayName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * <p>Setter for the field <code>displayName</code>.</p>
     *
     * @param displayName
     *         a {@link java.lang.String} object
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * <p>Getter for the field <code>hidden</code>.</p>
     *
     * @return a int
     */
    public int getHidden() {
        return hidden;
    }

    /**
     * <p>Setter for the field <code>hidden</code>.</p>
     *
     * @param hidden
     *         a int
     */
    public void setHidden(int hidden) {
        this.hidden = hidden;
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

    /**
     * <p>Getter for the field <code>icon</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIcon() {
        return icon;
    }

    /**
     * <p>Setter for the field <code>icon</code>.</p>
     *
     * @param icon
     *         a {@link java.lang.String} object
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * <p>Getter for the field <code>icongray</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIcongray() {
        return icongray;
    }

    /**
     * <p>Setter for the field <code>icongray</code>.</p>
     *
     * @param icongray
     *         a {@link java.lang.String} object
     */
    public void setIcongray(String icongray) {
        this.icongray = icongray;
    }
}
