package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SteamEconItemsSMPrefabConfig {
    @SerializedName("dropdown_id")
    private long dropdownId;
    private String name;
    private boolean enabled;
    @SerializedName("default_selection_id")
    private long defaultSelectionId;

    public long getDropdownId() {
        return dropdownId;
    }

    public void setDropdownId(long dropdownId) {
        this.dropdownId = dropdownId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDefaultSelectionId() {
        return defaultSelectionId;
    }

    public void setDefaultSelectionId(long defaultSelectionId) {
        this.defaultSelectionId = defaultSelectionId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
