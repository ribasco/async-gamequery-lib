/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Information about the items in a supporting game.
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getItemsGameUrl() {
        return itemsGameUrl;
    }

    public void setItemsGameUrl(String itemsGameUrl) {
        this.itemsGameUrl = itemsGameUrl;
    }

    public SteamEconSchemaItemQuality getQualities() {
        return qualities;
    }

    public void setQualities(SteamEconSchemaItemQuality qualities) {
        this.qualities = qualities;
    }

    public List<SteamEconSchemaOriginName> getOriginNames() {
        return originNames;
    }

    public void setOriginNames(List<SteamEconSchemaOriginName> originNames) {
        this.originNames = originNames;
    }

    public List<SteamEconSchemaItem> getItems() {
        return items;
    }

    public void setItems(List<SteamEconSchemaItem> items) {
        this.items = items;
    }

    public List<SteamEconSchemaAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SteamEconSchemaAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<SteamEconSchemaItemSet> getItemSets() {
        return itemSets;
    }

    public void setItemSets(List<SteamEconSchemaItemSet> itemSets) {
        this.itemSets = itemSets;
    }

    public List<SteamEconSchemaAcap> getAttributeControlledAttachedParticles() {
        return attributeControlledAttachedParticles;
    }

    public void setAttributeControlledAttachedParticles(List<SteamEconSchemaAcap> attributeControlledAttachedParticles) {
        this.attributeControlledAttachedParticles = attributeControlledAttachedParticles;
    }

    public List<SteamEconSchemaItemLevel> getItemLevels() {
        return itemLevels;
    }

    public void setItemLevels(List<SteamEconSchemaItemLevel> itemLevels) {
        this.itemLevels = itemLevels;
    }

    public List<SteamEconSchemaKest> getKillEaterScoreTypes() {
        return killEaterScoreTypes;
    }

    public void setKillEaterScoreTypes(List<SteamEconSchemaKest> killEaterScoreTypes) {
        this.killEaterScoreTypes = killEaterScoreTypes;
    }

    public List<SteamEconKillEaterRanks> getKillEaterRanks() {
        return killEaterRanks;
    }

    public void setKillEaterRanks(List<SteamEconKillEaterRanks> killEaterRanks) {
        this.killEaterRanks = killEaterRanks;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}