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
 * <p>SteamEconPlayerItem class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconPlayerItem {

    private int id;

    @SerializedName("original_id")
    private int originalId;

    @SerializedName("defindex")
    private int defIndex;

    private int level;

    private int quality;

    private int inventory;

    private int quantity;

    private int rarity;

    @SerializedName("flag_cannot_trade")
    private boolean cannotTrade;

    @SerializedName("attributes")
    private List<SteamEconPlayerItemAttribute> itemAttributes = new ArrayList<>();

    private List<SteamEconPlayerItemEquipInfo> equipInfos = new ArrayList<>();

    @SerializedName("flag_cannot_craft")
    private boolean cannotCraft; //tf2 specific

    @SerializedName("custom_name")
    private String customName;

    @SerializedName("custom_desc")
    private String customDescription;

    private int style;

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a int
     */
    public int getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id
     *         a int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>originalId</code>.</p>
     *
     * @return a int
     */
    public int getOriginalId() {
        return originalId;
    }

    /**
     * <p>Setter for the field <code>originalId</code>.</p>
     *
     * @param originalId
     *         a int
     */
    public void setOriginalId(int originalId) {
        this.originalId = originalId;
    }

    /**
     * <p>Getter for the field <code>defIndex</code>.</p>
     *
     * @return a int
     */
    public int getDefIndex() {
        return defIndex;
    }

    /**
     * <p>Setter for the field <code>defIndex</code>.</p>
     *
     * @param defIndex
     *         a int
     */
    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    /**
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return a int
     */
    public int getLevel() {
        return level;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level
     *         a int
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * <p>Getter for the field <code>quality</code>.</p>
     *
     * @return a int
     */
    public int getQuality() {
        return quality;
    }

    /**
     * <p>Setter for the field <code>quality</code>.</p>
     *
     * @param quality
     *         a int
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * <p>Getter for the field <code>inventory</code>.</p>
     *
     * @return a int
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * <p>Setter for the field <code>inventory</code>.</p>
     *
     * @param inventory
     *         a int
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    /**
     * <p>Getter for the field <code>quantity</code>.</p>
     *
     * @return a int
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * <p>Setter for the field <code>quantity</code>.</p>
     *
     * @param quantity
     *         a int
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * <p>Getter for the field <code>rarity</code>.</p>
     *
     * @return a int
     */
    public int getRarity() {
        return rarity;
    }

    /**
     * <p>Setter for the field <code>rarity</code>.</p>
     *
     * @param rarity
     *         a int
     */
    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    /**
     * <p>isCannotTrade.</p>
     *
     * @return a boolean
     */
    public boolean isCannotTrade() {
        return cannotTrade;
    }

    /**
     * <p>Setter for the field <code>cannotTrade</code>.</p>
     *
     * @param cannotTrade
     *         a boolean
     */
    public void setCannotTrade(boolean cannotTrade) {
        this.cannotTrade = cannotTrade;
    }

    /**
     * <p>Getter for the field <code>itemAttributes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconPlayerItemAttribute> getItemAttributes() {
        return itemAttributes;
    }

    /**
     * <p>Setter for the field <code>itemAttributes</code>.</p>
     *
     * @param itemAttributes
     *         a {@link java.util.List} object
     */
    public void setItemAttributes(List<SteamEconPlayerItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    /**
     * <p>Getter for the field <code>equipInfos</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconPlayerItemEquipInfo> getEquipInfos() {
        return equipInfos;
    }

    /**
     * <p>Setter for the field <code>equipInfos</code>.</p>
     *
     * @param equipInfos
     *         a {@link java.util.List} object
     */
    public void setEquipInfos(List<SteamEconPlayerItemEquipInfo> equipInfos) {
        this.equipInfos = equipInfos;
    }

    /**
     * <p>isCannotCraft.</p>
     *
     * @return a boolean
     */
    public boolean isCannotCraft() {
        return cannotCraft;
    }

    /**
     * <p>Setter for the field <code>cannotCraft</code>.</p>
     *
     * @param cannotCraft
     *         a boolean
     */
    public void setCannotCraft(boolean cannotCraft) {
        this.cannotCraft = cannotCraft;
    }

    /**
     * <p>Getter for the field <code>customName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * <p>Setter for the field <code>customName</code>.</p>
     *
     * @param customName
     *         a {@link java.lang.String} object
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * <p>Getter for the field <code>customDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCustomDescription() {
        return customDescription;
    }

    /**
     * <p>Setter for the field <code>customDescription</code>.</p>
     *
     * @param customDescription
     *         a {@link java.lang.String} object
     */
    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    /**
     * <p>Getter for the field <code>style</code>.</p>
     *
     * @return a int
     */
    public int getStyle() {
        return style;
    }

    /**
     * <p>Setter for the field <code>style</code>.</p>
     *
     * @param style
     *         a int
     */
    public void setStyle(int style) {
        this.style = style;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
