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
public class SteamAssetTag {

    @SerializedName("internal_name")
    private String internalName;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("category_name")
    private String categoryName;

    /**
     * <p>Getter for the field <code>internalName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getInternalName() {
        return internalName;
    }

    /**
     * <p>Setter for the field <code>internalName</code>.</p>
     *
     * @param internalName
     *         a {@link java.lang.String} object
     */
    public void setInternalName(String internalName) {
        this.internalName = internalName;
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
     * <p>Getter for the field <code>category</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCategory() {
        return category;
    }

    /**
     * <p>Setter for the field <code>category</code>.</p>
     *
     * @param category
     *         a {@link java.lang.String} object
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * <p>Getter for the field <code>categoryName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * <p>Setter for the field <code>categoryName</code>.</p>
     *
     * @param categoryName
     *         a {@link java.lang.String} object
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
