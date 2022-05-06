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
 * <p>SteamEconSchemaItemSet class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaItemSet {

    @SerializedName("item_set")
    private String itemSet;

    private String name;

    private List<String> items = new ArrayList<>();

    private List<SteamEconSchemaItemAttribute> attributes = new ArrayList<>();

    @SerializedName("store_bundle ")
    private String storeBundle;

    /**
     * <p>Getter for the field <code>itemSet</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemSet() {
        return itemSet;
    }

    /**
     * <p>Setter for the field <code>itemSet</code>.</p>
     *
     * @param itemSet
     *         a {@link java.lang.String} object
     */
    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
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
     * <p>Getter for the field <code>items</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * <p>Setter for the field <code>items</code>.</p>
     *
     * @param items
     *         a {@link java.util.List} object
     */
    public void setItems(List<String> items) {
        this.items = items;
    }

    /**
     * <p>Getter for the field <code>attributes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaItemAttribute> getAttributes() {
        return attributes;
    }

    /**
     * <p>Setter for the field <code>attributes</code>.</p>
     *
     * @param attributes
     *         a {@link java.util.List} object
     */
    public void setAttributes(List<SteamEconSchemaItemAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * <p>Getter for the field <code>storeBundle</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getStoreBundle() {
        return storeBundle;
    }

    /**
     * <p>Setter for the field <code>storeBundle</code>.</p>
     *
     * @param storeBundle
     *         a {@link java.lang.String} object
     */
    public void setStoreBundle(String storeBundle) {
        this.storeBundle = storeBundle;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
