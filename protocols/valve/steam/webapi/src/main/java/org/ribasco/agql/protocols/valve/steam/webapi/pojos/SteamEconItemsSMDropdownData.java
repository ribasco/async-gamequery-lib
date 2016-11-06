package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMDropdownData {
    private List<SteamEconItemsSMDropdown> dropdowns;
    private List<SteamEconItemsSMPrefab> prefabs;

    public List<SteamEconItemsSMDropdown> getDropdowns() {
        return dropdowns;
    }

    public void setDropdowns(List<SteamEconItemsSMDropdown> dropdowns) {
        this.dropdowns = dropdowns;
    }

    public List<SteamEconItemsSMPrefab> getPrefabs() {
        return prefabs;
    }

    public void setPrefabs(List<SteamEconItemsSMPrefab> prefabs) {
        this.prefabs = prefabs;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
