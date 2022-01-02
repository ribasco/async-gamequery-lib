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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 */
public class SteamAssetClassInfo {
    /**
     * An encoded URL to be passed to http://cdn.steamcommunity.com/economy/image/<encoded url>
     */
    @SerializedName("icon_url")
    private String encodedIconUrl;

    @SerializedName("icon_url_large")
    private String encodedIconUrlLarge;

    @SerializedName("icon_drag_url")
    private String encodedIconDragUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("market_hash_name")
    private String marketHashName;

    @SerializedName("market_name")
    private String marketName;

    @SerializedName("name_color")
    private String nameColor;

    @SerializedName("background_color")
    private String backgroundColor;

    @SerializedName("type")
    private String type;

    //TODO: Use TypeAdapters as provided by GSON then convert this into a boolean type
    @SerializedName("tradable")
    private String tradable;

    @SerializedName("marketable")
    private String marketable;

    @SerializedName("commodity")
    private String commodity;

    @SerializedName("market_tradable_restriction")
    private String marketTradableRestriction;

    @SerializedName("fraudwarnings")
    private String fraudWarnings;

    @SerializedName("descriptions")
    private Map<String, SteamAssetDescription> descriptions;

    @SerializedName("owner_descriptions")
    private String ownerDescriptions;

    @SerializedName("tags")
    private Map<String, SteamAssetTag> tags;

    @SerializedName("classid")
    private String classId;

    public String getEncodedIconUrl() {
        return encodedIconUrl;
    }

    public void setEncodedIconUrl(String encodedIconUrl) {
        this.encodedIconUrl = encodedIconUrl;
    }

    public String getEncodedIconUrlLarge() {
        return encodedIconUrlLarge;
    }

    public void setEncodedIconUrlLarge(String encodedIconUrlLarge) {
        this.encodedIconUrlLarge = encodedIconUrlLarge;
    }

    public String getEncodedIconDragUrl() {
        return encodedIconDragUrl;
    }

    public void setEncodedIconDragUrl(String encodedIconDragUrl) {
        this.encodedIconDragUrl = encodedIconDragUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketHashName() {
        return marketHashName;
    }

    public void setMarketHashName(String marketHashName) {
        this.marketHashName = marketHashName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTradable() {
        return tradable;
    }

    public void setTradable(String tradable) {
        this.tradable = tradable;
    }

    public String getMarketable() {
        return marketable;
    }

    public void setMarketable(String marketable) {
        this.marketable = marketable;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getMarketTradableRestriction() {
        return marketTradableRestriction;
    }

    public void setMarketTradableRestriction(String marketTradableRestriction) {
        this.marketTradableRestriction = marketTradableRestriction;
    }

    public String getFraudWarnings() {
        return fraudWarnings;
    }

    public void setFraudWarnings(String fraudWarnings) {
        this.fraudWarnings = fraudWarnings;
    }

    public Map<String, SteamAssetDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<String, SteamAssetDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public String getOwnerDescriptions() {
        return ownerDescriptions;
    }

    public void setOwnerDescriptions(String ownerDescriptions) {
        this.ownerDescriptions = ownerDescriptions;
    }

    public Map<String, SteamAssetTag> getTags() {
        return tags;
    }

    public void setTags(Map<String, SteamAssetTag> tags) {
        this.tags = tags;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("classId", getClassId())
                .append("name", getName())
                .append("marketName", getMarketName())
                .append("tags", getTags())
                .append("descriptions", getDescriptions())
                .toString();
    }
}
