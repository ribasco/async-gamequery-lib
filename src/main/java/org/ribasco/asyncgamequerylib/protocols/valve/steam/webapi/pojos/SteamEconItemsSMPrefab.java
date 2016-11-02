package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconItemsSMPrefab {
    private long id;
    private String name;
    @SerializedName("config")
    private List<SteamEconItemsSMPrefabConfig> configs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SteamEconItemsSMPrefabConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SteamEconItemsSMPrefabConfig> configs) {
        this.configs = configs;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
