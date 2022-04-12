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

/**
 * <p>SteamEconSchemaCapabilities class.</p>
 *
 * @author Rafael Luis Ibasco
 */
public class SteamEconSchemaCapabilities {
    private Boolean nameable;
    @SerializedName("can_gift_wrap")
    private Boolean canGiftWrap;
    @SerializedName("can_craft_mark")
    private Boolean canCraftMark;
    @SerializedName("can_be_restored    ")
    private Boolean canBeRestored;
    @SerializedName("strange_parts")
    private Boolean strangeParts;
    @SerializedName("can_card_upgrade")
    private Boolean canCardUpgrade;
    @SerializedName("can_strangify")
    private Boolean canStrangify;
    @SerializedName("can_killstreakify")
    private Boolean canKillStreakify;
    @SerializedName("can_consume")
    private Boolean canConsume;
    private Boolean decodable;
    @SerializedName("usable_gc")
    private Boolean usableGc;
    @SerializedName("usable_out_of_game")
    private Boolean usableOutOfGame;
    @SerializedName("can_sticker")
    private Boolean canSticker; //for version 2
    @SerializedName("can_stattrack_swap")
    private Boolean canStattrackSwap; //for version 2

    /**
     * <p>Getter for the field <code>nameable</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getNameable() {
        return nameable;
    }

    /**
     * <p>Setter for the field <code>nameable</code>.</p>
     *
     * @param nameable a {@link java.lang.Boolean} object
     */
    public void setNameable(Boolean nameable) {
        this.nameable = nameable;
    }

    /**
     * <p>Getter for the field <code>canGiftWrap</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanGiftWrap() {
        return canGiftWrap;
    }

    /**
     * <p>Setter for the field <code>canGiftWrap</code>.</p>
     *
     * @param canGiftWrap a {@link java.lang.Boolean} object
     */
    public void setCanGiftWrap(Boolean canGiftWrap) {
        this.canGiftWrap = canGiftWrap;
    }

    /**
     * <p>Getter for the field <code>canCraftMark</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanCraftMark() {
        return canCraftMark;
    }

    /**
     * <p>Setter for the field <code>canCraftMark</code>.</p>
     *
     * @param canCraftMark a {@link java.lang.Boolean} object
     */
    public void setCanCraftMark(Boolean canCraftMark) {
        this.canCraftMark = canCraftMark;
    }

    /**
     * <p>Getter for the field <code>canBeRestored</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanBeRestored() {
        return canBeRestored;
    }

    /**
     * <p>Setter for the field <code>canBeRestored</code>.</p>
     *
     * @param canBeRestored a {@link java.lang.Boolean} object
     */
    public void setCanBeRestored(Boolean canBeRestored) {
        this.canBeRestored = canBeRestored;
    }

    /**
     * <p>Getter for the field <code>strangeParts</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getStrangeParts() {
        return strangeParts;
    }

    /**
     * <p>Setter for the field <code>strangeParts</code>.</p>
     *
     * @param strangeParts a {@link java.lang.Boolean} object
     */
    public void setStrangeParts(Boolean strangeParts) {
        this.strangeParts = strangeParts;
    }

    /**
     * <p>Getter for the field <code>canCardUpgrade</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanCardUpgrade() {
        return canCardUpgrade;
    }

    /**
     * <p>Setter for the field <code>canCardUpgrade</code>.</p>
     *
     * @param canCardUpgrade a {@link java.lang.Boolean} object
     */
    public void setCanCardUpgrade(Boolean canCardUpgrade) {
        this.canCardUpgrade = canCardUpgrade;
    }

    /**
     * <p>Getter for the field <code>canStrangify</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanStrangify() {
        return canStrangify;
    }

    /**
     * <p>Setter for the field <code>canStrangify</code>.</p>
     *
     * @param canStrangify a {@link java.lang.Boolean} object
     */
    public void setCanStrangify(Boolean canStrangify) {
        this.canStrangify = canStrangify;
    }

    /**
     * <p>Getter for the field <code>canKillStreakify</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanKillStreakify() {
        return canKillStreakify;
    }

    /**
     * <p>Setter for the field <code>canKillStreakify</code>.</p>
     *
     * @param canKillStreakify a {@link java.lang.Boolean} object
     */
    public void setCanKillStreakify(Boolean canKillStreakify) {
        this.canKillStreakify = canKillStreakify;
    }

    /**
     * <p>Getter for the field <code>canConsume</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanConsume() {
        return canConsume;
    }

    /**
     * <p>Setter for the field <code>canConsume</code>.</p>
     *
     * @param canConsume a {@link java.lang.Boolean} object
     */
    public void setCanConsume(Boolean canConsume) {
        this.canConsume = canConsume;
    }

    /**
     * <p>Getter for the field <code>decodable</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getDecodable() {
        return decodable;
    }

    /**
     * <p>Setter for the field <code>decodable</code>.</p>
     *
     * @param decodable a {@link java.lang.Boolean} object
     */
    public void setDecodable(Boolean decodable) {
        this.decodable = decodable;
    }

    /**
     * <p>Getter for the field <code>usableGc</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getUsableGc() {
        return usableGc;
    }

    /**
     * <p>Setter for the field <code>usableGc</code>.</p>
     *
     * @param usableGc a {@link java.lang.Boolean} object
     */
    public void setUsableGc(Boolean usableGc) {
        this.usableGc = usableGc;
    }

    /**
     * <p>Getter for the field <code>usableOutOfGame</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getUsableOutOfGame() {
        return usableOutOfGame;
    }

    /**
     * <p>Setter for the field <code>usableOutOfGame</code>.</p>
     *
     * @param usableOutOfGame a {@link java.lang.Boolean} object
     */
    public void setUsableOutOfGame(Boolean usableOutOfGame) {
        this.usableOutOfGame = usableOutOfGame;
    }

    /**
     * <p>Getter for the field <code>canSticker</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanSticker() {
        return canSticker;
    }

    /**
     * <p>Setter for the field <code>canSticker</code>.</p>
     *
     * @param canSticker a {@link java.lang.Boolean} object
     */
    public void setCanSticker(Boolean canSticker) {
        this.canSticker = canSticker;
    }

    /**
     * <p>Getter for the field <code>canStattrackSwap</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object
     */
    public Boolean getCanStattrackSwap() {
        return canStattrackSwap;
    }

    /**
     * <p>Setter for the field <code>canStattrackSwap</code>.</p>
     *
     * @param canStattrackSwap a {@link java.lang.Boolean} object
     */
    public void setCanStattrackSwap(Boolean canStattrackSwap) {
        this.canStattrackSwap = canStattrackSwap;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
