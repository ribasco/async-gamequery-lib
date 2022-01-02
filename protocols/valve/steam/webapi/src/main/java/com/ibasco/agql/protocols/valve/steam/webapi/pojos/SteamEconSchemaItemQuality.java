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
