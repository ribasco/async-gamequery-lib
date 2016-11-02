package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private List<SteamEconItemsSMTabChild> children;
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
