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
 * <p>SteamEconItemsSMTab class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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

    /**
     * <p>Getter for the field <code>tabImageOverrideName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTabImageOverrideName() {
        return tabImageOverrideName;
    }

    /**
     * <p>Setter for the field <code>tabImageOverrideName</code>.</p>
     *
     * @param tabImageOverrideName
     *         a {@link java.lang.String} object
     */
    public void setTabImageOverrideName(String tabImageOverrideName) {
        this.tabImageOverrideName = tabImageOverrideName;
    }

    /**
     * <p>Getter for the field <code>label</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLabel() {
        return label;
    }

    /**
     * <p>Setter for the field <code>label</code>.</p>
     *
     * @param label
     *         a {@link java.lang.String} object
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a {@link java.lang.String} object
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>parentId</code>.</p>
     *
     * @return a int
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * <p>Setter for the field <code>parentId</code>.</p>
     *
     * @param parentId
     *         a int
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * <p>useLargeCells.</p>
     *
     * @return a boolean
     */
    public boolean useLargeCells() {
        return useLargeCells;
    }

    /**
     * <p>Setter for the field <code>useLargeCells</code>.</p>
     *
     * @param useLargeCells
     *         a boolean
     */
    public void setUseLargeCells(boolean useLargeCells) {
        this.useLargeCells = useLargeCells;
    }

    /**
     * <p>isDefaultTab.</p>
     *
     * @return a boolean
     */
    public boolean isDefaultTab() {
        return defaultTab;
    }

    /**
     * <p>Setter for the field <code>defaultTab</code>.</p>
     *
     * @param defaultTab
     *         a boolean
     */
    public void setDefaultTab(boolean defaultTab) {
        this.defaultTab = defaultTab;
    }

    /**
     * <p>Getter for the field <code>children</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconItemsSMTabChild> getChildren() {
        return children;
    }

    /**
     * <p>Setter for the field <code>children</code>.</p>
     *
     * @param children
     *         a {@link java.util.List} object
     */
    public void setChildren(List<SteamEconItemsSMTabChild> children) {
        this.children = children;
    }

    /**
     * <p>isHome.</p>
     *
     * @return a boolean
     */
    public boolean isHome() {
        return home;
    }

    /**
     * <p>Setter for the field <code>home</code>.</p>
     *
     * @param home
     *         a boolean
     */
    public void setHome(boolean home) {
        this.home = home;
    }

    /**
     * <p>Getter for the field <code>dropdownPrefabId</code>.</p>
     *
     * @return a long
     */
    public long getDropdownPrefabId() {
        return dropdownPrefabId;
    }

    /**
     * <p>Setter for the field <code>dropdownPrefabId</code>.</p>
     *
     * @param dropdownPrefabId
     *         a long
     */
    public void setDropdownPrefabId(long dropdownPrefabId) {
        this.dropdownPrefabId = dropdownPrefabId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
