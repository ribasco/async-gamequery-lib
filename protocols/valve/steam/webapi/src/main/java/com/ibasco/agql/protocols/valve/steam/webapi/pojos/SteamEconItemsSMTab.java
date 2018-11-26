/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
