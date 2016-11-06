package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

public class StoreAppAchievementHighlight {
    private String name;
    @SerializedName("path")
    private String pathUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }
}
