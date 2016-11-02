package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconSchemaItemLevel {
    private String name;
    private List<SteamEconSchemaItemLevelInfo> levels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SteamEconSchemaItemLevelInfo> getLevels() {
        return levels;
    }

    public void setLevels(List<SteamEconSchemaItemLevelInfo> levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
