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
 * <p>
 * "Kest" which stands for "Kill Eater Score Types". An object describing suffixes to use
 * after a kill eater value in an attribute display line.
 * </p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaKest {

    private int type;

    @SerializedName("type_name")
    private String name;

    @SerializedName("level_data")
    private String levelData;

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a int
     */
    public int getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type
     *         a int
     */
    public void setType(int type) {
        this.type = type;
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
     * <p>Getter for the field <code>levelData</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLevelData() {
        return levelData;
    }

    /**
     * <p>Setter for the field <code>levelData</code>.</p>
     *
     * @param levelData
     *         a {@link java.lang.String} object
     */
    public void setLevelData(String levelData) {
        this.levelData = levelData;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
