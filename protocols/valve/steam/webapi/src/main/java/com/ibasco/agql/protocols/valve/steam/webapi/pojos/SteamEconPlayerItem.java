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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOriginalId() {
        return originalId;
    }

    public void setOriginalId(int originalId) {
        this.originalId = originalId;
    }

    public int getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public boolean isCannotTrade() {
        return cannotTrade;
    }

    public void setCannotTrade(boolean cannotTrade) {
        this.cannotTrade = cannotTrade;
    }

    public List<SteamEconPlayerItemAttribute> getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(List<SteamEconPlayerItemAttribute> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public List<SteamEconPlayerItemEquipInfo> getEquipInfos() {
        return equipInfos;
    }

    public void setEquipInfos(List<SteamEconPlayerItemEquipInfo> equipInfos) {
        this.equipInfos = equipInfos;
    }

    public boolean isCannotCraft() {
        return cannotCraft;
    }

    public void setCannotCraft(boolean cannotCraft) {
        this.cannotCraft = cannotCraft;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCustomDescription() {
        return customDescription;
    }

    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
