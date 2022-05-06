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
import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamAssetDescription {

    private String type;

    private String value;

    @SerializedName("app_data")
    private Map<String, String> appData;

    private String color;

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type
     *         a {@link java.lang.String} object
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value
     *         a {@link java.lang.String} object
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>appData</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, String> getAppData() {
        return appData;
    }

    /**
     * <p>Setter for the field <code>appData</code>.</p>
     *
     * @param appData
     *         a {@link java.util.Map} object
     */
    public void setAppData(Map<String, String> appData) {
        this.appData = appData;
    }

    /**
     * <p>Getter for the field <code>color</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getColor() {
        return color;
    }

    /**
     * <p>Setter for the field <code>color</code>.</p>
     *
     * @param color
     *         a {@link java.lang.String} object
     */
    public void setColor(String color) {
        this.color = color;
    }
}
