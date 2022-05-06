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
 * <p>SteamEconItemsSMSorter class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconItemsSMSorter {

    private long id;

    private String name;

    @SerializedName("data_type")
    private String dataType;

    @SerializedName("sort_field")
    private String sortField;

    @SerializedName("sort_reversed")
    private boolean sortReversed;

    @SerializedName("localized_text")
    private String localizedText;

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
     * <p>Getter for the field <code>dataType</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * <p>Setter for the field <code>dataType</code>.</p>
     *
     * @param dataType
     *         a {@link java.lang.String} object
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * <p>Getter for the field <code>sortField</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * <p>Setter for the field <code>sortField</code>.</p>
     *
     * @param sortField
     *         a {@link java.lang.String} object
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * <p>isSortReversed.</p>
     *
     * @return a boolean
     */
    public boolean isSortReversed() {
        return sortReversed;
    }

    /**
     * <p>Setter for the field <code>sortReversed</code>.</p>
     *
     * @param sortReversed
     *         a boolean
     */
    public void setSortReversed(boolean sortReversed) {
        this.sortReversed = sortReversed;
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
     * @param localizedText
     *         a {@link java.lang.String} object
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
