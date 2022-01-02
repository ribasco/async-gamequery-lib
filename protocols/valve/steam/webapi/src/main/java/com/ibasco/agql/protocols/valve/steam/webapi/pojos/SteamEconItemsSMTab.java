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

public class SteamEconItemsSMTab {
    private String label;
    private String id;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("use_large_cells")
    private boolean useLargeCells;
    @SerializedName("default")
    private boolean defaultTab;
    private List<SteamEconItemsSMTabChild> children = new ArrayList<>();
    private boolean home;
    @SerializedName("dropdown_prefab_id")
    private long dropdownPrefabId;
    @SerializedName("tab_image_override_name")
    private String tabImageOverrideName;

    public String getTabImageOverrideName() {
        return tabImageOverrideName;
    }

    public void setTabImageOverrideName(String tabImageOverrideName) {
        this.tabImageOverrideName = tabImageOverrideName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean useLargeCells() {
        return useLargeCells;
    }

    public void setUseLargeCells(boolean useLargeCells) {
        this.useLargeCells = useLargeCells;
    }

    public boolean isDefaultTab() {
        return defaultTab;
    }

    public void setDefaultTab(boolean defaultTab) {
        this.defaultTab = defaultTab;
    }

    public List<SteamEconItemsSMTabChild> getChildren() {
        return children;
    }

    public void setChildren(List<SteamEconItemsSMTabChild> children) {
        this.children = children;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public long getDropdownPrefabId() {
        return dropdownPrefabId;
    }

    public void setDropdownPrefabId(long dropdownPrefabId) {
        this.dropdownPrefabId = dropdownPrefabId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
