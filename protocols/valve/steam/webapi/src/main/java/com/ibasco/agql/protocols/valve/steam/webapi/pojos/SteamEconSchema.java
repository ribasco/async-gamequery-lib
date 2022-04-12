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
 * Contains Information about the items in a supporting game.
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchema {
    /**
     * The status of the request, should always be 1.
     */
    private int status;
    /**
     * A string containing the URL to the full item schema as used by the game.
     */
    @SerializedName("items_game_url")
    private String itemsGameUrl;
    /**
     * An object containing the numeric values corresponding to each "quality" an item can have:
     */
    private SteamEconSchemaItemQuality qualities;
    /**
     * A list of objects describing an item's origin.
     */
    private List<SteamEconSchemaOriginName> originNames = new ArrayList<>();
    /**
     * A list of item objects.
     */
    private List<SteamEconSchemaItem> items = new ArrayList<>();
    /**
     * An object containing an array of Schema Attributes
     */
    private List<SteamEconSchemaAttribute> attributes = new ArrayList<>();
    /**
     * A list of objects containing item set definitions.
     */
    @SerializedName("item_sets")
    private List<SteamEconSchemaItemSet> itemSets = new ArrayList<>();
    /**
     * An object containing a list of objects that describe the defined particle effects.
     */
    @SerializedName("attribute_controlled_attached_particles")
    private List<SteamEconSchemaAcap> attributeControlledAttachedParticles = new ArrayList<>();
    /**
     * A list of objects that describe ranks for kill eater items.
     */
    @SerializedName("item_levels")
    private List<SteamEconSchemaItemLevel> itemLevels = new ArrayList<>();
    /**
     * An object containing a list of objects describing suffixes
     * to use after a kill eater value in an attribute display line.
     */
    @SerializedName("kill_eater_score_types")
    private List<SteamEconSchemaKest> killEaterScoreTypes = new ArrayList<>();

    @SerializedName("kill_eater_ranks")
    private List<SteamEconKillEaterRanks> killEaterRanks = new ArrayList<>();

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a int
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a int
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * <p>Getter for the field <code>itemsGameUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getItemsGameUrl() {
        return itemsGameUrl;
    }

    /**
     * <p>Setter for the field <code>itemsGameUrl</code>.</p>
     *
     * @param itemsGameUrl a {@link java.lang.String} object
     */
    public void setItemsGameUrl(String itemsGameUrl) {
        this.itemsGameUrl = itemsGameUrl;
    }

    /**
     * <p>Getter for the field <code>qualities</code>.</p>
     *
     * @return a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchemaItemQuality} object
     */
    public SteamEconSchemaItemQuality getQualities() {
        return qualities;
    }

    /**
     * <p>Setter for the field <code>qualities</code>.</p>
     *
     * @param qualities a {@link com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamEconSchemaItemQuality} object
     */
    public void setQualities(SteamEconSchemaItemQuality qualities) {
        this.qualities = qualities;
    }

    /**
     * <p>Getter for the field <code>originNames</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaOriginName> getOriginNames() {
        return originNames;
    }

    /**
     * <p>Setter for the field <code>originNames</code>.</p>
     *
     * @param originNames a {@link java.util.List} object
     */
    public void setOriginNames(List<SteamEconSchemaOriginName> originNames) {
        this.originNames = originNames;
    }

    /**
     * <p>Getter for the field <code>items</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaItem> getItems() {
        return items;
    }

    /**
     * <p>Setter for the field <code>items</code>.</p>
     *
     * @param items a {@link java.util.List} object
     */
    public void setItems(List<SteamEconSchemaItem> items) {
        this.items = items;
    }

    /**
     * <p>Getter for the field <code>attributes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaAttribute> getAttributes() {
        return attributes;
    }

    /**
     * <p>Setter for the field <code>attributes</code>.</p>
     *
     * @param attributes a {@link java.util.List} object
     */
    public void setAttributes(List<SteamEconSchemaAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * <p>Getter for the field <code>itemSets</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaItemSet> getItemSets() {
        return itemSets;
    }

    /**
     * <p>Setter for the field <code>itemSets</code>.</p>
     *
     * @param itemSets a {@link java.util.List} object
     */
    public void setItemSets(List<SteamEconSchemaItemSet> itemSets) {
        this.itemSets = itemSets;
    }

    /**
     * <p>Getter for the field <code>attributeControlledAttachedParticles</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaAcap> getAttributeControlledAttachedParticles() {
        return attributeControlledAttachedParticles;
    }

    /**
     * <p>Setter for the field <code>attributeControlledAttachedParticles</code>.</p>
     *
     * @param attributeControlledAttachedParticles a {@link java.util.List} object
     */
    public void setAttributeControlledAttachedParticles(List<SteamEconSchemaAcap> attributeControlledAttachedParticles) {
        this.attributeControlledAttachedParticles = attributeControlledAttachedParticles;
    }

    /**
     * <p>Getter for the field <code>itemLevels</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaItemLevel> getItemLevels() {
        return itemLevels;
    }

    /**
     * <p>Setter for the field <code>itemLevels</code>.</p>
     *
     * @param itemLevels a {@link java.util.List} object
     */
    public void setItemLevels(List<SteamEconSchemaItemLevel> itemLevels) {
        this.itemLevels = itemLevels;
    }

    /**
     * <p>Getter for the field <code>killEaterScoreTypes</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconSchemaKest> getKillEaterScoreTypes() {
        return killEaterScoreTypes;
    }

    /**
     * <p>Setter for the field <code>killEaterScoreTypes</code>.</p>
     *
     * @param killEaterScoreTypes a {@link java.util.List} object
     */
    public void setKillEaterScoreTypes(List<SteamEconSchemaKest> killEaterScoreTypes) {
        this.killEaterScoreTypes = killEaterScoreTypes;
    }

    /**
     * <p>Getter for the field <code>killEaterRanks</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<SteamEconKillEaterRanks> getKillEaterRanks() {
        return killEaterRanks;
    }

    /**
     * <p>Setter for the field <code>killEaterRanks</code>.</p>
     *
     * @param killEaterRanks a {@link java.util.List} object
     */
    public void setKillEaterRanks(List<SteamEconKillEaterRanks> killEaterRanks) {
        this.killEaterRanks = killEaterRanks;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
