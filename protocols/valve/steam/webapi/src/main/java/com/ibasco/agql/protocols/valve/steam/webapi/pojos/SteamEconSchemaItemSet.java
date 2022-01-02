/*
 * Copyright 2018-2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

public class SteamEconSchemaItemSet {
    @SerializedName("item_set")
    private String itemSet;
    private String name;
    private List<String> items = new ArrayList<>();
    private List<SteamEconSchemaItemAttribute> attributes = new ArrayList<>();
    @SerializedName("store_bundle ")
    private String storeBundle;

    public String getItemSet() {
        return itemSet;
    }

    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<SteamEconSchemaItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SteamEconSchemaItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getStoreBundle() {
        return storeBundle;
    }

    public void setStoreBundle(String storeBundle) {
        this.storeBundle = storeBundle;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
