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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * Created by raffy on 10/27/2016.
 *
 * @author Rafael Luis Ibasco
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

    /**
     * <p>Getter for the field <code>encodedIconUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEncodedIconUrl() {
        return encodedIconUrl;
    }

    /**
     * <p>Setter for the field <code>encodedIconUrl</code>.</p>
     *
     * @param encodedIconUrl a {@link java.lang.String} object
     */
    public void setEncodedIconUrl(String encodedIconUrl) {
        this.encodedIconUrl = encodedIconUrl;
    }

    /**
     * <p>Getter for the field <code>encodedIconUrlLarge</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEncodedIconUrlLarge() {
        return encodedIconUrlLarge;
    }

    /**
     * <p>Setter for the field <code>encodedIconUrlLarge</code>.</p>
     *
     * @param encodedIconUrlLarge a {@link java.lang.String} object
     */
    public void setEncodedIconUrlLarge(String encodedIconUrlLarge) {
        this.encodedIconUrlLarge = encodedIconUrlLarge;
    }

    /**
     * <p>Getter for the field <code>encodedIconDragUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEncodedIconDragUrl() {
        return encodedIconDragUrl;
    }

    /**
     * <p>Setter for the field <code>encodedIconDragUrl</code>.</p>
     *
     * @param encodedIconDragUrl a {@link java.lang.String} object
     */
    public void setEncodedIconDragUrl(String encodedIconDragUrl) {
        this.encodedIconDragUrl = encodedIconDragUrl;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>marketHashName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMarketHashName() {
        return marketHashName;
    }

    /**
     * <p>Setter for the field <code>marketHashName</code>.</p>
     *
     * @param marketHashName a {@link java.lang.String} object
     */
    public void setMarketHashName(String marketHashName) {
        this.marketHashName = marketHashName;
    }

    /**
     * <p>Getter for the field <code>marketName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * <p>Setter for the field <code>marketName</code>.</p>
     *
     * @param marketName a {@link java.lang.String} object
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * <p>Getter for the field <code>nameColor</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getNameColor() {
        return nameColor;
    }

    /**
     * <p>Setter for the field <code>nameColor</code>.</p>
     *
     * @param nameColor a {@link java.lang.String} object
     */
    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    /**
     * <p>Getter for the field <code>backgroundColor</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * <p>Setter for the field <code>backgroundColor</code>.</p>
     *
     * @param backgroundColor a {@link java.lang.String} object
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>tradable</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTradable() {
        return tradable;
    }

    /**
     * <p>Setter for the field <code>tradable</code>.</p>
     *
     * @param tradable a {@link java.lang.String} object
     */
    public void setTradable(String tradable) {
        this.tradable = tradable;
    }

    /**
     * <p>Getter for the field <code>marketable</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMarketable() {
        return marketable;
    }

    /**
     * <p>Setter for the field <code>marketable</code>.</p>
     *
     * @param marketable a {@link java.lang.String} object
     */
    public void setMarketable(String marketable) {
        this.marketable = marketable;
    }

    /**
     * <p>Getter for the field <code>commodity</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * <p>Setter for the field <code>commodity</code>.</p>
     *
     * @param commodity a {@link java.lang.String} object
     */
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    /**
     * <p>Getter for the field <code>marketTradableRestriction</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMarketTradableRestriction() {
        return marketTradableRestriction;
    }

    /**
     * <p>Setter for the field <code>marketTradableRestriction</code>.</p>
     *
     * @param marketTradableRestriction a {@link java.lang.String} object
     */
    public void setMarketTradableRestriction(String marketTradableRestriction) {
        this.marketTradableRestriction = marketTradableRestriction;
    }

    /**
     * <p>Getter for the field <code>fraudWarnings</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFraudWarnings() {
        return fraudWarnings;
    }

    /**
     * <p>Setter for the field <code>fraudWarnings</code>.</p>
     *
     * @param fraudWarnings a {@link java.lang.String} object
     */
    public void setFraudWarnings(String fraudWarnings) {
        this.fraudWarnings = fraudWarnings;
    }

    /**
     * <p>Getter for the field <code>descriptions</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, SteamAssetDescription> getDescriptions() {
        return descriptions;
    }

    /**
     * <p>Setter for the field <code>descriptions</code>.</p>
     *
     * @param descriptions a {@link java.util.Map} object
     */
    public void setDescriptions(Map<String, SteamAssetDescription> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * <p>Getter for the field <code>ownerDescriptions</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getOwnerDescriptions() {
        return ownerDescriptions;
    }

    /**
     * <p>Setter for the field <code>ownerDescriptions</code>.</p>
     *
     * @param ownerDescriptions a {@link java.lang.String} object
     */
    public void setOwnerDescriptions(String ownerDescriptions) {
        this.ownerDescriptions = ownerDescriptions;
    }

    /**
     * <p>Getter for the field <code>tags</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, SteamAssetTag> getTags() {
        return tags;
    }

    /**
     * <p>Setter for the field <code>tags</code>.</p>
     *
     * @param tags a {@link java.util.Map} object
     */
    public void setTags(Map<String, SteamAssetTag> tags) {
        this.tags = tags;
    }

    /**
     * <p>Getter for the field <code>classId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getClassId() {
        return classId;
    }

    /**
     * <p>Setter for the field <code>classId</code>.</p>
     *
     * @param classId a {@link java.lang.String} object
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }

    /** {@inheritDoc} */
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
