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
 * <p>SteamEconItemsSMPlayerClass class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMPlayerClass {
    private int id;
    @SerializedName("base_name")
    private String baseName;
    @SerializedName("localized_text")
    private String localizedText;

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a int
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>baseName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * <p>Setter for the field <code>baseName</code>.</p>
     *
     * @param baseName a {@link java.lang.String} object
     */
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    /**
     * <p>Getter for the field <code>localizedText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLocalizedText() {
        return localizedText;
    }

    /**
     * <p>Setter for the field <code>localizedText</code>.</p>
     *
     * @param localizedText a {@link java.lang.String} object
     */
    public void setLocalizedText(String localizedText) {
        this.localizedText = localizedText;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
