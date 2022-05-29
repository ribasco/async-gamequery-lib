package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SteamCommunityApp {

    @SerializedName("appid")
    @Expose
    private Integer appid;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("community_visible_stats")
    @Expose
    private Boolean communityVisibleStats;

    @SerializedName("propagation")
    @Expose
    private String propagation;

    @SerializedName("has_adult_content")
    @Expose
    private Boolean hasAdultContent;

    @SerializedName("app_type")
    @Expose
    private Integer appType;

    @SerializedName("tool")
    @Expose
    private Boolean tool;

    @SerializedName("is_visible_in_steam_china")
    @Expose
    private Boolean isVisibleInSteamChina;

    @SerializedName("media")
    @Expose
    private Boolean media;

    @SerializedName("friendly_name")
    @Expose
    private String friendlyName;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getCommunityVisibleStats() {
        return communityVisibleStats;
    }

    public void setCommunityVisibleStats(Boolean communityVisibleStats) {
        this.communityVisibleStats = communityVisibleStats;
    }

    public String getPropagation() {
        return propagation;
    }

    public void setPropagation(String propagation) {
        this.propagation = propagation;
    }

    public Boolean getHasAdultContent() {
        return hasAdultContent;
    }

    public void setHasAdultContent(Boolean hasAdultContent) {
        this.hasAdultContent = hasAdultContent;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Boolean getTool() {
        return tool;
    }

    public void setTool(Boolean tool) {
        this.tool = tool;
    }

    public Boolean getIsVisibleInSteamChina() {
        return isVisibleInSteamChina;
    }

    public void setIsVisibleInSteamChina(Boolean isVisibleInSteamChina) {
        this.isVisibleInSteamChina = isVisibleInSteamChina;
    }

    public Boolean getMedia() {
        return media;
    }

    public void setMedia(Boolean media) {
        this.media = media;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

}