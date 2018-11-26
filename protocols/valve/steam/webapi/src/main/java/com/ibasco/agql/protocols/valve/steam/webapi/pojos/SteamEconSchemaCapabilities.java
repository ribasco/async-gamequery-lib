/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
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
