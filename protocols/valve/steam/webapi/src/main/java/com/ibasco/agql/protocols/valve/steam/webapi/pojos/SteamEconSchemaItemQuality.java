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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>SteamEconSchemaItemQuality class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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

    /**
     * <p>Getter for the field <code>normal</code>.</p>
     *
     * @return a int
     */
    public int getNormal() {
        return normal;
    }

    /**
     * <p>Setter for the field <code>normal</code>.</p>
     *
     * @param normal a int
     */
    public void setNormal(int normal) {
        this.normal = normal;
    }

    /**
     * <p>Getter for the field <code>rarity1</code>.</p>
     *
     * @return a int
     */
    public int getRarity1() {
        return rarity1;
    }

    /**
     * <p>Setter for the field <code>rarity1</code>.</p>
     *
     * @param rarity1 a int
     */
    public void setRarity1(int rarity1) {
        this.rarity1 = rarity1;
    }

    /**
     * <p>Getter for the field <code>rarity2</code>.</p>
     *
     * @return a int
     */
    public int getRarity2() {
        return rarity2;
    }

    /**
     * <p>Setter for the field <code>rarity2</code>.</p>
     *
     * @param rarity2 a int
     */
    public void setRarity2(int rarity2) {
        this.rarity2 = rarity2;
    }

    /**
     * <p>Getter for the field <code>rarity3</code>.</p>
     *
     * @return a int
     */
    public int getRarity3() {
        return rarity3;
    }

    /**
     * <p>Setter for the field <code>rarity3</code>.</p>
     *
     * @param rarity3 a int
     */
    public void setRarity3(int rarity3) {
        this.rarity3 = rarity3;
    }

    /**
     * <p>Getter for the field <code>rarity4</code>.</p>
     *
     * @return a int
     */
    public int getRarity4() {
        return rarity4;
    }

    /**
     * <p>Setter for the field <code>rarity4</code>.</p>
     *
     * @param rarity4 a int
     */
    public void setRarity4(int rarity4) {
        this.rarity4 = rarity4;
    }

    /**
     * <p>Getter for the field <code>vintage</code>.</p>
     *
     * @return a int
     */
    public int getVintage() {
        return vintage;
    }

    /**
     * <p>Setter for the field <code>vintage</code>.</p>
     *
     * @param vintage a int
     */
    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    /**
     * <p>Getter for the field <code>unique</code>.</p>
     *
     * @return a int
     */
    public int getUnique() {
        return unique;
    }

    /**
     * <p>Setter for the field <code>unique</code>.</p>
     *
     * @param unique a int
     */
    public void setUnique(int unique) {
        this.unique = unique;
    }

    /**
     * <p>Getter for the field <code>community</code>.</p>
     *
     * @return a int
     */
    public int getCommunity() {
        return community;
    }

    /**
     * <p>Setter for the field <code>community</code>.</p>
     *
     * @param community a int
     */
    public void setCommunity(int community) {
        this.community = community;
    }

    /**
     * <p>Getter for the field <code>developer</code>.</p>
     *
     * @return a int
     */
    public int getDeveloper() {
        return developer;
    }

    /**
     * <p>Setter for the field <code>developer</code>.</p>
     *
     * @param developer a int
     */
    public void setDeveloper(int developer) {
        this.developer = developer;
    }

    /**
     * <p>Getter for the field <code>selfmade</code>.</p>
     *
     * @return a int
     */
    public int getSelfmade() {
        return selfmade;
    }

    /**
     * <p>Setter for the field <code>selfmade</code>.</p>
     *
     * @param selfmade a int
     */
    public void setSelfmade(int selfmade) {
        this.selfmade = selfmade;
    }

    /**
     * <p>Getter for the field <code>customized</code>.</p>
     *
     * @return a int
     */
    public int getCustomized() {
        return customized;
    }

    /**
     * <p>Setter for the field <code>customized</code>.</p>
     *
     * @param customized a int
     */
    public void setCustomized(int customized) {
        this.customized = customized;
    }

    /**
     * <p>Getter for the field <code>strange</code>.</p>
     *
     * @return a int
     */
    public int getStrange() {
        return strange;
    }

    /**
     * <p>Setter for the field <code>strange</code>.</p>
     *
     * @param strange a int
     */
    public void setStrange(int strange) {
        this.strange = strange;
    }

    /**
     * <p>Getter for the field <code>completed</code>.</p>
     *
     * @return a int
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * <p>Setter for the field <code>completed</code>.</p>
     *
     * @param completed a int
     */
    public void setCompleted(int completed) {
        this.completed = completed;
    }

    /**
     * <p>Getter for the field <code>haunted</code>.</p>
     *
     * @return a int
     */
    public int getHaunted() {
        return haunted;
    }

    /**
     * <p>Setter for the field <code>haunted</code>.</p>
     *
     * @param haunted a int
     */
    public void setHaunted(int haunted) {
        this.haunted = haunted;
    }

    /**
     * <p>Getter for the field <code>collectors</code>.</p>
     *
     * @return a int
     */
    public int getCollectors() {
        return collectors;
    }

    /**
     * <p>Setter for the field <code>collectors</code>.</p>
     *
     * @param collectors a int
     */
    public void setCollectors(int collectors) {
        this.collectors = collectors;
    }

    /**
     * <p>Getter for the field <code>paintkitweapon</code>.</p>
     *
     * @return a int
     */
    public int getPaintkitweapon() {
        return paintkitweapon;
    }

    /**
     * <p>Setter for the field <code>paintkitweapon</code>.</p>
     *
     * @param paintkitweapon a int
     */
    public void setPaintkitweapon(int paintkitweapon) {
        this.paintkitweapon = paintkitweapon;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
