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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SteamEconSchemaItemQuality {
    private int normal;
    private int rarity1;
    private int rarity2;
    private int rarity3;
    private int rarity4;
    private int vintage;
    private int unique;
    private int community;
    private int developer;
    private int selfmade;
    private int customized;
    private int strange;
    private int completed;
    private int haunted;
    private int collectors;
    private int paintkitweapon;

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getRarity1() {
        return rarity1;
    }

    public void setRarity1(int rarity1) {
        this.rarity1 = rarity1;
    }

    public int getRarity2() {
        return rarity2;
    }

    public void setRarity2(int rarity2) {
        this.rarity2 = rarity2;
    }

    public int getRarity3() {
        return rarity3;
    }

    public void setRarity3(int rarity3) {
        this.rarity3 = rarity3;
    }

    public int getRarity4() {
        return rarity4;
    }

    public void setRarity4(int rarity4) {
        this.rarity4 = rarity4;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public int getUnique() {
        return unique;
    }

    public void setUnique(int unique) {
        this.unique = unique;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public int getDeveloper() {
        return developer;
    }

    public void setDeveloper(int developer) {
        this.developer = developer;
    }

    public int getSelfmade() {
        return selfmade;
    }

    public void setSelfmade(int selfmade) {
        this.selfmade = selfmade;
    }

    public int getCustomized() {
        return customized;
    }

    public void setCustomized(int customized) {
        this.customized = customized;
    }

    public int getStrange() {
        return strange;
    }

    public void setStrange(int strange) {
        this.strange = strange;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getHaunted() {
        return haunted;
    }

    public void setHaunted(int haunted) {
        this.haunted = haunted;
    }

    public int getCollectors() {
        return collectors;
    }

    public void setCollectors(int collectors) {
        this.collectors = collectors;
    }

    public int getPaintkitweapon() {
        return paintkitweapon;
    }

    public void setPaintkitweapon(int paintkitweapon) {
        this.paintkitweapon = paintkitweapon;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
