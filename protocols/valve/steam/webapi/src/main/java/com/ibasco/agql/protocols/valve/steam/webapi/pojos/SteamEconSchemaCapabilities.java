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

    public Boolean getNameable() {
        return nameable;
    }

    public void setNameable(Boolean nameable) {
        this.nameable = nameable;
    }

    public Boolean getCanGiftWrap() {
        return canGiftWrap;
    }

    public void setCanGiftWrap(Boolean canGiftWrap) {
        this.canGiftWrap = canGiftWrap;
    }

    public Boolean getCanCraftMark() {
        return canCraftMark;
    }

    public void setCanCraftMark(Boolean canCraftMark) {
        this.canCraftMark = canCraftMark;
    }

    public Boolean getCanBeRestored() {
        return canBeRestored;
    }

    public void setCanBeRestored(Boolean canBeRestored) {
        this.canBeRestored = canBeRestored;
    }

    public Boolean getStrangeParts() {
        return strangeParts;
    }

    public void setStrangeParts(Boolean strangeParts) {
        this.strangeParts = strangeParts;
    }

    public Boolean getCanCardUpgrade() {
        return canCardUpgrade;
    }

    public void setCanCardUpgrade(Boolean canCardUpgrade) {
        this.canCardUpgrade = canCardUpgrade;
    }

    public Boolean getCanStrangify() {
        return canStrangify;
    }

    public void setCanStrangify(Boolean canStrangify) {
        this.canStrangify = canStrangify;
    }

    public Boolean getCanKillStreakify() {
        return canKillStreakify;
    }

    public void setCanKillStreakify(Boolean canKillStreakify) {
        this.canKillStreakify = canKillStreakify;
    }

    public Boolean getCanConsume() {
        return canConsume;
    }

    public void setCanConsume(Boolean canConsume) {
        this.canConsume = canConsume;
    }

    public Boolean getDecodable() {
        return decodable;
    }

    public void setDecodable(Boolean decodable) {
        this.decodable = decodable;
    }

    public Boolean getUsableGc() {
        return usableGc;
    }

    public void setUsableGc(Boolean usableGc) {
        this.usableGc = usableGc;
    }

    public Boolean getUsableOutOfGame() {
        return usableOutOfGame;
    }

    public void setUsableOutOfGame(Boolean usableOutOfGame) {
        this.usableOutOfGame = usableOutOfGame;
    }

    public Boolean getCanSticker() {
        return canSticker;
    }

    public void setCanSticker(Boolean canSticker) {
        this.canSticker = canSticker;
    }

    public Boolean getCanStattrackSwap() {
        return canStattrackSwap;
    }

    public void setCanStattrackSwap(Boolean canStattrackSwap) {
        this.canStattrackSwap = canStattrackSwap;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
