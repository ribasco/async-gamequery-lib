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
import java.util.Map;

/**
 * <p>SteamEconSchemaItem class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaItem {

    private String name;

    @SerializedName("defindex")
    private int defIndex;

    @SerializedName("item_class")
    private String itemClass;

    @SerializedName("item_type_name")
    private String itemTypeName;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("item_description")
    private String itemDescription;

    @SerializedName("proper_name")
    private boolean properName;

    @SerializedName("item_slot")
    private String itemSlot; //TODO: use enum instead?

    @SerializedName("model_player")
    private String modelPlayer;

    @SerializedName("item_quality")
    private int itemQuality;

    @SerializedName("image_inventory")
    private String imageInventory;

    @SerializedName("min_ilevel")
    private int minItemLevel;

    @SerializedName("max_ilevel")
    private int maxItemLevel;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("image_url_large")
    private String imageUrlLarge;

    @SerializedName("drop_type ")
    private String dropType;

    @SerializedName("item_set")
    private String itemSet;

    @SerializedName("holiday_restriction")
    private String holidayRestriction;

    @SerializedName("craft_class")
    private String craftClass;

    @SerializedName("craft_material_type")
    private String craftMaterialType;

    private SteamEconSchemaCapabilities capabilities;

    @SerializedName("used_by_classes")
    private List<String> usedByClasses = new ArrayList<>();

    private List<SteamEconSchemaItemAttribute> attributes = new ArrayList<>();

    /**
     * An object that describes class specific loadout slots for the item if applicable.
     */
    @SerializedName("per_class_loadout_slots")
    private Map<String, String> perClassLoadoutSlots;

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
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
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
     * <p>Getter for the field <code>itemClass</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemClass() {
        return itemClass;
    }

    /**
     * <p>Setter for the field <code>itemClass</code>.</p>
     *
     * @param itemClass
     *         a {@link java.lang.String} object
     */
    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    /**
     * <p>Getter for the field <code>itemTypeName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * <p>Setter for the field <code>itemTypeName</code>.</p>
     *
     * @param itemTypeName
     *         a {@link java.lang.String} object
     */
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    /**
     * <p>Getter for the field <code>itemName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * <p>Setter for the field <code>itemName</code>.</p>
     *
     * @param itemName
     *         a {@link java.lang.String} object
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * <p>Getter for the field <code>itemDescription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * <p>Setter for the field <code>itemDescription</code>.</p>
     *
     * @param itemDescription
     *         a {@link java.lang.String} object
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * <p>isProperName.</p>
     *
     * @return a boolean
     */
    public boolean isProperName() {
        return properName;
    }

    /**
     * <p>Setter for the field <code>properName</code>.</p>
     *
     * @param properName
     *         a boolean
     */
    public void setProperName(boolean properName) {
        this.properName = properName;
    }

    /**
     * <p>Getter for the field <code>itemSlot</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemSlot() {
        return itemSlot;
    }

    /**
     * <p>Setter for the field <code>itemSlot</code>.</p>
     *
     * @param itemSlot
     *         a {@link java.lang.String} object
     */
    public void setItemSlot(String itemSlot) {
        this.itemSlot = itemSlot;
    }

    /**
     * <p>Getter for the field <code>modelPlayer</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getModelPlayer() {
        return modelPlayer;
    }

    /**
     * <p>Setter for the field <code>modelPlayer</code>.</p>
     *
     * @param modelPlayer
     *         a {@link java.lang.String} object
     */
    public void setModelPlayer(String modelPlayer) {
        this.modelPlayer = modelPlayer;
    }

    /**
     * <p>Getter for the field <code>itemQuality</code>.</p>
     *
     * @return a int
     */
    public int getItemQuality() {
        return itemQuality;
    }

    /**
     * <p>Setter for the field <code>itemQuality</code>.</p>
     *
     * @param itemQuality
     *         a int
     */
    public void setItemQuality(int itemQuality) {
        this.itemQuality = itemQuality;
    }

    /**
     * <p>Getter for the field <code>imageInventory</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getImageInventory() {
        return imageInventory;
    }

    /**
     * <p>Setter for the field <code>imageInventory</code>.</p>
     *
     * @param imageInventory
     *         a {@link java.lang.String} object
     */
    public void setImageInventory(String imageInventory) {
        this.imageInventory = imageInventory;
    }

    /**
     * <p>Getter for the field <code>minItemLevel</code>.</p>
     *
     * @return a int
     */
    public int getMinItemLevel() {
        return minItemLevel;
    }

    /**
     * <p>Setter for the field <code>minItemLevel</code>.</p>
     *
     * @param minItemLevel
     *         a int
     */
    public void setMinItemLevel(int minItemLevel) {
        this.minItemLevel = minItemLevel;
    }

    /**
     * <p>Getter for the field <code>maxItemLevel</code>.</p>
     *
     * @return a int
     */
    public int getMaxItemLevel() {
        return maxItemLevel;
    }

    /**
     * <p>Setter for the field <code>maxItemLevel</code>.</p>
     *
     * @param maxItemLevel
     *         a int
     */
    public void setMaxItemLevel(int maxItemLevel) {
        this.maxItemLevel = maxItemLevel;
    }

    /**
     * <p>Getter for the field <code>imageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * <p>Setter for the field <code>imageUrl</code>.</p>
     *
     * @param imageUrl
     *         a {@link java.lang.String} object
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * <p>Getter for the field <code>imageUrlLarge</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    /**
     * <p>Setter for the field <code>imageUrlLarge</code>.</p>
     *
     * @param imageUrlLarge
     *         a {@link java.lang.String} object
     */
    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    /**
     * <p>Getter for the field <code>dropType</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDropType() {
        return dropType;
    }

    /**
     * <p>Setter for the field <code>dropType</code>.</p>
     *
     * @param dropType
     *         a {@link java.lang.String} object
     */
    public void setDropType(String dropType) {
        this.dropType = dropType;
    }

    /**
     * <p>Getter for the field <code>itemSet</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemSet() {
        return itemSet;
    }

    /**
     * <p>Setter for the field <code>itemSet</code>.</p>
     *
     * @param itemSet
     *         a {@link java.lang.String} object
     */
    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
    }

    /**
     * <p>Getter for the field <code>holidayRestriction</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getHolidayRestriction() {
        return holidayRestriction;
    }

    /**
     * <p>Setter for the field <code>holidayRestriction</code>.</p>
     *
     * @param holidayRestriction
     *         a {@link java.lang.String} object
     */
    public void setHolidayRestriction(String holidayRestriction) {
        this.holidayRestriction = holidayRestriction;
    }

    /**
     * <p>Getter for the field <code>craftClass</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCraftClass() {
        return craftClass;
    }

    /**
     * <p>Setter for the field <code>craftClass</code>.</p>
     *
     * @param craftClass
     *         a {@link java.lang.String} object
     */
    public void setCraftClass(String craftClass) {
        this.craftClass = craftClass;
    }

    /**
     * <p>Getter for the field <code>craftMaterialType</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getCraftMaterialType() {
        return craftMaterialType;
    }

    /**
     * <p>Setter for the field <code>craftMaterialType</code>.</p>
     *
     * @param craftMaterialType
     *         a {@link java.lang.String} object
     */
    public void setCraftMaterialType(String craftMaterialType) {
        this.craftMaterialType = craftMaterialType;
    }

    /**
     * <p>Getter for the field <code>capabilities</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchemaCapabilities} object
     */
    public SteamEconSchemaCapabilities getCapabilities() {
        return capabilities;
    }

    /**
     * <p>Setter for the field <code>capabilities</code>.</p>
     *
     * @param capabilities
     *         a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchemaCapabilities} object
     */
    public void setCapabilities(SteamEconSchemaCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * <p>Getter for the field <code>usedByClasses</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<String> getUsedByClasses() {
        return usedByClasses;
    }

    /**
     * <p>Setter for the field <code>usedByClasses</code>.</p>
     *
     * @param usedByClasses
     *         a {@link java.util.List} object
     */
    public void setUsedByClasses(List<String> usedByClasses) {
        this.usedByClasses = usedByClasses;
    }

    /**
     * <p>Getter for the field <code>attributes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaItemAttribute> getAttributes() {
        return attributes;
    }

    /**
     * <p>Setter for the field <code>attributes</code>.</p>
     *
     * @param attributes
     *         a {@link java.util.List} object
     */
    public void setAttributes(List<SteamEconSchemaItemAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * <p>Getter for the field <code>perClassLoadoutSlots</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, String> getPerClassLoadoutSlots() {
        return perClassLoadoutSlots;
    }

    /**
     * <p>Setter for the field <code>perClassLoadoutSlots</code>.</p>
     *
     * @param perClassLoadoutSlots
     *         a {@link java.util.Map} object
     */
    public void setPerClassLoadoutSlots(Map<String, String> perClassLoadoutSlots) {
        this.perClassLoadoutSlots = perClassLoadoutSlots;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
